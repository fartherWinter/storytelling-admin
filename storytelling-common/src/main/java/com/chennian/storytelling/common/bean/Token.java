package com.chennian.storytelling.common.bean;

import lombok.Data;

/**
 * token信息
 *
 * @author by chennian
 * @date 2023/4/7 16:29
 */
@Data
public class Token {
    /**
     * key值
     */
    private String key;
    /**
     * 时间戳
     */
    private Long timeStamp;
}
