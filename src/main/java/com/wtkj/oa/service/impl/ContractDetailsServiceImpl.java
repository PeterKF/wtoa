package com.wtkj.oa.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.wtkj.oa.common.constant.ContractEnum;
import com.wtkj.oa.common.constant.GXEnum;
import com.wtkj.oa.common.constant.PatentEnum;
import com.wtkj.oa.dao.CompanyMapper;
import com.wtkj.oa.dao.ContractMapper;
import com.wtkj.oa.dao.PatentMapper;
import com.wtkj.oa.dao.UserMapper;
import com.wtkj.oa.entity.*;
import com.wtkj.oa.exception.BusinessException;
import com.wtkj.oa.service.IContractDetailsService;
import com.wtkj.oa.service.IContractManageService;
import com.wtkj.oa.utils.RandomStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.text.CharSequenceUtil.isEmpty;

/**
 * @author chenq
 */
@Service
@Slf4j
public class ContractDetailsServiceImpl implements IContractDetailsService {
    @Resource
    private ContractMapper contractMapper;

    @Resource
    private IContractManageService contractManageService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CompanyMapper companyMapper;

    @Resource
    private PatentMapper patentMapper;

    @Override
    public List<ContractDetail> list(String companyId) {
        List<ContractDetail> contractDetails = new ArrayList<>();
        List<Contract> contracts = contractMapper.selectByCompanyId(companyId);
        contractManageService.completeList(contracts);
        if (!CollectionUtils.isEmpty(contracts)) {
            for (Contract c : contracts) {
                if (!ObjectUtils.isEmpty(c.getBusinessType()) && !isEmpty(c.getContractType()) && !ObjectUtils.isEmpty(c.getServiceDetails())) {
                    //高新技术
                    if (c.getBusinessType().equals(1)) {
                        if (!isEmpty(c.getContractType())) {
                            String[] types = c.getContractType().split(",");
                            for (String t : types) {
                                ContractDetail detail = new ContractDetail(GXEnum.getNameByType(t));
                                ContractDate cd = contractMapper.getStatus(c.getContractId(), t);
                                if (!ObjectUtils.isEmpty(cd)) {
                                    BeanUtils.copyProperties(cd, detail);
                                }

                                detail.setNumber(1);
                                if ("1".equals(t)) {
                                    detail.setUnitFee(Integer.valueOf(c.getServiceDetails().getSumFee()));
                                    detail.setEarlyFee(Integer.valueOf((c.getServiceDetails().getBeforeFee())));
                                } else {
                                    detail.setEarlyFee(0);

                                    switch (t) {
                                        case "2":
                                            detail.setUnitFee(Integer.valueOf((c.getServiceDetails().getCountryTecFee())));
                                            break;
                                        case "3":
                                            detail.setUnitFee(Integer.valueOf((c.getServiceDetails().getProvinceTecFee())));
                                            break;
                                        case "4":
                                            detail.setUnitFee(Integer.valueOf((c.getServiceDetails().getCityTecFee())));
                                            break;
                                        case "5":
                                            detail.setUnitFee(Integer.valueOf((c.getServiceDetails().getEnterpriseTecFee())));
                                            break;
                                        case "6":
                                            if (!isEmpty(c.getServiceDetails().getPatentFee())) {
                                                ContractDetail c1 = new ContractDetail("发明专利");
                                                c1.setContractId(c.getContractId());
                                                if (cd != null) {
                                                    c1.setStatus(cd.getStatus());
                                                }
                                                c1.setUnitFee(Integer.valueOf(c.getServiceDetails().getPatentFee()));
                                                contractDetails.add(c1);
                                            }
                                            if (!isEmpty(c.getServiceDetails().getPracticalFee())) {
                                                ContractDetail c2 = new ContractDetail("实用新型");
                                                c2.setContractId(c.getContractId());
                                                if (cd != null) {
                                                    c2.setStatus(cd.getStatus());
                                                }
                                                c2.setUnitFee(Integer.valueOf(c.getServiceDetails().getPracticalFee()));
                                                contractDetails.add(c2);
                                            }
                                            if (!isEmpty(c.getServiceDetails().getAppearanceFee())) {
                                                ContractDetail c3 = new ContractDetail("外观专利");
                                                c3.setContractId(c.getContractId());
                                                if (cd != null) {
                                                    c3.setStatus(cd.getStatus());
                                                }
                                                c3.setUnitFee(Integer.valueOf(c.getServiceDetails().getAppearanceFee()));
                                                contractDetails.add(c3);
                                            }
                                            if (!isEmpty(c.getServiceDetails().getSoftFee())) {
                                                ContractDetail c4 = new ContractDetail("软件著作权");
                                                c4.setContractId(c.getContractId());
                                                if (cd != null) {
                                                    c4.setStatus(cd.getStatus());
                                                }
                                                c4.setUnitFee(Integer.valueOf(c.getServiceDetails().getSoftFee()));
                                                contractDetails.add(c4);
                                            }
                                            break;
                                        default:
                                            detail.setUnitFee(Integer.valueOf((c.getServiceDetails().getGuidanceFee())));

                                    }
                                }
                                detail.setContractId(c.getContractId());
                                detail.setType(t);

                                if (!CollectionUtils.isEmpty(c.getDateList())) {
                                    String date = c.getDateList().stream().filter(contractDate -> contractDate.getType().equals(t))
                                            .findFirst().get().
                                                    getCompleteDate();
                                    detail.setCompleteDate(date);
                                }

                                if (!ObjectUtils.isEmpty(detail.getSumFee())) {
                                    detail.setLaterFee(detail.getSumFee() - detail.getEarlyFee());
                                }
                                contractDetails.add(detail);
                            }
                        }
                    } else {
                        getOtherBusiness(contractDetails, c);
                    }
                    contractDetails = contractDetails.stream().sorted(Comparator.comparing(detail -> detail.getType(),
                            Comparator.nullsLast(String::compareTo))).collect(Collectors.toList());
                }
            }
        }
        return contractDetails;
    }

