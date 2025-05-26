package com.chennian.storytelling.common.bean;

import lombok.Data;

/**
 * @author by chennian
 * @date 2023/4/12 16:21
 */
@Data
public class ResponseMessage {
    /**
     * 消息体
     */
    private String msg;
    /**
     * 是否成功
     */
    private String success;
}
