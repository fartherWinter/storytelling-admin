package com.chennian.storytelling.common.annotation;

import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.chennian.storytelling.common.enums.SystemType;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录
 *
 * @author by chennian
 * @date 2023/3/14 13:28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventTrack {
    /**
     * 模块类别
     */
    public SystemType type() default SystemType.SYSTEM_ADMIN;

    /**
     * 模块名称
     */
    public ModelType title() default ModelType.SYSTEM;

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
