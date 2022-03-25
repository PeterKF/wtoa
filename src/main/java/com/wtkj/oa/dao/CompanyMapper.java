package com.wtkj.oa.dao;

import com.wtkj.oa.entity.Company;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CompanyMapper {
    int deleteByPrimaryKey(String companyId);

    int insert(Company record);

    Company selectByPrimaryKey(String companyId);

    String getIdByName(String companyName);

    int updateByPrimaryKeySelective(Company record);

    List<Company> list();

    List<Company> companyList();

    List<Company> listByIds(List<String> list);

    List<Company> listByName(String companyName);
}