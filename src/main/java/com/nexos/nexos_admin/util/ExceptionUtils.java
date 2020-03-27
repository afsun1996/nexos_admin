package com.nexos.nexos_admin.util;

import org.slf4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @program: nexos_admin
 * @description:
 * @author: afsun
 * @create: 2020-03-27 15:35
 */
public class ExceptionUtils {

    public static String getMessage(Exception e) {
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        return out.toString();
    }

    public static void loggerMessage(Exception e, Logger logger) {
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        logger.error(out.toString());
    }
}