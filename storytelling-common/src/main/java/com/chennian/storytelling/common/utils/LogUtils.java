package com.chennian.storytelling.common.utils;

/**
 * @author by chennian
 * @date 2023/3/21 14:13
 */
public class LogUtils {
    public static String getBlock(Object msg) {
        if (msg == null) {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}
