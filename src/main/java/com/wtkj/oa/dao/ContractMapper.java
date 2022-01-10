package com.wtkj.oa.dao;

import com.wtkj.oa.entity.Contract;
import com.wtkj.oa.entity.ContractDate;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Blob;
import java.util.List;

@Mapper
public interface ContractMapper {
    int deleteByPrimaryKey(String contractId);

    int deleteByType(String companyId);

    int insert(Contract record);

    Contract selectByPrimaryKey(String contractId);

    List<Contract> list();

    int updateByPrimaryKeySelective(Contract record);

    String getContractContent(String contractId);

    String selectIdByType(Integer businessType);

    String getDateByType(String contractId, String type);

    List<ContractDate> getDateList(String contractId);

    List<String> dateList();

    List<Contract> selectByCompanyId(String companyId);

    int deleteDate(String contractId, String type);

    void deleteAllDate(String contractId);

    int addDate(ContractDate record);

    int batchInsert(List<Contract> contracts);

    void updateContractDate(ContractDate record);

    Integer getMaxStatus(String contractId);

    Integer getMinStatus(String contractId);

    List<Integer> getStatusById(String contractId);

    ContractDate getStatus(String contractId, String type);
}