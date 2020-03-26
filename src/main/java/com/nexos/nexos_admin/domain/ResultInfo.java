package com.nexos.nexos_admin.domain;

import lombok.Data;

/**
 * @program: NexosBlog
 * @description: 返回类型
 * @author: afsun
 * @create: 2019-10-30 13:37
 */
@Data
public class ResultInfo<T> {

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