package com.nexos.nexos_admin.exception;

import com.nexos.nexos_admin.util.ExceptionUtils;
import com.nexos.nexos_admin.vo.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @program: nexos_admin
 * @description:
 * @author: afsun
 * @create: 2020-03-27 13:28
 */
@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {


    /**
     * 处理业务错误
     * @param exception
     * @return
     */
    @ExceptionHandler(BussinessException.class)
    public ResultInfo handleBusinessException(BussinessException exception){
        ResultInfo resultInfo = ResultInfo.newInstance();
        resultInfo.setSuccess(false);
        resultInfo.setCode(String.valueOf(exception.getCode()));
        resultInfo.setResultDesc(exception.getDescMsg());
        return resultInfo;
    }

    /**
     * 处理业务错误
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResultInfo handleException(Exception exception){
        log.error(ExceptionUtils.getMessage(exception));
        ResultInfo resultInfo = ResultInfo.newInstance();
        resultInfo.setSuccess(false);
        resultInfo.setCode(String.valueOf(BusinessResponseCode.SYSTEM_BUSY.getCode()));
        resultInfo.setResultDesc(BusinessResponseCode.SYSTEM_BUSY.getMsg());
        return resultInfo;
    }





}