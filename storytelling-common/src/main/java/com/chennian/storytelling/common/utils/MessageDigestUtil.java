package com.chennian.storytelling.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * @author by chennian
 * @date 2025/4/28.
 */
public class MessageDigestUtil {
    public static String md5(byte[] input) {
        return encryptPassword(input, "md5");
    }
    public static String md5(String text) {
        return md5(text, "utf-8");
    }

    public static String md5(String text, String encoding) {
        return encryptPassword(text, encoding, "md5");
    }

    public static String md5(String text, int length) {
        String result = md5(text);
        if (result.length() > length)
            result = result.substring(0, length);
        return result;
    }

    public static String sha(String text, String encoding) {
        return encryptPassword(text, encoding, "sha");
    }

    private static String encryptPassword(String password, String encoding, String algorithm) {
        try {
            byte[] unencodedPassword = password.getBytes(encoding);
            return encryptPassword(unencodedPassword, algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
    private static String encryptPassword(byte[] unencodedPassword, String algorithm) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            return null;
        }
        md.reset();
        md.update(unencodedPassword);
        byte[] encodedPassword = md.digest();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < encodedPassword.length; i++) {
            if ((encodedPassword[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Integer.toString(encodedPassword[i] & 0xff, 16));
        }
        return buf.toString();
    }
}
