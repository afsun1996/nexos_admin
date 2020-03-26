package com.nexos.nexos_admin.shiro;    /**
 * Created by 孙爱飞 on 2020/3/26.
 */

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *@description: //TODO
 *@author: your name
 *@create: 2020-03-26 21:54
 *@version: 1.0
 */
@ConfigurationProperties("shiro")
@Component
public class ShiroProperties {

    public static String secret;

    public static String expireTime;

    public static String refreshTime;

}
