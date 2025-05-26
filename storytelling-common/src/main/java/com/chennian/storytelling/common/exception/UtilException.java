package com.chennian.storytelling.common.exception;

/**
 * 工具类异常
 *
 * @author by chennian
 * @date 2023/3/22 13:35
 */
public class UtilException extends RuntimeException {
    private static final long serialVersionUID = 8247610319171014183L;

    public UtilException(Throwable e) {
        super(e.getMessage(), e);
    }

    public UtilException(String message) {
        super(message);
    }

    public UtilException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
