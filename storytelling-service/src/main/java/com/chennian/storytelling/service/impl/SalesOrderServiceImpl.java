package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.Customer;
import com.chennian.storytelling.bean.model.Product;
import com.chennian.storytelling.bean.model.SalesOrder;
import com.chennian.storytelling.bean.model.SalesOrderItem;
import com.chennian.storytelling.bean.vo.MobileSalesOrderVO;
import com.chennian.storytelling.bean.vo.SalesStatisticsVO;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.CustomerMapper;
import com.chennian.storytelling.dao.ProductMapper;
import com.chennian.storytelling.dao.SalesOrderMapper;
import com.chennian.storytelling.service.SalesOrderItemService;
import com.chennian.storytelling.service.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 销售订单服务实现类
 * @author chennian
 * @date 2023/5/20
 */
@Service
public class SalesOrderServiceImpl extends ServiceImpl<SalesOrderMapper, SalesOrder> implements SalesOrderService {

    @Autowired
    private SalesOrderMapper salesOrderMapper;
    
    @Autowired
    private SalesOrderItemService salesOrderItemService;
    
    @Autowired
    private CustomerMapper customerMapper;
    
    @Autowired
    private ProductMapper productMapper;

    /**
     * 分页查询销售订单
     *
     * @param page 分页参数
     * @param salesOrder 查询条件
     * @return 分页结果
     */
    @Override
    public IPage<SalesOrder> findByPage(PageParam<SalesOrder> page, SalesOrder salesOrder) {
        LambdaQueryWrapper<SalesOrder> queryWrapper = new LambdaQueryWrapper<>();
        if (salesOrder != null) {
            // 根据订单编号模糊查询
            if (StringUtils.hasText(salesOrder.getOrderNo())) {
                queryWrapper.like(SalesOrder::getOrderNo, salesOrder.getOrderNo());
            }
            
            // 根据客户ID查询
            if (salesOrder.getCustomerId() != null) {
                queryWrapper.eq(SalesOrder::getCustomerId, salesOrder.getCustomerId());
            }
            
            // 根据订单状态查询
            if (StringUtils.hasText(salesOrder.getStatus())) {
                queryWrapper.eq(SalesOrder::getStatus, salesOrder.getStatus());
            }
            
            // 根据创建时间范围查询
            if (salesOrder.getCreateTime() != null) {
                queryWrapper.ge(SalesOrder::getCreateTime, salesOrder.getCreateTime());
            }
        }
        
        // 默认按创建时间降序排序
        queryWrapper.orderByDesc(SalesOrder::getCreateTime);
        
        // 执行分页查询
        IPage<SalesOrder> pageResult = page(new Page<>(page.getCurrent(), page.getSize()), queryWrapper);
        
        return pageResult;
    }

    /**
     * 根据ID查询销售订单
     *
     * @param orderId 订单ID
     * @return 销售订单信息
     */
    @Override
    public SalesOrder selectOrderById(Long orderId) {
        SalesOrder salesOrder = getById(orderId);
        if (salesOrder != null) {
            // 获取订单明细列表
            List<SalesOrderItem> items = salesOrderItemService.selectItemsByOrderId(orderId);
            salesOrder.setItems(items);
        }
        return salesOrder;
    }

