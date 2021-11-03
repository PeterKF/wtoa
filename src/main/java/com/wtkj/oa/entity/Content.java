package com.wtkj.oa.entity;

public class Content {
    //业务类型
    private Integer businessType;

    //合同类型
    private String contractType;

    private String title;

    private String content;

    public Content(Integer businessType, String contractType, String title, String content) {
        this.businessType = businessType;
        this.contractType = contractType;
        this.title = title;
        this.content = content;
    }

    public Content() {
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}