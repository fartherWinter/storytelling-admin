package com.chennian.storytelling.common.utils.message;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.chennian.storytelling.common.constant.CUCCApiConstant;
import com.chennian.storytelling.common.exception.StorytellingBindException;
import com.chennian.storytelling.common.utils.Json;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.text.StringSubstitutor;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 短信发送工具类
 *
 * @author by chennian
 * @date 2023/5/31 15:30
 */
public class MessageUtils {


    public static void Message(Map valuesMap, String template, String mobile) {
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        OkHttpClient client = new OkHttpClient();
        String content = sub.replace(template);
        Long timestamp = System.currentTimeMillis() / 1000;
        String contentUtf = "";
        try {
            byte[] bs = content.getBytes("UTF-8");
            contentUtf = new String(bs, "UTF-8");
            System.out.println(contentUtf);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String sign = SaSecureUtil.md5(CUCCApiConstant.ACCOUNT + CUCCApiConstant.PASSWORD + timestamp);
        String params = "userId=" + CUCCApiConstant.ACCOUNT + "&sign=" + sign +
                "&mobile=" + mobile + "&content=" + contentUtf + "&timestamp=" + timestamp;
        Request request = new Request.Builder().url(CUCCApiConstant.MSG_URL + params).addHeader("Content-type", "application/json;charset=UTF-8").build();
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            JsonNode jsonNode = Json.parseJson(result);
            if (Integer.valueOf(jsonNode.path("result").asText()) != 0) {
                throw new StorytellingBindException(jsonNode.path("result_msg").asText());
            }
        } catch (Exception e) {
            throw new StorytellingBindException("短信发送失败:" + e.getMessage());
        }
    }

    public static void messageReceive() {
        OkHttpClient client = new OkHttpClient();
        Long timestamp = System.currentTimeMillis() / 1000;
        String sign = SaSecureUtil.md5(CUCCApiConstant.ACCOUNT + CUCCApiConstant.PASSWORD + timestamp);
        String params = "userId=" + CUCCApiConstant.ACCOUNT + "&sign=" + sign + "&timestamp=" + timestamp;
        Request request = new Request.Builder().url(CUCCApiConstant.MO_SM_URL + params).addHeader("Content-type", "application/json;charset=UTF-8").build();
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            System.out.println(result);
            JsonNode jsonNode = Json.parseJson(result);
            System.out.println(jsonNode);
//            if (Integer.valueOf(jsonNode.path("result").asText()) != 0) {
//                throw new StorytellingBindException(jsonNode.path("result_msg").asText());
//            }
        } catch (Exception e) {
            throw new StorytellingBindException("短信发送失败:" + e.getMessage());
        }
    }
}
