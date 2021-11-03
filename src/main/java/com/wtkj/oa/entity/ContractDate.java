package com.wtkj.oa.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 使用在高新合同中多个完成时间
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@TableName("contract_date")
public class ContractDate implements Serializable {

    private static final long serialVersionUID = -3389877010679134157L;

    @TableField("contract_id")
    private String contractId;

    @TableField("company_id")
    private String companyId;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String companyName;

    @TableField("name")
    private String name;

    @TableField("type")
    private String type;

    @TableField("complete_date")
    private String completeDate;

    @TableField("status")
    private Integer status = 0;

    @TableField(exist = false)
    private Integer billingStatusBefore;

    @TableField(exist = false)
    private String billingDateBefore;

    @TableField(exist = false)
    private Integer collectionStatusBefore;

    @TableField(exist = false)
    private String collectionDateBefore;

    @TableField(exist = false)
    private Integer billingStatusAfter;

    @TableField(exist = false)
    private String billingDateAfter;

    @TableField(exist = false)
    private Integer collectionStatusAfter;

    @TableField(exist = false)
    private String collectionDateAfter;

    @TableField(exist = false)
    private int pageSize;

    @TableField(exist = false)
    private int pageNum;

    @TableField(exist = false)
    private String userId;

    public ContractDate(String contractId, String name, String type, String completeDate) {
        this.contractId = contractId;
        this.name = name;
        this.type = type;
        this.completeDate = completeDate;
    }
}
