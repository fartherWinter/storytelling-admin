package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.mall.MallOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商城订单Mapper接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Mapper
public interface MallOrderMapper extends BaseMapper<MallOrder> {
    /**
     * 分页查询用户订单列表
     *
     * @param page 分页参数
     * @param userId 用户ID
     * @param status 订单状态
     * @return 订单列表
     */
    IPage<MallOrder> selectUserOrderPage(IPage<MallOrder> page, @Param("userId") Long userId, @Param("status") String status);
    
    /**
     * 批量更新订单状态
     *
     * @param orderIds 订单ID列表
     * @param status 目标状态
     * @param updateBy 更新人
     * @return 更新成功的记录数
     */
    int batchUpdateStatus(@Param("orderIds") List<Long> orderIds, @Param("status") String status, @Param("updateBy") String updateBy);
    
    /**
     * 统计用户订单金额
     *
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 订单总金额
     */
    BigDecimal sumUserOrderAmount(@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    /**
     * 查询待处理订单
     *
     * @param status 订单状态列表
     * @param limit 限制数量
     * @return 待处理订单列表
     */
    List<MallOrder> selectPendingOrders(@Param("status") List<String> status, @Param("limit") Integer limit);
    
    /**
     * 批量插入订单
     *
     * @param orders 订单列表
     * @return 插入成功的记录数
     */
    int batchInsert(@Param("list") List<MallOrder> orders);
}