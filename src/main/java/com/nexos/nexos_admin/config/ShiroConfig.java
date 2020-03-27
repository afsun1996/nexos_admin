package com.nexos.nexos_admin.config;    /**
 * Created by 孙爱飞 on 2020/3/25.
 */
import com.nexos.nexos_admin.service.facade.RedisService;
import com.nexos.nexos_admin.shiro.LoginFilter;
import com.nexos.nexos_admin.shiro.ShiroProperties;
import com.nexos.nexos_admin.shiro.ShiroRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.HashMap;
import java.util.Map;

/**
 *@description: //Shiro 配置
 *@author: your name
 *@create: 2020-03-25 21:29
 *@version: 1.0
 */
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroRealm getShiroRealm(){
        ShiroRealm shiroRealm = new ShiroRealm();
        return shiroRealm;
    }

    @Bean
    public DefaultWebSecurityManager getSecurityManger(ShiroRealm shiroRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactory(RedisService redisService, DefaultWebSecurityManager securityManager,
                                                        ShiroProperties shiroProperties){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map filters = new HashMap();
        filters.put("login",new LoginFilter(shiroProperties,redisService));
        shiroFilterFactoryBean.setFilters(filters);
        // 设置过滤参数
        Map filterChainDefinitionMap = new HashMap();

        // swagger 过滤
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/configuration/**", "anon");

        filterChainDefinitionMap.put("/sys/login", "anon");
        filterChainDefinitionMap.put("/sys/registerUser", "anon");
        filterChainDefinitionMap.put("/sys/preRegisterCheck", "anon");
        filterChainDefinitionMap.put("/sys/loginout", "anon");
        filterChainDefinitionMap.put("/**","login");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        // 设置自定义过滤器
        return shiroFilterFactoryBean;
    }


    // 开启shiro注解支持
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public static DefaultAdvisorAutoProxyCreator getLifecycleBeanPostProcessor() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
