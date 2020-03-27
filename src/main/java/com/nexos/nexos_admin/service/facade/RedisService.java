package com.nexos.nexos_admin.service.facade;

public interface RedisService {

    boolean getLock(String lockName, String value, int expireTime);

    boolean releaseLock(String lockName, String value);

    void set(String keyName, Object Value, long time);

    void delete(String KeyName);

    Object get(String keyName);
}
