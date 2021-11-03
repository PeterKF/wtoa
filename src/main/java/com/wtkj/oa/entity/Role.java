package com.wtkj.oa.entity;

import java.io.Serializable;

public class Role implements Serializable {
    private static final long serialVersionUID = 5598893546595452312L;

    private String roleId;

    private String roleName;

    public Role(String roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public Role() {
        super();
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }
}