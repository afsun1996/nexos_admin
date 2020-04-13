package com.nexos.nexos_admin.config;

import com.nexos.nexos_admin.util.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @program: nexos_admin
 * @description:
 * @author: afsun
 * @create: 2020-04-13 13:58
 */
@Configuration
@Slf4j
public class ZookeeperCfg {

    @Value("${zookeeper.address}")
    private String address;

    @Value("${zookeeper.timeout}")
    private int timeout;

//    @Resource
//    private ZookeeperParam zookeeperParam ;

    private static CuratorFramework client = null ;


    /**
     * zookeeper基础方式
     * @return
     * @throws InterruptedException
     */
    @Bean(name = "zkClient")
    public ZooKeeper zkClient() throws InterruptedException {
        ZooKeeper zooKeeper = null;
        log.info("初始化zookeeper,监听心跳 address = {}",address);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            zooKeeper = new ZooKeeper(address, timeout, watchedEvent -> {
                if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    countDownLatch.countDown();
                    log.info("初始化zookeeper成功");
                }
            });
        } catch (Exception e) {
            log.error("初始化zookeeper异常"+ExceptionUtils.getMessage(e));
        }
        countDownLatch.await();
        return zooKeeper;
    }


//    /**
//     * curator组件封装方式
//     */
//    @PostConstruct
//    public void init (){
//        //重试策略，初试时间1秒，重试10次
//        RetryPolicy policy = new ExponentialBackoffRetry(
//                1000,
//                2);
//        //通过工厂创建Curator
//        client = CuratorFrameworkFactory.builder()
//                .connectString("192.168.254.3:2181")
////                .authorization("digest",zookeeperParam.getDigest().getBytes())
//                .connectionTimeoutMs(3000)
//                .sessionTimeoutMs(3000)
//                .retryPolicy(policy).build();
//        //开启连接
//        client.start();
//        log.info("zookeeper 初始化完成...");
//    }
//
//    public static CuratorFramework getClient (){
//        return client ;
//    }
//    public static void closeClient (){
//        if (client != null){
//            client.close();
//        }
//    }


}