package com.wtkj.oa.entity;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ContractDetail implements Serializable {
    private static final long serialVersionUID = 6663979979804188126L;

    private String contractId;

    private String type;

    //合同事项
    private String contractItem;

    //单价
    private Double unitFee;

    //数量
    private String number = "1";

    //总价
    private BigDecimal sumFee = BigDecimal.valueOf(0);

    //前期付款
    private BigDecimal earlyFee;

    //后期付款
    private BigDecimal laterFee;

    //完成时间
    private String completeDate;

    //合同状态
    private Integer status = 0;

    //前期收款状态
    private Integer earlyPayStatus = 0;

    //后期收款状态
    private Integer laterPayStatus = 0;

    private Integer billingStatusBefore;

    private String billingDateBefore;

    private Integer collectionStatusBefore;

    private String collectionDateBefore;

    private Integer billingStatusAfter;

    private String billingDateAfter;

    private Integer collectionStatusAfter;

    private String collectionDateAfter;

    public ContractDetail() {
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ContractDetail(String contractItem) {
        this.contractItem = contractItem;
    }

    public String getContractItem() {
        return contractItem;
    }

    public void setContractItem(String contractItem) {
        this.contractItem = contractItem;
    }

    public Double getUnitFee() {
        return unitFee;
    }

    public void setUnitFee(Double unitFee) {
        this.unitFee = unitFee;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getSumFee() {
        if (StringUtils.isNotBlank(this.number) && "0".equals(this.number)  && this.unitFee != null) {
            if("%".equals(this.number.substring(this.number.length()-1))){
                sumFee = BigDecimal.valueOf(Float.valueOf(number.substring(0,this.number.length()-1)) * unitFee).setScale(2, RoundingMode.HALF_UP);
            }else {
                sumFee = BigDecimal.valueOf(Float.valueOf(number) * unitFee).setScale(2, RoundingMode.HALF_UP);
            }
        }
        return sumFee;
    }

    public void setSumFee(BigDecimal sumFee) {
        this.sumFee = sumFee;
    }

    public BigDecimal getEarlyFee() {
        return earlyFee;
    }

    public void setEarlyFee(BigDecimal earlyFee) {
        this.earlyFee = earlyFee;
    }

    public BigDecimal getLaterFee() {
        return laterFee;
    }

    public void setLaterFee(BigDecimal laterFee) {
        this.laterFee = laterFee;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getEarlyPayStatus() {
        return earlyPayStatus;
    }

    public void setEarlyPayStatus(Integer earlyPayStatus) {
        this.earlyPayStatus = earlyPayStatus;
    }

    public Integer getLaterPayStatus() {
        return laterPayStatus;
    }

    public void setLaterPayStatus(Integer laterPayStatus) {
        this.laterPayStatus = laterPayStatus;
    }

    public Integer getBillingStatusBefore() {
        return billingStatusBefore;
    }

    public void setBillingStatusBefore(Integer billingStatusBefore) {
        this.billingStatusBefore = billingStatusBefore;
    }

    public String getBillingDateBefore() {
        return billingDateBefore;
    }

    public void setBillingDateBefore(String billingDateBefore) {
        this.billingDateBefore = billingDateBefore;
    }

    public Integer getCollectionStatusBefore() {
        return collectionStatusBefore;
    }

    public void setCollectionStatusBefore(Integer collectionStatusBefore) {
        this.collectionStatusBefore = collectionStatusBefore;
    }

    public String getCollectionDateBefore() {
        return collectionDateBefore;
    }

    public void setCollectionDateBefore(String collectionDateBefore) {
        this.collectionDateBefore = collectionDateBefore;
    }

    public Integer getBillingStatusAfter() {
        return billingStatusAfter;
    }

    public void setBillingStatusAfter(Integer billingStatusAfter) {
        this.billingStatusAfter = billingStatusAfter;
    }

    public String getBillingDateAfter() {
        return billingDateAfter;
    }

    public void setBillingDateAfter(String billingDateAfter) {
        this.billingDateAfter = billingDateAfter;
    }

    public Integer getCollectionStatusAfter() {
        return collectionStatusAfter;
    }

    public void setCollectionStatusAfter(Integer collectionStatusAfter) {
        this.collectionStatusAfter = collectionStatusAfter;
    }

    public String getCollectionDateAfter() {
        return collectionDateAfter;
    }

    public void setCollectionDateAfter(String collectionDateAfter) {
        this.collectionDateAfter = collectionDateAfter;
    }
}
