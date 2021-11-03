package com.wtkj.oa.common.constant;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public enum PatentEnum {
    A("发明", 1),
    B("实用", 2),
    C("外观", 3),
    D("软著", 4);

    private String name;

    private Integer type;

    PatentEnum(String name, Integer type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public static String getNameByType(Integer type) {
        for (PatentEnum e : PatentEnum.values()) {
            if (!ObjectUtils.isEmpty(type) && type.equals(e.getType())) {
                return e.getName();
            }
        }
        return null;
    }

    public static Integer getTypeByName(String name) {
        for (PatentEnum e : PatentEnum.values()) {
            if (!StringUtils.isEmpty(name) && e.getName().equals(name)) {
                return e.getType();
            }
        }
        return null;
    }
}
