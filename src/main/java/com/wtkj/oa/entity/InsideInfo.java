package com.wtkj.oa.entity;

public class InsideInfo {
    private Integer companyType;

    private String companyName;

    private String manager;

    private String mobile;

    private String address;

    private String bank;

    private String bankNo;

    private String accountNo;

    private String taxNo;

    public InsideInfo(Integer companyType, String companyName, String manager, String mobile, String address, String bank,
                      String bankNo, String accountNo, String taxNo) {
        this.companyType = companyType;
        this.companyName = companyName;
        this.manager = manager;
        this.mobile = mobile;
        this.address = address;
        this.bank = bank;
        this.bankNo = bankNo;
        this.accountNo = accountNo;
        this.taxNo = taxNo;
    }

    public InsideInfo() {
        super();
    }

    public Integer getCompanyType() {
        return companyType;
    }

    public void setCompanyType(Integer companyType) {
        this.companyType = companyType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager == null ? null : manager.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank == null ? null : bank.trim();
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo == null ? null : bankNo.trim();
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo == null ? null : accountNo.trim();
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo == null ? null : taxNo.trim();
    }
}