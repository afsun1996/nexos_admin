package com.nexos.nexos_admin;

import com.nexos.nexos_admin.mapper.SysUserMapper;
import com.nexos.nexos_admin.po.SysUser;
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

    @Test
    void contextLoads() {

        SysUser sysUser = new SysUser();
//        sysUser.setId(11);
        sysUser.setUserName("root");
        sysUser.setPwd("123");
        sysUser.setSalt("1212");
        sysUserMapper.insert(sysUser);

//        sysUser.setEmail("1213");
//
//        sysUserMapper.updateById(sysUser);

    }

}
