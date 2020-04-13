package com.nexos.nexos_admin.controller.webTools;
/**
 * Created by 孙爱飞 on 2020/3/28.
 *
 */

/**
 *@description: //TODO
 *@author: your name
 *@create: 2020-03-28 20:04
 *@version: 1.0
 */

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.nexos.nexos_admin.constant.Constant;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//@Order(1)
@WebFilter(filterName="httpRequestFilter",urlPatterns = "/*")
public class RequestContextFilter implements Filter {

    private static final String REQUESTID = "REQUEST_ID";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (StringUtils.isEmpty(MDC.get(REQUESTID))) {
            // 产生uuid
            DefaultIdentifierGenerator generator = new  DefaultIdentifierGenerator();
            Long uuid = generator.nextId(null);
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            request.setAttribute(Constant.SERIAL_NUMBER,uuid);
            MDC.put(REQUESTID, String.valueOf(uuid));
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
