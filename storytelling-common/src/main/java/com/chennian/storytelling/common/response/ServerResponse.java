package com.chennian.storytelling.common.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 统一数据响应体
 *
 * @author by chennian
 * @date 2023/3/14 14:29
 */
@Data
public class ServerResponse<T> implements Serializable {
    private int code;

    private String msg;

    private T obj;

    public boolean isSuccess() {
        return Objects.equals(ResponseCode.SUCCESS, this.code);
    }
}
