package com.wtkj.oa.dao;

import com.wtkj.oa.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(User record);

    User selectByPrimaryKey(String userId);

    User selectByName(String loginName);

    List<User> selectByRole(String roleName);

    List<User> list();

    int updateByPrimaryKeySelective(User record);

    String getIdByName(String userName);

    String getUserId(String userId);
}