package com.chennian.storytelling.common.annotation;

import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.chennian.storytelling.common.enums.SystemType;

import java.lang.annotation.*;

/**
 * @author by chennian
 * @date 2023/5/15 18:44
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MovieEventTrackLogPoint {
    /**
     * 模块类别
     */
    public SystemType type() default SystemType.SYSTEM_ADMIN;

    /**
     * 模块名称
     */
    public String title() default "";

    /**
     * 功能:操作
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作用户类型
     */
    public OperatorType operatorType() default OperatorType.OTHER;

    /**
     * 业务描述
     */
    String description() default "";
}
