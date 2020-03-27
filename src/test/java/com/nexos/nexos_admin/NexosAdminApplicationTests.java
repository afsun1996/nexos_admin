package com.nexos.nexos_admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nexos.nexos_admin.mapper.SysUserMapper;
import com.nexos.nexos_admin.po.SysUser;
import com.nexos.nexos_admin.shiro.ShiroProperties;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {NexosAdminApplication.class})
@RunWith(SpringRunner.class)
class NexosAdminApplicationTests {

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    ShiroProperties shiroProperties;

    @Test
    void contextLoads() {


        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name","root");
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        System.out.println(sysUser);
    }

}
