package com.nexos.nexos_admin.util;

import java.util.UUID;

/**
 * @program: nexos_admin
 * @description:
 * @author: afsun
 * @create: 2020-03-26 15:42
 */
public class SaltGenerator {

    public static String generator(){
        String uuid = UUID.randomUUID().toString();
        return  uuid.replaceAll("-", "");
    }

}