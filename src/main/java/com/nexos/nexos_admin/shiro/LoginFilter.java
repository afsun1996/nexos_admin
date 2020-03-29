package com.nexos.nexos_admin.shiro;
/**
 * Created by 孙爱飞 on 2020/3/25.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nexos.nexos_admin.constant.Constant;
import com.nexos.nexos_admin.exception.BusinessResponseCode;
import com.nexos.nexos_admin.exception.BussinessException;
import com.nexos.nexos_admin.exception.ResponseCode;
import com.nexos.nexos_admin.service.facade.RedisService;
import com.nexos.nexos_admin.util.ExceptionUtils;
import com.nexos.nexos_admin.util.JwtUtil;
import com.nexos.nexos_admin.vo.ResultInfo;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @description: // 登录过滤器
 * @author: your name
 * @create: 2020-03-25 21:25
 * @version: 1.0
 */
@Slf4j
public class LoginFilter extends BasicHttpAuthenticationFilter {

    RedisService redisService;

    ShiroProperties shiroProperties;

    public LoginFilter(ShiroProperties shiroProperties, RedisService redisService) {
        this.shiroProperties = shiroProperties;
        this.redisService = redisService;
    }

    /**
     * 登录
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        long currentTimeMillis = System.currentTimeMillis();
        String token = httpServletRequest.getHeader(Constant.AUTH_HEAD);
        JwtToken jwtToken = new JwtToken(token);
        try {
            getSubject(request, response).login(jwtToken);
            // 判断是否需要刷新
            Claims claimsFromToken = JwtUtil.getClaimsFromToken(token);
            Date expiration = claimsFromToken.getExpiration();
            // 打印日志
            long overTime = expiration.getTime() - currentTimeMillis;
            long tokenCreateTime = (long) claimsFromToken.get(Constant.TOKEN_CREATIE_TIME); // token中解析的创建时间
            log.info("token过期时间还剩{}min, token刷新时间还剩{}min", TimeUnit.MILLISECONDS.toMinutes(overTime),
                    TimeUnit.MILLISECONDS.toMinutes(shiroProperties.getRefreshTime() -(currentTimeMillis - tokenCreateTime)));
            if (currentTimeMillis - tokenCreateTime> shiroProperties.getRefreshTime()) { //
                try {
                    // 加上分布式锁
                    boolean lock = redisService.getLock(claimsFromToken.getSubject(), token, 60);// 上锁
                    if (lock) {
                        Object result = redisService.get(Constant.REFRESH_KEY + claimsFromToken.getSubject());
                        if (result != null) {
                            long lastCreateTime = (long) result; // redis存储的token创建时间
                            if (lastCreateTime > tokenCreateTime) { // 时间不一致,则说明是用旧的token登录
                                throw new BussinessException(BusinessResponseCode.TOKEN_OLD);
                            }
                            redisService.delete(Constant.REFRESH_KEY + claimsFromToken.getSubject());
                        }
                        Map userMap = new HashMap();
                        userMap.put(Constant.TOKEN_ROLE, claimsFromToken.get(Constant.TOKEN_ROLE));
                        userMap.put(Constant.TOKEN_PERMISSION, claimsFromToken.get(Constant.TOKEN_PERMISSION));
                        userMap.put(Constant.TOKEN_CREATIE_TIME, currentTimeMillis);
                        String newToken = JwtUtil.generateToken("nexos", claimsFromToken.getSubject(),
                                userMap, shiroProperties.getSecret(), shiroProperties.getExpireTime());
                        log.info("刷新token获得newToken = {}",newToken);
                        redisService.set(Constant.REFRESH_KEY + claimsFromToken.getSubject(), currentTimeMillis
                                , shiroProperties.getExpireTime()); // 存入redis缓存中
                        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                        httpServletResponse.setHeader(Constant.AUTH_HEAD, newToken);
                        httpServletResponse.setHeader("Access-Control-Expose-Headers", Constant.AUTH_HEAD);
                    }
                    else {
                        throw new BussinessException(BusinessResponseCode.OPERATE_FAST);
                    }
                } finally {
                    redisService.releaseLock(claimsFromToken.getSubject(), token);                // 释放锁
                }
            }
        }
        catch (Exception exception) {
            throw exception;
        }
        return true;
    }


    /**
     * 是否允许访问
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            this.executeLogin(request, response);
            return true;
        } catch (BussinessException exception) {
            this.errorMsgHandler(exception, request, response);
        }
        catch (AuthenticationException autenException){
            this.errorMsgHandler(new BussinessException(BusinessResponseCode.TOKEN_EXPRIED), request, response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 拒绝访问
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return false;
    }

    /**
     * 处理过滤器中的错误
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected boolean errorMsgHandler(BussinessException exception, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        ResultInfo resultInfo = ResultInfo.newInstance();
        resultInfo.setSuccess(false);
        resultInfo.setCode(String.valueOf(exception.getCode()));
        resultInfo.setResultDesc(exception.getDescMsg());
        Object uuid = request.getAttribute("UUID");
        if (uuid != null) {
            resultInfo.setUuid((Long) uuid);
        }
        OutputStream writer = null;
        try {
            writer = httpServletResponse.getOutputStream();
            writer.write(JSON.toJSONString(resultInfo, SerializerFeature.WriteMapNullValue).getBytes("UTF-8"));
            writer.flush();
        } catch (IOException e) {
            log.error(ExceptionUtils.getMessage(e));
        }finally {
          if (writer != null){
              try {
                  writer.close();
              } catch (IOException e) {
                  log.error(ExceptionUtils.getMessage(e));
              }
          }
        }
        return false;
    }
}
