package com.chennian.storytelling.common.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author chennian
 * @Date 2023/11/15 11:05
 */
@Slf4j
public class TimestampCheckUtils {
    public static Boolean timestampCheck(Long timestamp) {
        Long newTimestamp = System.currentTimeMillis();
        return newTimestamp - timestamp > 60 * 1000 || newTimestamp < timestamp;
    }
}
