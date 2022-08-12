package com.wtkj.oa.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.word.Word07Writer;
import com.github.pagehelper.PageHelper;
import com.wtkj.oa.common.config.PageInfo;
import com.wtkj.oa.common.constant.PatentEnum;
import com.wtkj.oa.dao.PatentMapper;
import com.wtkj.oa.dao.UserMapper;
import com.wtkj.oa.entity.*;
import com.wtkj.oa.exception.BusinessException;
import com.wtkj.oa.service.ICompanyManageService;
import com.wtkj.oa.service.IPatentManageService;
import com.wtkj.oa.utils.RandomStringUtils;
import com.wtkj.oa.utils.YamlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PatentManageServiceImpl implements IPatentManageService {

    @Resource
    private PatentMapper patentMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ICompanyManageService companyManageService;

    @Override
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

    @Override
    public void delete(String patentId) {
        if (CharSequenceUtil.isEmpty(patentId)) {
            throw new BusinessException("请先选择要删除的专利！");
        }
        patentMapper.deleteByPrimaryKey(patentId);
    }

    @Override
    public void update(Patent patent) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        patent.setLastUpdateTime(time);
        patentMapper.updateByPrimaryKeySelective(patent);
    }

    @Override
    public PageInfo<Patent> list(Patent patent) {
        List<Patent> patentList = getPatents(patent);
        return new PageInfo<>(patent.getPageNum(), patent.getPageSize(), patentList);
    }

    public List<Patent> getPatents(Patent patent) {
        List<Patent> patents;
        if (CharSequenceUtil.isEmpty(patent.getPatentName())) {
            patents = patentMapper.listByName(null);
        } else {
            patents = patentMapper.listLikeName(patent.getPatentName());
        }

        String year = patent.getYear();
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
                if (!CharSequenceUtil.isEmpty(patent.getCompanyName())) {
                    patentList = patentList.stream().filter(p -> !CharSequenceUtil.isEmpty(p.getCompanyName())
                            && p.getCompanyName().contains(patent.getCompanyName())).collect(Collectors.toList());
                }
                //通过年份过滤
                if (CharSequenceUtil.isNotEmpty(year)) {
                    patentList = patentList.stream().filter(p -> CharSequenceUtil.isNotEmpty(p.getApplicationDate())
                            && p.getApplicationDate().split("-")[0].equals(year)).collect(Collectors.toList());
                }

                Map<String, String> companyMap = companyManageService.getCompanyMap();
                for (Patent p : patents) {
                    if (!CollectionUtils.isEmpty(companyMap)) {
                        p.setUserName(companyMap.get(p.getCompanyName()));
                    }
                }

                if (CharSequenceUtil.isNotEmpty(patent.getUserName())) {
                    List<String> useNames = Arrays.asList(patent.getUserName().split(","));
                    patentList = patentList.stream().filter(p -> CharSequenceUtil.isNotEmpty(p.getUserName())
                            && useNames.contains(p.getUserName())).collect(Collectors.toList());
                }
            }
        }
        return patentList;
    }

    private void getPatentList(List<Patent> patents, List<Patent> patentList, Map<String, String> idMap, String userId) {
        for (Patent p : patents) {
            if (!CollectionUtils.isEmpty(idMap) && CharSequenceUtil.isNotEmpty(idMap.get(p.getCompanyId()))
                    && idMap.get(p.getCompanyId()).equals(userId)) {
                patentList.add(p);
            }
        }
    }

    /**
     * 获取专利费用清单
     */
    public void getPatentExpenseList(List<String> patentIds, String companyType, HttpServletResponse response) {
        if (CollUtil.isEmpty(patentIds)) {
            throw new BusinessException("请选择要导出的专利清单");
        }

        ExcelWriter writer = ExcelUtil.getWriter();
        writer.merge(patentIds.size() - 1, "专 利 费 用 结 算 单");
        writer.merge(patentIds.size() - 1, "公司名称");
        writer.merge(patentIds.size() - 1, "  您好！贵公司近期需要缴纳的专利费用清单如下：");
        List<Map<String, Object>> patentList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < patentIds.size(); i++) {
            Patent patent = patentMapper.selectByPatentId(patentIds.get(i));
            Map<String, Object> patentMap = new HashMap<String, Object>();
            patentMap.put("序号", i + 1);
            patentMap.put("类型", PatentEnum.getNameByType(patent.getPatentType()));
            patentMap.put("专利申请号", patent.getPatentId());
            patentMap.put("专利名称", patent.getPatentName());
            patentMap.put("实用代理费(元)", patent.getAgencyFee());
            patentMap.put("申请官费(元)", patent.getOfficialFee());
            patentMap.put("费用(元)", Integer.parseInt(patent.getAgencyFee()) + Integer.parseInt(patent.getOfficialFee()));
            patentList.add(patentMap);
        }
        writer.merge(patentList.size() + 3, patentList.size() + 3, 0, 4,
                "费  用  合  计", true);
        writer.write(patentList, true);
        writer.merge(patentList.size() + 4, patentList.size() + 4, 0, 6,
                "说明：", true);
        writer.merge(patentList.size() + 5, patentList.size() + 5, 0, 7,
                "    1、专利申请结算费用包括：专利申请代理费、申请费、实审费、办理登记费、年费等国家知识产权局收取的相关行政" +
                        "事业收费，由我方代收代缴；", true);
        writer.merge(patentList.size() + 6, patentList.size() + 6, 0, 6,
                "    2、按照约定，当收到“专利受理通知书”，企业需要支付费用合计￥12600元，请务必尽快安排费用付款。" +
                        "", true);
        writer.merge(patentList.size() + 7, patentList.size() + 7, 0, 6,
                "    3、因专利权人未及时安排费用导致专利视为撤回或终止，我公司概不负责，请谅解。" +
                        "", true);
        writer.merge(patentList.size() + 8, patentList.size() + 8, 0, 6,
                "    祝商祺，谢谢！" +
                        "", true);
        //填写乙方信息
        List<InsideInfo> infos = YamlUtils.read(List.class, "/company");
        InsideInfo insideInfo = infos.stream().filter(i -> i.getCompanyType().equals(Integer.parseInt(companyType))).findFirst().get();
        writer.merge(patentList.size() + 9, patentList.size() + 9, 0, 1,
                "    单位名称：\t\n", true);
        writer.merge(patentList.size() + 9, patentList.size() + 9, 2, 7,
                insideInfo.getCompanyName(), true);
        writer.merge(patentList.size() + 10, patentList.size() + 10, 0, 1,
                "    开 户 行：\t\n", true);
        writer.merge(patentList.size() + 10, patentList.size() + 10, 2, 7,
                insideInfo.getBank(), true);
        writer.merge(patentList.size() + 11, patentList.size() + 11, 0, 1,
                "    账  　号：\t\n", true);
        writer.merge(patentList.size() + 11, patentList.size() + 11, 2, 7,
                insideInfo.getAccountNo(), true);
        writer.merge(patentList.size() + 12, patentList.size() + 12, 0, 1,
                "    行    号：\t\n", true);
        writer.merge(patentList.size() + 12, patentList.size() + 12, 2, 7,
                insideInfo.getBankNo(), true);
        //response为HttpServletResponse对象
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=test.xls");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            writer.flush(out, true);
            // 关闭writer，释放内存
            writer.close();
            IoUtil.close(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成专利结算清单
     *
     * @param companyId   甲方公司id
     * @param companyType 乙方公司id
     * @param patentIds   合同id
     * @return
     */
    @Override
    public PatentDetail getPatentDetail(String companyId, String companyType, List<String> patentIds) {
        if (CollUtil.isEmpty(patentIds)) {
            throw new BusinessException("请选择要导出的专利清单");
        }
        PatentDetail detail = new PatentDetail();
        //甲方信息
        Company company = companyManageService.selectOne(companyId);
        if (company == null) {
            throw new BusinessException("请选择专利所在的公司");
        }
        detail.setCompanyName(company.getCompanyName());
        List<Patent> patentList = new ArrayList<Patent>(patentIds.size());
        Integer sumFee = 0;
        for (int i = 0; i < patentIds.size(); i++) {
            Patent patent = patentMapper.selectByPatentId(patentIds.get(i));
            if (!companyId.equals(patent.getCompanyId())) {
                throw new IllegalArgumentException("选择的合同必须是在同一家公司下");
            }
            String type = PatentEnum.getNameByType(patent.getPatentType());
            patent.setType(type);
            patent.setExpense(Integer.parseInt(patent.getAgencyFee()) + Integer.parseInt(patent.getOfficialFee()));
            sumFee += patent.getExpense();
            patentList.add(patent);
        }
        detail.setSumFee(sumFee);
        detail.setPatentList(patentList);
        //乙方信息
        List<InsideInfo> infos = YamlUtils.read(List.class, "/company");
        InsideInfo insideInfo = infos.stream().filter(i -> i.getCompanyType()
                .equals(Integer.parseInt(companyType))).findFirst().get();
        detail.setInsideInfo(insideInfo);
        String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        detail.setCurrentDate(date);
        return detail;
    }


}