    /**
     * 新增销售订单
     *
     * @param salesOrder 销售订单信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insertOrder(SalesOrder salesOrder) {
        // 生成订单编号
        String orderNo = generateOrderNo();
        salesOrder.setOrderNo(orderNo);
        
        // 设置订单状态为草稿
        salesOrder.setStatus("0");
        
        // 设置创建时间
        salesOrder.setCreateTime(new Date());
        
        // 保存订单主表信息
        save(salesOrder);
        
        // 保存订单明细
        List<SalesOrderItem> items = salesOrder.getItems();
        if (items != null && !items.isEmpty()) {
            for (SalesOrderItem item : items) {
                item.setOrderId(salesOrder.getOrderId());
                item.setCreateTime(new Date());
            }
            salesOrderItemService.saveBatch(items);
        }
        
        return 1;
    }

    /**
     * 修改销售订单
     *
     * @param salesOrder 销售订单信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateOrder(SalesOrder salesOrder) {
        // 设置更新时间
        salesOrder.setUpdateTime(new Date());
        
        // 更新订单主表信息
        updateById(salesOrder);
        
        // 更新订单明细
        List<SalesOrderItem> items = salesOrder.getItems();
        if (items != null && !items.isEmpty()) {
            // 先删除原有明细
            salesOrderItemService.deleteByOrderId(salesOrder.getOrderId());
            
            // 重新保存明细
            for (SalesOrderItem item : items) {
                item.setOrderId(salesOrder.getOrderId());
                item.setCreateTime(new Date());
            }
            salesOrderItemService.saveBatch(items);
        }
        
        return 1;
    }

    /**
     * 批量删除销售订单
     *
     * @param orderIds 需要删除的销售订单ID数组
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteOrderByIds(Long[] orderIds) {
        // 删除订单明细
        for (Long orderId : orderIds) {
            salesOrderItemService.deleteByOrderId(orderId);
        }
        
        // 删除订单主表
        removeByIds(Arrays.asList(orderIds));
        
        return orderIds.length;
    }

    /**
     * 确认销售订单
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @Override
    public Integer confirmOrder(Long orderId) {
        SalesOrder salesOrder = getById(orderId);
        if (salesOrder != null) {
            // 设置订单状态为已确认
            salesOrder.setStatus("1");
            salesOrder.setUpdateTime(new Date());
            updateById(salesOrder);
            return 1;
        }
        return 0;
    }

    /**
     * 订单发货
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @Override
    public Integer shipOrder(Long orderId) {
        SalesOrder salesOrder = getById(orderId);
        if (salesOrder != null) {
            // 设置订单状态为已发货
            salesOrder.setStatus("2");
            salesOrder.setUpdateTime(new Date());
            updateById(salesOrder);
            return 1;
        }
        return 0;
    }

    /**
     * 完成销售订单
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @Override
    public Integer completeOrder(Long orderId) {
        SalesOrder salesOrder = getById(orderId);
        if (salesOrder != null) {
            // 设置订单状态为已完成
            salesOrder.setStatus("3");
            // 设置实际交付日期
            salesOrder.setActualDeliveryDate(new Date());
            salesOrder.setUpdateTime(new Date());
            updateById(salesOrder);
            return 1;
        }
        return 0;
    }

    /**
     * 取消销售订单
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @Override
    public Integer cancelOrder(Long orderId) {
        SalesOrder salesOrder = getById(orderId);
        if (salesOrder != null) {
            // 设置订单状态为已取消
            salesOrder.setStatus("4");
            salesOrder.setUpdateTime(new Date());
            updateById(salesOrder);
            return 1;
        }
        return 0;
    }

    /**
     * 获取销售统计数据
     *
     * @param params 查询参数
     * @return 销售统计数据VO对象
     */
    @Override
    public SalesStatisticsVO getSalesStatistics(Map<String, Object> params) {
        // 创建销售统计VO对象
        SalesStatisticsVO statistics = new SalesStatisticsVO();
        
        // 从参数中获取日期范围
        String dateRange = params.get("dateRange") != null ? params.get("dateRange").toString() : null;
        // 从参数中获取产品类别
        String productCategory = params.get("productCategory") != null ? params.get("productCategory").toString() : null;
        
        try {
            // 使用CompletableFuture并行执行多个查询任务
            
            // 1. 获取订单总数
            CompletableFuture<Long> totalOrdersFuture = CompletableFuture.supplyAsync(() -> count());
            
            // 2. 获取不同状态的订单数量 - 草稿
            CompletableFuture<Long> draftOrdersFuture = CompletableFuture.supplyAsync(() -> {
                LambdaQueryWrapper<SalesOrder> draftQuery = new LambdaQueryWrapper<>();
                draftQuery.eq(SalesOrder::getStatus, "0");
                return count(draftQuery);
            });
            
            // 3. 获取不同状态的订单数量 - 已确认
            CompletableFuture<Long> confirmedOrdersFuture = CompletableFuture.supplyAsync(() -> {
                LambdaQueryWrapper<SalesOrder> confirmedQuery = new LambdaQueryWrapper<>();
                confirmedQuery.eq(SalesOrder::getStatus, "1");
                return count(confirmedQuery);
            });
            
            // 4. 获取不同状态的订单数量 - 已发货
            CompletableFuture<Long> shippedOrdersFuture = CompletableFuture.supplyAsync(() -> {
                LambdaQueryWrapper<SalesOrder> shippedQuery = new LambdaQueryWrapper<>();
                shippedQuery.eq(SalesOrder::getStatus, "2");
                return count(shippedQuery);
            });
            
            // 5. 获取不同状态的订单数量 - 已完成
            CompletableFuture<Long> completedOrdersFuture = CompletableFuture.supplyAsync(() -> {
                LambdaQueryWrapper<SalesOrder> completedQuery = new LambdaQueryWrapper<>();
                completedQuery.eq(SalesOrder::getStatus, "3");
                return count(completedQuery);
            });
            
            // 6. 获取不同状态的订单数量 - 已取消
            CompletableFuture<Long> cancelledOrdersFuture = CompletableFuture.supplyAsync(() -> {
                LambdaQueryWrapper<SalesOrder> cancelledQuery = new LambdaQueryWrapper<>();
                cancelledQuery.eq(SalesOrder::getStatus, "4");
                return count(cancelledQuery);
            });
            
            // 7. 获取销售总金额
            CompletableFuture<List<Map<String, Object>>> salesAmountFuture = CompletableFuture.supplyAsync(() -> 
                salesOrderMapper.getSalesAmount(dateRange)
            );
            
            // 8. 获取热销产品
            CompletableFuture<List<Map<String, Object>>> hotProductsFuture = CompletableFuture.supplyAsync(() -> 
                salesOrderMapper.getHotProducts(params)
            );
            
            // 9. 如果有产品类别参数，获取该类别的销售数据
            CompletableFuture<List<Map<String, Object>>> categoryStatsFuture = null;
            if (StringUtils.hasText(productCategory)) {
                categoryStatsFuture = CompletableFuture.supplyAsync(() -> 
                    salesOrderMapper.getCategoryStats(productCategory, dateRange)
                );
            }
            
            // 等待所有异步任务完成并获取结果
            Long totalOrders = totalOrdersFuture.get();
            Long draftOrders = draftOrdersFuture.get();
            Long confirmedOrders = confirmedOrdersFuture.get();
            Long shippedOrders = shippedOrdersFuture.get();
            Long completedOrders = completedOrdersFuture.get();
            Long cancelledOrders = cancelledOrdersFuture.get();
            List<Map<String, Object>> salesAmount = salesAmountFuture.get();
            List<Map<String, Object>> hotProducts = hotProductsFuture.get();
            
            // 设置VO对象属性
            statistics.setTotalOrders(totalOrders);
            statistics.setDraftOrders(draftOrders);
            statistics.setConfirmedOrders(confirmedOrders);
            statistics.setShippedOrders(shippedOrders);
            statistics.setCompletedOrders(completedOrders);
            statistics.setCancelledOrders(cancelledOrders);
            statistics.setSalesAmount(salesAmount);
            statistics.setHotProducts(hotProducts);
            
            // 计算平均订单金额
            if (salesAmount != null && !salesAmount.isEmpty() && totalOrders > 0) {
                BigDecimal totalAmount = BigDecimal.ZERO;
                for (Map<String, Object> amount : salesAmount) {
                    if (amount.get("amount") != null) {
                        totalAmount = totalAmount.add(new BigDecimal(amount.get("amount").toString()));
                    }
                }
                BigDecimal averageOrderAmount = totalAmount.divide(new BigDecimal(totalOrders), 2, RoundingMode.HALF_UP);
                statistics.setAverageOrderAmount(averageOrderAmount);
            }
            
            // 如果有产品类别参数，添加类别统计数据
            if (categoryStatsFuture != null) {
                statistics.setCategoryStats(categoryStatsFuture.get());
            }
            
        } catch (InterruptedException | ExecutionException e) {
            // 发生异常时，回退到串行执行
            return getSalesStatisticsFallback(params);
        }
        
        return statistics;
    }
    
