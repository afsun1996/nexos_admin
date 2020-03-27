package com.nexos.nexos_admin.service;

import com.nexos.nexos_admin.service.facade.RedisService;
import com.sun.corba.se.spi.ior.ObjectKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @program: nexos_admin
 * @description: 并发锁
 * @author: afsun
 * @create: 2020-03-27 09:22
 */
@Component
public class RedisServiceImpl implements RedisService {

    public static final String LOCK_PREFIX = "redis_lock";

    String releaseScript = "if \n" +
            "    redis.call('get', KEYS[1]) == ARGV[1] \n" +
            "then \n" +
            "    return redis.call('del', KEYS[1]) \n" +
            "else \n" +
            "    return 0 \n" +
            "end";

    String lockScript = "if \n" +
            "        redis.call('setNx',KEYS[1],ARGV[1]) \n" +
            "    then \n" +
            "        if redis.call('get',KEYS[1])==ARGV[1] \n" +
            "    then \n" +
            "        return redis.call('expire',KEYS[1],ARGV[2]) \n" +
            "    else \n" +
            "        return 0 \n" +
            "    end \n" +
            "end\n";

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 加锁
     * @param lockName
     * @param value
     * @param expireTime
     * @return
     */
    public boolean getLock(String lockName,String value,int expireTime){
        Boolean isSuccess = redisTemplate.opsForValue().setIfAbsent(LOCK_PREFIX + lockName, "",
                expireTime, TimeUnit.SECONDS);
        return isSuccess;
    }

    /**
     * 解锁
     * @param lockName
     * @param value
     * @return
     */
    public boolean releaseLock(String lockName,String value){
        DefaultRedisScript defaultRedisScript = new DefaultRedisScript();
        defaultRedisScript.setScriptText(releaseScript);
        Object result = redisTemplate.execute(defaultRedisScript, new StringRedisSerializer(),
                new StringRedisSerializer(), Collections.singletonList(lockName), value);
        return "1".equals(result);
    }

    public void set(String keyName,Object Value,long time){
        redisTemplate.opsForValue().set(keyName,Value,time,TimeUnit.SECONDS);
    }

    public Object get(String keyName){
        return redisTemplate.opsForValue().get(keyName);
    }


}