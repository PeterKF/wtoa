package com.wtkj.oa;

import cn.hutool.core.collection.CollectionUtil;
import com.wtkj.oa.service.IMenuService;
import com.wtkj.oa.service.InitDataService;
import org.junit.Test;
import org.junit.platform.commons.util.CollectionUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OaApplicationTests {
    @Resource
    private InitDataService initDtaService;

    @Resource
    private IMenuService menuService;

    @Test
    public void contextLoads() {
        System.out.println("test");
    }

    @Test
    public void init() {

        //initDtaService.initCompanies("C:\\Users\\Peter\\Desktop\\闻涛\\新增-2021年科技型中小企业名单（导入）.xlsx");
        /*List<String> names = initDtaService.initPatents("C:\\Users\\Peter\\Desktop\\闻涛\\闻涛-2021-7月-8月新增专利导入清单.xlsx");
        if (CollectionUtil.isNotEmpty(names)) {
            for (String name : names) {
                System.out.println(name);
            }
        }*/
    }


    @Test
    public void list() {
 /*       List<Menu> menus = menuService.list();
        for (Menu m : menus) {
            System.out.println(m.getRoleId() + "==" + m.getRoleAuthority().get(0).getUrl()+"=="+ m.getRoleAuthority().get(1).getUrl());
        }*/
    }
}
