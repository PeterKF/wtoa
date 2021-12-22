package com.wtkj.oa.service;

import com.wtkj.oa.entity.Content;
import com.wtkj.oa.entity.ResultDTO;

public interface IHTContractService {

    ResultDTO getContentByType(String companyId, Integer companyType, Integer businessType, String contractType);

    String getContractInfo(Integer businessType, String contractType);

    void updateContract(Content content);
}
