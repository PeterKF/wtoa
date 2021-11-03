package com.wtkj.oa.service;

import com.wtkj.oa.common.config.PageInfo;
import com.wtkj.oa.entity.Role;
import com.wtkj.oa.entity.User;

import java.util.List;

public interface IUserManageService {

    void addRole(Role role);

    void deleteRole(String roleId);

    void updateRole(Role role);

    List<Role> roleList();

    void addUser(User user);

    void deleteUser(String userId);

    void updateUser(User user);

    List<User> userList();

    List<User> commonUsers();

    PageInfo<User> userList(User user);

    void reset(String userId);

    List<User> getUsersByRole(String roleName);
}
