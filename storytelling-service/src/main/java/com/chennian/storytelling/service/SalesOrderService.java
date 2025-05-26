package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.SalesOrder;
import com.chennian.storytelling.bean.vo.MobileSalesOrderVO;
import com.chennian.storytelling.bean.vo.SalesStatisticsVO;
import com.chennian.storytelling.common.utils.PageParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 销售订单服务接口
 * @author chennian
 */
public interface SalesOrderService extends IService<SalesOrder> {

    /**
     * 分页查询销售订单列表
     * @param page 分页参数
     * @param salesOrder 查询条件
     * @return 销售订单分页数据
     */
    IPage<SalesOrder> findByPage(PageParam<SalesOrder> page, SalesOrder salesOrder);

    /**
     * 根据ID查询销售订单
     * @param orderId 订单ID
     * @return 销售订单信息
     */
    SalesOrder selectOrderById(Long orderId);

    /**
     * 新增销售订单
     * @param salesOrder 销售订单信息
     * @return 结果
     */
    Integer insertOrder(SalesOrder salesOrder);

    /**
     * 修改销售订单
     * @param salesOrder 销售订单信息
     * @return 结果
     */
    Integer updateOrder(SalesOrder salesOrder);

    /**
     * 批量删除销售订单
     * @param orderIds 需要删除的销售订单ID数组
     * @return 结果
     */
    Integer deleteOrderByIds(Long[] orderIds);
    
    /**
     * 确认销售订单
     * @param orderId 订单ID
     * @return 结果
     */
    Integer confirmOrder(Long orderId);
    
    /**
     * 订单发货
     * @param orderId 订单ID
     * @return 结果
     */
    Integer shipOrder(Long orderId);
    
    /**
     * 完成销售订单
     * @param orderId 订单ID
     * @return 结果
     */
    Integer completeOrder(Long orderId);
    
    /**
     * 取消销售订单
     * @param orderId 订单ID
     * @return 结果
     */
    Integer cancelOrder(Long orderId);
    
    /**
     * 获取销售统计数据
     *
     * @param params 查询参数
     * @return 销售统计数据VO对象
     */
    SalesStatisticsVO getSalesStatistics(Map<String, Object> params);
    
    /**
     * 获取销售趋势数据
     *
     * @param params 查询参数，包含period（daily, weekly, monthly, yearly）
     * @return 销售趋势数据
     */
    Map<String, Object> getSalesTrend(Map<String, Object> params);

    /**
     * 根据客户ID查询销售订单
     * @param customerId 客户ID
     * @return 销售订单列表
     */
    List<SalesOrder> selectOrdersByCustomerId(Long customerId);
    
    /**
     * 获取移动端订单列表
     * @param params 查询参数
     * @return 移动端订单列表
     */
    List<MobileSalesOrderVO> getMobileOrderList(Map<String, Object> params);
    
    /**
     * 获取移动端订单详情
     * @param orderId 订单ID
     * @return 移动端订单详情
     */
    MobileSalesOrderVO getMobileOrderDetail(Long orderId);
    
    /**
     * 移动端快速创建订单
     * @param orderVO 订单信息
     * @return 订单ID
     */
    Long quickAddOrder(MobileSalesOrderVO orderVO);
    
    /**
     * 获取今日销售额
     * @return 今日销售额
     */
    BigDecimal getTodaySales();
    
    /**
     * 获取待处理订单数
     * @return 待处理订单数
     */
    Long getPendingOrdersCount();
}