package com.wtkj.oa;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@EnableConfigurationProperties
@SpringBootApplication
@EnableSwagger2
public class App {

    public static void main(String[] args) {
        log.info("项目开始启动");
        SpringApplication.run(App.class, args);
    }
}
