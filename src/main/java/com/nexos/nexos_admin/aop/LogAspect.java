package com.nexos.nexos_admin.aop;

import com.alibaba.fastjson.JSON;
import com.nexos.nexos_admin.constant.Constant;
import com.nexos.nexos_admin.po.SysLog;
import com.nexos.nexos_admin.util.JwtUtil;
import com.nexos.nexos_admin.util.NetWorkUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: nexos_admin
 * @description:
 * @author: afsun
 * @create: 2020-03-27 17:16
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.nexos.nexos_admin.aop.LogMethod)")
    public void logPointCut(){}



    @Around("logPointCut()")
    public void saveLog(ProceedingJoinPoint joinPoint) throws Throwable {
        SysLog sysLog = new SysLog();
        long currentTimeMillis = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        HttpServletRequest request = NetWorkUtils.getHttpServletRequest();
        sysLog.setConsumeTime(System.currentTimeMillis()-currentTimeMillis);// 消耗时间
        sysLog.setResponse(JSON.toJSONString(proceed));// 响应
        sysLog.setRequestIp(NetWorkUtils.getIpAddr(request));// 请求ip
        String token = request.getHeader(Constant.AUTH_HEAD);
        Claims claims = JwtUtil.getClaimsFromToken(token);
        sysLog.setUserId(claims.getSubject());//用户id
    }



}