package com.wtkj.oa.service;

import org.springframework.web.multipart.MultipartFile;

public interface InitDataService {
    String initCompanies(MultipartFile file);

    String initPatents(MultipartFile file);
}
