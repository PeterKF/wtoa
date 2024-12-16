package com.wtkj.oa.common.constant;

public enum GXEnum {
    A("高新技术企业", "1"),
    B("科技型中小企业评价", "2"),
    C("省级科技型中小企业", "3"),
    D("市级高新技术企业研发中心", "4"),
    E("企业研发费加计扣除", "5"),
    F("知识产权代理服务", "6"),
    G("企业研发费财务辅导", "7"),
    H("企业研发费加计扣除", "10");

    GXEnum(String name, String contractType) {
        this.name = name;
        this.contractType = contractType;
    }

    private String name;

    private String contractType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public static String getNameByType(String contractType) {
        for (GXEnum e : GXEnum.values()) {
            if (contractType.contains("-")) {
                String type = contractType.split("-")[0];
                String year = contractType.split("-")[1];
                if (type.equals(e.getContractType())) {
                    return year + " " + e.getName();
                }
            }

            if (contractType.equals(e.getContractType())) {
                return e.getName();
            }
        }
        return null;
    }
}
