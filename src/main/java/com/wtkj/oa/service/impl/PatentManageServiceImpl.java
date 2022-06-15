package com.wtkj.oa.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.word.Word07Writer;
import com.github.pagehelper.PageHelper;
import com.wtkj.oa.common.config.PageInfo;
import com.wtkj.oa.common.constant.PatentEnum;
import com.wtkj.oa.dao.PatentMapper;
import com.wtkj.oa.dao.UserMapper;
import com.wtkj.oa.entity.InsideInfo;
import com.wtkj.oa.entity.Patent;
import com.wtkj.oa.entity.User;
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

    /**
     * 获取专利费用清单
     */
    @Override
    public void getPatentExpenseList(List<String> patentIds, String companyType, HttpServletResponse response) {
        Word07Writer writer = new Word07Writer();
        writer.addText(ParagraphAlignment.CENTER, new Font("微软雅黑", Font.PLAIN, 22), "专利费用结算单");
        writer.addText(new Font("宋体", Font.PLAIN, 30), "公司名称");
        writer.addText(new Font("宋体", Font.PLAIN, 30), "    您好！贵公司近期需要缴纳的专利费用清单如下：");
        List<Map<String, String>> contentList = new ArrayList<>();
        if (CollUtil.isEmpty(patentIds)) {
            throw new BusinessException("请选择要导出的专利清单");
        }

        Integer sumFee = 0;
        for (int i = 0; i < patentIds.size(); i++) {
            LinkedHashMap<String, String> contentMap = new LinkedHashMap<>();
            if (i == patentIds.size() - 1) {
                contentMap.put("费用类型", "费用合计");
                contentMap.put("费用（单位：元）", String.valueOf(sumFee));
            } else {
                Patent patent = patentMapper.selectByPatentId(patentIds.get(i));
                contentMap.put("序号", String.valueOf(i + 1));
                contentMap.put("类型", PatentEnum.getNameByType(patent.getPatentType()));
                contentMap.put("专利申请号", patent.getPatentId());
                contentMap.put("专利名称", patent.getPatentName());
                contentMap.put("费用类型", "实用代理费 " + patent.getAgencyFee() + "+" + "申请费 " + patent.getOfficialFee());
                contentMap.put("费用（单位：元）", String.valueOf(patent.getAgencyFee() + patent.getOfficialFee()));
                sumFee = sumFee + patent.getAgencyFee() + patent.getOfficialFee();
            }
            contentList.add(contentMap);
        }
        writer.addTable(contentList);
        writer.addText(new Font("宋体", Font.PLAIN, 12), "说明：");
        writer.addText(new Font("宋体", Font.PLAIN, 12), "1、专利申请结算费用包括：专利申请代理费、申请费、实审费、办理登记费、年费等国家知识产权局收取的相关行政事业收费，由我方代收代缴；");
        writer.addText(new Font("宋体", Font.PLAIN, 12), "2、按照约定，当收到“专利受理通知书”，企业需要支付费用合计￥12600元，请务必尽快安排费用付款。");
        writer.addText(new Font("宋体", Font.PLAIN, 12), "3、因专利权人未及时安排费用导致专利视为撤回或终止，我公司概不负责，请谅解。");
        writer.addText(new Font("宋体", Font.PLAIN, 12), "   祝商祺，谢谢！");
        //填写乙方信息
        List<InsideInfo> infos = YamlUtils.read(List.class, "/company");
        InsideInfo insideInfo = infos.stream().filter(i -> i.getCompanyType().equals(Integer.parseInt(companyType))).findFirst().get();
        writer.addText(new Font("宋体", Font.PLAIN, 12), "   单位名称：" + insideInfo.getCompanyName());
        writer.addText(new Font("宋体", Font.PLAIN, 12), "   开户行：" + insideInfo.getBank());
        writer.addText(new Font("宋体", Font.PLAIN, 12), "   账号：" + insideInfo.getBankNo());
        writer.addText(new Font("宋体", Font.PLAIN, 12), "                              " + insideInfo.getCompanyName());
        writer.addText(new Font("宋体", Font.PLAIN, 12), "                              " + DateUtil.format(new Date(), "yyyy-MM-dd"));

        response.setHeader("Content-Disposition", "attachment;filename=patentInfo.docx");
        try (ServletOutputStream out = response.getOutputStream();) {
            writer.flush(out, true);
        } catch (IOException e) {
            log.info("error message", e);
        }
        writer.close();
    }

    public static void main(String[] args) {
        Word07Writer writer = new Word07Writer();
        writer.addText(ParagraphAlignment.CENTER, new Font("微软雅黑", Font.PLAIN, 22), "专利费用结算单");
        writer.addText(new Font("宋体", Font.PLAIN, 12), "公司名称");
        writer.addText(new Font("宋体", Font.PLAIN, 12), "    您好！贵公司近期需要缴纳的专利费用清单如下：");
        List<LinkedHashMap<String, String>> contentList = new ArrayList<>();
        LinkedHashMap<String, String> contentMap = new LinkedHashMap<>();
        contentMap.put("序号", "1");
        contentMap.put("类型", "实用");
        contentMap.put("专利申请号", "12345678");
        contentMap.put("专利名称", "dasdfa");
        contentMap.put("费用类型", "qwer23r");
        contentMap.put("费用（单位：元）", "1000");
        LinkedHashMap<String, String> contentMap2 = new LinkedHashMap<>();
        contentMap2.put("序号", "2");
        contentMap2.put("类型", "实用");
        contentMap2.put("专利申请号", "23432234");
        contentMap2.put("专利名称", "1234qwe");
        contentMap2.put("费用类型", "1234qwer");
        contentMap2.put("费用（单位：元）", "2000");
        contentList.add(contentMap);
        contentList.add(contentMap2);
        writer.addTable(contentList);
        writer.addText(new Font("宋体", Font.PLAIN, 12), "说明：");
        writer.addText(new Font("宋体", Font.PLAIN, 12), "1、专利申请结算费用包括：专利申请代理费、申请费、实审费、办理登记费、年费等国家知识产权局收取的相关行政事业收费，由我方代收代缴；");
        writer.addText(new Font("宋体", Font.PLAIN, 12), "2、按照约定，当收到“专利受理通知书”，企业需要支付费用合计￥12600元，请务必尽快安排费用付款。");
        writer.addText(new Font("宋体", Font.PLAIN, 12), "3、因专利权人未及时安排费用导致专利视为撤回或终止，我公司概不负责，请谅解。");
        writer.addText(new Font("宋体", Font.PLAIN, 12), "  祝商祺，谢谢！");
        writer.flush(FileUtil.file("D:\\test\\wordWrite.docx"));
        writer.close();
    }
}
