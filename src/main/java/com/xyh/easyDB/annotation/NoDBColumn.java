package com.xyh.easyDB.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xiayuhui on 2016/8/24.
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoDBColumn {

    /**
     * 数据库id字段名
     * @return
     */
    String value() default "";
}
