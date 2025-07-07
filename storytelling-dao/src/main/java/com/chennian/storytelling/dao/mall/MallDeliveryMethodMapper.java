package com.chennian.storytelling.dao.mall;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.mall.MallDeliveryMethod;
import com.chennian.storytelling.bean.vo.MallDeliveryMethodVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 物流配送方式Mapper接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Mapper
public interface MallDeliveryMethodMapper extends BaseMapper<MallDeliveryMethod> {

    /**
     * 分页查询配送方式列表
     *
     * @param page 分页参数
     * @param methodType 配送类型
     * @param deliveryScope 配送范围
     * @param status 状态
     * @return 配送方式VO分页列表
     */
    IPage<MallDeliveryMethodVO> selectMethodPage(
            @Param("page") Page<MallDeliveryMethodVO> page,
            @Param("methodType") Integer methodType,
            @Param("deliveryScope") Integer deliveryScope,
            @Param("status") Integer status
    );

    /**
     * 查询可用的配送方式列表
     *
     * @param areaCode 区域编码
     * @param weight 重量
     * @param amount 订单金额
     * @return 配送方式VO列表
     */
    List<MallDeliveryMethodVO> selectAvailableMethods(
            @Param("areaCode") String areaCode,
            @Param("weight") BigDecimal weight,
            @Param("amount") BigDecimal amount
    );

    /**
     * 计算配送费用
     *
     * @param methodId 配送方式ID
     * @param areaCode 区域编码
     * @param weight 重量
     * @param amount 订单金额
     * @param timeCode 时间段编码
     * @return 配送费用
     */
    BigDecimal calculateFreight(
            @Param("methodId") Long methodId,
            @Param("areaCode") String areaCode,
            @Param("weight") BigDecimal weight,
            @Param("amount") BigDecimal amount,
            @Param("timeCode") String timeCode
    );

    /**
     * 查询特殊区域加价规则
     *
     * @param methodId 配送方式ID
     * @return 区域加价规则列表
     */
    List<MallDeliveryMethodVO.AreaPriceRule> selectAreaPriceRules(@Param("methodId") Long methodId);

    /**
     * 查询配送时间规则
     *
     * @param methodId 配送方式ID
     * @return 时间规则列表
     */
    List<MallDeliveryMethodVO.TimeRule> selectTimeRules(@Param("methodId") Long methodId);

    /**
     * 查询配送方式使用统计
     *
     * @param methodId 配送方式ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 使用统计信息
     */
    MallDeliveryMethodVO.UsageStatistics selectUsageStatistics(
            @Param("methodId") Long methodId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    /**
     * 更新配送方式状态
     *
     * @param methodId 配送方式ID
     * @param status 状态
     * @return 更新数量
     */
    int updateMethodStatus(
            @Param("methodId") Long methodId,
            @Param("status") Integer status
    );

    /**
     * 更新配送方式优先级
     *
     * @param methodId 配送方式ID
     * @param priority 优先级
     * @return 更新数量
     */
    int updateMethodPriority(
            @Param("methodId") Long methodId,
            @Param("priority") Integer priority
    );

    /**
     * 更新特殊区域加价规则
     *
     * @param methodId 配送方式ID
     * @param rules 加价规则列表
     * @return 更新数量
     */
    int updateAreaPriceRules(
            @Param("methodId") Long methodId,
            @Param("rules") List<MallDeliveryMethodVO.AreaPriceRule> rules
    );

    /**
     * 更新配送时间规则
     *
     * @param methodId 配送方式ID
     * @param rules 时间规则列表
     * @return 更新数量
     */
    int updateTimeRules(
            @Param("methodId") Long methodId,
            @Param("rules") List<MallDeliveryMethodVO.TimeRule> rules
    );

    /**
     * 查询支持配送的区域列表
     *
     * @param methodId 配送方式ID
     * @return 区域列表
     */
    List<String> selectSupportedAreas(@Param("methodId") Long methodId);

    /**
     * 查询不支持配送的区域列表
     *
     * @param methodId 配送方式ID
     * @return 区域列表
     */
    List<String> selectUnsupportedAreas(@Param("methodId") Long methodId);
} 