package com.nexos.nexos_admin;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
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

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer(){
        return builder -> builder.featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    }

    public static void main(String[] args) {
        SpringApplication.run(NexosAdminApplication.class, args);
    }

}
