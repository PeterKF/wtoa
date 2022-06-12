package com.wtkj.oa.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.wtkj.oa.common.constant.PatentEnum;
import com.wtkj.oa.dao.*;
import com.wtkj.oa.entity.Company;
import com.wtkj.oa.entity.Contract;
import com.wtkj.oa.entity.ContractDate;
import com.wtkj.oa.entity.Patent;
import com.wtkj.oa.exception.BusinessException;
import com.wtkj.oa.service.ICompanyManageService;
import com.wtkj.oa.service.IContractManageService;
import com.wtkj.oa.service.IHTContractService;
import com.wtkj.oa.service.InitDataService;
import com.wtkj.oa.utils.RandomStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.SheetUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description 初始化数据实现类
 * @Date 2021/11/9 10:43
 * @Author Peter.Chen
 */
@Service
@Slf4j
public class InitDataServiceImpl implements InitDataService {

    @Resource
    private IContractManageService contractManageService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CompanyMapper companyMapper;

    @Resource
    private PatentMapper patentMapper;

    @Resource
    private ContractMapper contractMapper;

    @Resource
    private IHTContractService htContractServiceImpl;

    @Resource
    private ICompanyManageService companyManageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String initCompanies(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("请先选择上传文件");
        }

        int count = 0;
        try (ExcelReader reader = ExcelUtil.getReader(file.getInputStream(), 0)) {
            List<List<Object>> objectList = reader.read();
            if (CollUtil.isNotEmpty(objectList)) {
                for (int i = 1; i < objectList.size(); i++) {
                    List<Object> objects = objectList.get(i);
                    List<String> comNames = new ArrayList<>();
                    String companyId = String.valueOf(objects.get(0));
                    if (CharSequenceUtil.isEmpty(companyId)) {
                        //客户id为空时，新增客户
                        Company company = new Company().setCompanyId(RandomStringUtils.getNextVal())
                                .setYear(String.valueOf(objects.get(1)))
                                .setCompanyName(String.valueOf(objects.get(2)))
                                .setCreditCode(String.valueOf(objects.get(3)))
                                .setAddress(String.valueOf(objects.get(4)))
                                .setContact(String.valueOf(objects.get(6)))
                                .setTelephone(String.valueOf(objects.get(7)))
                                .setDirector(String.valueOf(objects.get(8)))
                                .setPhone(String.valueOf(objects.get(9)))
                                .setRegion(String.valueOf(objects.get(10)));
                        if (CollUtil.isEmpty(comNames) || !comNames.contains(company.getCompanyName())) {
                            comNames.add(company.getCompanyName());
                            String userId = userMapper.getIdByName(String.valueOf(objects.get(5)));
                            company.setUserId(userId);
                            companyMapper.insert(company);
                            count++;
                        }
                    } else {
                        //客户id不为空时，编辑客户
                        Company company = new Company().setCompanyId(companyId)
                                .setYear(String.valueOf(objects.get(1)))
                                .setCompanyName(String.valueOf(objects.get(2)))
                                .setCreditCode(String.valueOf(objects.get(3)))
                                .setAddress(String.valueOf(objects.get(4)))
                                .setContact(String.valueOf(objects.get(6)))
                                .setTelephone(String.valueOf(objects.get(7)))
                                .setDirector(String.valueOf(objects.get(8)))
                                .setPhone(String.valueOf(objects.get(9)))
                                .setRegion(String.valueOf(objects.get(10)));
                        String userId = userMapper.getIdByName(String.valueOf(objects.get(5)));
                        company.setUserId(userId);
                        companyMapper.updateByPrimaryKeySelective(company);
                    }
                }
            }
        } catch (IOException e) {
            log.warn("IO Error is", e);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("文档中有重复的客户名，详情报错信息:" + e.getCause());
        }

