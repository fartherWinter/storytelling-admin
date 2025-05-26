package com.chennian.storytelling.common.utils;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Arrays;

/**
 * @Author chennian
 * @Date 2023/9/28 9:15
 */
public class SM3Utils {
    private static final String ENCODING = "UTF-8";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * sm3 算法加密
     *
     * @param paramStr * 待加密的参数字符串
     * @param appKey   * 加密的密钥 appKey(接口访问秘钥 Key）
     * @return 返回 SM3 加密后固定长度为 32 的 16 进制密内容文字符串
     * @explain
     */
    public static String encrypt(String paramStr, String appKey) {
        // 将返回的 hash 值转换成 16 进制字符串
        String resultHexStr = "";
        try {
            // 将字符串转换成 byte 数组
            byte[] srcData = paramStr.getBytes(ENCODING);
            // 调用 hash()
            byte[] resultHash = hmac(srcData, appKey.getBytes(ENCODING));

            // 将返回的 hash 值转换成 16 进制字符串
            resultHexStr = ByteUtils.toHexString(resultHash);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return resultHexStr;
    }

    /**
     * 通过密钥进行哈希
     *
     * @param appKey  * 加密的密钥 appKey 的 byte 数组
     * @param srcData 被加密参数字符串的 byte 数组
     * @return
     */
    public static byte[] hmac(byte[] appKey, byte[] srcData) {
        KeyParameter keyParameter = new KeyParameter(appKey);
        SM3Digest digest = new SM3Digest();
        HMac mac = new HMac(digest);
        mac.init(keyParameter);
        mac.update(srcData, 0, srcData.length);
        byte[] result = new byte[mac.getMacSize()];
        mac.doFinal(result, 0);
        return result;
    }

    /**
     * 判断待加密的原始数据与加密后的数据是否一致
     *
     * @param paramStr     * 待加密的参数字符串
     * @param resultHexStr * SM3 加密后固定长度为 32 的 16 进制密内容文字符串
     *                     第 10 页 共 121 页
     * @param appKey       * 加密的密钥 appKey 的 byte 数组
     * @return 校验结果
     */
    public static boolean verify(String paramStr, String resultHexStr, String appKey) {
        boolean flag = false;
        try {
            byte[] srcData = paramStr.getBytes(ENCODING);
            byte[] sm3Hash = ByteUtils.fromHexString(resultHexStr);
            byte[] newHash = hmac(srcData, appKey.getBytes(ENCODING));
            if (Arrays.equals(newHash, sm3Hash)) {
                flag = true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return flag;
    }

//    public static void main(String[] args) {
//// 待加密的待加密的参数字符串
//        String paramStr = "6bba9d97c252661067758b2b6f80e7c558e562b8";
//// 加密的密钥 appKey
//        String appKey = "RaOF35Kw";
//// sm3 算法加密
//        String resultHexStr = SM3Utils.encrypt(paramStr, appKey);
//        System.out.println("待加密的待加密的参数字符串:" + paramStr);
//        System.out.println("SM3 加密后的密文字符串内容:" + resultHexStr);
//        System.out.println("校验结果:" + SM3Utils.verify(paramStr, resultHexStr, appKey));
//    }

}
