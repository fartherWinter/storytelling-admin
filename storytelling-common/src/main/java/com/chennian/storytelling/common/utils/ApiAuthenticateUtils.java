package com.chennian.storytelling.common.utils;


import com.chennian.storytelling.common.response.ServerResponseEntity;

/**
 * api接口鉴权方法
 *
 * @author by chennian
 * @date 2023/6/15 16:27
 */
public class ApiAuthenticateUtils {
    public static ServerResponseEntity<?> signature() {
        return ServerResponseEntity.success();
    }
}
