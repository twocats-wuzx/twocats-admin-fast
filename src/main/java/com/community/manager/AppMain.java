package com.community.manager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity
@SpringBootApplication
@MapperScan(basePackages = {"com.community.manager.module.admin.mapper"})
public class AppMain {

    public static void main(String[] args) {
        SpringApplication.run(AppMain.class, args);
    }

}