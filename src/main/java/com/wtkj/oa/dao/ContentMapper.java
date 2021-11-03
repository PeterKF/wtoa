package com.wtkj.oa.dao;


import com.wtkj.oa.entity.Content;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContentMapper {
    Content getContentByType(Integer businessType, String contractType);
}