    /**
     * 获取销售统计数据的回退方法（串行执行）
     * 当并行执行发生异常时使用此方法
     *
     * @param params 查询参数
     * @return 销售统计数据VO对象
     */
    private SalesStatisticsVO getSalesStatisticsFallback(Map<String, Object> params) {
        // 创建销售统计VO对象
        SalesStatisticsVO statistics = new SalesStatisticsVO();
        
        // 从参数中获取日期范围
        String dateRange = params.get("dateRange") != null ? params.get("dateRange").toString() : null;
        // 从参数中获取产品类别
        String productCategory = params.get("productCategory") != null ? params.get("productCategory").toString() : null;
        
        // 使用串行执行方式获取数据，确保在并行执行失败时能够正常工作
        
        // 获取订单总数
        Long totalOrders = count();
        statistics.setTotalOrders(totalOrders);
        
        // 获取不同状态的订单数量
        LambdaQueryWrapper<SalesOrder> draftQuery = new LambdaQueryWrapper<>();
        draftQuery.eq(SalesOrder::getStatus, "0");
        Long draftOrders = count(draftQuery);
        statistics.setDraftOrders(draftOrders);
        
        LambdaQueryWrapper<SalesOrder> confirmedQuery = new LambdaQueryWrapper<>();
        confirmedQuery.eq(SalesOrder::getStatus, "1");
        Long confirmedOrders = count(confirmedQuery);
        statistics.setConfirmedOrders(confirmedOrders);
        
        LambdaQueryWrapper<SalesOrder> shippedQuery = new LambdaQueryWrapper<>();
        shippedQuery.eq(SalesOrder::getStatus, "2");
        Long shippedOrders = count(shippedQuery);
        statistics.setShippedOrders(shippedOrders);
        
        LambdaQueryWrapper<SalesOrder> completedQuery = new LambdaQueryWrapper<>();
        completedQuery.eq(SalesOrder::getStatus, "3");
        Long completedOrders = count(completedQuery);
        statistics.setCompletedOrders(completedOrders);
        
        LambdaQueryWrapper<SalesOrder> cancelledQuery = new LambdaQueryWrapper<>();
        cancelledQuery.eq(SalesOrder::getStatus, "4");
        Long cancelledOrders = count(cancelledQuery);
        statistics.setCancelledOrders(cancelledOrders);
        
        // 获取销售总金额
        List<Map<String, Object>> salesAmount = salesOrderMapper.getSalesAmount(dateRange);
        statistics.setSalesAmount(salesAmount);
        
        // 获取热销产品
        List<Map<String, Object>> hotProducts = salesOrderMapper.getHotProducts(params);
        statistics.setHotProducts(hotProducts);
        
        // 计算平均订单金额
        if (salesAmount != null && !salesAmount.isEmpty() && totalOrders > 0) {
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (Map<String, Object> amount : salesAmount) {
                if (amount.get("amount") != null) {
                    totalAmount = totalAmount.add(new BigDecimal(amount.get("amount").toString()));
                }
            }
            BigDecimal averageOrderAmount = totalAmount.divide(new BigDecimal(totalOrders), 2, RoundingMode.HALF_UP);
            statistics.setAverageOrderAmount(averageOrderAmount);
        }
        
        // 如果有产品类别参数，获取该类别的销售数据
        if (StringUtils.hasText(productCategory)) {
            List<Map<String, Object>> categoryStats = salesOrderMapper.getCategoryStats(productCategory, dateRange);
            statistics.setCategoryStats(categoryStats);
        }
        
        return statistics;
    }

