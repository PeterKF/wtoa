package com.wtkj.oa.entity;

import lombok.Data;

import java.util.List;

/**
 * @Description 专利结算清单内容
 * @Date 2022/8/10 10:19
 * @Author Peter.Chen
 */
@Data
public class PatentDetail {

    private String companyName;

    private List<Patent> patentList;

    private InsideInfo insideInfo;

    private String currentDate;
}
