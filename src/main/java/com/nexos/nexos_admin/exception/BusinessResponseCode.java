package com.nexos.nexos_admin.exception;

/**
 * @program: nexos_admin
 * @description:
 * @author: afsun
 * @create: 2020-03-27 13:37
 */
public enum BusinessResponseCode implements ResponseCode {
    SUCCESS(0, "操作成功"),
    SYSTEM_BUSY(50001, "系统繁忙"),
    RPC_BUSY(50002,"RPC繁忙"),
    OPERATE_ERROR(50003,"操作失败"),

    TOKEN_EXPRIED(2001,"登录TOKEN已经过期"),
    TOKEN_BLANK(2002,"登录TOKEN为空"),
    TOKEN_ERROR(2003,"登录TOKEN错误"),

    PWD_ERROR(3001,"登录密码错误"),
    USER_INVALID(3002,"用户已经被注销"),
    USER_LOCK(3003,"用户已经被锁,请联系管理员解锁"),
    ACCOUNT_UNFIND(3004,"用户名不存在"),
    ;

    private int code;

    private String msg;

    BusinessResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}