package com.wtkj.oa.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wtkj.oa.common.constant.ContractEnum;
import com.wtkj.oa.common.constant.GXEnum;
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

    @TableField("user_name")
    private String userName;

    @TableField("company_name")
    private String companyName;

    private String name;

    @TableField("business_type")
    private Integer businessType;

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

    @TableField("user_id")
    private String userId;

    public String getName() {
        if (this.businessType == null) {
            return name;
        }

        if (this.businessType.equals(1)) {
            return GXEnum.getNameByType(this.getType());
        } else {
            return ContractEnum.getNameByType(this.businessType, this.getType());
        }
    }

    public ContractDate(String contractId, String name, String type, String completeDate) {
        this.contractId = contractId;
        this.name = name;
        this.type = type;
        this.completeDate = completeDate;
    }
}
