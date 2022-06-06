package com.wtkj.oa.service;

import com.wtkj.oa.common.config.PageInfo;
import com.wtkj.oa.entity.Patent;

import java.util.List;

public interface IPatentManageService {
    void addPatent(Patent patent);

    void delete(String patentId);

    void update(Patent patent);

    PageInfo<Patent> list(Patent patent);

    void getPatentExpenseList(List<String> patentIds, String cpmpanyType);
}
