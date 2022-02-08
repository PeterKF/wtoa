package com.wtkj.oa.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.wtkj.oa.common.config.PageInfo;
import com.wtkj.oa.common.constant.ContractEnum;
import com.wtkj.oa.common.constant.GXEnum;
import com.wtkj.oa.dao.*;
import com.wtkj.oa.entity.*;
import com.wtkj.oa.exception.BusinessException;
import com.wtkj.oa.service.ICompanyManageService;
import com.wtkj.oa.service.IContractManageService;
import com.wtkj.oa.utils.RandomStringUtils;
import com.wtkj.oa.utils.ReadWordUtils;
import com.wtkj.oa.utils.YamlUtils;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static cn.hutool.core.text.CharSequenceUtil.isEmpty;


@Service
public class ContractManageServiceImpl implements IContractManageService {
    @Value("{image.logoPath}")
    private String imagePath;

    @Resource
    private ContractMapper contractMapper;

    @Resource
    private CompanyMapper companyMapper;

    @Resource
    private ServiceDetailMapper serviceDetailMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private ICompanyManageService companyManageService;

    private final static String PATH = "resource/";

    private final static String WORD_PATH = "wordFile/";

    private final static String HTML_PATH = "htmlFile/";

    /**
     * 读取word文件的内容
     *
     * @param contractType
     * @return
     */
    public String getWordContent(Integer businessType, String contractType) {
        String fileName = ContractEnum.getNameByType(businessType, contractType);
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String content = "";
        if (suffix.equals("doc")) {
            content = ReadWordUtils.readDocFile(PATH + fileName);
        } else if (suffix.equals("docx")) {
            content = ReadWordUtils.readDocxFile(PATH + fileName);
        }
        return content;
    }


    /**
     * 添加合同
     *
     * @param contract
     */
    public void addContract(Contract contract) {
        if (isEmpty(contract.getCompanyId())) {
            throw new BusinessException("请先选择一个客户！");
        }

        String contractName = "";
        if (contract.getBusinessType().equals(1)) {
            contractName = ContractEnum.getNameByType(1, "1");
        } else {
            contractName = ContractEnum.getNameByType(contract.getBusinessType(), contract.getContractType());
        }

        String contractId = contractMapper.selectIdByType(contract.getBusinessType());
        if (isEmpty(contractId)) {
            contract.setContractId(RandomStringUtils.getContractCode(contract.getBusinessType(), 0));
        } else {
            contract.setContractId(RandomStringUtils.getContractCode(contract.getBusinessType(), Integer.valueOf(contractId)));
        }

        contract.setContractName(contractName);
        contract.setContractStatus(0).setInvoiceStatus(0).setCollectionStatus(0);

        ServiceDetail serviceDetail = contract.getServiceDetails();
        if (!ObjectUtils.isEmpty(serviceDetail)) {
            serviceDetail.setServiceId(RandomStringUtils.getNextVal());
            serviceDetail.setContractId(contract.getContractId());
            serviceDetailMapper.insert(serviceDetail);
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            contract.setCreateTime(time);
            contract.setLastUpdateTime(time);
            contractMapper.insert(contract);
        } else {
            throw new BusinessException("请填写合同内容！");
        }
    }


    /**
     * 删除合同
     *
     * @param contractId
     */
    @Override
    public void delete(String contractId) {
        if (StringUtils.isEmpty(contractId)) {
            throw new BusinessException("请先选择一个合同！");
        }
        contractMapper.deleteAllDate(contractId);
        serviceDetailMapper.deleteByForeignKey(contractId);
        contractMapper.deleteByPrimaryKey(contractId);
    }

