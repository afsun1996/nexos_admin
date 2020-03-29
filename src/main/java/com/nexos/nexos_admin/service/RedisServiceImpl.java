package com.nexos.nexos_admin.service;

import com.nexos.nexos_admin.service.facade.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: nexos_admin
 * @description: 并发锁
 * @author: afsun
 * @create: 2020-03-27 09:22
 */
@Component
public class RedisServiceImpl implements RedisService {

    private final String LOCK_PREFIX = "redis_lock:";

    private DefaultRedisScript releaseLockScrpter =new DefaultRedisScript();

    private String releaseScript = "";

    @Autowired
    RedisTemplate redisTemplate;


    @PostConstruct
    public void init(){
        releaseLockScrpter.setResultType(List.class);
        ResourceScriptSource resources = new ResourceScriptSource(new ClassPathResource("lua/releaseLock.lua"));
        releaseLockScrpter.setScriptSource(resources);
    }

    /**
     * 加锁
     *
     * @param lockName
     * @param value
     * @param expireTime
     * @return
     */
    @Override
    public boolean getLock(String lockName, String value, int expireTime) {
        Boolean isSuccess = redisTemplate.opsForValue().setIfAbsent(LOCK_PREFIX + lockName, value,
                expireTime, TimeUnit.SECONDS);
        return isSuccess;
    }

    /**
     * 解锁
     *
     * @param lockName
     * @param value
     * @return
     */
    @Override
    public boolean releaseLock(String lockName, String value) {
        /**
         * List设置lua的KEYS
         */
        List<String> keyList = new ArrayList();
        keyList.add(this.LOCK_PREFIX+lockName);
        long result = (long) ((ArrayList) redisTemplate.execute(releaseLockScrpter, keyList, value)).get(0);
        return result > 0;
    }

    @Override
    public void set(String keyName, Object Value, long time) {
        redisTemplate.opsForValue().set(keyName, Value, time, TimeUnit.SECONDS);
    }

    @Override
    public void delete(String KeyName) {
        redisTemplate.delete(KeyName);
    }

    @Override
    public Object get(String keyName) {
        return redisTemplate.opsForValue().get(keyName);
    }


}