package com.wtkj.oa.dao;

import com.wtkj.oa.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {
    int deleteByPrimaryKey(String roleId);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(String roleId);

    List<Role> list();

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}