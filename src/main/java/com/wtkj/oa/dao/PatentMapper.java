package com.wtkj.oa.dao;

import com.wtkj.oa.entity.Patent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PatentMapper {
    int deleteByPrimaryKey(String patentId);

    String getId(List<String> patentIds);

    List<String> getPatentIds();

    int insert(Patent record);

    void insertBatch(List<Patent> patents);

    int insertSelective(Patent record);

    Patent selectByPrimaryKey(String id);

    Patent selectByPatentId(String patentId);

    List<Patent> listByName(@Param("patentName") String patentName);

    List<Patent> listLikeName(@Param("patentName") String patentName);

    int updateByPrimaryKeySelective(Patent record);
}