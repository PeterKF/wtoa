package com.wtkj.oa.service.impl;

import cn.hutool.core.util.StrUtil;
import com.wtkj.oa.common.config.PageInfo;
import com.wtkj.oa.dao.CompanyMapper;
import com.wtkj.oa.dao.ContractMapper;
import com.wtkj.oa.dao.UserMapper;
import com.wtkj.oa.entity.Company;
import com.wtkj.oa.entity.InsideInfo;
import com.wtkj.oa.entity.User;
import com.wtkj.oa.exception.BusinessException;
import com.wtkj.oa.service.ICompanyManageService;
import com.wtkj.oa.utils.RandomStringUtils;
import com.wtkj.oa.utils.YamlUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompanyManageServiceImpl implements ICompanyManageService {

    @Resource
    private CompanyMapper companyMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ContractMapper contractMapper;

    @Override
    public void addCompany(Company company) {
        if (StringUtils.isEmpty(company.getCompanyName())) {
            throw new BusinessException("客户名称不能为空！");
        }

        if (StringUtils.isEmpty(company.getCreditCode())) {
            throw new BusinessException("统一社会信用代码不能为空！");
        }

        this.checkRepeat(company.getCompanyName());
        company.setCompanyId(RandomStringUtils.getNextVal());
        String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        company.setCreateTime(nowDate);
        company.setLastUpdateTime(nowDate);
        companyMapper.insert(company);
    }

    public void checkRepeat(String name) {
        List<Company> companies = companyMapper.list();
        if (companies.stream().anyMatch(c -> !StringUtils.isEmpty(c.getCompanyName()) && c.getCompanyName().equals(name))) {
            throw new BusinessException(name + "已存在，不可再添加！");
        }
    }

    @Override
    public void deleteCompany(String companyId) {
        if (StringUtils.isEmpty(companyId)) {
            throw new BusinessException("请先选择一个客户！");
        }
        companyMapper.deleteByPrimaryKey(companyId);
    }

    @Override
    public void updateCompany(Company company) {
        List<Company> companies = companyMapper.list();
        if (companies.stream().anyMatch(c -> !c.getCompanyId().equals(company.getCompanyId())
                && !StringUtils.isEmpty(c.getCompanyName()) && c.getCompanyName().equals(company.getCompanyName()))) {
            throw new BusinessException(company.getCompanyName() + "已存在，不可再添加！");
        }
        company.setLastUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        companyMapper.updateByPrimaryKeySelective(company);
    }

    @Override
    public List<Company> list(String companyName, Integer agentFlag) {
        List<Company> companies = new ArrayList<>();
        if (StringUtils.isEmpty(companyName)) {
            companies = companyMapper.list();
        } else {
            companies = companyMapper.listByName(companyName);
        }
        if (!ObjectUtils.isEmpty(agentFlag) && agentFlag.equals(1)) {
            companies = this.listByStatus();
        }
        return companies;
    }

    /**
     * 根据业务员id查看公司信息
     *
     * @param company
     * @return
     */
    @Override
    public PageInfo<Company> list(Company company) {
        List<Company> companies = getCompanyList(company);
        return new PageInfo<>(company.getPageNum(), company.getPageSize(), companies);
    }

    @Override
    public List<Company> getCompanyList(Company company) {
        List<Company> companies = companyMapper.listByName(company.getCompanyName());
        if (!CollectionUtils.isEmpty(companies)) {
            if (!StringUtils.isEmpty(company.getUserId())) {
                List<User> users = userMapper.list();
                //每个业务员只能看到自己和助手管理的公司，管理员可以看到所有的公司
                if (!CollectionUtils.isEmpty(users)) {
                    String roleName = users.stream().filter(u -> u.getUserId().equals(company.getUserId())).findFirst().get().getRoleName();
                    if (roleName.equals("管理员") || roleName.equals("财务主管") || roleName.equals("财务助理")) {

                    } else if (roleName.equals("业务助理")) {
                        User one = users.stream().filter(c -> !StringUtils.isEmpty(c.getAssistantIds())
                                && c.getAssistantIds().equals(company.getUserId())).findFirst().get();
                        if (ObjectUtils.isEmpty(one)) {
                            companies = new ArrayList<>();
                        } else {
                            companies = companies.stream().filter(c -> !StringUtils.isEmpty(c.getUserId())
                                    && c.getUserId().equals(one.getUserId())).collect(Collectors.toList());
                        }
                    } else {
                        companies = companies.stream().filter(c -> !StringUtils.isEmpty(c.getUserId())
                                && c.getUserId().equals(company.getUserId())).collect(Collectors.toList());
                    }
                }
            }
            if (!CollectionUtils.isEmpty(companies) && !StringUtils.isEmpty(company.getRegion())) {
                companies = companies.stream().filter(c -> !StringUtils.isEmpty(c.getRegion())
                        && c.getRegion().contains(company.getRegion())).collect(Collectors.toList());
            }
            //根据业务经理查询
            if (StrUtil.isNotEmpty(company.getUserName())) {
                List<String> useNames = Arrays.asList(company.getUserName().split(","));
                companies = companies.stream().filter(c -> StrUtil.isNotEmpty(c.getUserName())
                        && useNames.contains(c.getUserName())).collect(Collectors.toList());
            }
        }
        return companies;
    }


    @Override
    public Company selectOne(String companyId) {
        return companyMapper.selectByPrimaryKey(companyId);
    }

    @Override
    public List<String> getCompanyNames() {
        return companyMapper.list().stream().map(Company::getCompanyName).collect(Collectors.toList());
    }

    @Override
    public Map<String, String> getCompanyMap() {
        List<Company> companies = companyMapper.list();
        if (!CollectionUtils.isEmpty(companies)) {
            Map<String, String> companyMap = new HashMap<>();
            for (Company c : companies) {
                companyMap.put(c.getCompanyName(), c.getUserName());
            }
            return companyMap;
        }
        return null;
    }

    @Override
    public Map<String, String> getIdMap() {
        List<Company> companies = companyMapper.list();
        if (!CollectionUtils.isEmpty(companies)) {
            Map<String, String> companyMap = new HashMap<>();
            for (Company c : companies) {
                companyMap.put(c.getCompanyId(), c.getUserId());
            }
            return companyMap;
        }
        return null;
    }

    @Override
    public List<Company> listByStatus() {
        List<String> ids = contractMapper.dateList();
        List<Company> companies = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ids)) {
            companies = companyMapper.listByIds((List<String>) ids);
        }
        return companies;
    }

    public static void main(String[] args) {
        /*List<InsideInfo> infos = YamlUtils.read(List.class, "/company");
        for (InsideInfo i : infos) {
            if (i.getCompanyType().equals(1)) {
                System.out.println(i.getAddress());
            }
        }*/

    }
}