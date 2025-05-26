package com.chennian.storytelling.common.bean;

import lombok.Data;

/**
 * api开放平台签名实体
 *
 * @author by chennian
 * @date 2023/4/3 14:10
 */
@Data
public class ApiSignature {
    /**
     * key值
     */
    private String appKey;
//    /**
//     * secret值:
//     */
//    private String appSecret;
    /**
     * 时间戳
     */
    private String timeStamp;
    /**
     * 签名:将appSecret进行数据加密之后返回
     */
    private String sign;
}
