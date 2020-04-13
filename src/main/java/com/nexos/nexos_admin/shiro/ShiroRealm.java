package com.nexos.nexos_admin.shiro;    /**
 * Created by 孙爱飞 on 2020/3/25.
 */

import com.nexos.nexos_admin.constant.Constant;
import com.nexos.nexos_admin.util.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


/**
 *@description: //TODO
 *@author: your name
 *@create: 2020-03-25 21:21
 *@version: 1.0
 */
public class ShiroRealm extends AuthorizingRealm {


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String token = (String) principalCollection.getPrimaryPrincipal();
//        Object claim = JwtUtil.getClaim(token, Constant.TOKEN_PERMISSION);
        authorizationInfo.addStringPermissions((ArrayList) JwtUtil.getClaim(token, Constant.TOKEN_PERMISSION));
        return authorizationInfo;
    }


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 认证处理
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) authenticationToken;
        if (jwtToken == null ){
            throw new AuthenticationException("token is empty");
        }

        String token = (String) jwtToken.getCredentials();
        boolean validateToken = JwtUtil.validateToken(token);
        if (validateToken){
            // 认证成功
            return new SimpleAuthenticationInfo(token,token,"shiroRealm");
        }else {
            throw new AuthenticationException("token is expried or not incorrect");
        }
    }
}
