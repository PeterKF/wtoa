package com.wtkj.oa.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface InitDataService {

    String initCompanies(MultipartFile file);

    String initPatents(MultipartFile file);

    String initContracts(MultipartFile file);

    void exportExcel(String fileType, HttpServletResponse response);

    void deletePatents(MultipartFile file);

    void exportCompanyInfo(HttpServletResponse response);
}
