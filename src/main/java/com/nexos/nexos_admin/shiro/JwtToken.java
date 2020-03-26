package com.nexos.nexos_admin.shiro;
/**
 * Created by 孙爱飞 on 2020/3/25.
 */

import org.apache.shiro.authc.AuthenticationToken;

/**
 *@description: //登录主体信息
 *@author: your name
 *@create: 2020-03-25 21:18
 *@version: 1.0
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
