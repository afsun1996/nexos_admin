package com.nexos.nexos_admin.po;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class SysLog implements Serializable {
    /**
     * 日志号
     */
    @TableId(type = IdType.ASSIGN_ID)
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

    private String moduleName;

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

