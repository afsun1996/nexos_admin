package com.nexos.nexos_admin.shiro;    /**
 * Created by 孙爱飞 on 2020/3/26.
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 *@description: //TODO
 *@author: your name
 *@create: 2020-03-26 21:54
 *@version: 1.0
 */
@ConfigurationProperties(prefix = "shiro")
@Configuration
@Data
public class ShiroProperties {

      String secret;

      long expireTime;

      long refreshTime;

}