        return "添加客户数：" + count + "条！";
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletePatents(MultipartFile file) {
        try {
            List<List<Object>> dataList = ExcelUtil.getReader(file.getInputStream(), 0).read();
            for (int i = 0; i < dataList.size(); i++) {
                String companyName = String.valueOf(dataList.get(i).get(0));
                String companyId = companyMapper.getIdByName(companyName);
                contractMapper.deleteByType(companyId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化合同信息
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String initPatents(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("请先选择上传文件");
        }
        List<List<Object>> dataList = new ArrayList<>();
        try {
            dataList = ExcelUtil.getReader(file.getInputStream(), 0).read();
        } catch (IOException e) {
            log.warn("IO ERROR,", e);
        }
        List<Patent> patents = new ArrayList<>();

        if (CollUtil.isNotEmpty(dataList)) {
            List<String> ids = patentMapper.getPatentIds();
            String companyId = "";
            for (int i = 1; i < dataList.size(); i++) {
                List<Object> list = dataList.get(i);
                String companyName = String.valueOf(list.get(2));
                companyId = companyMapper.getIdByName(companyName);

                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String applicationDate = String.valueOf(list.get(1)).split(" ")[0];

                String patentId = String.valueOf(list.get(0));

                if (Strings.isEmpty(patentId)) {
                    patentId = "";
                }

                Patent patent = new Patent().setId(RandomStringUtils.getNextVal()).setPatentId(patentId)
                        .setCompanyId(companyId).setApplicationDate(applicationDate)
                        .setPatentName(String.valueOf(list.get(3)))
                        .setPatentType(PatentEnum.getTypeByName(String.valueOf(list.get(4))))
                        .setCreateTime(date).setLastUpdateTime(date);

                if (ids.stream().noneMatch(id -> id.equals(patent.getPatentId()))) {
                    patents.add(patent);
                } else {
                    patentMapper.updateByPrimaryKeySelective(patent);
                }
            }

            if (!CollUtil.isEmpty(patents)) {
                patentMapper.insertBatch(patents);
            }
        }
        return "初始化知识产权" + patents.size() + "条！";
    }

    /**
     * 批量导入中小型合同
     *
     * @param file
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String initContracts(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("请先选择上传的文件");
        }

        List<Contract> contracts = new ArrayList<>();
        try (ExcelReader reader = ExcelUtil.getReader(file.getInputStream(), 0)) {
            List<List<Object>> objectList = reader.read();
            if (CollUtil.isNotEmpty(objectList)) {
                int i;
                for (i = 1; i < objectList.size(); i++) {
                    Contract contract = new Contract();
                    String number = "";
                    if (i == 1) {
                        number = contractMapper.selectIdByType(2);
                    } else {
                        number = contracts.get(i - 2).getContractId().substring(8);
                    }

                    if (CharSequenceUtil.isEmpty(number)) {
                        contract.setContractId(RandomStringUtils.getContractCode(2, 0));
                    } else {
                        String contractId = RandomStringUtils.getContractCode(2, Integer.valueOf(number));
                        contract.setContractId(contractId);
                    }

                    contract.setContractName("浙江省科技型中小企业");
                    contract.setBusinessType(2).setContractType("4").setCollectionStatus(0).setInvoiceStatus(0);
                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    contract.setCreateTime(date).setLastUpdateTime(date);

                    String companyName = String.valueOf(objectList.get(i).get(0));
                    String companyId = companyMapper.getIdByName(companyName);
                    if (StrUtil.isEmpty(companyId)) {
                        throw new BusinessException(companyName + "没有配置，请先配置");
                    }
                    contract.setCompanyId(companyId);

                    String userId = userMapper.getIdByName(String.valueOf(objectList.get(i).get(1)));
                    contract.setUserId(userId);

                    String completeDate = String.valueOf(objectList.get(i).get(3)).substring(0, 10);
                    contract.setCompleteDate(completeDate);

                    //合同中甲方信息
                    String content = contractManageService.getHtmlContentByType(2, "4", companyId);

                    //合同中乙方信息
                    String city = String.valueOf(objectList.get(i).get(2));
                    if ("杭州".equals(city)) {
                        content = htContractServiceImpl.getHtmlContent(1, content);
                    } else {
                        content = htContractServiceImpl.getHtmlContent(2, content);
                    }

                    contract.setContractFile(content).setContractStatus(2);
                    contracts.add(contract);
                }
                //添加到合同表中
                try {
                    contractMapper.batchInsert(contracts);
                } catch (DuplicateKeyException e) {
                    throw new BusinessException("数据重复，原因：" + e.getMessage());
                }

                //添加到合同时间表中 （用于添加完成时间）
                for (Contract c : contracts) {
                    ContractDate cDate = new ContractDate();
                    cDate.setCompanyId(c.getCompanyId()).setContractId(c.getContractId()).setType(c.getContractType())
                            .setStatus(2).setCompleteDate(c.getCompleteDate());
                    try {
                        contractMapper.addDate(cDate);
                    } catch (DuplicateKeyException e) {
                        contractMapper.updateContractDate(cDate);
                    }
                }
            }
        } catch (IOException e) {
            log.warn("IO Error,", e);
        }
        return "初始化" + contracts.size() + "份浙江省科技型中小企业";
    }

    /**
     * 下载excel模板
     *
     * @param fileType
     * @param response
     */
    public void exportExcel(String fileType, HttpServletResponse response) {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        List<String> titles = new ArrayList<>();
        String fileName = "";
        if ("1".equals(fileType)) {
            fileName = "客户名单.xlsx";
            titles = CollUtil.newArrayList("客户ID(默认不填)", "年份", "客户名称", "统一社会信用代码", "客户地址", "项目经理", "联系人", "联系电话", "企业负责人", "负责人电话", "所属地区");
        } else if ("2".equals(fileType)) {
            fileName = "专利清单.xlsx";
            titles = CollUtil.newArrayList("申请号", "申请日", "公司名称", "申请名称", "类型");
        } else {
            fileName = "浙江省中小型企业合同清单.xlsx";
            titles = CollUtil.newArrayList("客户名称", "客户经理", "乙方公司地区", "完成时间");
        }
        try {
            fileName = URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8));
        } catch (UnsupportedEncodingException e) {
            log.info("Unsupported encoding", e);
        }
        Font font = writer.createFont();
        font.setBold(true);
        font.setFontName("宋体");
        writer.getStyleSet().setFont(font, true);
        writer.writeRow(titles, true);
        //response为HttpServletResponse对象
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        try (ServletOutputStream out = response.getOutputStream();) {
            writer.flush(out, true);
        } catch (IOException e) {
            log.info("error message", e);
        }
        writer.close();
    }


    /**
     * 导出客户信息
     *
     * @param response
     */
    @Override
    public void exportCompanyInfo(HttpServletResponse response) {
        List<Company> companyList = companyMapper.companyInfo();
        if (CollUtil.isEmpty(companyList)) {
            return;
        }

        List<Map<String, Object>> mapList = new ArrayList<>(companyList.size());
        for (Company com : companyList) {
            Map<String, Object> comInfoMap = new LinkedHashMap<>();
            comInfoMap.put("客户ID(不可编辑)", com.getCompanyId());
            comInfoMap.put("年份", com.getYear());
            comInfoMap.put("客户名称", com.getCompanyName());
            comInfoMap.put("统一社会信用代码", com.getCreditCode());
            comInfoMap.put("客户地址", com.getAddress());
            comInfoMap.put("项目经理", com.getUserName());
            comInfoMap.put("联系人", com.getContact());
            comInfoMap.put("联系电话", com.getTelephone());
            comInfoMap.put("企业负责人", com.getDirector());
            comInfoMap.put("负责人电话", com.getPhone());
            comInfoMap.put("所属地区", com.getRegion());
            mapList.add(comInfoMap);
        }

        ExcelWriter writer = com.wtkj.oa.common.config.ExcelWriter.getBigWriter();
        Font font = writer.createFont();
        //设置字体
        font.setFontName("宋体");
        writer.getStyleSet().setFont(font, true);
        writer.write(mapList, true);
        //所有列宽度自适应
        writer.autoSizeColumnAll();

        //response为HttpServletResponse对象
        // response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=companyInfo.xlsx");
        try (ServletOutputStream out = response.getOutputStream();) {
            writer.flush(out, true);
        } catch (IOException e) {
            log.info("error message", e);
        }
        writer.close();
    }

    /**
     * 导出专利信息
     *
     * @param response
     */
    @Override
    public void exportPatentInfo(HttpServletResponse response) {
        List<Patent> patentList = patentMapper.listByName(null);
        if (CollUtil.isEmpty(patentList)) {
            return;
        }

        List<Map<String, Object>> mapList = new ArrayList<>(patentList.size());
        Map<String, String> companyMap = companyManageService.getCompanyMap();
        String userName = "";
        for (Patent patent : patentList) {
            Map<String, Object> patentMap = new LinkedHashMap();
            patentMap.put("年份", patent.getApplicationDate().split("-")[0]);
            patentMap.put("专利编号", patent.getPatentId());
            patentMap.put("专利名称", patent.getPatentName());
            patentMap.put("客户名称", patent.getCompanyName());

            if (CollUtil.isNotEmpty(companyMap)) {
                userName = companyMap.get(patent.getCompanyName());
            }
            patentMap.put("项目经理", userName);
            patentMap.put("专利类型", PatentEnum.getNameByType(patent.getPatentType()));
            mapList.add(patentMap);
        }

        ExcelWriter writer = com.wtkj.oa.common.config.ExcelWriter.getBigWriter();
        Font font = writer.createFont();
        //设置字体
        font.setFontName("宋体");
        writer.getStyleSet().setFont(font, true);
        writer.write(mapList, true);
        //所有列宽度自适应
        writer.autoSizeColumnAll();

        //response为HttpServletResponse对象
        // response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=patentInfo.xlsx");
        try (ServletOutputStream out = response.getOutputStream();) {
            writer.flush(out, true);
        } catch (IOException e) {
            log.info("error message", e);
        }
        writer.close();
    }
}
