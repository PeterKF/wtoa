package com.wtkj.oa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wtkj.oa.entity.ContractDate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContractDateMapper extends BaseMapper<ContractDate> {

    List<ContractDate> taskMatterList(ContractDate contractDate);
}
