//package com.chennian.storytelling.common.config;
//
//import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
//import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
//import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
//import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
//import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
//import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
//import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
//import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
//import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.security.GeneralSecurityException;
//import java.security.PrivateKey;
//
///**
// * @Author chennian
// * @Date 2023/9/5 10:21
// */
//
//@Slf4j
//@Data
//@Configuration
//@ConfigurationProperties("h5pay")
//public class H5WxPayConfig {
//    /**
//     * 商户号
//     */
//    private String mchId;
//
//    /**
//     * 商户API证书序列号
//     */
//    private String mchSerialNo;
//
//    /**
//     * 商户私钥文件
//     */
//    private String privateKeyPath;
//
//    /**
//     * APIv3密钥
//     */
//    private String apiV3Key;
//
//    /**
//     * APPID
//     */
//    private String appid;
//
//    /**
//     * 微信服务器地址
//     */
//    private String domain;
//
//    /**
//     * 接收结果通知地址
//     */
//    private String notifyDomain;
//
//    /**
//     * 获取商户的私钥文件
//     *
//     * @param filename 证书地址
//     * @return 私钥文件
//     */
//    public PrivateKey getH5PrivateKey(String filename) {
//        try {
//            return PemUtil.loadPrivateKey(new FileInputStream(filename));
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException("私钥文件不存在");
//        }
//    }
//
//    /**
//     * 获取签名验证器
//     */
//    @Bean
//    public Verifier getH5Verifier() {
//        // 获取商户私钥
//        final PrivateKey privateKey = getH5PrivateKey(privateKeyPath);
//        // 私钥签名对象
//        PrivateKeySigner privateKeySigner = new PrivateKeySigner(mchSerialNo, privateKey);
//        // 身份认证对象
//        WechatPay2Credentials wechatPay2Credentials = new WechatPay2Credentials(mchId, privateKeySigner);
//        // 获取证书管理器实例
//        CertificatesManager certificatesManager = CertificatesManager.getInstance();
//        try {
//            // 向证书管理器增加需要自动更新平台证书的商户信息
//            certificatesManager.putMerchant(mchId, wechatPay2Credentials, apiV3Key.getBytes(StandardCharsets.UTF_8));
//        } catch (IOException | GeneralSecurityException | HttpCodeException e) {
//            e.printStackTrace();
//        }
//        try {
//            return certificatesManager.getVerifier(mchId);
//        } catch (NotFoundException e) {
//            e.printStackTrace();
//            throw new RuntimeException("获取签名验证器失败");
//        }
//    }
//
//    /**
//     * 获取微信支付的远程请求对象
//     *
//     * @return Http请求对象
//     */
//    @Bean
//    public CloseableHttpClient getH5WxPayClient() {
//
//        // 获取签名验证器
//        Verifier verifier = getH5Verifier();
//
//        // 获取商户私钥
//        final PrivateKey privateKey = getH5PrivateKey(privateKeyPath);
//
//        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create().withMerchant(mchId, mchSerialNo, privateKey).withValidator(new WechatPay2Validator(verifier));
//
//        return builder.build();
//    }
//}
