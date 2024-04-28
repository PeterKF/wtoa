package com.wtkj.oa.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.wtkj.oa.common.constant.ContractEnum;
import com.wtkj.oa.common.constant.GXEnum;
import com.wtkj.oa.dao.*;
import com.wtkj.oa.entity.Company;
import com.wtkj.oa.entity.Contract;
import com.wtkj.oa.entity.ContractDate;
import com.wtkj.oa.entity.ContractDetail;
import com.wtkj.oa.exception.BusinessException;
import com.wtkj.oa.service.IContractDetailsService;
import com.wtkj.oa.service.IContractManageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
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
    @Resource
    private ContractDateMapper contractDateMapper;

    @Override
    public List<ContractDetail> list(String companyId) {
        List<ContractDetail> contractDetails = new ArrayList<>();
        List<Contract> contracts = contractMapper.selectByCompanyId(companyId);
        contractManageService.completeList(contracts);
        if (!CollectionUtils.isEmpty(contracts)) {
            for (Contract c : contracts) {
                if (!ObjectUtils.isEmpty(c.getBusinessType()) && !isEmpty(c.getContractType()) &&
                        !ObjectUtils.isEmpty(c.getServiceDetails())) {
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
                                    detail.setUnitFee(Double.valueOf(c.getServiceDetails().getSumFee()));
                                    detail.setEarlyFee(new BigDecimal(c.getServiceDetails().getBeforeFee()));
                                } else {
                                    detail.setEarlyFee(BigDecimal.valueOf(0));

                                    getContractDetails(contractDetails, c, t, detail, cd);
                                }
                                detail.setContractId(c.getContractId());
                                detail.setType(t);

                                if (!CollectionUtils.isEmpty(c.getDateList())) {
                                    Optional<ContractDate> optional = c.getDateList().stream().filter(contractDate -> contractDate.getType().equals(t))
                                            .findFirst();
                                    if (optional.isPresent()) {
                                        String date = optional.get().getCompleteDate();
                                        detail.setCompleteDate(date);
                                    }
                                }

                                if (!ObjectUtils.isEmpty(detail.getSumFee())) {
                                    detail.setLaterFee(detail.getSumFee().subtract(detail.getEarlyFee()));
                                }
                                contractDetails.add(detail);
                            }
                        }
                    } else {
                        getOtherBusiness(contractDetails, c);
                    }
                }
            }
            contractDetails = contractDetails.stream().sorted(Comparator.comparing(ContractDetail::getCompleteDate,
                    Comparator.nullsFirst(String::compareTo)).thenComparing(detail -> detail.getType(),
                    Comparator.nullsFirst(String::compareTo)).reversed()).collect(Collectors.toList());
        }
        return contractDetails;
    }

    /**
     * 获取除了高新合同以外的合同详情
     *
     * @param contractDetails
     * @param c
     * @param t
     * @param detail
     * @param cd
     */
    private void getContractDetails(List<ContractDetail> contractDetails, Contract c, String t, ContractDetail detail, ContractDate cd) {
        Boolean flag=true;
        if(t.length()>1 && t.substring(0,1).equals("5")){
            t="5";
        }
        if(t.length()>1 && t.substring(0,2).equals("10")){
            flag=false;
            t="5";
        }
        switch (t) {
            case "2":
                detail.setUnitFee(Double.valueOf((c.getServiceDetails().getCountryTecFee())));
                break;
            case "3":
                detail.setUnitFee(Double.valueOf((c.getServiceDetails().getProvinceTecFee())));
                break;
            case "4":
                detail.setUnitFee(Double.valueOf((c.getServiceDetails().getCityTecFee())));
                break;
            case "5":
                //企业研发费加计扣除
                    if(flag){
                        ContractDate contractDate = contractDateMapper.selectOne(new QueryWrapper<>(new ContractDate().setContractId(c.getContractId()).setType(t)).select("contract_year"));
                        if(contractDate==null){

                        }else {
                            detail.setNumber(Float.parseFloat(contractDate.getContractYear()==null?"1":contractDate.getContractYear()));
                        }
                    }else {
                        detail.setNumber(Float.parseFloat(c.getServiceDetails().getPercent()==null?"1":c.getServiceDetails().getPercent()));
                    }
                detail.setUnitFee(Double.valueOf(c.getServiceDetails().getEnterpriseTecFee()));
                break;
            case "6":
                if (!isEmpty(c.getServiceDetails().getPatentFee())) {
                    ContractDetail c1 = new ContractDetail("发明专利");
                    c1.setContractId(c.getContractId());
                    if (cd != null) {
                        c1.setStatus(cd.getStatus());
                    }
                    c1.setUnitFee(Double.valueOf(c.getServiceDetails().getPatentFee()));
                    contractDetails.add(c1);
                }
                if (!isEmpty(c.getServiceDetails().getPracticalFee())) {
                    ContractDetail c2 = new ContractDetail("实用新型");
                    c2.setContractId(c.getContractId());
                    if (cd != null) {
                        c2.setStatus(cd.getStatus());
                    }
                    c2.setUnitFee(Double.valueOf(c.getServiceDetails().getPracticalFee()));
                    contractDetails.add(c2);
                }
                if (!isEmpty(c.getServiceDetails().getAppearanceFee())) {
                    ContractDetail c3 = new ContractDetail("外观专利");
                    c3.setContractId(c.getContractId());
                    if (cd != null) {
                        c3.setStatus(cd.getStatus());
                    }
                    c3.setUnitFee(Double.valueOf(c.getServiceDetails().getAppearanceFee()));
                    contractDetails.add(c3);
                }
                if (!isEmpty(c.getServiceDetails().getSoftFee())) {
                    ContractDetail c4 = new ContractDetail("软件著作权");
                    c4.setContractId(c.getContractId());
                    if (cd != null) {
                        c4.setStatus(cd.getStatus());
                    }
                    c4.setUnitFee(Double.valueOf(c.getServiceDetails().getSoftFee()));
                    contractDetails.add(c4);
                }
                break;
            case "10":
                detail.setUnitFee(0d);
                detail.setNumber(Float.valueOf(c.getServiceDetails().getPercent()));
                break;
            default:
                detail.setUnitFee(Double.valueOf((c.getServiceDetails().getGuidanceFee())));

        }
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

        detail.setUnitFee(Double.valueOf((c.getServiceDetails().getExpense())));

        //默认为1
        if (c.getBusinessType().equals(2)) {
            //各级科技项目
            if (c.getContractType().equals("3")) {
                detail.setNumber(StrUtil.isNotEmpty(c.getServiceDetails().getPercent()) ?
                        Float.parseFloat(c.getServiceDetails().getPercent())  : 1);
                detail.setUnitFee(Double.valueOf(Objects.toString(c.getContractMatter(), "0")));
            }
        } else if (c.getBusinessType().equals(3) && "3".equals(c.getContractType())) {
            //企业研发费加计扣除
            detail.setNumber(StrUtil.isNotEmpty(c.getServiceDetails().getPercent()) ?
                    Float.parseFloat(c.getServiceDetails().getPercent()) : 1);
            detail.setUnitFee(Double.valueOf(Objects.toString(c.getContractMatter(), "0")));
        }

        //前端付款
        detail.setEarlyFee(BigDecimal.valueOf(0));
        //后期付款
        detail.setLaterFee(detail.getSumFee().subtract(detail.getEarlyFee()));
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
        if (contract.getContractStatus().equals(0)) {
            throw new BusinessException("请先上传合同附件，再修改合同状态");
        }
        contractMapper.updateContractDate(contractDate);
        if(contractDate.getStatus()==5){
            companyMapper.updateByPrimaryKeySelective(new Company().setCompanyId(contract.getCompanyId()).setCompanyStatus(0));
        }
        List<Integer> status = contractMapper.getStatusById(contractDate.getContractId());
        if (CollUtil.isNotEmpty(status)) {
            Integer max = Collections.max(status);
            Integer min = Collections.min(status);
            if (max != null && max.equals(min)) {
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

        if (file == null) {
            throw new BusinessException("请选择上传文件！");
        }

        File f = new File("resource/contract");
        if (!f.exists()) {
            f.mkdir();
        }

        if (StrUtil.isNotEmpty(contract.getFileName())) {
            File oldFile = new File(System.getProperty("user.dir") + "/resource/contract/", contract.getFileName());
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }

        String fileName = "";
        try {
            fileName = new String(file.getOriginalFilename().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            fileName=fileName+ IdWorker.getId();
            f = new File(System.getProperty("user.dir") + "/resource/contract/", fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            file.transferTo(f);
        } catch (IOException e) {
            throw new BusinessException("上传文件失败！" + e);
        }

        contractMapper.updateByPrimaryKeySelective(new Contract(contractId, fileName, 2));
        contractMapper.updateContractDate(new ContractDate().setStatus(2));
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

        Contract contract = contractMapper.selectByPrimaryKey(contractId);
        String fileName = contract.getFileName();
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