    /**
     * 根据客户ID查询销售订单
     *
     * @param customerId 客户ID
     * @return 销售订单列表
     */
    @Override
    public List<SalesOrder> selectOrdersByCustomerId(Long customerId) {
        LambdaQueryWrapper<SalesOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalesOrder::getCustomerId, customerId);
        queryWrapper.orderByDesc(SalesOrder::getCreateTime);
        return list(queryWrapper);
    }
    
    /**
     * 获取移动端订单列表
     * @param params 查询参数
     * @return 移动端订单列表
     */
    @Override
    public List<MobileSalesOrderVO> getMobileOrderList(Map<String, Object> params) {
        // 获取查询参数
        String keyword = (String) params.get("keyword");
        String status = (String) params.get("status");
        Integer pageNum = (Integer) params.get("page");
        Integer pageSize = (Integer) params.get("size");
        
        // 构建查询条件
        LambdaQueryWrapper<SalesOrder> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(SalesOrder::getOrderNo, keyword)
                    .or().like(SalesOrder::getCustomerName, keyword);
        }
        if (StringUtils.hasText(status)) {
            queryWrapper.eq(SalesOrder::getStatus, status);
        }
        
        // 按创建时间降序排序
        queryWrapper.orderByDesc(SalesOrder::getCreateTime);
        
        // 执行分页查询
        Page<SalesOrder> page = new Page<>(pageNum, pageSize);
        Page<SalesOrder> orderPage = page(page, queryWrapper);
        
        // 转换为移动端VO对象
        List<MobileSalesOrderVO> result = new ArrayList<>();
        for (SalesOrder order : orderPage.getRecords()) {
            MobileSalesOrderVO vo = new MobileSalesOrderVO();
            BeanUtils.copyProperties(order, vo);
            
            // 设置订单时间（毫秒时间戳）
            if (order.getCreateTime() != null) {
                vo.setOrderTime(order.getCreateTime().getTime());
            }
            
            // 设置订单金额
            vo.setAmount(order.getTotalAmount());
            
            // 获取订单项（简化版）
            List<SalesOrderItem> items = salesOrderItemService.selectItemsByOrderId(order.getOrderId());
            if (items != null && !items.isEmpty()) {
                List<Map<String, Object>> orderItems = new ArrayList<>();
                for (SalesOrderItem item : items) {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("itemId", item.getItemId());
                    itemMap.put("productId", item.getProductId());
                    itemMap.put("productName", item.getProductName());
                    itemMap.put("quantity", item.getQuantity());
                    itemMap.put("price", item.getUnitPrice());
                    itemMap.put("amount", item.getSubtotal());
                    orderItems.add(itemMap);
                }
                vo.setOrderItems(orderItems);
            }
            
            result.add(vo);
        }
        
        return result;
    }
    
    /**
     * 获取移动端订单详情
     * @param orderId 订单ID
     * @return 移动端订单详情
     */
    @Override
    public MobileSalesOrderVO getMobileOrderDetail(Long orderId) {
        // 获取订单基本信息
        SalesOrder order = getById(orderId);
        if (order == null) {
            return null;
        }
        
        // 转换为移动端VO对象
        MobileSalesOrderVO vo = new MobileSalesOrderVO();
        BeanUtils.copyProperties(order, vo);
        
        // 设置订单时间（毫秒时间戳）
        if (order.getCreateTime() != null) {
            vo.setOrderTime(order.getCreateTime().getTime());
        }
        if (order.getExpectedDeliveryDate() != null) {
            vo.setExpectedDeliveryTime(order.getExpectedDeliveryDate().getTime());
        }
        if (order.getActualDeliveryDate() != null) {
            vo.setActualDeliveryTime(order.getActualDeliveryDate().getTime());
        }
        
        // 设置订单金额
        vo.setAmount(order.getTotalAmount());
        
        // 获取订单项
        List<SalesOrderItem> items = salesOrderItemService.selectItemsByOrderId(orderId);
        if (items != null && !items.isEmpty()) {
            List<Map<String, Object>> orderItems = new ArrayList<>();
            for (SalesOrderItem item : items) {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("itemId", item.getItemId());
                itemMap.put("productId", item.getProductId());
                itemMap.put("productName", item.getProductName());
                itemMap.put("quantity", item.getQuantity());
                itemMap.put("price", item.getUnitPrice());
                itemMap.put("amount", item.getSubtotal());
                orderItems.add(itemMap);
            }
            vo.setOrderItems(orderItems);
        }
        
        // 设置配送信息
        Map<String, Object> deliveryInfo = new HashMap<>();
        deliveryInfo.put("address", order.getShippingAddress());
        deliveryInfo.put("contact", order.getShippingContact());
        deliveryInfo.put("phone", order.getShippingPhone());
        deliveryInfo.put("status", order.getStatus()); // 使用订单状态作为配送状态
        vo.setDeliveryInfo(deliveryInfo);
        
        return vo;
    }
    
    /**
     * 移动端快速创建订单
     * @param orderVO 订单信息
     * @return 订单ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long quickAddOrder(MobileSalesOrderVO orderVO) {
        // 创建订单对象
        SalesOrder order = new SalesOrder();
        order.setCustomerId(orderVO.getCustomerId());
        
        // 获取客户信息
        Customer customer = customerMapper.selectById(orderVO.getCustomerId());
        if (customer != null) {
            order.setCustomerName(customer.getCustomerName());
        }
        
        // 生成订单编号
        String orderNo = generateOrderNo();
        order.setOrderNo(orderNo);
        
        // 设置订单状态为草稿
        order.setStatus("0");
        
        // 设置创建时间
        order.setCreateTime(new Date());
        
        // 设置备注
        order.setRemark(orderVO.getRemark());
        
        // 计算订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<Map<String, Object>> orderItems = orderVO.getOrderItems();
        List<SalesOrderItem> items = new ArrayList<>();
        
        if (orderItems != null && !orderItems.isEmpty()) {
            for (Map<String, Object> itemMap : orderItems) {
                SalesOrderItem item = new SalesOrderItem();
                
                // 设置产品信息
                Long productId = Long.valueOf(itemMap.get("productId").toString());
                item.setProductId(productId);
                
                // 获取产品信息
                Product product = productMapper.selectById(productId);
                if (product != null) {
                    item.setProductName(product.getProductName());
                    item.setProductCode(product.getProductCode());
                }
                
                // 设置数量和价格
                Integer quantity = Integer.valueOf(itemMap.get("quantity").toString());
                BigDecimal price = new BigDecimal(itemMap.get("price").toString());
                item.setQuantity(quantity);
                item.setUnitPrice(price);
                
                // 计算金额
                BigDecimal amount = price.multiply(new BigDecimal(quantity));
                item.setSubtotal(amount);
                
                // 累加总金额
                totalAmount = totalAmount.add(amount);
                
                items.add(item);
            }
        }
        
        order.setTotalAmount(totalAmount);
        
        // 保存订单主表信息
        save(order);
        
        // 保存订单明细
        if (!items.isEmpty()) {
            for (SalesOrderItem item : items) {
                item.setOrderId(order.getOrderId());
                item.setCreateTime(new Date());
            }
            salesOrderItemService.saveBatch(items);
        }
        
        return order.getOrderId();
    }
    
    /**
     * 获取今日销售额
     * @return 今日销售额
     */
    @Override
    public BigDecimal getTodaySales() {
        // 获取今天的开始时间和结束时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startTime = calendar.getTime();
        
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endTime = calendar.getTime();
        
        // 查询今日已完成的订单
        LambdaQueryWrapper<SalesOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(SalesOrder::getCreateTime, startTime, endTime)
                .in(SalesOrder::getStatus, Arrays.asList("3", "4")); // 假设3,4是已完成状态
        
        // 计算销售总额
        BigDecimal totalSales = BigDecimal.ZERO;
        List<SalesOrder> orders = list(queryWrapper);
        for (SalesOrder order : orders) {
            if (order.getTotalAmount() != null) {
                totalSales = totalSales.add(order.getTotalAmount());
            }
        }
        
        return totalSales;
    }
    
    /**
     * 获取待处理订单数
     * @return 待处理订单数
     */
    @Override
    public Long getPendingOrdersCount() {
        // 查询待处理的订单数量
        LambdaQueryWrapper<SalesOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SalesOrder::getStatus, Arrays.asList("0", "1", "2")); // 假设0,1,2是待处理状态
        
        return count(queryWrapper);
    }
    
    /**
     * 获取销售趋势数据
     *
     * @param params 查询参数，包含period（daily, weekly, monthly, yearly）
     * @return 销售趋势数据
     */
    @Override
    public Map<String, Object> getSalesTrend(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取时间周期参数
        String period = params.get("period") != null ? params.get("period").toString() : "monthly";
        
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();
        
        // 根据不同时间周期设置开始日期和格式化模式
        Date startDate;
        String formatPattern;
        String timeUnit;
        
        switch (period) {
            case "daily":
                // 过去30天的数据
                calendar.add(Calendar.DAY_OF_MONTH, -30);
                startDate = calendar.getTime();
                formatPattern = "yyyy-MM-dd";
                timeUnit = "天";
                break;
            case "weekly":
                // 过去12周的数据
                calendar.add(Calendar.WEEK_OF_YEAR, -12);
                startDate = calendar.getTime();
                formatPattern = "yyyy-'W'ww"; // 年份-周数
                timeUnit = "周";
                break;
            case "yearly":
                // 过去5年的数据
                calendar.add(Calendar.YEAR, -5);
                startDate = calendar.getTime();
                formatPattern = "yyyy";
                timeUnit = "年";
                break;
            case "monthly":
            default:
                // 过去12个月的数据
                calendar.add(Calendar.MONTH, -12);
                startDate = calendar.getTime();
                formatPattern = "yyyy-MM";
                timeUnit = "月";
                break;
        }
        
        // 构建查询条件
        LambdaQueryWrapper<SalesOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(SalesOrder::getCreateTime, startDate, endDate);
        queryWrapper.in(SalesOrder::getStatus, Arrays.asList("1", "2", "3")); // 只统计已确认、已发货和已完成的订单
        
        // 查询订单数据
        List<SalesOrder> orders = list(queryWrapper);
        
        // 按时间周期分组统计销售额
        Map<String, BigDecimal> salesByPeriod = new LinkedHashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatPattern);
        
        // 初始化时间周期，确保没有数据的时间段也显示为0
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTime(startDate);
        
        while (!tempCalendar.getTime().after(endDate)) {
            String periodKey = dateFormat.format(tempCalendar.getTime());
            salesByPeriod.put(periodKey, BigDecimal.ZERO);
            
            // 根据不同时间周期增加日期
            switch (period) {
                case "daily":
                    tempCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    break;
                case "weekly":
                    tempCalendar.add(Calendar.WEEK_OF_YEAR, 1);
                    break;
                case "yearly":
                    tempCalendar.add(Calendar.YEAR, 1);
                    break;
                case "monthly":
                default:
                    tempCalendar.add(Calendar.MONTH, 1);
                    break;
            }
        }
        
        // 统计各时间段的销售额
        for (SalesOrder order : orders) {
            if (order.getCreateTime() != null && order.getTotalAmount() != null) {
                String periodKey = dateFormat.format(order.getCreateTime());
                if (salesByPeriod.containsKey(periodKey)) {
                    BigDecimal currentAmount = salesByPeriod.get(periodKey);
                    salesByPeriod.put(periodKey, currentAmount.add(order.getTotalAmount()));
                }
            }
        }
        
        // 构建返回结果
        List<String> timeLabels = new ArrayList<>(salesByPeriod.keySet());
        List<BigDecimal> salesData = new ArrayList<>(salesByPeriod.values());
        
        result.put("timeLabels", timeLabels);
        result.put("salesData", salesData);
        result.put("timeUnit", timeUnit);
        result.put("period", period);
        
        // 计算总销售额和平均销售额
        BigDecimal totalSales = BigDecimal.ZERO;
        for (BigDecimal amount : salesData) {
            totalSales = totalSales.add(amount);
        }
        
        BigDecimal averageSales = BigDecimal.ZERO;
        if (!salesData.isEmpty()) {
            averageSales = totalSales.divide(new BigDecimal(salesData.size()), 2, RoundingMode.HALF_UP);
        }
        
        result.put("totalSales", totalSales);
        result.put("averageSales", averageSales);
        
        return result;
    }
    
    /**
     * 生成订单编号
     * 规则：SO + 年月日 + 6位随机数
     */
    private String generateOrderNo() {
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
        String day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        
        // 生成6位随机数
        Random random = new Random();
        int randomNum = random.nextInt(900000) + 100000;
        
        return "SO" + year + month + day + randomNum;
    }
}