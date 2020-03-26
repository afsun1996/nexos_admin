package com.nexos.nexos_admin.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Data;

public enum SysUserStatus {

    ACCOUNT_VALID("0","有效"),ACCOUNT_INVALID("1","无效"),
    ACCOUNT_LOCK("1","锁住"),ACCOUNT_UNLOCK("0","无锁");

    private SysUserStatus(String code,String desc){

    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @EnumValue
    private String code;

    private String desc;

}
