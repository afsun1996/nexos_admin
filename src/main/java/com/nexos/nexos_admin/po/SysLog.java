package com.nexos.nexos_admin.po;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class SysLog implements Serializable {
    /**
     * 日志号
     */
    private Long id;

    /**
     * 请求ip
     */
    private String requestIp;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 调用耗时
     */
    private Long consumeTime;

    /**
     * 请求地址
     */
    private String requestPath;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 响应参数
     */
    private String response;

    /**
     *
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}

