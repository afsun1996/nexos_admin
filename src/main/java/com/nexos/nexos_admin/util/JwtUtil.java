package com.nexos.nexos_admin.util;

import com.nexos.nexos_admin.exception.BusinessResponseCode;
import com.nexos.nexos_admin.exception.BussinessException;
import com.nexos.nexos_admin.shiro.ShiroProperties;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: NexosBlog
 * @description:
 * @author: afsun
 * @create: 2020-03-25 14:07
 */
@Component
public class JwtUtil {

    @Autowired
    ShiroProperties shiroProperties;

    @Autowired
    private static JwtUtil jwtUtil;

    @PostConstruct
    public void init() {
        jwtUtil = this;
        jwtUtil.shiroProperties = this.shiroProperties;
    }


    /**
     * 获取签发token
     * @param issuer 签发人
     * @param subject 所有人
     * @param claims 储存非敏感信息
     * @param secret 秘钥
     * @param duration 过期时间
     * @return
     */
    public static String  generateToken(String issuer, String subject, Map<String,Object> claims,String secret,long duration){
        if (duration < 0){
            throw new RuntimeException("");
        }
        // 获取加密算法
        SignatureAlgorithm hs256 = SignatureAlgorithm.HS256;
        // 获取工厂
        JwtBuilder builder = Jwts.builder();

        builder.addClaims(claims);
        builder.setIssuer(issuer);
        builder.setSubject(subject);
        builder.setExpiration(new Date(System.currentTimeMillis()+duration));
        JwtBuilder jwtBuilder = builder.signWith(hs256,secret);
        return jwtBuilder.compact();
    }

    /**
     * 获取验签的信息
     * @param token
     * @return
     */
    public static Claims getClaimsFromToken(String token){
        Claims body = null;
        try {
            body = Jwts.parser().setSigningKey(new BASE64Decoder().decodeBuffer(jwtUtil.shiroProperties.getSecret()))
                    .parseClaimsJws(token).getBody();
        }
        catch (ExpiredJwtException e){
            throw new BussinessException(BusinessResponseCode.TOKEN_EXPRIED);
        }
        catch (Exception e) {
            throw new BussinessException(BusinessResponseCode.SYSTEM_BUSY);
        }
        return body;

    }

    public static Object getClaim(String token,String name){
        Claims claimsFromToken = JwtUtil.getClaimsFromToken(token);
        return claimsFromToken.get(name);
    }


    /**
     *  判断是否过期
     * @param token
     * @return
     */
    public static boolean isExpired(String token){
        Claims claimsFromToken = JwtUtil.getClaimsFromToken(token);
        Date expiration = claimsFromToken.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 验签
     * @param token
     * @return
     */
    public static boolean validateToken(String token){
        Claims claimsFromToken = JwtUtil.getClaimsFromToken(token);
        return claimsFromToken!=null;
    }

    public static void main(String[] args) throws InterruptedException {
//        String issuer = "afsun";
//        String subject = "admin";
//        Map claim = new HashMap();
//        claim.put("role","administration");
//        claim.put("permission","sys");
//        String token = JwtUtil.generateToken(issuer, subject, claim, ShiroProperties.secret, 2400000);
//        System.out.println(token);
////        Thread.sleep(2400);
//        System.out.println(JwtUtil.getClaimsFromToken(token));

    }


}