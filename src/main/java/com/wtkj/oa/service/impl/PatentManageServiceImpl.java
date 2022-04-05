package com.wtkj.oa.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.wtkj.oa.common.config.PageInfo;
import com.wtkj.oa.dao.PatentMapper;
import com.wtkj.oa.dao.UserMapper;
import com.wtkj.oa.entity.Patent;
import com.wtkj.oa.entity.User;
import com.wtkj.oa.exception.BusinessException;
import com.wtkj.oa.service.ICompanyManageService;
import com.wtkj.oa.service.IPatentManageService;
import com.wtkj.oa.utils.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PatentManageServiceImpl implements IPatentManageService {

    @Resource
    private PatentMapper patentMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ICompanyManageService companyManageService;

    public void addPatent(Patent patent) {
        if (StringUtils.isEmpty(patent.getCompanyId())) {
            throw new BusinessException("请选择一个客户！");
        }

        if (StringUtils.isEmpty(patent.getPatentName())) {
            throw new BusinessException("请填写专利名称！");
        }

        if (StringUtils.isEmpty(patent.getPatentType())) {
            throw new BusinessException("请选择一种专利类型");
        }
        this.checkRepeat(patent.getPatentName());
        patent.setId(RandomStringUtils.getNextVal());
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        patent.setCreateTime(time);
        patent.setLastUpdateTime(time);
        patentMapper.insert(patent);
    }

    public void checkRepeat(String name) {
        List<Patent> patents = patentMapper.listByName(name);
        if (patents.size() > 0) {
            throw new BusinessException(name + "专利已经存在，不可再添加！");
        }
    }

    public void delete(String patentId) {
        if (StringUtils.isEmpty(patentId)) {
            throw new BusinessException("请先选择要删除的专利！");
        }
        patentMapper.deleteByPrimaryKey(patentId);
    }

    public void update(Patent patent) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        patent.setLastUpdateTime(time);
        patentMapper.updateByPrimaryKeySelective(patent);
    }

    @Override
    public PageInfo<Patent> list(Patent patent) {
        List<Patent> patents;
        PageHelper.startPage(patent.getPageNum(), patent.getPageSize());
        if (StringUtils.isEmpty(patent.getPatentName())) {
            patents = patentMapper.listByName(null);
        } else {
            patents = patentMapper.listLikeName(patent.getPatentName());
        }
        List<Patent> patentList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(patents)) {
            Map<String, String> idMap = companyManageService.getIdMap();
            List<User> users = userMapper.list();
            if (!CollectionUtils.isEmpty(users)) {
                String roleName = users.stream().filter(u -> u.getUserId().equals(patent.getUserId())).findFirst().get().getRoleName();
                if (roleName.equals("管理员") || roleName.equals("财务主管") || roleName.equals("财务助理") || roleName.equals("IP管理员")) {
                    patentList = patents;
                } else if (roleName.equals("业务助理")) {
                    User one = users.stream().filter(c -> !StringUtils.isEmpty(c.getAssistantIds())
                            && c.getAssistantIds().equals(patent.getUserId())).findFirst().get();
                    if (ObjectUtils.isEmpty(one)) {

                    } else {
                        getPatentList(patents, patentList, idMap, one.getUserId());
                    }
                } else {
                    getPatentList(patents, patentList, idMap, patent.getUserId());
                }
            }

            if (!CollectionUtils.isEmpty(patentList)) {
                if (!StringUtils.isEmpty(patent.getCompanyName())) {
                    patentList = patentList.stream().filter(p -> !StringUtils.isEmpty(p.getCompanyName())
                            && p.getCompanyName().contains(patent.getCompanyName())).collect(Collectors.toList());
                }

                Map<String, String> companyMap = companyManageService.getCompanyMap();
                for (Patent p : patents) {
                    if (!CollectionUtils.isEmpty(companyMap)) {
                        p.setUserName(companyMap.get(p.getCompanyName()));
                    }
                }

                if (StrUtil.isNotEmpty(patent.getUserName())) {
                    List<String> useNames = Arrays.asList(patent.getUserName().split(","));
                    patentList = patentList.stream().filter(p -> StrUtil.isNotEmpty(p.getUserName())
                            && useNames.contains(p.getUserName())).collect(Collectors.toList());
                }
                return new PageInfo<>(patent.getPageNum(), patent.getPageSize(), patentList);
            }
        }
        return new PageInfo<>(patent.getPageNum(), patent.getPageSize(), patentList);
    }

    private void getPatentList(List<Patent> patents, List<Patent> patentList, Map<String, String> idMap, String userId) {
        for (Patent p : patents) {
            if (!CollectionUtils.isEmpty(idMap) && !StringUtils.isEmpty(idMap.get(p.getCompanyId()))
                    && idMap.get(p.getCompanyId()).equals(userId)) {
                patentList.add(p);
            }
        }
    }
}
