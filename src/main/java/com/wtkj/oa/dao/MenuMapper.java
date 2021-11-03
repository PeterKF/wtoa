package com.wtkj.oa.dao;


import com.wtkj.oa.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuMapper {
    int insert(Menu record);

    void update(Menu record);

    Menu selectByPrimaryKey(String roleId);
}