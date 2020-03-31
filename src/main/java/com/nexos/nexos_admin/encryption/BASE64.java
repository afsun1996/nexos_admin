package com.nexos.nexos_admin.encryption;

import java.util.Base64;

/**
 * @program: nexos_admin
 * @description:
 * @author: afsun
 * @create: 2020-03-31 16:52
 */
public class BASE64 {

    /**
     * 加密
     * @param bytes
     * @return
     */
    public static String encode(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }


    /**
     * 解密
     * @param message
     * @return
     */
    public static byte[] decode(String message){
        return Base64.getDecoder().decode(message);
    }


}