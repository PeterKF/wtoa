package com.wtkj.oa.service.impl;

import com.wtkj.oa.dao.MenuMapper;
import com.wtkj.oa.entity.Menu;
import com.wtkj.oa.exception.BusinessException;
import com.wtkj.oa.service.IMenuService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service
public class MenuServiceImpl implements IMenuService {

    @Resource
    private MenuMapper menuMapper;

    public void update(Menu menu) {
        if (StringUtils.isEmpty(menu.getRoleId())) {
            throw new BusinessException("请先选择一个角色！");
        } else {
            Menu m = menuMapper.selectByPrimaryKey(menu.getRoleId());
            if (ObjectUtils.isEmpty(m)) {
                menuMapper.insert(menu);
            } else {
                menuMapper.update(menu);
            }
        }

    }

    public Menu getMenuByRoleId(String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            throw new BusinessException("请先选择一个角色！");
        } else {
            return menuMapper.selectByPrimaryKey(roleId);
        }
    }
}
