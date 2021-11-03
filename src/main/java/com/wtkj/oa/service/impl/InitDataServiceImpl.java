package com.wtkj.oa.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.wtkj.oa.common.constant.PatentEnum;
import com.wtkj.oa.dao.CompanyMapper;
import com.wtkj.oa.dao.PatentMapper;
import com.wtkj.oa.dao.UserMapper;
import com.wtkj.oa.entity.Company;
import com.wtkj.oa.entity.Patent;
import com.wtkj.oa.exception.BusinessException;
import com.wtkj.oa.service.InitDataService;
import com.wtkj.oa.utils.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isEmpty;

@Service
public class InitDataServiceImpl implements InitDataService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private CompanyMapper companyMapper;
    @Resource
    private PatentMapper patentMapper;

    @Override
    //@Transactional(rollbackFor = Exception.class)
    public String initCompanies(MultipartFile file) {
        /*if (file.isEmpty()) {
            throw new BusinessException("请先选择上传文件");
        }

        int count = 0;
        ExcelReader reader = null;
        try {
            reader = ExcelUtil.getReader(file.getInputStream(), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<List<Object>> objectList = reader.read();

        if (CollectionUtil.isNotEmpty(objectList)) {
            for (int i = 0; i < objectList.size(); ++i) {
                List<Object> objects = objectList.get(i);
                List<String> comNames = new ArrayList();
                Company company = new Company().setCompanyId(RandomStringUtils.getNextVal())
                        .setYear(String.valueOf(objects.get(0)))
                        .setCompanyName(String.valueOf(objects.get(1))).setRegion(String.valueOf(objects.get(2)))
                        .setDirector(String.valueOf(objects.get(3))).setPhone(String.valueOf(objects.get(4)))
                        .setContact(String.valueOf(objects.get(5))).setTelephone(String.valueOf(objects.get(6)));

                if (CollectionUtil.isEmpty(comNames) || !comNames.contains(company.getCompanyName())) {
                    comNames.add(company.getCompanyName());
                    String userId = userMapper.getIdByName(String.valueOf(objects.get(7)));
                    company.setUserId(userId);
                    companyMapper.insert(company);
                    count++;
                }
            }
        }*/
        return "初始化客户数：" + 0 + "条！";
    }

    /**
     * 初始化合同信息
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String initPatents(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("请先选择上传文件");
        }
        List<List<Object>> dataList = null;
        try {
            dataList = ExcelUtil.getReader(file.getInputStream(), 0).read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Patent> patents = new ArrayList<>();

        if (!CollectionUtils.isEmpty(dataList)) {
            List<String> ids = patentMapper.getPatentIds();
            //List<Company> companies = companyMapper.list();
            String companyId = "";
            for (int i = 1; i < dataList.size(); i++) {
                List<Object> list = dataList.get(i);
                String companyName = String.valueOf(list.get(2));
                companyId = companyMapper.getIdByName(companyName);

                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String applicationDate = String.valueOf(list.get(1)).split(" ")[0];

                String patentId = String.valueOf(list.get(0));

                if (isEmpty(patentId)) {
                    patentId = "";
                }

                Patent patent = new Patent().setId(RandomStringUtils.getNextVal()).setPatentId(patentId)
                        .setCompanyId(companyId).setApplicationDate(applicationDate)
                        .setPatentName(String.valueOf(list.get(3)))
                        .setPatentType(PatentEnum.getTypeByName(String.valueOf(list.get(4))))
                        .setCreateTime(date).setLastUpdateTime(date);

                if (ids.stream().noneMatch(id -> id.equals(patent.getPatentId()))) {
                    patents.add(patent);
                }
                companyId = "";
            }

            if (!CollectionUtils.isEmpty(patents)) {
                patentMapper.insertBatch(patents);
            }
        }
        return "初始化知识产权" + patents.size() + "条！";
    }
}
