package com.wtkj.oa.entity;

import org.springframework.util.StringUtils;

import java.io.Serializable;

public class Menu implements Serializable {
    private static final long serialVersionUID = 4193950136586600707L;

    private String roleId;

    private String menu;

    private String[] menus;

    public Menu() {
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String[] getMenus() {
        if (!StringUtils.isEmpty(menu)) {
            return menu.replace(" ", "").split(",");
        }
        return menus;
    }

    public void setMenus(String[] menus) {
        this.menus = menus;
    }
}
