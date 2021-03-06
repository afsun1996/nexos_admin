package com.nexos.nexos_admin.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.io.Serializable;

public enum SysUserStatus {

    ACCOUNT_VALID("0","有效"),ACCOUNT_INVALID("1","无效");

    private SysUserStatus(String code,String desc){
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
