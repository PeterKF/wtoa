package com.wtkj.oa.entity;

import org.springframework.util.StringUtils;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = -4881184339166784177L;

    private String userId;

    //助理Id
    private String assistantIds;

    //助理名称
    private String assistants;

    private String userName;

    private String loginName;

    private String password;

    //联系电话
    private String telephone;

    private String roleName;

    private String roleId;

    private String menu;

    private String[] menus;

    private int pageSize;

    private int pageNum;

    public User(String userId, String userName, String loginName, String password, String roleName, String roleId) {
        this.userId = userId;
        this.userName = userName;
        this.loginName = loginName;
        this.password = password;
        this.roleName = roleName;
        this.roleId = roleId;
    }

    public User() {
        super();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAssistantIds() {
        return assistantIds;
    }

    public void setAssistantIds(String assistantIds) {
        this.assistantIds = assistantIds;
    }

    public String getAssistants() {
        return assistants;
    }

    public void setAssistants(String assistants) {
        this.assistants = assistants;
    }
}