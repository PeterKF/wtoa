package com.wtkj.oa.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wtkj.oa.common.config.PageInfo;
import com.wtkj.oa.common.constant.GXEnum;
import com.wtkj.oa.dao.CompanyMapper;
import com.wtkj.oa.dao.ContractDateMapper;
import com.wtkj.oa.dao.ContractMapper;
import com.wtkj.oa.dao.UserMapper;
import com.wtkj.oa.entity.Contract;
import com.wtkj.oa.entity.ContractDate;
import com.wtkj.oa.entity.User;
import com.wtkj.oa.service.ICompanyManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @PACKAGE_NAME: com.wtkj.oa.service.impl
 * @Description 任务事项
 * @Date 2021/5/23 15:51
 * @Author Peter.Chen
 */
@Service
public class TaskMatterServiceImpl extends ServiceImpl<ContractDateMapper, ContractDate> {

    @Resource
    private ICompanyManageService companyManageService;

    @Resource
    private ContractMapper contractMapper;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private ContractDateMapper contractDateMapper;

    public PageInfo<ContractDate> taskList(ContractDate contractDate) {
        List<ContractDate> contractDates = contractDateMapper.taskMatterList(contractDate);
        if (CollUtil.isNotEmpty(contractDates)) {
            for (ContractDate c : contractDates) {
                Contract contract = contractMapper.selectByPrimaryKey(c.getContractId());
                if (contract != null) {
                    if (contract.getBusinessType().equals(1)) {
                        c.setName(GXEnum.getNameByType(c.getType()));
                    } else {
                        c.setName(contract.getContractName());
                    }
                }
            }
        }

        if (contractDate.getUserId() != null) {
            List<ContractDate> contractDateList = new ArrayList<>();
            String userId = contractDate.getUserId();
            Map<String, String> idMap = companyManageService.getIdMap();
            List<User> users = userMapper.list();
            String roleName = users.stream().filter(u -> u.getUserId().equals(userId)).findFirst().get().getRoleName();
            boolean flag = true;
            if (roleName.equals("管理员") || roleName.equals("财务主管") || roleName.equals("财务助理")) {
                flag = false;
            } else if (roleName.equals("业务助理")) {
                User one = users.stream().filter(c -> !StringUtils.isEmpty(c.getAssistantIds())
                        && c.getAssistantIds().equals(userId)).findFirst().get();
                if (one != null) {
                    for (ContractDate c : contractDates) {
                        if (!CollectionUtils.isEmpty(idMap) && !StringUtils.isEmpty(idMap.get(c.getCompanyId()))
                                && idMap.get(c.getCompanyId()).equals(one.getUserId())) {
                            contractDateList.add(c);
                        }
                    }
                }
            } else {
                for (ContractDate c : contractDates) {
                    if (!CollectionUtils.isEmpty(idMap) && !StringUtils.isEmpty(idMap.get(c.getCompanyId()))
                            && idMap.get(c.getCompanyId()).equals(userId)) {
                        contractDateList.add(c);
                    }
                }
            }
            if (flag) {
                contractDates = contractDateList;
            }
        }

        return new PageInfo<>(contractDate.getPageNum(), contractDate.getPageSize(), contractDates);
    }
}
