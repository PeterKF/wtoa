package com.wtkj.oa.service.impl;

import com.wtkj.oa.dao.UserMapper;
import com.wtkj.oa.entity.User;
import com.wtkj.oa.exception.BusinessException;
import com.wtkj.oa.service.ILoginService;
import com.wtkj.oa.service.IUserManageService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LoginServiceImpl implements ILoginService {
    @Resource
    private IUserManageService userManageService;

    @Resource
    private UserMapper userMapper;

    public User login(User userDTO) {
        if (StringUtils.isEmpty(userDTO.getLoginName())) {
            throw new BusinessException("用户名不能为空！");
        }

        if (StringUtils.isEmpty(userDTO.getPassword())) {
            throw new BusinessException("用户密码不能为空！");
        }
        List<User> users = userManageService.userList();
        if (CollectionUtils.isEmpty(users)) {
            throw new BusinessException("没有注册用户，请管理员添加用户！");
        } else {
            User user = userMapper.selectByName(userDTO.getLoginName());
            if (ObjectUtils.isEmpty(user)) {
                throw new BusinessException("用户名或密码输入错误！");
            } else {
                if (users.stream().anyMatch(u -> u.getLoginName().equals(userDTO.getLoginName())
                        && u.getPassword().equals(userDTO.getPassword()))) {
                    return user;
                } else {
                    throw new BusinessException("用户名或密码输入错误！");
                }
            }
        }
    }
}
