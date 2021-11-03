package com.wtkj.oa.service;

import com.wtkj.oa.entity.Menu;


public interface IMenuService {
    void update(Menu menu);

    Menu getMenuByRoleId(String roleId);
}
