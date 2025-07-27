package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * 支付渠道配置请求
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "支付渠道配置请求")
public class PaymentChannelConfigRequest {

    @Schema(description = "渠道编码")
    @NotBlank(message = "渠道编码不能为空")
    private String channelCode;

    @Schema(description = "配置类型")
    private String configType;

    @Schema(description = "配置数据")
    private Map<String, Object> configData;

    @Schema(description = "是否加密")
    private Boolean encrypted;

    @Schema(description = "环境类型")
    private String environment;

    @Schema(description = "操作人ID")
    private Long operatorId;

    @Schema(description = "备注")
    private String remark;
}