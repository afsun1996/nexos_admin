package com.nexos.nexos_admin.vo;

import io.swagger.annotations.Api;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: NexosBlog
 * @description: 返回类型
 * @author: afsun
 * @create: 2019-10-30 13:37
 */
@Data
@Api("输出结果")
public class ResultInfo<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    // 结果对象内容
    private T result;

    // 成功标志位
    private boolean success;

    // 错误码
    private String code;

    // 错误描述
    private String ResultDesc;

    public static ResultInfo newInstance(){
        return new ResultInfo();
    }

}