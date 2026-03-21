package com.crise.demoj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.crise.demoj.dao.mapper")
public class DemoJApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoJApplication.class, args);
    }

}
