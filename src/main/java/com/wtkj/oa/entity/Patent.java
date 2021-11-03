package com.wtkj.oa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Patent implements Serializable {
    private static final long serialVersionUID = 3736066381728708350L;

    private String id;

    private String patentId;

    private String companyId;

    private String companyName;

    private String patentName;

    private Integer patentType;

    private String userId;

    private String userName;

    //专利收款
    private String collection;

    //专利付款
    private String payment;

    //收款日期
    private String collectionDate;

    //申请日
    private String applicationDate;

    //授权日
    private String authorizationDate;

    private String createTime;

    private String lastUpdateTime;

    private int pageSize;

    private int pageNum;
}