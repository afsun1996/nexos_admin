package com.nexos.nexos_admin.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nexos.nexos_admin.constant.Constant;
import com.nexos.nexos_admin.po.SysLog;
import com.nexos.nexos_admin.service.facade.SysLogService;
import com.nexos.nexos_admin.util.ExceptionUtils;
import com.nexos.nexos_admin.util.JwtUtil;
import com.nexos.nexos_admin.util.NetWorkUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    @Autowired
    SysLogService sysLogService;

    @Pointcut("@annotation(com.nexos.nexos_admin.aop.LogMethod)")
    public void logPointCut(){}

    @Around("logPointCut()")
    public Object saveLog(ProceedingJoinPoint joinPoint) throws Throwable {
        SysLog sysLog = new SysLog();
        long currentTimeMillis = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LogMethod annotation = signature.getMethod().getAnnotation(LogMethod.class);
        sysLog.setModuleName(annotation.modualName()+"-"+annotation.modualName());
        HttpServletRequest request = NetWorkUtils.getHttpServletRequest();
        sysLog.setConsumeTime(System.currentTimeMillis()-currentTimeMillis);// 消耗时间
        sysLog.setResponse(JSON.toJSONString(proceed,SerializerFeature.WriteMapNullValue));// 响应
        sysLog.setRequestIp(NetWorkUtils.getIpAddr(request));// 请求ip
        String token = request.getHeader(Constant.AUTH_HEAD);
        if (!StringUtils.isEmpty(token)){
            Claims claims = null;
            try {
                claims = JwtUtil.getClaimsFromToken(token);
                sysLog.setUserId(claims.getSubject());//用户id
            } catch (Exception e) {
               log.error(ExceptionUtils.getMessage(e));
            }
        }
        sysLog.setRequestParam(JSON.toJSONString(joinPoint.getArgs(),SerializerFeature.WriteMapNullValue));// 请求参数
        sysLog.setId((Long)request.getAttribute(Constant.SERIAL_NUMBER));
        sysLog.setRequestPath(request.getServletPath());
        sysLogService.saveSyslog(sysLog);
        return proceed;
    }



}