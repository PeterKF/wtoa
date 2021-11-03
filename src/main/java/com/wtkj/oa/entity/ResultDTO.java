package com.wtkj.oa.entity;

import java.util.List;

public class ResultDTO {
    private String serviceId;

    private String html;

    private List<String> fields;

    private Integer comType;

    public ResultDTO() {
    }

    public ResultDTO(String html, List<String> fields) {
        this.html = html;
        this.fields = fields;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getComType() {
        return comType;
    }

    public void setComType(Integer comType) {
        this.comType = comType;
    }
}
