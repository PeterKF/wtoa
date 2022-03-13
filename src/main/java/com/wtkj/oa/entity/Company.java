package com.wtkj.oa.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Company implements Serializable {
    private static final long serialVersionUID = -5169083803505009458L;

    private String companyId;

    private String year;

    /**
     * 统一社会信用代码
     */
    private String creditCode;

    //公司名称
    private String companyName;

    //地址
    private String address;

    //联系人
    private String contact;

    //联系电话
    private String telephone;

    //负责人
    private String director;

    //负责人电话
    private String phone;

    //市
    private String city;

    //区
    private String area;

    //街道
    private String county;

    //员工id
    private String userId;

    //业务员
    private String userName;

    //员工助理id
    private String assistantId;

    //合同状态 （0 未上传 1 已上传）
    private Integer status = 1;

    private String region;

    private String lastUpdateTime;

    private int pageSize;

    private int pageNum;
}
