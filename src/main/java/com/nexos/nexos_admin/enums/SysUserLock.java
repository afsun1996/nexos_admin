package com.nexos.nexos_admin.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SysUserLock {

    ACCOUNT_LOCK("1","锁住"),ACCOUNT_UNLOCK("0","无锁");

    private SysUserLock(String code,String desc){
        this.code = code;
        this.desc = desc;
    }

    @EnumValue
    @JsonValue
    private String code;

    private String desc;

    @Override
    public String toString() {
        return desc;
    }
}
