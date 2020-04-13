package com.nexos.nexos_admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nexos.nexos_admin.config.ZookeeperCfg;
import com.nexos.nexos_admin.mapper.SysUserMapper;
import com.nexos.nexos_admin.po.SysUser;
import com.nexos.nexos_admin.service.facade.RedisService;
import com.nexos.nexos_admin.shiro.ShiroProperties;
import com.nexos.nexos_admin.util.ZkLockUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.aspectj.lang.annotation.Around;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = {NexosAdminApplication.class})
@RunWith(SpringRunner.class)
class NexosAdminApplicationTests {

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    ZooKeeper zkClient;


    @Test
    void contextLoads() throws Exception {

//        System.out.println(zkClient.getChildren("/dubbo", null));
//        zkClient.create("/afsun2","212325".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        CuratorFramework client = ZookeeperCfg.getClient();
//        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).forPath("/afsun1","".getBytes());
//        List<String> strings = client.getChildren().forPath("/dubbo");
//        System.out.println(strings);

        ZkLockUtils.lock(null);





    }

}
