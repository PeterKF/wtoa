package com.wtkj.oa.entity;

import lombok.Data;

import java.util.List;

/**
 * @Description 前端请求参数
 * @Date 2022/8/12 10:03
 * @Author Peter.Chen
 */
@Data
public class RequestPramBody {

    private String companyId;

    private String companyType;

    private List<String> patentIds;
}
