package com.chennian.security.admin.util;

import com.chennian.storytelling.common.exception.StorytellingBindException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

import static com.chennian.storytelling.security.common.constant.RSAConstants.*;

/**
 * @author by chennian
 * @date 2023/4/23 9:40
 */
public class RSAUtils {
    public static void main(String[] args) throws Exception {
        //生成密钥对
        HashMap<String, String> keyPairMap = RSAUtils.getKeyPairMap();
        String privateKey = keyPairMap.get("privateKey");
        String publicKey = keyPairMap.get("publicKey");
        System.out.println("私钥 => " + privateKey + "\n");
        System.out.println("公钥 =>" + publicKey + "\n");
        //RSA加密
        String encryptData = RSAUtils.encrypt("data", publicKey);
        System.out.println("加密后内容 => " + encryptData + "\n");
        // RSA解密\
        try{
            String decryptData = RSAUtils.decrypt(encryptData, privateKey);
            decryptData = java.net.URLDecoder.decode(decryptData, "UTF-8");
            System.out.println("解密后内容 => " + decryptData + "\n");
            String sign = RSAUtils.sign("data", RSAUtils.getPrivateKey(privateKey));
            // RSA验签
            boolean result = RSAUtils.verify("data", RSAUtils.getPublicKey(publicKey), sign);
            System.out.println(result);
        } catch (Exception e) {
            throw new StorytellingBindException("rsa解密错误", e);
        }

    }

    public static HashMap<String, String> getKeyPairMap() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM_NAME);
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();
        //将公钥私钥进行base64编码、使用encodeBase64进行编译编码，并返回一个byte字节数组
        String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
        String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
        HashMap<String, String> keyMap = new HashMap<>();
        keyMap.put("privateKey", privateKey);
        keyMap.put("publicKey", publicKey);
        return keyMap;
    }

    /**
     * RSA加密，获取公钥
     *
     * @param publicKey base64加密的公钥字符串
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        //使用decodeBase64进行破译编码，并返回一个byte字节数组
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        //使用X509标准作为密钥规范管理的编码格式,按照 X509 标准对其进行编码的密钥。复制数组的内容，以防随后的修改。
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        //创建一个KeyFactory对象。
        //密钥工厂用于将 密钥 （ Key类型的不透明密码密钥）转换为 密钥规范 （底层密钥资料的透明表示）

        //返回一个KeyFactory对象，用于转换指定算法的公钥/私钥。
        //返回封装指定Provider对象的KeyFactorySpi实现的新KeyFactory对象。 请注意，指定的Provider对象不必在提供程序列表中注册。
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
        //根据提供的密钥规范（密钥材料）生成公钥对象。
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * RSA加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     */
    public static String encrypt(String data, String publicKey) throws Exception {
        //避免前端解密时，出现中文乱码情况，提前将数据进行中编码
        //data = new String(data.getBytes("utf8"));
        data = java.net.URLEncoder.encode(data, "UTF-8");
        //Cipher此类为加密和解密提供密码功能
        //创建 Cipher 对象，应用程序调用 Cipher 的 getInstance 方法并将所请求转换 的名称传递给它
        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
        //RSA加密，获取公钥
        PublicKey publicKeyP = getPublicKey(publicKey);
//        Cipher对象需要初始化
//        init(int opmode, Key key, AlgorithmParameterSpec params)
//        (1)opmode ：Cipher.ENCRYPT_MODE(加密模式)和 Cipher.DECRYPT_MODE(解密模式)
//        (2)key ：密匙，使用传入的盐构造出一个密匙，可以使用SecretKeySpec、KeyGenerator和KeyPairGenerator创建密匙，其中
//                * SecretKeySpec和KeyGenerator支持AES，DES，DESede三种加密算法创建密匙
//                * KeyPairGenerator支持RSA加密算法创建密匙
//        (3)params ：使用CBC模式时必须传入该参数，该项目使用IvParameterSpec创建iv 对象
        cipher.init(Cipher.ENCRYPT_MODE, publicKeyP);
        //获取加密内容的长度
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;

        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                //加密或解密
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return new String(Base64.encodeBase64(encryptedData));
    }

    /**
     * 获取私钥
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
        //根据提供的密钥规范（密钥材料）生成公钥对象。
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * RSA解密
     *
     * @param data       待解密数据
     * @param privateKey 私钥
     */
    public static String decrypt(String data, String privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
        PrivateKey privateKeyP = getPrivateKey(privateKey);
        cipher.init(Cipher.DECRYPT_MODE, privateKeyP);
        byte[] dataBytes = Base64.decodeBase64(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // 解密后的内容
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    /**
     * RSA签名，签名
     *
     * @param data       待签名数据
     * @param privateKey 私钥
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        //以主编码格式返回密钥，如果此密钥不支持编码，则返回null。
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
        //根据提供的密钥规范（密钥材料）生成私钥对象。
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        //Signature类用于提供数字签名，用于保证数据的完整性，用非对称密钥中的私钥签名，公钥验签。
        //与 Java Security 中其他基于算法的类一样，Signature 提供了与实现无关的算法，因此，调用方（应用程序代码）
        // 会请求特定的签名算法并将它传回给已被正确初始化的 Signature 对象。如果需要，还可以通过特定的提供程序请求特定的算法
        //getInstance指定签名算法
        Signature signature = Signature.getInstance(MD5_RSA);
        //初始化签署签名的私钥
        signature.initSign(key);
        //根据初始化类型，这可更新要签名或验证的字节
        signature.update(data.getBytes());
        //signature.sign()签署或验证所有更新字节的签名
        return new String(Base64.encodeBase64(signature.sign()));
    }

    /**
     * 验签
     *
     * @param srcData   原始字符串
     * @param publicKey 公钥
     * @param sign      签名
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(MD5_RSA);
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        //signature.verify签署或验证所有更新字节的签名
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }

}
