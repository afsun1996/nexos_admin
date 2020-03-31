package com.nexos.nexos_admin.encryption;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.netty.handler.codec.base64.Base64Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

/**
 * @program: nexos_admin
 * @description:
 * @author: afsun
 * @create: 2020-03-31 16:48
 */
public class AESUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(AESUtils.class);

    /**
     * @param data
     * @param key
     * @return
     */
    public static String encrypt(String data, String key) {
        if (StringUtils.isBlank(data) || StringUtils.isBlank(key)) {
            LOGGER.info("参数和密钥不允许为空");
            return null;
        }
        String strs = null;
        byte[] bytes = doAES(Cipher.ENCRYPT_MODE, data.getBytes(), key.getBytes());
        // base64编码字节
        strs = BASE64.encode(bytes);
        return strs;
    }

    /**
     * @param data
     * @param key
     * @return
     */
    public static String decrypt(String data, String key) {
        if (StringUtils.isBlank(data) || StringUtils.isBlank(key)) {
            LOGGER.info("参数和密钥不允许为空");
            return null;
        }
        String strs = null;
        try {
            byte[] src = BASE64.decode(data);
            byte[] bytes = doAES(Cipher.DECRYPT_MODE, src, key.getBytes());
            strs = new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("解密失败，errormsg={}", e.getMessage());
        }
        return strs;

    }

    public static byte[] doAES(int mode, byte[] data, byte[] key) {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            kgen.init(128, new SecureRandom(key));
            //3.产生原始对称密钥
            SecretKey secretKey = kgen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] enCodeFormat = secretKey.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(mode, keySpec);// 初始化
            return cipher.doFinal(data);
        } catch (Exception e) {
            LOGGER.error("加解密失败，errormsg={}", e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        JSONObject json = new JSONObject();
        json.put("首联", "一叶扁舟伴水流");
        String data = encrypt(json.toJSONString(), "123456789");
        System.out.println("明文是:" + json);
        System.out.println("加密后:" + data);
        System.out.println("解密后：" + decrypt(data, "123456789"));
    }
}
