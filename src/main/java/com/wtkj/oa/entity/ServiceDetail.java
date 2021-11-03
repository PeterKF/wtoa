package com.wtkj.oa.entity;

import cn.hutool.core.util.StrUtil;

import java.io.Serializable;

public class ServiceDetail implements Serializable {
    private static final long serialVersionUID = -3622388619199702912L;

    private String serviceId;

    // 服务名称
    private String serviceName;

    // 有效期
    private String period;

    // 年份
    private String year;

    // 费用
    private String expense;

    private String contractId;

    //总费用
    private String sumFee;

    //前期支付费用
    private String beforeFee;

    //国家技术服务费
    private String countryTecFee;

    //省级技术服务费
    private String provinceTecFee;

    //市级技术服务费
    private String cityTecFee;

    //企业技术服务费
    private String enterpriseTecFee;

    //合同事项
    private String patentMatters;

    //辅导费
    private String guidanceFee;

    //专利费
    private String patentFee;

    //实用新型费用
    private String practicalFee;

    //专利外观费用
    private String appearanceFee;

    //软著费用
    private String softFee;

    private String proxyContent;

    private String agent;

    private String agentFee;

    private String manager;

    private String managerTelephone;

    //签字日期
    private String signDate;

    private String percent;

    public ServiceDetail() {
    }

    public ServiceDetail(String serviceId, String serviceName, String period, String year, String expense, String contractId,
                         String sumFee, String beforeFee, String countryTecFee, String provinceTecFee, String cityTecFee,
                         String enterpriseTecFee, String patentMatters, String guidanceFee) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.period = period;
        this.year = year;
        this.expense = expense;
        this.contractId = contractId;
        this.sumFee = sumFee;
        this.beforeFee = beforeFee;
        this.countryTecFee = countryTecFee;
        this.provinceTecFee = provinceTecFee;
        this.cityTecFee = cityTecFee;
        this.enterpriseTecFee = enterpriseTecFee;
        this.patentMatters = patentMatters;
        this.guidanceFee = guidanceFee;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getExpense() {
        if (StrUtil.isEmpty(expense)) {
            return "0";
        }
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getSumFee() {
        if (StrUtil.isEmpty(sumFee)) {
            return "0";
        }
        return sumFee;
    }

    public void setSumFee(String sumFee) {
        this.sumFee = sumFee;
    }

    public String getBeforeFee() {
        if (StrUtil.isEmpty(beforeFee)) {
            return "0";
        }
        return beforeFee;
    }

    public void setBeforeFee(String beforeFee) {
        this.beforeFee = beforeFee;
    }

    public String getCountryTecFee() {
        if (StrUtil.isEmpty(countryTecFee)) {
            return "0";
        }
        return countryTecFee;
    }

    public void setCountryTecFee(String countryTecFee) {
        this.countryTecFee = countryTecFee;
    }

    public String getProvinceTecFee() {
        if (StrUtil.isEmpty(provinceTecFee)) {
            return "0";
        }
        return provinceTecFee;
    }

    public void setProvinceTecFee(String provinceTecFee) {
        this.provinceTecFee = provinceTecFee;
    }

    public String getCityTecFee() {
        if (StrUtil.isEmpty(cityTecFee)) {
            return "0";
        }
        return cityTecFee;
    }

    public void setCityTecFee(String cityTecFee) {
        this.cityTecFee = cityTecFee;
    }

    public String getEnterpriseTecFee() {
        if (StrUtil.isEmpty(enterpriseTecFee)) {
            return "0";
        }
        return enterpriseTecFee;
    }

    public void setEnterpriseTecFee(String enterpriseTecFee) {
        this.enterpriseTecFee = enterpriseTecFee;
    }

    public String getPatentMatters() {
        return patentMatters;
    }

    public void setPatentMatters(String patentMatters) {
        this.patentMatters = patentMatters;
    }

    public String getGuidanceFee() {
        if (StrUtil.isEmpty(guidanceFee)) {
            return "0";
        }
        return guidanceFee;
    }

    public void setGuidanceFee(String guidanceFee) {
        this.guidanceFee = guidanceFee;
    }

    public String getProxyContent() {
        return proxyContent;
    }

    public void setProxyContent(String proxyContent) {
        this.proxyContent = proxyContent;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getAgentFee() {
        return agentFee;
    }

    public void setAgentFee(String agentFee) {
        this.agentFee = agentFee;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getManagerTelephone() {
        return managerTelephone;
    }

    public void setManagerTelephone(String managerTelephone) {
        this.managerTelephone = managerTelephone;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getPatentFee() {
        return patentFee;
    }

    public void setPatentFee(String patentFee) {
        this.patentFee = patentFee;
    }

    public String getPracticalFee() {
        return practicalFee;
    }

    public void setPracticalFee(String practicalFee) {
        this.practicalFee = practicalFee;
    }

    public String getAppearanceFee() {
        return appearanceFee;
    }

    public void setAppearanceFee(String appearanceFee) {
        this.appearanceFee = appearanceFee;
    }

    public String getSoftFee() {
        return softFee;
    }

    public void setSoftFee(String softFee) {
        this.softFee = softFee;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}