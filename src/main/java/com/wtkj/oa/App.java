package com.wtkj.oa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableConfigurationProperties
@SpringBootApplication
@EnableSwagger2
@MapperScan("com.wtkj.oa.dao")
public class App {

    public static void main(String[] args) {
        System.out.println("server启动成功！");
        SpringApplication.run(App.class, args);
    }
}
