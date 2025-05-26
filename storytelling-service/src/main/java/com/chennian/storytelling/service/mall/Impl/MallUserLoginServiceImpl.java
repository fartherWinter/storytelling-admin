package com.chennian.storytelling.service.mall.Impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chennian.storytelling.bean.bo.MallUserAutoLoginBo;
import com.chennian.storytelling.bean.bo.MallUserLoginBo;
import com.chennian.storytelling.bean.model.mall.MallUser;
import com.chennian.storytelling.common.bean.WeChatAccessToken;
import com.chennian.storytelling.common.constant.CUCCApiConstant;
import com.chennian.storytelling.common.constant.CacheConstants;
import com.chennian.storytelling.common.constant.WeChatAppConstants;
import com.chennian.storytelling.common.constant.WeChatTicketConstants;
import com.chennian.storytelling.common.exception.StorytellingBindException;
import com.chennian.storytelling.common.redis.RedisCache;
import com.chennian.storytelling.common.utils.DateUtils;
import com.chennian.storytelling.common.utils.Json;
import com.chennian.storytelling.common.utils.message.MessageUtils;
import com.chennian.storytelling.dao.MallUserMapper;
import com.chennian.storytelling.service.mall.MallUserLoginService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author by chennian
 * @date 2025/5/25.
 */
@Service
public class MallUserLoginServiceImpl implements MallUserLoginService {
    private final MallUserMapper mallUserMapper;
    private final RedisCache redisCache;

    public MallUserLoginServiceImpl(MallUserMapper mallUserMapper, RedisCache redisCache) {
        this.mallUserMapper = mallUserMapper;
        this.redisCache = redisCache;
    }

    private static final Integer IN_VALID = 1;

    private static final String[] MOBILE_UNREAL = {"170", "162", "165", "171", "167"};

