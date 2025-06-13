package com.chennian.storytelling.bean.model.asset;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资产折旧记录实体类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("asset_depreciation")
@Schema(description = "资产折旧记录")
public class AssetDepreciation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "资产ID")
    private Long assetId;

    @Schema(description = "资产编号")
    private String assetCode;

    @Schema(description = "资产名称")
    private String assetName;

    @Schema(description = "折旧年月")
    private String depreciationMonth;

    @Schema(description = "折旧方法：1-直线法，2-年数总和法，3-双倍余额递减法")
    private Integer depreciationMethod;

    @Schema(description = "资产原值")
    private BigDecimal originalValue;

    @Schema(description = "累计折旧")
    private BigDecimal accumulatedDepreciation;

    @Schema(description = "本期折旧")
    private BigDecimal currentDepreciation;

    @Schema(description = "净值")
    private BigDecimal netValue;

    @Schema(description = "预计使用年限（月）")
    private Integer usefulLife;

    @Schema(description = "已使用月数")
    private Integer usedMonths;

    @Schema(description = "剩余月数")
    private Integer remainingMonths;

    @Schema(description = "折旧率")
    private BigDecimal depreciationRate;

    @Schema(description = "残值率")
    private BigDecimal residualRate;

    @Schema(description = "残值")
    private BigDecimal residualValue;

    @Schema(description = "折旧状态：0-未开始，1-折旧中，2-已完成")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "更新人")
    private String updateBy;
}