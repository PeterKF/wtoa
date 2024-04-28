package com.wtkj.oa.common.constant;

/**
 * 合同枚举
 *
 * @author chenq
 */
@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
public enum ContractEnum {
    A("省级企业研究院", 2, "1", "XM"),
    B("省级企业研发中心", 2, "2", "XM"),
    C("科技计划项目", 2, "3", "XM"),
    D("省科技型中小企业", 2, "4", "XM"),
    K("双软企业", 2, "5", "XM"),
    E("企业研发费财务辅导", 3, "1", "TX"),
    F("企业研发费归集", 3, "2", "TX"),
    G("企业研发费加计扣除", 3, "3", "TX"),
    H("专利代理委托合同书", 4, "1", "IP"),
    I("高新技术企业培育辅导", 1, "1", "GQ");

    /**
     * 合同名称
     */
    private String name;

    private Integer businessType;

    private String contractType;

    private String code;

    ContractEnum(String name, Integer businessType, String contractType, String code) {
        this.name = name;
        this.businessType = businessType;
        this.contractType = contractType;
        this.code = code;
    }

    /**
     * 通过合同类型找到名称
     *
     * @param businessType
     * @param contractType
     * @return
     */
    public static String getNameByType(Integer businessType, String contractType) {
        for (ContractEnum e : ContractEnum.values()) {
            if (e.businessType.equals(businessType) && e.contractType.equals(contractType)) {
                return e.name;
            }
        }
        return null;
    }

    /**
     * 通过业务类型获得编码
     *
     * @param businessType
     * @return
     */
    public static String getCodeByType(Integer businessType) {
        for (ContractEnum e : ContractEnum.values()) {
            if (e.businessType.equals(businessType)) {
                return e.getCode();
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
