package com.chennian.storytelling.common.utils;


import com.chennian.storytelling.common.bean.WeChatAccessOpenId;
import com.chennian.storytelling.common.bean.WeChatAccessToken;
import com.chennian.storytelling.common.bean.WeChatPayComment;
import com.chennian.storytelling.common.constant.WeChatConstants;
import com.chennian.storytelling.common.constant.WeChatTicketConstants;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 微信开发工具类
 *
 * @author by chennian
 * @date 2023/3/30 14:48
 */
public class WeChatUtils {

    /**
     * 获取openid
     */
    public static WeChatAccessOpenId getOpenId(String code) {
        OkHttpClient client = new OkHttpClient();
        Request openIdRequest = new Request.Builder()
                .url(WeChatConstants.WE_LOGIN_URL + "?appid=" + WeChatConstants.APP_ID
                        + "&secret=" + WeChatConstants.APP_SECRET
                        + "&js_code=" + code
                        + "&grant_type=authorization_code")
                .build();
        try {
            Response response = client.newCall(openIdRequest).execute();
            WeChatAccessOpenId weChatAccessOpenId = Json.parseObject(response.body().string(), WeChatAccessOpenId.class);
            return weChatAccessOpenId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取access_token
     */
    public static WeChatAccessToken getAccessToken() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(WeChatTicketConstants.WE_ACCESS_TOKEN + "appid=" + WeChatTicketConstants.APP_ID
                        + "&secret=" + WeChatTicketConstants.APP_SECRET + "&grant_type=client_credential")
                .addHeader("Content-type", "application/json;charset=UTF-8")
                .build();
        try {
            Response response = client.newCall(request).execute();
            WeChatAccessToken weChatAccessToken = Json.parseObject(response.body().string(), WeChatAccessToken.class);
            return weChatAccessToken;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptNew(String encryptedData, String sessionKey, String iv) throws Exception {
        String result = "";
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足. 这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                result = new String(resultByte, "UTF-8");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取小程序微信支付评价
     */
    public static WeChatPayComment getMiniProgramPayScore(Integer offset, String accessToken) {
        OkHttpClient client = new OkHttpClient();
        // 获取当前时间
        LocalDateTime currentTime = LocalDateTime.now();
        // 计算一小时前的时间
        LocalDateTime oneHourAgo = currentTime.minusMonths(9);
        // 转换为时间戳
        Instant instant = oneHourAgo.toInstant(ZoneOffset.UTC);
        long startTime = instant.getEpochSecond();
        Instant nowInstant = currentTime.toInstant(ZoneOffset.UTC);
        long endTime = nowInstant.getEpochSecond();
        Request request = new Request.Builder()
                .url(WeChatConstants.WE_PAY_COMMENT_URL + "?startTime=" + startTime + "&endTime=" + endTime + "&filterType=6" + "&offset=" + offset + "&access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            WeChatPayComment weChatPayComment = Json.parseObject(result, WeChatPayComment.class);
            return weChatPayComment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


