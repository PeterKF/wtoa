package com.wtkj.oa.service;

import com.wtkj.oa.entity.Company;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface InitDataService {

    String initCompanies(MultipartFile file);

    String initPatents(MultipartFile file);

    String initContracts(MultipartFile file);

    void exportExcel(String fileType, HttpServletResponse response);

    void deletePatents(MultipartFile file);

    void exportCompanyInfo(Company company, HttpServletResponse response);

    void exportPatentInfo(HttpServletResponse response);
}
