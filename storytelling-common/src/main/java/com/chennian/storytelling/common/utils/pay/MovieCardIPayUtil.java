package com.chennian.storytelling.common.utils.pay;

import com.chennian.storytelling.common.bean.MoviePayResult;

public interface MovieCardIPayUtil {

    public MoviePayResult authPay(String codePwd, String orderCode, Long orderAmount, boolean isWxPay, String payType, int count);

    public MoviePayResult pay(String codePwd, String orderCode, Long orderAmount, boolean isWxPay, String payType, int count, Long userId);
}