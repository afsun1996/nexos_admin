package com.nexos.nexos_admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nexos.nexos_admin.mapper.SysUserMapper;
import com.nexos.nexos_admin.po.SysUser;
import com.nexos.nexos_admin.service.facade.RedisService;
import com.nexos.nexos_admin.shiro.ShiroProperties;
import org.aspectj.lang.annotation.Around;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {NexosAdminApplication.class})
@RunWith(SpringRunner.class)
class NexosAdminApplicationTests {

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    ShiroProperties shiroProperties;

    @Autowired
    RedisService redisService;

    @Autowired
    MappingJackson2HttpMessageConverter httpMessageConverter;

    @Test
    void contextLoads() {

        System.out.println(httpMessageConverter);
        System.out.println("lock:"+redisService.getLock("test", "test", 60));
        System.out.println("release:"+redisService.releaseLock("test", "test"));
    }

}
