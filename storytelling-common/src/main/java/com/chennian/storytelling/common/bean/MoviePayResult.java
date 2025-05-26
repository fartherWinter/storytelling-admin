package com.chennian.storytelling.common.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MoviePayResult {
    @Schema(description = "支付状态")
    private boolean success;
    @Schema(description = "微信支付金额")
    private BigDecimal wxPayMoney;
    @Schema(description = "卡券支付金额")
    private BigDecimal cardPayMoney;
    @Schema(description = "支付的批次ids")
    private String openIds;
    private String msg;

    /****************微信支付返回******************/
    @Schema(description = "json.put(‘prepay_id’, prepay_id)")
    private String prepayId;
    @Schema(description = "json.put(‘mweb_url’, mweb_url);")
    private String mwebUrl;
}
