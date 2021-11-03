package com.wtkj.oa.dao;

import com.wtkj.oa.entity.ServiceDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ServiceDetailMapper {
    int deleteByPrimaryKey(String serviceId);

    int deleteByForeignKey(String contractId);

    int insert(ServiceDetail record);

    ServiceDetail selectByPrimaryKey(String serviceId);

    ServiceDetail selectByForeignKey(String contractId);

    int updateByPrimaryKeySelective(ServiceDetail record);

    int updateByPrimaryKey(ServiceDetail record);
}