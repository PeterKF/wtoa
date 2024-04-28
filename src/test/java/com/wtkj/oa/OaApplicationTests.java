package com.wtkj.oa;

import cn.hutool.core.collection.CollectionUtil;
import com.wtkj.oa.service.IMenuService;
import org.junit.Test;
import org.junit.platform.commons.util.CollectionUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OaApplicationTests {

    /*@Test
    public void getConection() {
        String url = "jdbc:postgresql://124.222.251.37:26219/test";
        String user = "root";
        String password = "Dbapp@2022";
        String sql = "select * from test.auto_table";
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();) {
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                System.out.println(resultSet.getRow());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void contextLoads() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT banner as 版本信息 FROM v$version");
        for (Map<String, Object> map : list) {
            for (String key : map.keySet()) {
                System.out.println(key + "-" + map.get(key));
            }
        }
    }

    @Test
    public void getDBConection() {
        Connection conn = null;
        String name = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://124.222.251.3:22322/test?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "Dbapp@2022";
        try {
            Class.forName(name);
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("数据库连接成功");
            String sql = "select * from test.test";
            Statement state = conn.createStatement();
            ResultSet res = state.executeQuery(sql);
        } catch (Exception e) {
            System.out.println(e);
        }
    }*/
}

