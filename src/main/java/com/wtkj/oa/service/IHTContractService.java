package com.wtkj.oa.service;

import com.wtkj.oa.entity.ResultDTO;

public interface IHTContractService {

    ResultDTO getContentByType(String companyId, Integer companyType, Integer businessType, String contractType);
}
