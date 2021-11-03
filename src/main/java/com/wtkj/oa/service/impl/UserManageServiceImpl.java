package com.wtkj.oa.service.impl;

import com.wtkj.oa.common.config.PageInfo;
import com.wtkj.oa.dao.RoleMapper;
import com.wtkj.oa.dao.UserMapper;
import com.wtkj.oa.entity.Role;
import com.wtkj.oa.entity.User;
import com.wtkj.oa.exception.BusinessException;
import com.wtkj.oa.service.IUserManageService;
import com.wtkj.oa.utils.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManageServiceImpl implements IUserManageService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    //角色管理
    public void addRole(Role role) {
        if (StringUtils.isEmpty(role.getRoleName())) {
            throw new BusinessException("角色名不能为空！");
        }
        this.ifExist(role);
        role.setRoleId(RandomStringUtils.getNextVal());
        roleMapper.insert(role);
    }

    public void ifExist(Role role) {
        List<Role> roles = roleMapper.list();
        if (roles.stream().anyMatch(r -> r.getRoleName().equals(role.getRoleName()))) {
            throw new BusinessException("角色" + role.getRoleName() + "已经存在！");
        }
    }

    public void deleteRole(String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            throw new BusinessException("请先选择一个角色！");
        }
        roleMapper.deleteByPrimaryKey(roleId);
    }

    public void updateRole(Role role) {
        if (StringUtils.isEmpty(role.getRoleId())) {
            throw new BusinessException("编辑的角色不存在！");
        }
        this.checkRepeat(role);
        roleMapper.updateByPrimaryKeySelective(role);
    }

    public void checkRepeat(Role role) {
        List<Role> roles = roleMapper.list();
        if (roles.stream().filter(r1 -> !r1.getRoleId().equals(role.getRoleId())).anyMatch(r2 -> StringUtils.isEmpty(r2.getRoleName())
                && r2.getRoleName().equals(role.getRoleName()))) {
            throw new BusinessException("角色" + role.getRoleName() + "已经存在！");
        }
    }

    public List<Role> roleList() {
        return roleMapper.list().size() == 0 ? new ArrayList<>() : roleMapper.list();
    }


    //用户管理
    public void addUser(User user) {
        if (StringUtils.isEmpty(user.getUserName())) {
            throw new BusinessException("用户名不能为空！");
        }

        if (StringUtils.isEmpty(user.getRoleId())) {
            throw new BusinessException("请选择该用户对应的角色！");
        }

        if (StringUtils.isEmpty(user.getLoginName())) {
            throw new BusinessException("请填写登录名！");
        } else {
            //登录名去重
            this.checkLoginName(user);
        }

        if (StringUtils.isEmpty(user.getPassword())) {
            throw new BusinessException("请填写登录密码！");
        }
        user.setUserId(RandomStringUtils.getNextVal());
        userMapper.insert(user);
    }

    public void checkLoginName(User user) {
        List<User> users = this.userList();
        if (!CollectionUtils.isEmpty(users)) {
            if (users.stream().anyMatch(u -> u.getLoginName().equals(user.getLoginName()))) {
                throw new BusinessException("该登录名已经使用，请使用别的登录名！");
            }
        }
    }

    public void deleteUser(String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException("请先选择一个用户！");
        }
        userMapper.deleteByPrimaryKey(userId);
    }

    public void updateUser(User user) {
        if (StringUtils.isEmpty(user.getUserId())) {
            throw new BusinessException("请先选择编辑的用户！");
        }
        List<User> users = this.userList();
        if (!CollectionUtils.isEmpty(users)) {
            if (users.stream().anyMatch(u -> !u.getUserId().equals(user.getUserId()) &&
                    !StringUtils.isEmpty(u.getAssistantIds()) && u.getAssistantIds().equals(user.getAssistantIds()))) {
                throw new BusinessException("该助理已经分配给别的主管，不可再分配！");
            }
        }
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 用户列表
     *
     * @return
     */
    public List<User> userList() {
        List<User> users = userMapper.list();
        return CollectionUtils.isEmpty(users) ? new ArrayList<>() : users;
    }

    /**
     * 业务员列表
     *
     * @return
     */
    public List<User> commonUsers() {
        List<User> users = this.userList();
        if (!CollectionUtils.isEmpty(users)) {
            users = users.stream().filter(u -> u.getRoleName().equals("业务助理")).collect(Collectors.toList());
        }
        return users;
    }

    /**
     * 用户分页
     *
     * @param user
     * @return
     */
    public PageInfo<User> userList(User user) {
        List<User> users = userMapper.list();
        if (!CollectionUtils.isEmpty(users)) {
            for (User u : users) {
                if (!StringUtils.isEmpty(u.getAssistantIds())) {
                    String[] strs = u.getAssistantIds().replace(" ", "").split(",");
                    StringBuilder sb = new StringBuilder();
                    for (String str : strs) {
                        User one = userMapper.selectByPrimaryKey(str);
                        if (!ObjectUtils.isEmpty(one)) {
                            sb.append(one.getUserName() + ",");
                        }
                    }
                    if (!StringUtils.isEmpty(sb.toString())) {
                        String assistants = sb.toString().substring(0, sb.toString().length() - 1);
                        u.setAssistants(assistants);
                    }
                }
            }

            if (!StringUtils.isEmpty(user.getUserName())) {
                users = users.stream().filter(u -> u.getUserName().contains(user.getUserName())).collect(Collectors.toList());
            }
        }
        return new PageInfo<>(user.getPageNum(), user.getPageSize(), users);
    }

    /**
     * 重置密码
     *
     * @param userId
     */
    public void reset(String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException("请选择重置密码的用户！");
        }
        User user = userMapper.selectByPrimaryKey(userId);
        user.setPassword("123456");
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 根据角色名查询用户列表
     *
     * @param roleName
     * @return
     */
    public List<User> getUsersByRole(String roleName) {
        List<User> users = userMapper.selectByRole(roleName);
        return users;
    }
}