    /**
     * 其它业务
     *
     * @param contractDetails 合同详情
     * @param c               合同
     */
    private void getOtherBusiness(List<ContractDetail> contractDetails, Contract c) {
        ContractDetail detail = new ContractDetail(ContractEnum.getNameByType(c.getBusinessType(), c.getContractType()));
        ContractDate cd = contractMapper.getStatus(c.getContractId(), c.getContractType());
        if (!ObjectUtils.isEmpty(cd)) {
            BeanUtils.copyProperties(cd, detail);
        }

        detail.setUnitFee(Integer.parseInt((c.getServiceDetails().getExpense())));

        //默认为1
        if (c.getBusinessType().equals(2) || c.getBusinessType().equals(3)) {
            if (c.getContractType().equals("3")) {
                detail.setNumber(StrUtil.isNotEmpty(c.getServiceDetails().getPercent()) ?
                        Float.valueOf(c.getServiceDetails().getPercent()) / 100 : 1);
            }
        }

        //前端付款
        detail.setEarlyFee(0);
        //后期付款
        detail.setLaterFee(detail.getSumFee() - detail.getEarlyFee());
        //完成时间
        if (!CollectionUtils.isEmpty(c.getDateList())) {
            String date = c.getDateList().stream().filter(contractDate -> contractDate.getType().equals(c.getContractType()))
                    .findFirst().get().getCompleteDate();
            detail.setCompleteDate(date);
        }
        detail.setContractId(c.getContractId());
        detail.setType(c.getContractType());
        detail.setStatus(c.getContractStatus());
        contractDetails.add(detail);
    }

    /**
     * 更新合同状态
     *
     * @param contractDate
     */
    @Override
    public void updateStatus(ContractDate contractDate) {
        if (isEmpty(contractDate.getContractId())) {
            throw new BusinessException("请选择要编辑的合同");
        }

        Contract contract = contractMapper.selectByPrimaryKey(contractDate.getContractId());
        if (contract.getContractStatus().equals(0) || contract.getContractStatus().equals(1)) {
            throw new BusinessException("请先上传合同附件，再修改合同状态");
        }
        contractMapper.updateContractDate(contractDate);

        List<Integer> status = contractMapper.getStatusById(contractDate.getContractId());
        if (!CollectionUtils.isEmpty(status) && !status.contains(null)) {
            Integer max = contractMapper.getMaxStatus(contractDate.getContractId());
            Integer min = contractMapper.getMinStatus(contractDate.getContractId());
            if (!ObjectUtils.isEmpty(max) && !ObjectUtils.isEmpty(min) && max.equals(min)) {
                contractMapper.updateByPrimaryKeySelective(new Contract(contractDate.getContractId(), max));
            }
        }
    }

    /**
     * 上传文件
     */
    @Override
    public void uploadFile(MultipartFile file, String contractId) {
        if (isEmpty(contractId)) {
            throw new BusinessException("请先选择一个需要上传文件的合同！");
        }

        Contract contract = contractMapper.selectByPrimaryKey(contractId);
        if (ObjectUtil.isNotEmpty(contract.getContractStatus()) && contract.getContractStatus().equals(0)) {
            throw new BusinessException("当前合同处于待签定状态，不可上传文件!");
        }

        if (file == null) {
            throw new BusinessException("请选择上传文件！");
        }

        File f = new File("resource/contract");
        if (!f.exists()) {
            f.mkdir();
        }
        String fileName = "";
        try {
            fileName = new String(file.getOriginalFilename().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            f = new File(System.getProperty("user.dir") + "/resource/contract/", fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            file.transferTo(f);
        } catch (IOException e) {
            throw new BusinessException("上传文件失败！" + e);
        }

        contractMapper.updateByPrimaryKeySelective(new Contract(contractId, fileName, 1));
        contractMapper.updateContractDate(new ContractDate().setStatus(1));
    }

    /**
     * 预览文件
     *
     * @param response
     */
    @Override
    public void viewFile(HttpServletResponse response, String contractId) {
        if (isEmpty(contractId)) {
            throw new BusinessException("请先选择一个合同！");
        }

        String fileName = contractMapper.selectByPrimaryKey(contractId).getFileName();
        if (isEmpty(fileName)) {
            throw new BusinessException("该合同没有上传相应的合同文件！");
        }

        File f = new File(System.getProperty("user.dir") + "/resource/contract/" + fileName);
        if (!f.exists()) {
            throw new BusinessException("该合同没有上传相应的合同文件！");
        }

        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = in.read(buf)) != -1) {
                response.getOutputStream().write(buf, 0, len);
            }
            in.close();
        } catch (IOException e) {
            throw new BusinessException("合同文件读取失败！" + e);
        }
    }
}
