package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.mall.MallRefund;
import com.chennian.storytelling.bean.vo.MallRefundVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商城退款Mapper接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Mapper
public interface MallRefundMapper extends BaseMapper<MallRefund> {
    
    /**
     * 分页查询退款列表
     *
     * @param page 分页参数
     * @param orderId 订单ID
     * @param userId 用户ID
     * @param status 退款状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 退款分页列表
     */
    IPage<MallRefundVO> selectRefundPage(
        Page<MallRefundVO> page,
        @Param("orderId") Long orderId,
        @Param("userId") Long userId,
        @Param("status") Integer status,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    /**
     * 查询订单的退款信息
     *
     * @param orderId 订单ID
     * @return 退款信息列表
     */
    List<MallRefundVO> selectByOrderId(@Param("orderId") Long orderId);
    
    /**
     * 查询子订单的退款信息
     *
     * @param subOrderId 子订单ID
     * @return 退款信息
     */
    MallRefundVO selectBySubOrderId(@Param("subOrderId") Long subOrderId);
    
    /**
     * 统计退款情况
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 退款统计信息
     */
    MallRefundVO selectRefundStats(
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    /**
     * 批量更新退款状态
     *
     * @param refundIds 退款ID列表
     * @param status 目标状态
     * @param operator 操作人
     * @param remark 备注
     * @return 更新成功的记录数
     */
    int batchUpdateStatus(
        @Param("refundIds") List<Long> refundIds,
        @Param("status") Integer status,
        @Param("operator") String operator,
        @Param("remark") String remark
    );
    
    /**
     * 查询用户退款历史
     *
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 退款历史列表
     */
    List<MallRefundVO> selectUserRefundHistory(
        @Param("userId") Long userId,
        @Param("limit") Integer limit
    );
    
    /**
     * 统计用户退款次数
     *
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 退款次数
     */
    Integer countUserRefunds(
        @Param("userId") Long userId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    /**
     * 统计用户退款金额
     *
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 退款金额
     */
    BigDecimal sumUserRefundAmount(
        @Param("userId") Long userId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    /**
     * 查询待处理的退款申请
     *
     * @param limit 限制数量
     * @return 待处理退款列表
     */
    List<MallRefundVO> selectPendingRefunds(@Param("limit") Integer limit);
    
    /**
     * 查询超时未处理的退款申请
     *
     * @param timeoutHours 超时小时数
     * @return 超时退款列表
     */
    List<MallRefundVO> selectTimeoutRefunds(@Param("timeoutHours") Integer timeoutHours);
} 