    /**
     * 更新合同 (修改完成时间)
     *
     * @param contract
     */
    @Override
    public void update(Contract contract) {
        if (!CollectionUtils.isEmpty(contract.getDateList())) {
            int count = 0;
            for (ContractDate date : contract.getDateList()) {
                if (StrUtil.isNotEmpty(date.getCompleteDate())) {
                    count++;
                }
                contractMapper.deleteDate(date.getContractId(), date.getType());
                date.setCompanyId(contract.getCompanyId());
                if (contract.getContractStatus().equals(0) || contract.getContractStatus().equals(1)) {
                    date.setStatus(2);
                }
                contractMapper.addDate(date);
            }
            if (count > 0) {
                if (contract.getContractStatus().equals(0) || contract.getContractStatus().equals(1)) {
                    contract.setContractStatus(2);
                }
            }
        }

        if (!ObjectUtils.isEmpty(contract.getServiceDetails())) {
            contract.getServiceDetails().setContractId(contract.getContractId());
            serviceDetailMapper.updateByPrimaryKeySelective(contract.getServiceDetails());
        } else {
            if (StrUtil.isNotEmpty(contract.getExpense())) {
                ServiceDetail serviceDetail = new ServiceDetail();
                serviceDetail.setExpense(contract.getExpense());
                serviceDetail.setContractId(contract.getContractId());
                contract.setServiceDetails(serviceDetail);
                serviceDetailMapper.updateByPrimaryKeySelective(contract.getServiceDetails());
            }
        }

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        contract.setLastUpdateTime(time);
        contractMapper.updateByPrimaryKeySelective(contract);
    }

    /**
     * 合同列表页
     *
     * @return
     */
    public PageInfo<Contract> list(Contract contract) {
        List<Contract> contracts = contractMapper.list();
        completeList(contracts);
        if (!StringUtils.isEmpty(contract.getContractName())) {
            contracts = contracts.stream().filter(c -> c.getContractName().contains(contract.getContractName())).collect(Collectors.toList());
        }
        if (!StringUtils.isEmpty(contract.getUserId())) {
            contracts = contracts.stream().filter(c -> !StringUtils.isEmpty(c.getUserId()) && c.getUserId().equals(contract.getUserId())).collect(Collectors.toList());
        }

        //根据公司名称分组
        // Map<String, List<Contract>> map = contracts.stream().collect(Collectors.groupingBy(c -> c.getCompanyName()));
        return new PageInfo<>(contract.getPageNum(), contract.getPageSize(), contracts);
    }

    /**
     * 公司列表页 (前端只需要取公司名和业务经理两个字段)
     */
    @Override
    public PageInfo<Company> companyList(Company company) {
        List<User> users = userMapper.list();
        if (CollectionUtil.isEmpty(users)) {
            return null;
        }

        List<Company> companies = companyMapper.list();
        if (CollectionUtil.isEmpty(companies)) {
            return null;
        }

        String roleName = users.stream().filter(u -> u.getUserId().equals(company.getUserId())).findFirst().get().getRoleName();
        if (roleName.equals("管理员") || roleName.equals("财务主管") || roleName.equals("财务助理")) {

        } else if (roleName.equals("业务助理")) {
            User user = users.stream().filter(c -> StrUtil.isNotEmpty(c.getAssistantIds())
                    && c.getAssistantIds().equals(company.getUserId())).findFirst().get();
            if (ObjectUtil.isNotEmpty(user)) {
                companies = companies.stream().filter(c -> c.getUserId().equals(user.getUserId())).collect(Collectors.toList());
            } else {
                return null;
            }
        } else {
            companies = companies.stream().filter(c -> c.getUserId().equals(company.getUserId())).collect(Collectors.toList());
        }

        if (StrUtil.isNotEmpty(company.getUserName())) {
            companies = companies.stream().filter(c -> StrUtil.isNotEmpty(c.getUserName()) && c.getUserName().contains(company.getUserName())).collect(Collectors.toList());
        }

        if (StrUtil.isNotEmpty(company.getCompanyName())) {
            companies = companies.stream().filter(c -> StrUtil.isNotEmpty(c.getCompanyName()) && c.getCompanyName().contains(company.getCompanyName())).collect(Collectors.toList());
        }
        return new PageInfo<>(company.getPageNum(), company.getPageSize(), companies);
    }

