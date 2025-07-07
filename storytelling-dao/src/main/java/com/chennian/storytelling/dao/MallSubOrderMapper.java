package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.mall.MallSubOrder;
import com.chennian.storytelling.bean.vo.MallSubOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商城子订单Mapper接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Mapper
public interface MallSubOrderMapper extends BaseMapper<MallSubOrder> {
    
    /**
     * 分页查询子订单列表
     *
     * @param page 分页参数
     * @param orderId 订单ID
     * @param productId 商品ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 子订单分页列表
     */
    IPage<MallSubOrderVO> selectSubOrderPage(
        Page<MallSubOrderVO> page,
        @Param("orderId") Long orderId,
        @Param("productId") Long productId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    /**
     * 批量插入子订单
     *
     * @param subOrders 子订单列表
     * @return 插入成功的记录数
     */
    int batchInsert(@Param("list") List<MallSubOrder> subOrders);
    
    /**
     * 查询订单的所有子订单
     *
     * @param orderId 订单ID
     * @return 子订单列表
     */
    List<MallSubOrderVO> selectByOrderId(@Param("orderId") Long orderId);
    
    /**
     * 统计商品销售情况
     *
     * @param productId 商品ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 销售统计信息
     */
    MallSubOrderVO selectProductSalesStats(
        @Param("productId") Long productId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    /**
     * 查询热销商品排行
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param limit 限制数量
     * @return 热销商品列表
     */
    List<MallSubOrderVO> selectHotProducts(
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        @Param("limit") Integer limit
    );
    
    /**
     * 统计订单商品总金额
     *
     * @param orderId 订单ID
     * @return 商品总金额
     */
    BigDecimal sumOrderAmount(@Param("orderId") Long orderId);
    
    /**
     * 批量更新子订单状态
     *
     * @param subOrderIds 子订单ID列表
     * @param status 目标状态
     * @param operator 操作人
     * @return 更新成功的记录数
     */
    int batchUpdateStatus(
        @Param("subOrderIds") List<Long> subOrderIds,
        @Param("status") Integer status,
        @Param("operator") String operator
    );
    
    /**
     * 查询商品的购买记录
     *
     * @param productId 商品ID
     * @param limit 限制数量
     * @return 购买记录列表
     */
    List<MallSubOrderVO> selectProductPurchaseHistory(
        @Param("productId") Long productId,
        @Param("limit") Integer limit
    );
    
    /**
     * 统计用户购买商品数量
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 购买数量
     */
    Integer countUserProductPurchases(
        @Param("userId") Long userId,
        @Param("productId") Long productId
    );
}