    /**
     * 获取openid
     *
     * @param loginBo
     * @return
     */
    @Override
    public String getUserOpenid(MallUserLoginBo loginBo) {
        String content = "";
        OkHttpClient client = new OkHttpClient();
        String params = "appid=" + WeChatAppConstants.APP_ID + "&secret=" + WeChatAppConstants.APP_SECRET
                + "&js_code=" + loginBo.getJsCode() + "&grant_type=" + WeChatTicketConstants.GRANT_TYPE;
        Request request = new Request.Builder().url(WeChatTicketConstants.WE_LOGIN_URL + params).addHeader("Content-type", "application/json;charset=UTF-8").build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            content = response.body().string();
            System.out.println(content);
        } catch (IOException e) {
            throw new StorytellingBindException("获取当前用户openid异常。");
        }
        return content;
    }

    /**
     * 获取用户信息
     *
     * @param loginBo
     * @return
     */
    @Override
    public String decryptAesData(MallUserLoginBo loginBo) {
        String result = "";
        // 被加密的数据
        byte[] dataByte = Base64.decode(loginBo.getEncryptedData());
        // 加密秘钥
        byte[] keyByte = Base64.decode(loginBo.getSessionKey());
        // 偏移量
        byte[] ivByte = Base64.decode(loginBo.getIv());
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
     * 获取token
     *
     * @return
     */
    @Override
    public String getMinAppToken() {
        OkHttpClient client = new OkHttpClient();
        String params = "appid=" + WeChatAppConstants.APP_ID + "&secret=" + WeChatAppConstants.APP_SECRET
                + "&grant_type=client_credential";
        Request request = new Request.Builder().url(WeChatAppConstants.WE_ACCESS_TOKEN + params).addHeader("Content-type", "application/json;charset=UTF-8").build();
        WeChatAccessToken weChatAccessToken = null;
        try {
            Response response = client.newCall(request).execute();
            weChatAccessToken = Json.parseObject(response.body().string(), WeChatAccessToken.class);
        } catch (IOException e) {
            throw new StorytellingBindException("获取登录token失败");
        }
        String token = weChatAccessToken.getAccess_token();
        return token;
    }

    /**
     * 更新用户手机号
     *
     * @param loginBo
     */
    @Override
    public MallUser userLogin(MallUserLoginBo loginBo) {
        //虚拟手机号验证
        verifyVirtuallyMobile(loginBo.getMobile());
        //查看当前用户是否注册成功：以手机号进行登陆判断
        MallUser mallUser = mallUserMapper.selectOne(new LambdaQueryWrapper<MallUser>()
                .eq(MallUser::getMobile, loginBo.getMobile()));
        if (mallUser == null) {
            //用户未进行登陆
            MallUser user = new MallUser();
            user.setOpenid(loginBo.getOpenid());
            user.setMobile(loginBo.getMobile());
            user.setUnionid(loginBo.getUnionid());
            user.setSource("mini-apps");
            user.setCreateTime(DateUtils.currentTime());
            user.setLastLoginTime(DateUtils.currentTime());
            mallUserMapper.insert(mallUser);
        } else {
            //用户存在，更新用户openid
            mallUser.setOpenid(loginBo.getOpenid());
            mallUser.setUnionid(loginBo.getUnionid());
            mallUser.setLastLoginTime(DateUtils.currentTime());
            mallUserMapper.updateById(mallUser);
        }
        //返回更新后的用户信息
        return mallUserMapper.selectOne(new LambdaQueryWrapper<MallUser>()
                .eq(MallUser::getMobile, loginBo.getMobile())
                .eq(MallUser::getOpenid, loginBo.getOpenid()));
    }

    /**
     * 发送短信
     *
     * @param loginBo
     * @return
     */
    @Override
    public String sendSms(MallUserLoginBo loginBo) {
        verifyVirtuallyMobile(loginBo.getMobile());
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        String uuid = UUID.fastUUID().toString();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        redisCache.setCacheObject(verifyKey, code, 10, TimeUnit.MINUTES);
        Map valueMap = new HashMap();
        valueMap.put("code", code);
        MessageUtils.Message(valueMap, CUCCApiConstant.CODE_TEMPLATE, loginBo.getMobile());
        return uuid;
    }

    /**
     * 用户手机号登陆
     *
     * @param loginBo
     * @return
     */
    @Override
    public MallUser loginMobile(MallUserLoginBo loginBo) {
        verifyVirtuallyMobile(loginBo.getMobile());
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + loginBo.getUuid();
        String smsCode = redisCache.getCacheObject(verifyKey);
        if (!loginBo.getCode().equals(smsCode)) {
            throw new StorytellingBindException("验证码输入错误,请重新输入");
        }
        String openid = loginBo.getOpenid();
        String mobile = loginBo.getMobile();
        MallUser mallUser = mallUserMapper.selectOne(new LambdaQueryWrapper<MallUser>()
                .eq(MallUser::getOpenid, loginBo.getOpenid()).eq(MallUser::getMobile, loginBo.getMobile()));
        //以openid为基准进行手机号码更新
        if (mallUser == null) {
            mallUserMapper.delete(new LambdaQueryWrapper<MallUser>()
                    .eq(MallUser::getMobile, mobile).or().eq(MallUser::getOpenid, openid));
            //创建用户数据
            MallUser user = new MallUser();
            user.setMobile(loginBo.getMobile());
            user.setOpenid(loginBo.getOpenid());
            user.setSource("APP-MALL");
            user.setUnionid(loginBo.getUnionid());
            user.setCreateTime(DateUtils.currentTime());
            mallUserMapper.insert(user);
            return user;
        } else {
            //更新用户信息
            mallUser.setMobile(loginBo.getMobile());
            mallUser.setOpenid(loginBo.getOpenid());
            mallUser.setUpdateTime(DateUtils.currentTime());
            mallUserMapper.updateById(mallUser);
            return mallUser;
        }
    }


    /**
     * 自动登录
     *
     * @param loginBo
     * @return
     */
    @Override
    public MallUser autoLogin(MallUserAutoLoginBo loginBo) {
        String openid = loginBo.getOpenid();
        String mobile = loginBo.getMobile();
        //查看是否为历史数据，进行历史用户数据更新
        MallUser mallUser = mallUserMapper.selectOne(new LambdaQueryWrapper<MallUser>()
                .eq(MallUser::getOpenid, openid).eq(MallUser::getMobile, mobile));
        if (mallUser != null) {
            mallUser.setLastLoginTime(new Date());
            mallUserMapper.updateById(mallUser);
            return mallUser;
        } else {
            mallUserMapper.delete(new LambdaQueryWrapper<MallUser>()
                    .eq(MallUser::getMobile, mobile).or().eq(MallUser::getOpenid, openid));
            MallUser user = new MallUser();
            user.setMobile(mobile);
            user.setOpenid(openid);
            user.setSource("mini-app");
            user.setCreateTime(new Date());
            user.setLastLoginTime(new Date());
            mallUserMapper.insert(user);
            return user;
        }
    }

    private void verifyVirtuallyMobile(String mobile) {
        List<String> mobileHeadUnreal = java.util.Arrays.asList(MOBILE_UNREAL);
        if (mobileHeadUnreal.contains(mobile.substring(0, 2))) {
            throw new StorytellingBindException("请勿使用虚拟手机号登录");
        }
    }

}
