package com.chennian.security.admin.util;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.chennian.storytelling.common.constant.TokenConstants;
import com.chennian.storytelling.common.enums.ResultCode;
import com.chennian.storytelling.common.exception.StorytellingBindException;
import com.chennian.storytelling.common.redis.RedisCache;
import com.chennian.storytelling.common.utils.HttpContextUtils;
import com.chennian.storytelling.common.utils.SpringContextUtils;
import com.chennian.storytelling.common.utils.StringUtils;

import java.security.MessageDigest;


/**
 * 安全服务工具类
 *
 * @author by chennian
 * @date 2023/3/21 17:13
 */
public class SecurityUtils {
    private final static String AES_KEY = "storytelling";
    /**
     * 后台管理请求地址
     */
    private final static String SYS_REQUEST = "/sys/";
    /**
     * 小程序后台请求地址
     */
    private final static String APP_REQUEST = "/app/";

    /**
     * md5加密
     */
    public static String secureMd5(String text) {
        return SaSecureUtil.md5(text);
    }

    /**
     * sha1加密
     */
    public static String secureSha1(String text) {
        return SaSecureUtil.sha1(text);
    }

    /**
     * sha256加密
     */
    public static String secureSha256(String text) {
        return SaSecureUtil.sha256(text);
    }

    /**
     * 对称加密
     */
    public static String encryptAes(String text) {
        return SaSecureUtil.aesEncrypt(AES_KEY, text);
    }

    /**
     * 对称解密
     */
    public static String decryptAes(String text) {
        return SaSecureUtil.aesDecrypt(AES_KEY, text);
    }

    /**
     * 非对称加密:私钥
     */
    public static String encryptResByPrivate(String text) {
        return SaSecureUtil.rsaEncryptByPrivate("key", text);
    }

    /**
     * 非对称加密:公钥
     */
    public static String encryptRsaByPublic(String text) {
        return SaSecureUtil.rsaEncryptByPublic("key", text);
    }

    /**
     * 非对称解密：私钥
     */
    public static String decryptRsaByPrivate(String text) {
        return SaSecureUtil.rsaDecryptByPrivate("key", text);
    }

    /**
     * 非对称解密：公钥
     */
    public static String decryptResByPublic(String text) {
        return SaSecureUtil.rsaDecryptByPublic("key", text);
    }

    /**
     * @return
     * @Comment SHA1实现
     * @Author Ron
     * @Date 2017年9月13日 下午3:30:36
     */
    public static String shaEncode(String signStr) throws Exception {
        StringBuffer hexValue = new StringBuffer();
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
            byte[] byteArray = signStr.getBytes("UTF-8");
            byte[] md5Bytes = sha.digest(byteArray);
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return hexValue.toString();
    }

    /**
     * 获取token中的用户信息
     */
    public static Integer getUser() {
        RedisCache redisCache = SpringContextUtils.getBean("redisCache", RedisCache.class);
        String token = HttpContextUtils.getHttpServletRequest().getHeader("token");
        Object userId = redisCache.getCacheObject(TokenConstants.LOGIN_PREFIX + token);
        if (StringUtils.isNull(userId)) {
            throw new StorytellingBindException(ResultCode.USER_OVERDUE_CONNECT_ERROR.message(), ResultCode.USER_OVERDUE_CONNECT_ERROR.code());
        }
        return (Integer) userId;
    }

    /**
     * 获取微信openId
     */
    public static String getOpenId() {
        String openId = HttpContextUtils.getHttpServletRequest().getHeader("openid");
        if (StringUtils.isNull(openId)) {
            throw new StorytellingBindException(ResultCode.USER_OVERDUE_CONNECT_ERROR.message(), ResultCode.USER_OVERDUE_CONNECT_ERROR.code());
        }
        return openId;
    }

    /**
     * 获取userId
     */
    public static Long getUserId() {
        String userId = HttpContextUtils.getHttpServletRequest().getHeader("userId");
        if (StringUtils.isNull(userId)) {
            throw new StorytellingBindException(ResultCode.USER_OVERDUE_CONNECT_ERROR.message(), ResultCode.USER_OVERDUE_CONNECT_ERROR.code());
        }
        return Long.valueOf(userId);
    }

    /**
     * 获取userId
     */
    public static String getUserName() {
        String userName = HttpContextUtils.getHttpServletRequest().getHeader("userName");
        if (StringUtils.isNull(userName)) {
            throw new StorytellingBindException(ResultCode.USER_OVERDUE_CONNECT_ERROR.message(), ResultCode.USER_OVERDUE_CONNECT_ERROR.code());
        }
        return userName;
    }

    /**
     * 获取accessid
     */
    public static Integer getAccessId() {
        RedisCache redisCache = SpringContextUtils.getBean("redisCache", RedisCache.class);
        String token = HttpContextUtils.getHttpServletRequest().getHeader("accessid");
        Object accessid = redisCache.getCacheObject(TokenConstants.LOGIN_PREFIX + token);
        if (StringUtils.isNull(accessid)) {
            throw new StorytellingBindException(ResultCode.USER_OVERDUE_CONNECT_ERROR.message(), ResultCode.USER_OVERDUE_CONNECT_ERROR.code());
        }
        return (Integer) accessid;
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }
}
