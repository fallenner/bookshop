package com.jsdai.jsdaimanage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.jsdai.jsdaimanage.dao")
public class JsdaiManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsdaiManageApplication.class, args);
    }
}
