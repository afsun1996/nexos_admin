package com.nexos.nexos_admin.exception;

import lombok.Data;

/**
 * @program: nexos_admin
 * @description:
 * @author: afsun
 * @create: 2020-03-27 13:31
 */
@Data
public class BussinessException extends RuntimeException {

    private final int code;

    private final String descMsg;

    public BussinessException(int code, String descMsg) {
        super(descMsg);
        this.code = code;
        this.descMsg = descMsg;
    }

    public BussinessException(ResponseCode responseCode) {
        super(responseCode.getMsg());
        this.code = responseCode.getCode();
        this.descMsg = responseCode.getMsg();
    }

}