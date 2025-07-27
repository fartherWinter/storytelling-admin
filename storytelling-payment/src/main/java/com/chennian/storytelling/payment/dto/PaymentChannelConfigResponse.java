package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 支付渠道配置响应
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "支付渠道配置响应")
public class PaymentChannelConfigResponse {

    @Schema(description = "配置ID")
    private Long configId;

    @Schema(description = "渠道编码")
    private String channelCode;

    @Schema(description = "配置类型")
    private String configType;

    @Schema(description = "配置数据")
    private Map<String, Object> configData;

    @Schema(description = "是否加密")
    private Boolean encrypted;

    @Schema(description = "环境类型")
    private String environment;

    @Schema(description = "配置状态")
    private String status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "操作人ID")
    private Long operatorId;

    @Schema(description = "备注")
    private String remark;
}