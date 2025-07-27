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
 * 支付记录实体类
 *
 * @author chennian
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("payment_record")
@Schema(description = "支付记录")
public class PaymentRecord {

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "支付订单ID")
    @TableField("payment_order_id")
    @NotNull(message = "支付订单ID不能为空")
    private Long paymentOrderId;

    @Schema(description = "支付订单号")
    @TableField("payment_order_no")
    @NotBlank(message = "支付订单号不能为空")
    @Size(max = 64, message = "支付订单号长度不能超过64个字符")
    private String paymentOrderNo;

    @Schema(description = "记录类型")
    @TableField("record_type")
    @NotBlank(message = "记录类型不能为空")
    @Size(max = 32, message = "记录类型长度不能超过32个字符")
    private String recordType;

    @Schema(description = "操作类型")
    @TableField("operation_type")
    @NotBlank(message = "操作类型不能为空")
    @Size(max = 32, message = "操作类型长度不能超过32个字符")
    private String operationType;

    @Schema(description = "支付渠道")
    @TableField("payment_channel")
    @NotBlank(message = "支付渠道不能为空")
    @Size(max = 32, message = "支付渠道长度不能超过32个字符")
    private String paymentChannel;

    @Schema(description = "支付方式")
    @TableField("payment_method")
    @NotBlank(message = "支付方式不能为空")
    @Size(max = 32, message = "支付方式长度不能超过32个字符")
    private String paymentMethod;

    @Schema(description = "金额（分）")
    @TableField("amount")
    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0", message = "金额不能小于0")
    private BigDecimal amount;

    @Schema(description = "货币类型")
    @TableField("currency")
    @NotBlank(message = "货币类型不能为空")
    @Size(max = 8, message = "货币类型长度不能超过8个字符")
    private String currency;

    @Schema(description = "操作状态")
    @TableField("status")
    @NotBlank(message = "操作状态不能为空")
    @Size(max = 16, message = "操作状态长度不能超过16个字符")
    private String status;

    @Schema(description = "第三方交易号")
    @TableField("third_party_trade_no")
    @Size(max = 128, message = "第三方交易号长度不能超过128个字符")
    private String thirdPartyTradeNo;

    @Schema(description = "第三方订单号")
    @TableField("third_party_order_no")
    @Size(max = 128, message = "第三方订单号长度不能超过128个字符")
    private String thirdPartyOrderNo;

    @Schema(description = "请求参数")
    @TableField("request_params")
    private String requestParams;

    @Schema(description = "响应参数")
    @TableField("response_params")
    private String responseParams;

    @Schema(description = "错误码")
    @TableField("error_code")
    @Size(max = 32, message = "错误码长度不能超过32个字符")
    private String errorCode;

    @Schema(description = "错误信息")
    @TableField("error_message")
    @Size(max = 512, message = "错误信息长度不能超过512个字符")
    private String errorMessage;

    @Schema(description = "处理时长（毫秒）")
    @TableField("process_time")
    @Min(value = 0, message = "处理时长不能小于0")
    private Long processTime;

    @Schema(description = "重试次数")
    @TableField("retry_count")
    @Min(value = 0, message = "重试次数不能小于0")
    private Integer retryCount;

    @Schema(description = "最大重试次数")
    @TableField("max_retry_count")
    @Min(value = 0, message = "最大重试次数不能小于0")
    private Integer maxRetryCount;

    @Schema(description = "下次重试时间")
    @TableField("next_retry_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime nextRetryTime;

    @Schema(description = "客户端IP")
    @TableField("client_ip")
    @Size(max = 64, message = "客户端IP长度不能超过64个字符")
    private String clientIp;

    @Schema(description = "用户代理")
    @TableField("user_agent")
    @Size(max = 512, message = "用户代理长度不能超过512个字符")
    private String userAgent;

    @Schema(description = "设备信息")
    @TableField("device_info")
    @Size(max = 256, message = "设备信息长度不能超过256个字符")
    private String deviceInfo;

    @Schema(description = "操作开始时间")
    @TableField("start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "操作结束时间")
    @TableField("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Schema(description = "扩展参数")
    @TableField("extra_params")
    private String extraParams;

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