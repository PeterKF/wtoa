package com.wtkj.oa.entity;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Contract implements Serializable {
    private static final long serialVersionUID = 2339368070597169198L;

    private String contractId;

    private String contractName;

    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 乙方公司类型  (1 杭州 2 台州)
     */
    private String companyType;

    private String contractType;

    private String companyId;

    private String companyName;

    private String userId;

    private String userName;

    private ServiceDetail serviceDetails;

    private Boolean dateFlag;

    /**
     * 合同状态
     */
    private Integer contractStatus = 0;

    //发票状态
    private Integer invoiceStatus = 0;

    //收款状态
    private Integer collectionStatus = 0;

    private String createTime;

    private String lastUpdateTime;

    private String contractFile;

    private Date billingDate;

    private Date collectionDate;

    private String fileName;

    private List<Contract> contracts;

    private List<ContractDate> dateList;

    private String expense;

    private int pageSize;

    private int pageNum;

    public Contract(String contractId, String contractName, Integer businessType, String companyType, String contractType,
                    String companyId, String companyName, String userId, Integer contractStatus,
                    Integer invoiceStatus, Integer collectionStatus, String createTime, String lastUpdateTime,
                    String contractFile, int pageSize, int pageNum) {
        this.contractId = contractId;
        this.contractName = contractName;
        this.businessType = businessType;
        this.companyType = companyType;
        this.contractType = contractType;
        this.companyId = companyId;
        this.companyName = companyName;
        this.userId = userId;
        this.contractStatus = contractStatus;
        this.invoiceStatus = invoiceStatus;
        this.collectionStatus = collectionStatus;
        this.createTime = createTime;
        this.lastUpdateTime = lastUpdateTime;
        this.contractFile = contractFile;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    public Contract() {
    }

    public Contract(String contractId, Integer contractStatus) {
        this.contractId = contractId;
        this.contractStatus = contractStatus;
    }

    public Contract(String companyName) {
        this.companyName = companyName;
    }

    public Contract(String contractId, String fileName, Integer contractStatus) {
        this.contractId = contractId;
        this.fileName = fileName;
        this.contractStatus = contractStatus;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId == null ? null : contractId.trim();
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName == null ? null : contractName.trim();
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Integer getContractStatus() {
        return contractStatus;
    }

    public Integer getInvoiceStatus() {
        return invoiceStatus;
    }

    public Integer getCollectionStatus() {
        return collectionStatus;
    }

    public void setContractStatus(Integer contractStatus) {
        this.contractStatus = contractStatus;
    }

    public void setInvoiceStatus(Integer invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public void setCollectionStatus(Integer collectionStatus) {
        this.collectionStatus = collectionStatus;
    }

    public String getContractFile() {
        return contractFile;
    }

    public void setContractFile(String contractFile) {
        this.contractFile = contractFile;
    }

    public ServiceDetail getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(ServiceDetail serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public List<ContractDate> getDateList() {
        return dateList;
    }

    public void setDateList(List<ContractDate> dateList) {
        this.dateList = dateList;
    }

    public Date getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(Date billingDate) {
        this.billingDate = billingDate;
    }

    public Date getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Boolean getDateFlag() {
        return dateFlag;
    }

    public void setDateFlag(Boolean dateFlag) {
        this.dateFlag = dateFlag;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }
}