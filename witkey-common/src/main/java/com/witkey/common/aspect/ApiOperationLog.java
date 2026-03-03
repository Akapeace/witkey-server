package com.witkey.common.aspect;

import java.lang.annotation.*;

/**
 * @author peace
 * @date 2026/3/3 16:04
 * @description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.METHOD})
@Documented
public @interface ApiOperationLog {

    /**
     * api 功能描述
     * @return
     */
    String description() default "";
}
