package com.nexos.nexos_admin;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableScheduling
@ServletComponentScan(basePackages = "top.afsun1996.nexos.controller")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan(basePackages = {"com.nexos.nexos_admin.mapper"})
public class NexosAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(NexosAdminApplication.class, args);
    }

}
