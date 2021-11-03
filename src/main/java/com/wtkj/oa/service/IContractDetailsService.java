package com.wtkj.oa.service;

import com.wtkj.oa.entity.ContractDate;
import com.wtkj.oa.entity.ContractDetail;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IContractDetailsService {
    List<ContractDetail> list(String companyId);

    void uploadFile(MultipartFile file, String contractId);

    void updateStatus(ContractDate contractDate);

    void viewFile(HttpServletResponse response, String contractId);
}


