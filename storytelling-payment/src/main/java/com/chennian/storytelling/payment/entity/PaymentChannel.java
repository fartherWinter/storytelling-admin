package com.chennian.storytelling.payment.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付渠道配置实体类
 *
 * @author chennian
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("payment_channel")
@Schema(description = "支付渠道配置")
public class PaymentChannel {

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "渠道编码")
    @TableField("channel_code")
    @NotBlank(message = "渠道编码不能为空")
    @Size(max = 32, message = "渠道编码长度不能超过32个字符")
    private String channelCode;

    @Schema(description = "渠道名称")
    @TableField("channel_name")
    @NotBlank(message = "渠道名称不能为空")
    @Size(max = 64, message = "渠道名称长度不能超过64个字符")
    private String channelName;

    @Schema(description = "渠道类型")
    @TableField("channel_type")
    @NotBlank(message = "渠道类型不能为空")
    @Size(max = 32, message = "渠道类型长度不能超过32个字符")
    private String channelType;

    @Schema(description = "支付方式")
    @TableField("payment_methods")
    @NotBlank(message = "支付方式不能为空")
    private String paymentMethods;

    @Schema(description = "商户号")
    @TableField("merchant_id")
    @NotBlank(message = "商户号不能为空")
    @Size(max = 64, message = "商户号长度不能超过64个字符")
    private String merchantId;

    @Schema(description = "应用ID")
    @TableField("app_id")
    @Size(max = 64, message = "应用ID长度不能超过64个字符")
    private String appId;

    @Schema(description = "公钥")
    @TableField("public_key")
    private String publicKey;

    @Schema(description = "私钥")
    @TableField("private_key")
    private String privateKey;

    @Schema(description = "API密钥")
    @TableField("api_key")
    private String apiKey;

    @Schema(description = "API密钥V3")
    @TableField("api_key_v3")
    private String apiKeyV3;

    @Schema(description = "证书序列号")
    @TableField("cert_serial_no")
    @Size(max = 64, message = "证书序列号长度不能超过64个字符")
    private String certSerialNo;

    @Schema(description = "证书路径")
    @TableField("cert_path")
    @Size(max = 256, message = "证书路径长度不能超过256个字符")
    private String certPath;

    @Schema(description = "网关地址")
    @TableField("gateway_url")
    @NotBlank(message = "网关地址不能为空")
    @Size(max = 256, message = "网关地址长度不能超过256个字符")
    private String gatewayUrl;

    @Schema(description = "沙箱网关地址")
    @TableField("sandbox_gateway_url")
    @Size(max = 256, message = "沙箱网关地址长度不能超过256个字符")
    private String sandboxGatewayUrl;

    @Schema(description = "是否沙箱环境")
    @TableField("is_sandbox")
    private Boolean isSandbox;

    @Schema(description = "签名类型")
    @TableField("sign_type")
    @NotBlank(message = "签名类型不能为空")
    @Size(max = 16, message = "签名类型长度不能超过16个字符")
    private String signType;

    @Schema(description = "字符编码")
    @TableField("charset")
    @NotBlank(message = "字符编码不能为空")
    @Size(max = 16, message = "字符编码长度不能超过16个字符")
    private String charset;

    @Schema(description = "数据格式")
    @TableField("format")
    @NotBlank(message = "数据格式不能为空")
    @Size(max = 16, message = "数据格式长度不能超过16个字符")
    private String format;

    @Schema(description = "版本号")
    @TableField("version")
    @NotBlank(message = "版本号不能为空")
    @Size(max = 16, message = "版本号长度不能超过16个字符")
    private String version;

    @Schema(description = "最小支付金额（分）")
    @TableField("min_amount")
    @DecimalMin(value = "0", message = "最小支付金额不能小于0")
    private BigDecimal minAmount;

    @Schema(description = "最大支付金额（分）")
    @TableField("max_amount")
    @DecimalMin(value = "0", message = "最大支付金额不能小于0")
    private BigDecimal maxAmount;

    @Schema(description = "手续费率")
    @TableField("fee_rate")
    @DecimalMin(value = "0", message = "手续费率不能小于0")
    @DecimalMax(value = "1", message = "手续费率不能大于1")
    private BigDecimal feeRate;

    @Schema(description = "固定手续费（分）")
    @TableField("fixed_fee")
    @DecimalMin(value = "0", message = "固定手续费不能小于0")
    private BigDecimal fixedFee;

    @Schema(description = "支持的货币")
    @TableField("supported_currencies")
    @NotBlank(message = "支持的货币不能为空")
    private String supportedCurrencies;

    @Schema(description = "超时时间（分钟）")
    @TableField("timeout_minutes")
    @Min(value = 1, message = "超时时间不能小于1分钟")
    @Max(value = 1440, message = "超时时间不能大于1440分钟")
    private Integer timeoutMinutes;

    @Schema(description = "异步通知地址")
    @TableField("notify_url")
    @Size(max = 512, message = "异步通知地址长度不能超过512个字符")
    private String notifyUrl;

    @Schema(description = "同步返回地址")
    @TableField("return_url")
    @Size(max = 512, message = "同步返回地址长度不能超过512个字符")
    private String returnUrl;

    @Schema(description = "是否启用")
    @TableField("is_enabled")
    @NotNull(message = "是否启用不能为空")
    private Boolean isEnabled;

    @Schema(description = "排序")
    @TableField("sort_order")
    @Min(value = 0, message = "排序不能小于0")
    private Integer sortOrder;

    @Schema(description = "扩展配置")
    @TableField("extra_config")
    private String extraConfig;

    @Schema(description = "备注")
    @TableField("remark")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;

    @Schema(description = "版本号")
    @TableField("version")
    @Version
    private Integer version;

    @Schema(description = "是否删除")
    @TableField("is_deleted")
    @TableLogic
    private Boolean isDeleted;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Schema(description = "创建人")
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    @Schema(description = "更新人")
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
}