package com.chennian.storytelling.common.utils;

import com.chennian.storytelling.common.utils.MessageDigestUtil;
import java.util.*;

/**
 * @Author chennian
 * @Date 2023/9/5 9:15
 */
public class AuthUtils {
    /**
     * 参数字典排序
     */
    private static String sortParam(Map<String, String> params) {
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(params.entrySet());
        infoIds.sort(Map.Entry.comparingByKey());
        String ret = "";

        for (Map.Entry<String, String> entry : infoIds) {
            ret += entry.getKey();
            ret += "=";
            ret += entry.getValue();
            ret += "&";
        }
        ret = ret.substring(0, ret.length() - 1);
        return ret;
    }

    /**
     * 接口鉴权方法
     *
     * @param params
     * @return
     */
    public static String getSign(Map<String, String> params, String appSecurity) {
        //对参数进行字典排序
        String signature = sortParam(params) + "&key=" + appSecurity;
        String sign = MessageDigestUtil.md5(signature).toUpperCase();
        return sign;
    }

    /**
     * 接口鉴权方法
     *
     * @param params
     * @return
     */
    public static String getSignObject(Map<String, Object> params, String appSecurity) {
        //对参数进行字典排序
        String signature = sortParamObject(params) + "&key=" + appSecurity;
        String sign = MessageDigestUtil.md5(signature).toUpperCase();
        return sign;
    }

    /**
     * 参数字典排序
     */
    private static String sortParamObject(Map<String, Object> params) {
        List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(params.entrySet());
        infoIds.sort(Map.Entry.comparingByKey());
        String ret = "";

        for (Map.Entry<String, Object> entry : infoIds) {
            ret += entry.getKey();
            ret += "=";
            ret += entry.getValue();
            ret += "&";
        }
        ret = ret.substring(0, ret.length() - 1);
        return ret;
    }

//    /**
//     * 无body参数的签名
//     */
//    public HttpHeaders noBodyContentHeader() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("channelType", "MICANG");
//        String md5Value = "MICANG" + "123456";
//        String md5Sign = MessageDigestUtil.md5(md5Value).toLowerCase();
//        return headers;
//    }
}
