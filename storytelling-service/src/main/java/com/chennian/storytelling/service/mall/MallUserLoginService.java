package com.chennian.storytelling.service.mall;

import com.chennian.storytelling.bean.bo.MallUserAutoLoginBo;
import com.chennian.storytelling.bean.bo.MallUserLoginBo;
import com.chennian.storytelling.bean.model.mall.MallUser;

/**
 * @author by chennian
 * @date 2025/5/25.
 */
public interface MallUserLoginService {

    /**
     * 获取openid
     *
     * @param loginBo
     * @return
     */
    String getUserOpenid(MallUserLoginBo loginBo);

    /**
     * 获取用户信息
     *
     * @param loginBo
     * @return
     */
    String decryptAesData(MallUserLoginBo loginBo);

    /**
     * 获取token数据
     *
     * @return
     */
    String getMinAppToken();

    /**
     * 更新手机号
     *
     * @param loginBo
     * @return MallUser
     */
    MallUser userLogin(MallUserLoginBo loginBo);

    /**
     * 发送短信
     *
     * @param loginBo
     * @return
     */
    String sendSms(MallUserLoginBo loginBo);

    /**
     * 手机号登陆
     *
     * @param loginBo
     * @return
     */
    MallUser loginMobile(MallUserLoginBo loginBo);

    /**
     * 自动登录
     * @param loginBo
     * @return
     */
    MallUser autoLogin(MallUserAutoLoginBo loginBo);
}
