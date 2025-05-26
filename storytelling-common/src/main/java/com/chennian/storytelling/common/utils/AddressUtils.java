package com.chennian.storytelling.common.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author by chennian
 * @date 2023/3/21 14:11
 */
@Slf4j
public class AddressUtils {
    // IP地址查询
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    // 未知地址
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip) {
        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }
//        if (RuoYiConfig.isAddressEnabled()) {
//            try {
//                String rspStr = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", Constants.GBK);
//                if (StringUtils.isEmpty(rspStr)) {
//                    log.error("获取地理位置异常 {}", ip);
//                    return UNKNOWN;
//                }
//                JSONObject obj = JSON.parseObject(rspStr);
//                String region = obj.getString("pro");
//                String city = obj.getString("city");
//                return String.format("%s %s", region, city);
//            } catch (Exception e) {
//                log.error("获取地理位置异常 {}", ip);
//            }
//        }
        return UNKNOWN;
    }
}
