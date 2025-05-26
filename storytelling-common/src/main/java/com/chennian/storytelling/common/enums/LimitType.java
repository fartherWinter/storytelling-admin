package com.chennian.storytelling.common.enums;

/**
 * @author by chennian
 * @date 2023/3/23 16:15
 */
public enum LimitType {
    /**
     * 默认策略全局限流
     */
    DEFAULT,

    /**
     * 根据请求者IP进行限流
     */
    IP
}
