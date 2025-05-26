package com.chennian.storytelling.common.utils.pay;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.chennian.storytelling.common.exception.ServiceException;
import com.chennian.storytelling.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

/**
 * @Author chennian
 * @Date 2023/8/9 11:07
 */
@Slf4j
public class WxH5Utils {
    private final static String APP_ID = "";
    private final static String APP_SECURITY = "";
    public final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
    public final static String ACCESS_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    public final static String ERRCODE = "errcode";
    public final static String ACCESS_TOKEN = "access_token";
    public final static String TICKET = "ticket";


    /**
     * 获取accessToken
     *
     * @return
     */
    public String getAccessTokenH5() {
        String requestUrl = ACCESS_TOKEN_URL.replace("APPID", APP_ID)
                .replace("SECRET", APP_SECURITY);
        String response = HttpUtil.get(requestUrl);
        JSONObject jsonObject = JSONUtil.parseObj(response);
//         判断微信返回结果是否异常
        if (StringUtils.isEmpty((Collection<?>) jsonObject.get(ERRCODE)) || Integer.parseInt(String.valueOf(jsonObject.get(ERRCODE))) == 0) {
            return jsonObject.getStr(ACCESS_TOKEN);
        } else {
            log.error(jsonObject.toStringPretty());
            throw new ServiceException("获取公众号token信息异常");
        }
    }

    /**
     * 获取H5的ticket
     *
     * @return
     */
    public String getTicketH5() {
        String accessToken = getAccessTokenH5();
        String requestUrl = ACCESS_TICKET_URL.replace("ACCESS_TOKEN", accessToken);
        String response = HttpUtil.get(requestUrl);
        JSONObject jsonObject = JSONUtil.parseObj(response);
        // 判断微信返回结果是否异常
        if (StringUtils.isEmpty(String.valueOf(jsonObject.get(ERRCODE))) || Integer.parseInt(String.valueOf(jsonObject.get(ERRCODE))) == 0) {
            return jsonObject.getStr(TICKET);
        } else {
            log.error(jsonObject.toStringPretty());
            throw new ServiceException("获取公众号ticket信息异常");
        }
    }

    public static String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