    /**
     * 根据公司名称展示合同列表
     */
    @Override
    public List<Contract> contractsByCompanyId(String companyId) {
        List<Contract> contracts = contractMapper.selectByCompanyId(companyId);
        if (CollectionUtil.isEmpty(contracts)) {
            return Collections.emptyList();
        }

        for (Contract c : contracts) {
            if (c.getBusinessType().equals(1)) {
                List<ContractDate> dateList = new ArrayList<>();
                String[] strs = c.getContractType().split(",");
                for (String str : strs) {
                    String completeDate = contractMapper.getDateByType(c.getContractId(), str);

                    dateList.add(new ContractDate(c.getContractId(), GXEnum.getNameByType(str), str, completeDate));
                }
                c.setDateList(dateList);
            } else {
                List<ContractDate> dateList = new ArrayList<>();
                String completeDate = contractMapper.getDateByType(c.getContractId(), c.getContractType());
                dateList.add(new ContractDate(c.getContractId(), "合同完成时间", c.getContractType(), completeDate));
                c.setDateList(dateList);
            }
        }
        return contracts;
    }


    public void completeList(List<Contract> contracts) {
        if (!CollectionUtils.isEmpty(contracts)) {
            for (Contract c : contracts) {
                ServiceDetail serviceDetails = serviceDetailMapper.selectByForeignKey(c.getContractId());
                c.setServiceDetails(serviceDetails);
                List<ContractDate> dateList = contractMapper.getDateList(c.getContractId());
                if (!CollectionUtils.isEmpty(dateList)) {
                    c.setDateList(dateList);
                }
            }
        }
    }

    /**
     * 根据公司名称展示数据 (折叠显示)
     *
     * @return
     */
    @Override
    public List<Contract> listByName(String userId, String companyName) {
        List<Contract> result = new ArrayList<>();
        List<Contract> contracts = contractMapper.list();
        if (CollectionUtils.isEmpty(contracts)) {
            return result;
        }
        List<Contract> cList = new ArrayList<>();
        List<User> users = userMapper.list();
        if (!CollectionUtils.isEmpty(users)) {
            Map<String, String> idMap = companyManageService.getIdMap();
            String roleName = users.stream().filter(u -> u.getUserId().equals(userId)).findFirst().get().getRoleName();
            if (roleName.equals("管理员") || roleName.equals("财务主管") || roleName.equals("财务助理")) {
                cList = contracts;
            } else if (roleName.equals("业务助理")) {
                User one = users.stream().filter(c -> !StringUtils.isEmpty(c.getAssistantIds())
                        && c.getAssistantIds().equals(userId)).findFirst().get();

                if (ObjectUtil.isNotEmpty(one)) {
                    for (Contract c : contracts) {
                        if (!CollectionUtils.isEmpty(idMap) && StrUtil.isNotEmpty(idMap.get(c.getCompanyId()))
                                && idMap.get(c.getCompanyId()).equals(one.getUserId())) {
                            cList.add(c);
                        }
                    }
                }
            } else {
                for (Contract c : contracts) {
                    if (!CollectionUtils.isEmpty(idMap) && !StringUtils.isEmpty(idMap.get(c.getCompanyId()))
                            && idMap.get(c.getCompanyId()).equals(userId)) {
                        cList.add(c);
                    }
                }
            }
        }

        if (!CollectionUtils.isEmpty(cList)) {
            Set<String> companyNames = cList.stream().map(Contract::getCompanyName).collect(Collectors.toSet());
            for (String name : companyNames) {
                Contract contract = new Contract(name);
                Map<String, String> companyMap = companyManageService.getCompanyMap();
                if (!CollectionUtils.isEmpty(companyMap)) {
                    contract.setUserName(companyMap.get(name));
                }

                List<Contract> contractList = cList.stream().filter(c -> c.getCompanyName().equals(name)).collect(Collectors.toList());
                Boolean dateFlag = true;
                for (Contract c : contractList) {
                    if (c.getBusinessType().equals(1)) {
                        List<ContractDate> dateList = new ArrayList<>();
                        String[] strs = c.getContractType().split(",");
                        for (String str : strs) {
                            String completeDate = contractMapper.getDateByType(c.getContractId(), str);
                            if (StringUtils.isEmpty(completeDate)) {
                                dateFlag = false;
                            }
                            dateList.add(new ContractDate(c.getContractId(), GXEnum.getNameByType(str), str, completeDate));
                        }
                        c.setDateList(dateList);
                    } else {
                        List<ContractDate> dateList = new ArrayList<>();
                        String completeDate = contractMapper.getDateByType(c.getContractId(), c.getContractType());
                        if (StringUtils.isEmpty(completeDate)) {
                            dateFlag = false;
                        }
                        dateList.add(new ContractDate(c.getContractId(), "合同完成时间", c.getContractType(), completeDate));
                        c.setDateList(dateList);

                        ServiceDetail serviceDetail = serviceDetailMapper.selectByForeignKey(c.getContractId());
                        if (serviceDetail != null) {
                            c.setExpense(serviceDetail.getExpense());
                        }
                    }
                }
                contract.setDateFlag(dateFlag);
                contract.setContracts(contractList);
                result.add(contract);
            }

            if (companyName != null) {
                result = result.stream().filter(c -> c.getCompanyName().contains(companyName)).collect(Collectors.toList());
            }
        }
        return result;
    }

