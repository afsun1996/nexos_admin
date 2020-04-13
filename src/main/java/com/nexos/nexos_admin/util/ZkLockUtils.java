package com.nexos.nexos_admin.util;

import com.baomidou.mybatisplus.extension.api.R;
import com.nexos.nexos_admin.config.SpringContextHolder;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @program: nexos_admin
 * @description:
 * @author: afsun
 * @create: 2020-04-13 16:14
 */
public class ZkLockUtils {

    private static final String LOCKNAME = "/locks";

    private String frontNodeName;

    private String currentNodeName;

    ZooKeeper zkClient = null;

    public ZkLockUtils(String frontNodeName) {
        zkClient = SpringContextHolder.getBean("zkClient");
    }

    public boolean tryLock() throws KeeperException, InterruptedException {
        String lock = zkClient.create(LOCKNAME + "/lock", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, // 先写入节点
                CreateMode.PERSISTENT_SEQUENTIAL);
        currentNodeName = lock.split(LOCKNAME)[1];
        if (isGetLock()) {
            return true;
        }
        List<String> children = zkClient.getChildren(LOCKNAME, null);  // 获取所有的子节点
        Collections.sort(children);
        int index = Collections.binarySearch(children, currentNodeName); // 获取排名
        frontNodeName = children.get(index - 1);  // 前一个节点的名称
        return false;
    }

    public boolean lock() throws KeeperException, InterruptedException {
        boolean lock = lock();
        if (lock) {
            return true;
        }
        while (!lock) {
            await();
            if (isGetLock()) {
                lock = true;
            }
        }
        return true;
    }

    private void await(){
        if (null == frontNodeName){
            throw new RuntimeException();
        }
        CountDownLatch latch = new CountDownLatch(1);
        zkClient.get
    }

    private boolean isGetLock() throws KeeperException, InterruptedException {
        List<String> children = zkClient.getChildren(LOCKNAME, null);  // 获取所有的子节点
        Collections.sort(children);
        if (children.get(0).equals(currentNodeName)) { // 如果是第一个节点,则可以运行
            return true;
        }
        return false;

    }


}