package com.nexos.nexos_admin.vo;

import com.nexos.nexos_admin.constant.Constant;
import com.nexos.nexos_admin.util.NetWorkUtils;
import io.swagger.annotations.Api;
import lombok.Data;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
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
    private boolean success = true;

    // 错误码
    private String code;

    // 错误描述
    private String ResultDesc;

    // 唯一表示
    private long uuid;

    public static ResultInfo newInstance(){
        ResultInfo resultInfo = new ResultInfo();
        HttpServletRequest httpServletRequest = NetWorkUtils.getHttpServletRequest();
        if (httpServletRequest != null){
            Object attribute = httpServletRequest.getAttribute(Constant.SERIAL_NUMBER);
            if (attribute!=null){
                resultInfo.setUuid((Long) attribute);
            }
        }
        return resultInfo;
    }

}