    /**
     * 根据表中数据获取合同模板
     *
     * @param contractType
     * @return
     */
    public String getHtmlContentByType(Integer businessType, String contractType, String companyId) {
        String result = contentMapper.getContentByType(businessType, contractType).getContent();
        Company company = companyManageService.selectOne(companyId);
        if (CharSequenceUtil.isNotEmpty(result) && !ObjectUtils.isEmpty(company)) {
            result = result.replace("{companyName}", "" + Objects.toString(company.getCompanyName(), "") + "")
                    .replace("{address}", "" + Objects.toString(company.getAddress(), "") + "")
                    .replace("{director}", "" + Objects.toString(company.getDirector(), "") + "")
                    .replace("{phone}", "" + Objects.toString(company.getPhone(), "") + "")
                    .replace("{signDate}", new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
        }
        return result;
    }

    /**
     * 获取html文件内容
     *
     * @param fileName
     * @return
     */
    private String getHtmlContent(String fileName) {
        String result = "";
        try (InputStream input = new FileInputStream(new File(PATH + fileName));
             BufferedReader br = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            result = br.lines().parallel()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (FileNotFoundException e) {
            throw new BusinessException("ERROR:文件未找到！");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将html转换成word文件
     *
     * @param contractId
     */
    public String htmlToWord(String contractId) {
        Contract contract = contractMapper.selectByPrimaryKey(contractId);
        if (ObjectUtils.isEmpty(contract)) {
            throw new BusinessException("该合同不存在，请确认！");
        }
        String contractName = contract.getContractName();
        if (StringUtils.isEmpty(contractName)) {
            throw new BusinessException("合同名称不能为空！");
        }
        String contractContent = "<html>" + contractMapper.getContractContent(contractId) + "</html>";
        File file = new File(PATH);
        if (file.exists()) {
            try {
                byte b[] = contractContent.getBytes(StandardCharsets.UTF_8);
                ByteArrayInputStream baIS = new ByteArrayInputStream(b);
                POIFSFileSystem pfs = new POIFSFileSystem();
                DirectoryEntry directory = pfs.getRoot();
                directory.createDocument("WordDocument", baIS);
                FileOutputStream fos = new FileOutputStream(WORD_PATH + contractId + "-" + contractName + ".doc");
                pfs.writeFilesystem(fos);
                baIS.close();
                fos.close();
            } catch (IOException e) {
                throw new BusinessException("ERROR：word文件生成失败，" + e.getMessage());
            }
        }
        String path = WORD_PATH + contractId + "-" + contractName + ".doc";
        return path;
    }


    /**
     * 将html转换成pdf
     *
     * @param fileName
     */
    public void htmlToPDF(String fileName) {
        //读取html的流
        try (InputStream inputStream = new FileInputStream(PATH + fileName + ".html");
             OutputStream os = new FileOutputStream(PATH + fileName + ".pdf")) {
            //流转换成字符串
            StringBuilder out = new StringBuilder();
            byte[] b = new byte[4096];
            for (int n; (n = inputStream.read(b)) != -1; ) {
                out.append(new String(b, 0, n));
            }

            String html = out.toString();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            ITextFontResolver fontResolver = renderer.getFontResolver();
            /*添加字体支持,路径可以自身项目的实际情况设置，我这里是本地项目，而且为了方便测试，就写成固定的了
            实际项目中，可以获取改字体所在真实的服务器的路径,这个方法是本地地址和网络地址都支持的
            这里面添加的是宋体*/
            fontResolver.addFont("c:/Windows/Fonts/simsun.ttc",
                    BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            renderer.layout();
            renderer.createPDF(os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    /**
     * 下载文件
     */
    public void downLoadFile(String path, HttpServletRequest request, HttpServletResponse response) {
        File file = new File(path);
        String fileName = file.getName();
        String browser = request.getHeader("USER-AGENT");
        try {
            if (!StringUtils.isEmpty(browser) && browser.indexOf("Fireforx") != -1) {
                fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), "iso-8859-1");
            } else {
                fileName = URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8));
            }
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException("ERROR:不支持该字符集," + e.getMessage());
        }
        OutputStream os = null;
        response.reset();
        response.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        response.setContentType("multipart/form-data");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
            byte[] data = new byte[bis.available() + 1000];
            int i = 0;
            //直接下载
            os = response.getOutputStream();
            while ((i = bis.read(data)) != -1) {
                os.write(data, 0, i);
            }
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            throw new BusinessException("ERROR:无法找到该文件，" + e.getMessage());
        } catch (IOException e) {
            throw new BusinessException("ERROR: details is " + e.getMessage());
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 下载word文件
     *
     * @param contractId
     * @param request
     * @param response
     */
    public void downLoadWord(String contractId, HttpServletRequest request, HttpServletResponse response) {
        String path = this.htmlToWord(contractId);
        this.downLoadFile(path, request, response);
    }

    /**
     * 预览合同
     *
     * @param contractId
     * @return
     */
    @Override
    public ResultDTO preview(String contractId) {
        ResultDTO resultDTO = new ResultDTO();
        if (StringUtils.isEmpty(contractId)) {
            throw new BusinessException("请选择要预览的合同！");
        }
        String htmlContent = contractMapper.getContractContent(contractId);
        resultDTO.setHtml(htmlContent);
        List<String> fields = getFields(new ServiceDetail());
        resultDTO.setFields(fields);
        return resultDTO;
    }

    /**
     * 将某个类的属性对位list中的值
     *
     * @param object
     * @return
     */
    public List<String> getFields(Object object) {
        List<String> fields = new ArrayList<>();
        for (Field field : object.getClass().getDeclaredFields()) {
            fields.add(field.getName());
        }
        return fields;
    }

    public void createHtml(String contractId) {
        String htmlContent = contractMapper.getContractContent(contractId);
        File file = new File(HTML_PATH + "htmlContent.html");
        try (
                OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
                BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(htmlContent);
            bw.flush();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new BusinessException("ERROR: 文件没有找到," + e.getMessage());
        } catch (IOException e) {
            throw new BusinessException("ERROR: IO异常，" + e.getMessage());
        }
    }
}
