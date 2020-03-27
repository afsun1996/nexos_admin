package com.nexos.nexos_admin.service;

import com.nexos.nexos_admin.service.facade.RedisService;
import com.sun.corba.se.spi.ior.ObjectKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
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

    public static final String LOCK_PREFIX = "redis_lock:";

    String releaseScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

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
     *
     * @param lockName
     * @param value
     * @param expireTime
     * @return
     */
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
    public boolean releaseLock(String lockName, String value) {
        byte[][] keysAndArgs = new byte[2][];
        keysAndArgs[0] = lockName.getBytes(Charset.forName("UTF-8"));
        keysAndArgs[1] = value.getBytes(Charset.forName("UTF-8"));
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        RedisConnection conn = factory.getConnection();
        try {
            Long result = (Long)conn.scriptingCommands().eval(releaseScript.getBytes(Charset.forName("UTF-8")), ReturnType.INTEGER, 1, keysAndArgs);
            if(result!=null && result>0)
                return true;
        }finally {
            RedisConnectionUtils.releaseConnection(conn, factory);
        }
        return false;
    }

    public void set(String keyName, Object Value, long time) {
        redisTemplate.opsForValue().set(keyName, Value, time, TimeUnit.SECONDS);
    }

    @Override
    public void delete(String KeyName) {
        redisTemplate.delete(KeyName);
    }

    public Object get(String keyName) {
        return redisTemplate.opsForValue().get(keyName);
    }


}