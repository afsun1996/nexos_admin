package com.nexos.nexos_admin.aop;


import lombok.Data;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogMethod {

    String modualName() default "";

    String fuctionName() default "";
}
