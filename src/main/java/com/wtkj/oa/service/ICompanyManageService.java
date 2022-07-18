package com.wtkj.oa.service;

import com.wtkj.oa.common.config.PageInfo;
import com.wtkj.oa.entity.Company;

import java.util.List;
import java.util.Map;

public interface ICompanyManageService {
    void addCompany(Company company);

    void deleteCompany(String companyId);

    void updateCompany(Company company);

    List<Company> list(String companyName, Integer agentFlag);

    PageInfo<Company> list(Company company);

    Company selectOne(String companyId);

    List<String> getCompanyNames();

    List<Company> getCompanyList(Company company);

    Map<String, String> getCompanyMap();

    Map<String, String> getIdMap();

    List<Company> listByStatus();
}
