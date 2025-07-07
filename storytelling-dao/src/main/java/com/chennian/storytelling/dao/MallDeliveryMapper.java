package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.mall.MallDelivery;
import com.chennian.storytelling.bean.vo.MallDeliveryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商城配送Mapper接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Mapper
public interface MallDeliveryMapper extends BaseMapper<MallDelivery> {
    
    /**
     * 分页查询配送列表
     *
     * @param page 分页参数
     * @param orderId 订单ID
     * @param userId 用户ID
     * @param status 配送状态
     * @param deliveryMethod 配送方式
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 配送分页列表
     */
    IPage<MallDeliveryVO> selectDeliveryPage(
        Page<MallDeliveryVO> page,
        @Param("orderId") Long orderId,
        @Param("userId") Long userId,
        @Param("status") Integer status,
        @Param("deliveryMethod") Integer deliveryMethod,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    /**
     * 查询订单的配送信息
     *
     * @param orderId 订单ID
     * @return 配送信息列表
     */
    List<MallDeliveryVO> selectByOrderId(@Param("orderId") Long orderId);
    
    /**
     * 查询子订单的配送信息
     *
     * @param subOrderId 子订单ID
     * @return 配送信息
     */
    MallDeliveryVO selectBySubOrderId(@Param("subOrderId") Long subOrderId);
    
    /**
     * 统计配送情况
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 配送统计信息
     */
    MallDeliveryVO selectDeliveryStats(
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    /**
     * 批量更新配送状态
     *
     * @param deliveryIds 配送ID列表
     * @param status 目标状态
     * @param operator 操作人
     * @param remark 备注
     * @return 更新成功的记录数
     */
    int batchUpdateStatus(
        @Param("deliveryIds") List<Long> deliveryIds,
        @Param("status") Integer status,
        @Param("operator") String operator,
        @Param("remark") String remark
    );
    
    /**
     * 查询待发货订单
     *
     * @param limit 限制数量
     * @return 待发货订单列表
     */
    List<MallDeliveryVO> selectPendingDeliveries(@Param("limit") Integer limit);
    
    /**
     * 查询超时未发货订单
     *
     * @param timeoutHours 超时小时数
     * @return 超时订单列表
     */
    List<MallDeliveryVO> selectTimeoutDeliveries(@Param("timeoutHours") Integer timeoutHours);
    
    /**
     * 统计配送费用
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 配送费用总额
     */
    BigDecimal sumDeliveryFee(
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    /**
     * 查询同城配送订单
     *
     * @param city 城市
     * @param status 配送状态
     * @return 同城配送订单列表
     */
    List<MallDeliveryVO> selectLocalDeliveries(
        @Param("city") String city,
        @Param("status") Integer status
    );
    
    /**
     * 查询自提订单
     *
     * @param pickupCode 自提点编码
     * @param status 配送状态
     * @return 自提订单列表
     */
    List<MallDeliveryVO> selectPickupDeliveries(
        @Param("pickupCode") String pickupCode,
        @Param("status") Integer status
    );
} 