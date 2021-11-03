package com.wtkj.oa.service;

import com.wtkj.oa.common.config.PageInfo;
import com.wtkj.oa.entity.Company;
import com.wtkj.oa.entity.Contract;
import com.wtkj.oa.entity.ResultDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


public interface IContractManageService {
    String getWordContent(Integer businessType, String contractType);

    String getHtmlContentByType(Integer businessType, String contractType, String companyId);

    void addContract(Contract contract);

    void delete(String contractId);

    void update(Contract contract);

    PageInfo<Contract> list(Contract contract);

    ResultDTO preview(String contractId);

    String htmlToWord(String contractId);

    void downLoadWord(String contractId, HttpServletRequest request, HttpServletResponse response);

    List<String> getFields(Object object);

    List<Contract> listByName(String userId, String companyName);

    void completeList(List<Contract> contracts);

    PageInfo<Company> companyList(Company company);

    List<Contract> contractsByCompanyId(String companyId);
}
