package com.wtkj.oa.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Contract implements Serializable {
    private static final long serialVersionUID = 2339368070597169198L;

    private String contractId;

    private String contractName;

    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 乙方公司类型  (1 杭州 2 台州)
     */
    private String companyType;

    private String contractType;

    private String companyId;

    private String companyName;

    private String userId;

    private String userName;

    private ServiceDetail serviceDetails;

    private Boolean dateFlag;

    /**
     * 合同上传状态 (0 未上传 1 已上传)
     */
    private Integer uploadStatus;

    /**
     * 合同状态 (0 待签订 1 待上传 2 已上传(执行中) 3 已完成)
     */
    private Integer contractStatus;

    //发票状态
    private Integer invoiceStatus;

    //收款状态
    private Integer collectionStatus;

    private String createTime;

    private String lastUpdateTime;

    private String contractFile;

    private String completeDate;

    private Date billingDate;

    private Date collectionDate;

    private String fileName;

    private List<Contract> contracts;

    private List<ContractDate> dateList;

    private String expense;

    private String contractMatter;

    private int pageSize;

    private int pageNum;

    public Contract(String contractId, Integer contractStatus) {
        this.contractId = contractId;
        this.contractStatus = contractStatus;
    }

    public Contract(String companyName) {
        this.companyName = companyName;
    }

    public Contract(String contractId, String fileName, Integer contractStatus) {
        this.contractId = contractId;
        this.fileName = fileName;
        this.contractStatus = contractStatus;
    }


    public void setContractId(String contractId) {
        this.contractId = contractId == null ? null : contractId.trim();
    }


    public void setContractName(String contractName) {
        this.contractName = contractName == null ? null : contractName.trim();
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Integer getUploadStatus() {
        if (this.contractStatus < 2) {
            return 0;
        } else {
            return 1;
        }
    }

    public void setUploadStatus(Integer uploadStatus) {
        this.uploadStatus = uploadStatus;
    }
}