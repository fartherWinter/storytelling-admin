package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.Customer;
import com.chennian.storytelling.bean.model.SalesOrder;
import com.chennian.storytelling.bean.model.SalesOrderItem;
import com.chennian.storytelling.bean.vo.CustomerFollowupVO;
import com.chennian.storytelling.bean.vo.CustomerStatisticsVO;
import com.chennian.storytelling.bean.vo.MobileCustomerVO;
import com.chennian.storytelling.common.enums.CustomerFollowupTypeEnum;
import com.chennian.storytelling.common.enums.CustomerLevelEnum;
import com.chennian.storytelling.common.enums.CustomerRegionEnum;
import com.chennian.storytelling.common.enums.CustomerTypeEnum;
import com.chennian.storytelling.common.enums.MonthEnum;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.CustomerMapper;
import com.chennian.storytelling.dao.SalesOrderMapper;
import com.chennian.storytelling.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 客户服务实现类
 * @author chennian
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Autowired
    private SalesOrderMapper salesOrderMapper;

    @Autowired
    private CustomerMapper customerMapper;

    /**
     * 分页查询客户列表
     * @param page 分页参数
     * @param customer 查询条件
     * @return 客户分页数据
     */
    @Override
    public IPage<Customer> findByPage(PageParam<Customer> page, Customer customer) {
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        if (customer != null) {
            // 根据客户编码模糊查询
            if (StringUtils.hasText(customer.getCustomerCode())) {
                queryWrapper.like(Customer::getCustomerCode, customer.getCustomerCode());
            }
            // 根据客户名称模糊查询
            if (StringUtils.hasText(customer.getCustomerName())) {
                queryWrapper.like(Customer::getCustomerName, customer.getCustomerName());
            }
            // 根据客户类型精确查询
            if (StringUtils.hasText(customer.getCustomerType())) {
                queryWrapper.eq(Customer::getCustomerType, customer.getCustomerType());
            }
            // 根据状态精确查询
            if (StringUtils.hasText(customer.getStatus())) {
                queryWrapper.eq(Customer::getStatus, customer.getStatus());
            }
        }
        // 按创建时间降序排序
        queryWrapper.orderByDesc(Customer::getCreateTime);
        return page(page, queryWrapper);
    }

    /**
     * 根据ID查询客户
     * @param customerId 客户ID
     * @return 客户信息
     */
    @Override
    public Customer selectCustomerById(Long customerId) {
        return getById(customerId);
    }

    /**
     * 新增客户
     * @param customer 客户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertCustomer(Customer customer) {
        return save(customer) ? 1 : 0;
    }

    /**
     * 修改客户
     * @param customer 客户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCustomer(Customer customer) {
        return updateById(customer) ? 1 : 0;
    }

    /**
     * 批量删除客户
     * @param customerIds 需要删除的客户ID数组
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCustomerByIds(Long[] customerIds) {
        return customerMapper.deleteByIds(Arrays.asList(customerIds));
    }

    /**
     * 更新客户状态
     * @param customer 客户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCustomerStatus(Customer customer) {
        Customer updateCustomer = new Customer();
        updateCustomer.setCustomerId(customer.getCustomerId());
        updateCustomer.setStatus(customer.getStatus());
        return updateById(updateCustomer) ? 1 : 0;
    }

    /**
     * 获取客户统计数据
     * @return 统计数据
     */
    @Override
    public CustomerStatisticsVO getCustomerStatistics() {
        CustomerStatisticsVO statistics = new CustomerStatisticsVO();
        
        // 客户总数
        statistics.setTotalCustomers(count());
        
        // 本月新增客户数（模拟数据）
        statistics.setNewCustomersThisMonth(15);
        
        // 活跃客户数（模拟数据）
        statistics.setActiveCustomers(42);
        
        // 客户类型分布
        Map<String, Integer> typeDistribution = new HashMap<>();
        for (CustomerTypeEnum type : CustomerTypeEnum.values()) {
            typeDistribution.put(type.getName(), CustomerTypeEnum.getDistributionCount(type));
        }
        statistics.setCustomerTypeDistribution(typeDistribution);
        
        // 客户等级分布
        Map<String, Integer> levelDistribution = new HashMap<>();
        for (CustomerLevelEnum level : CustomerLevelEnum.values()) {
            levelDistribution.put(level.getName(), CustomerLevelEnum.getDistributionCount(level));
        }
        statistics.setCustomerLevelDistribution(levelDistribution);
        
        // 客户地区分布
        Map<String, Integer> regionDistribution = new HashMap<>();
        for (CustomerRegionEnum region : CustomerRegionEnum.values()) {
            regionDistribution.put(region.getName(), CustomerRegionEnum.getDistributionCount(region));
        }
        statistics.setCustomerRegionDistribution(regionDistribution);
        
        // 客户消费金额统计（模拟数据）
        statistics.setTotalSalesAmount(new BigDecimal("1250000.00"));
        
        // 月度客户增长趋势
        List<Map<String, Object>> growthTrend = new ArrayList<>();
        for (MonthEnum month : MonthEnum.values()) {
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", month.getName());
            monthData.put("count", month.getGrowthCount());
            growthTrend.add(monthData);
        }
        statistics.setMonthlyGrowthTrend(growthTrend);
        
        // 客户跟进统计
        Map<String, Integer> followupStats = new HashMap<>();
        for (CustomerFollowupTypeEnum type : CustomerFollowupTypeEnum.values()) {
            followupStats.put(type.getName(), CustomerFollowupTypeEnum.getDistributionCount(type));
        }
        statistics.setFollowupStatistics(followupStats);
        
        return statistics;
    }

    /**
     * 获取客户分布数据
     * @return 客户分布数据
     */
    @Override
    public Map<String, Object> getCustomerDistribution() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取地区分布数据
        List<Map<String, Object>> regionData = new ArrayList<>();
        int totalCustomers = 0;
        
        // 遍历所有地区枚举，获取各地区客户数量
        for (CustomerRegionEnum region : CustomerRegionEnum.values()) {
            Map<String, Object> regionItem = new HashMap<>();
            int count = CustomerRegionEnum.getDistributionCount(region);
            
            regionItem.put("name", region.getName());
            regionItem.put("code", region.getCode());
            regionItem.put("value", count);
            regionItem.put("description", region.getDescription());
            
            regionData.add(regionItem);
            totalCustomers += count;
        }
        
        // 计算各地区占比
        for (Map<String, Object> region : regionData) {
            int value = (int) region.get("value");
            double percentage = totalCustomers > 0 ? (double) value / totalCustomers * 100 : 0;
            // 保留两位小数
            region.put("percentage", Math.round(percentage * 100) / 100.0);
        }
        
        // 按客户数量降序排序
        regionData.sort((a, b) -> Integer.compare((int) b.get("value"), (int) a.get("value")));
        
        result.put("regions", regionData);
        result.put("total", totalCustomers);
        
        return result;
    }
    
    /**
     * 获取客户跟进记录
     * @param customerId 客户ID
     * @return 跟进记录列表
     */
    @Override
    public List<CustomerFollowupVO> getFollowupRecords(Long customerId) {
        // 模拟数据，实际应该从数据库查询
        List<CustomerFollowupVO> records = new ArrayList<>();
        
        // 获取客户信息
        Customer customer = getById(customerId);
        if (customer == null) {
            return records;
        }
        
        // 模拟几条跟进记录
        CustomerFollowupVO record1 = new CustomerFollowupVO();
        record1.setFollowupId(1L);
        record1.setCustomerId(customerId);
        record1.setCustomerName(customer.getCustomerName());
        // 电话
        record1.setFollowupType("1");
        record1.setFollowupContent("询问产品使用情况");
        record1.setFollowupResult("客户反馈良好，有新需求");
        record1.setFollowupPerson("张三");
        record1.setFollowupTime(new Date());
        // 一周后
        record1.setNextFollowupTime(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000));
        record1.setRemark("需要准备新方案");
        record1.setCreateBy("admin");
        record1.setCreateTime(new Date());
        records.add(record1);
        
        CustomerFollowupVO record2 = new CustomerFollowupVO();
        record2.setFollowupId(2L);
        record2.setCustomerId(customerId);
        record2.setCustomerName(customer.getCustomerName());
        // 拜访
        record2.setFollowupType("3");
        record2.setFollowupContent("上门拜访，介绍新产品");
        record2.setFollowupResult("客户对新产品很感兴趣");
        record2.setFollowupPerson("李四");
        // 15天前
        record2.setFollowupTime(new Date(System.currentTimeMillis() - 15 * 24 * 60 * 60 * 1000));
        // 两周后
        record2.setNextFollowupTime(new Date(System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000));
        record2.setRemark("准备详细方案和报价");
        record2.setCreateBy("admin");
        record2.setCreateTime(new Date(System.currentTimeMillis() - 15 * 24 * 60 * 60 * 1000));
        records.add(record2);
        
        return records;
    }

    /**
     * 添加客户跟进记录
     * @param followup 跟进信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addFollowupRecord(Map<String, Object> followup) {
        // 实际应该将Map转换为实体对象并保存到数据库
        // 这里模拟成功添加
        return 1;
    }
    
    /**
     * 获取移动端客户列表
     * @param params 查询参数，包含keyword、page、size
     * @return 移动端客户列表
     */
    @Override
    public List<MobileCustomerVO> getMobileCustomerList(Map<String, Object> params) {
        // 获取查询参数
        String keyword = (String) params.get("keyword");
        Integer pageNum = (Integer) params.get("page");
        Integer pageSize = (Integer) params.get("size");
        
        // 构建查询条件
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(Customer::getCustomerName, keyword)
                    .or().like(Customer::getContactPerson, keyword)
                    .or().like(Customer::getContactPhone, keyword);
        }
        
        // 按创建时间降序排序
        queryWrapper.orderByDesc(Customer::getCreateTime);
        
        // 执行分页查询
        Page<Customer> page = new Page<>(pageNum, pageSize);
        Page<Customer> customerPage = page(page, queryWrapper);
        
        // 转换为移动端VO对象
        List<MobileCustomerVO> result = new ArrayList<>();
        for (Customer customer : customerPage.getRecords()) {
            MobileCustomerVO vo = new MobileCustomerVO();
            BeanUtils.copyProperties(customer, vo);
            
            // 获取最近订单
            LambdaQueryWrapper<SalesOrder> orderWrapper = new LambdaQueryWrapper<>();
            orderWrapper.eq(SalesOrder::getCustomerId, customer.getCustomerId())
                    .orderByDesc(SalesOrder::getCreateTime)
                    .last("LIMIT 1");
            SalesOrder recentOrder = salesOrderMapper.selectOne(orderWrapper);
            
            if (recentOrder != null) {
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("orderId", recentOrder.getOrderId());
                orderMap.put("orderNo", recentOrder.getOrderNo());
                orderMap.put("amount", recentOrder.getTotalAmount());
                orderMap.put("status", recentOrder.getStatus());
                orderMap.put("createTime", recentOrder.getCreateTime());
                vo.setRecentOrder(orderMap);
            }
            
            // 获取未结订单数
            LambdaQueryWrapper<SalesOrder> pendingWrapper = new LambdaQueryWrapper<>();
            pendingWrapper.eq(SalesOrder::getCustomerId, customer.getCustomerId())
                    .in(SalesOrder::getStatus, Arrays.asList("0", "1", "2")); // 假设0,1,2是未完成状态
            Long pendingCount = salesOrderMapper.selectCount(pendingWrapper);
            vo.setPendingOrders(pendingCount);
            
            // 设置客户等级
            vo.setLevel(customer.getCustomerLevel());
            
            result.add(vo);
        }
        
        return result;
    }
    
    /**
     * 获取移动端客户详情
     * @param customerId 客户ID
     * @return 移动端客户详情
     */
    @Override
    public MobileCustomerVO getMobileCustomerDetail(Long customerId) {
        // 获取客户基本信息
        Customer customer = getById(customerId);
        if (customer == null) {
            return null;
        }
        
        // 转换为移动端VO对象
        MobileCustomerVO vo = new MobileCustomerVO();
        BeanUtils.copyProperties(customer, vo);
        
        // 获取最近订单
        LambdaQueryWrapper<SalesOrder> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(SalesOrder::getCustomerId, customerId)
                .orderByDesc(SalesOrder::getCreateTime)
                .last("LIMIT 1");
        SalesOrder recentOrder = salesOrderMapper.selectOne(orderWrapper);
        
        if (recentOrder != null) {
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("orderId", recentOrder.getOrderId());
            orderMap.put("orderNo", recentOrder.getOrderNo());
            orderMap.put("amount", recentOrder.getTotalAmount());
            orderMap.put("status", recentOrder.getStatus());
            orderMap.put("createTime", recentOrder.getCreateTime());
            vo.setRecentOrder(orderMap);
        }
        
        // 获取未结订单数
        LambdaQueryWrapper<SalesOrder> pendingWrapper = new LambdaQueryWrapper<>();
        pendingWrapper.eq(SalesOrder::getCustomerId, customerId)
                .in(SalesOrder::getStatus, Arrays.asList("0", "1", "2")); // 假设0,1,2是未完成状态
        Long pendingCount = salesOrderMapper.selectCount(pendingWrapper);
        vo.setPendingOrders(pendingCount);
        
        // 设置客户等级
        vo.setLevel(customer.getLevel());
        
        // 获取最近跟进记录
        List<CustomerFollowupVO> followups = getFollowupRecords(customerId);
        if (followups != null && !followups.isEmpty()) {
            List<Map<String, Object>> recentFollowups = followups.stream()
                    .limit(3) // 只取最近3条
                    .map(f -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", f.getFollowupId());
                        map.put("type", f.getFollowupType());
                        map.put("content", f.getFollowupContent());
                        map.put("createTime", f.getCreateTime());
                        map.put("creator", f.getCreateBy());
                        return map;
                    })
                    .collect(Collectors.toList());
            vo.setRecentFollowups(recentFollowups);
        }
        
        return vo;
    }

    
    /**
     * 获取最近客户活动
     * @param limit 限制数量
     * @return 最近活动列表
     */
    @Override
    public List<Map<String, Object>> getRecentActivities(int limit) {
        List<Map<String, Object>> activities = new ArrayList<>();
        
        // 获取最近添加的客户
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Customer::getCreateTime)
                .last("LIMIT " + limit);
        List<Customer> recentCustomers = list(queryWrapper);
        
        // 转换为活动记录
        for (Customer customer : recentCustomers) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("type", "customer_add");
            activity.put("customerId", customer.getCustomerId());
            activity.put("customerName", customer.getCustomerName());
            activity.put("time", customer.getCreateTime());
            activity.put("description", "新增客户: " + customer.getCustomerName());
            
            activities.add(activity);
        }
        
        return activities;
    }

    /**
     * 获取活跃客户数量
     * @return 活跃客户数量
     */
    @Override
    public Integer getActiveCustomerCount() {
        // 查询最近30天内有订单的客户数量
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        Date thirtyDaysAgo = calendar.getTime();
        
        // 查询最近30天内有订单的客户ID列表
        LambdaQueryWrapper<SalesOrder> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.ge(SalesOrder::getCreateTime, thirtyDaysAgo);
        List<SalesOrder> recentOrders = salesOrderMapper.selectList(orderWrapper);
        
        // 统计不重复的客户ID数量
        Set<Long> activeCustomerIds = recentOrders.stream()
                .map(SalesOrder::getCustomerId)
                .collect(Collectors.toSet());
        
        return activeCustomerIds.size();
    }

    /**
     * 获取客户订单状态
     * @param customerId 客户ID
     * @return 客户订单状态信息
     */
    @Override
    public Object getCustomerOrderStatus(Long customerId) {
        Map<String, Object> result = new HashMap<>();
        
        // 验证客户是否存在
        Customer customer = getById(customerId);
        if (customer == null) {
            return null;
        }
        
        // 查询客户的所有订单
        LambdaQueryWrapper<SalesOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalesOrder::getCustomerId, customerId);
        List<SalesOrder> orders = salesOrderMapper.selectList(queryWrapper);
        
        // 统计各状态订单数量
        Map<String, Integer> statusCounts = new HashMap<>();
        // 初始化所有状态计数为0
        statusCounts.put("draft", 0);       // 草稿
        statusCounts.put("pending", 0);     // 待处理
        statusCounts.put("processing", 0);  // 处理中
        statusCounts.put("completed", 0);   // 已完成
        statusCounts.put("cancelled", 0);   // 已取消
        
        // 统计订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        // 最近一笔订单
        SalesOrder latestOrder = null;
        
        for (SalesOrder order : orders) {
            // 更新状态计数
            switch (order.getStatus()) {
                case "0":
                    statusCounts.put("draft", statusCounts.get("draft") + 1);
                    break;
                case "1":
                    statusCounts.put("pending", statusCounts.get("pending") + 1);
                    break;
                case "2":
                    statusCounts.put("processing", statusCounts.get("processing") + 1);
                    break;
                case "3":
                    statusCounts.put("completed", statusCounts.get("completed") + 1);
                    // 累计已完成订单金额
                    totalAmount = totalAmount.add(order.getTotalAmount());
                    break;
                case "4":
                    statusCounts.put("cancelled", statusCounts.get("cancelled") + 1);
                    break;
                default:
                    break;
            }
            
            // 更新最近订单
            if (latestOrder == null || order.getCreateTime().after(latestOrder.getCreateTime())) {
                latestOrder = order;
            }
        }
        
        // 构建结果
        result.put("customer", customer.getCustomerName());
        result.put("totalOrders", orders.size());
        result.put("statusCounts", statusCounts);
        result.put("totalAmount", totalAmount);
        
        // 添加最近订单信息
        if (latestOrder != null) {
            Map<String, Object> latestOrderInfo = new HashMap<>();
            latestOrderInfo.put("orderId", latestOrder.getOrderId());
            latestOrderInfo.put("orderNo", latestOrder.getOrderNo());
            latestOrderInfo.put("createTime", latestOrder.getCreateTime());
            latestOrderInfo.put("status", latestOrder.getStatus());
            latestOrderInfo.put("amount", latestOrder.getTotalAmount());
            result.put("latestOrder", latestOrderInfo);
        }
        
        return result;
    }

    /**
     * 获取产品推荐
     * @param customerId 客户ID
     * @return 推荐产品列表
     */
    @Override
    public Object getProductRecommendations(Long customerId) {
        // 验证客户是否存在
        Customer customer = getById(customerId);
        if (customer == null) {
            return null;
        }
        
        // 查询客户的历史订单
        LambdaQueryWrapper<SalesOrder> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(SalesOrder::getCustomerId, customerId);
        List<SalesOrder> customerOrders = salesOrderMapper.selectList(orderWrapper);
        
        // 获取客户历史购买的产品ID列表
        List<Long> purchasedProductIds = new ArrayList<>();
        for (SalesOrder order : customerOrders) {
            // 这里假设有一个方法可以获取订单中的产品ID列表
            // 实际实现中应该查询订单明细表获取产品ID
            List<SalesOrderItem> items = order.getItems();
            if (items != null) {
                for (SalesOrderItem item : items) {
                    purchasedProductIds.add(item.getProductId());
                }
            }
        }
        
        // 模拟基于客户历史购买记录的推荐算法
        // 实际项目中可能需要更复杂的推荐算法或调用专门的推荐服务
        List<Map<String, Object>> recommendations = new ArrayList<>();
        
        // 模拟5个推荐产品
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> product = new HashMap<>();
            product.put("productId", 100L + i);
            product.put("productName", "推荐产品" + i);
            product.put("price", new BigDecimal("99." + i));
            product.put("description", "基于您的购买历史推荐");
            product.put("matchScore", 95 - i * 5); // 模拟匹配度分数
            recommendations.add(product);
        }
        
        // 构建结果
        Map<String, Object> result = new HashMap<>();
        result.put("customerId", customerId);
        result.put("customerName", customer.getCustomerName());
        result.put("recommendations", recommendations);
        result.put("generatedTime", new Date());
        
        return result;
    }

    /**
     * 获取协作消息
     * @param customerId 客户ID
     * @return 协作消息列表
     */
    @Override
    public Object getCollaborationMessages(Long customerId) {
        // 验证客户是否存在
        Customer customer = getById(customerId);
        if (customer == null) {
            return null;
        }
        
        // 模拟协作消息数据
        // 实际项目中应该从消息表或协作系统中查询
        List<Map<String, Object>> messages = new ArrayList<>();
        
        // 模拟几条消息
        String[] senders = {"张三", "李四", "王五", "系统通知"};
        String[] contents = {
            "客户" + customer.getCustomerName() + "的合同已经准备好，请查收",
            "已经完成" + customer.getCustomerName() + "的需求分析",
            "客户反馈产品质量良好，考虑追加订单",
            "提醒：客户" + customer.getCustomerName() + "的回访计划已创建"
        };
        
        // 生成模拟消息
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 4; i++) {
            Map<String, Object> message = new HashMap<>();
            message.put("messageId", (long)(i + 1));
            message.put("customerId", customerId);
            message.put("sender", senders[i]);
            message.put("content", contents[i]);
            message.put("isRead", i < 2); // 前两条已读
            
            // 设置递减的时间
            calendar.add(Calendar.DAY_OF_MONTH, -i);
            message.put("createTime", calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, i); // 恢复时间
            
            messages.add(message);
        }
        
        // 构建结果
        Map<String, Object> result = new HashMap<>();
        result.put("customerId", customerId);
        result.put("customerName", customer.getCustomerName());
        result.put("totalMessages", messages.size());
        result.put("unreadMessages", messages.stream().filter(m -> !(boolean)m.get("isRead")).count());
        result.put("messages", messages);
        
        return result;
    }

    /**
     * 发送协作消息
     * @param customerId 客户ID
     * @param message 消息内容
     */
    @Override
    public void sendCollaborationMessage(Long customerId, Map<String, Object> message) {
        // 验证客户是否存在
        Customer customer = getById(customerId);
        if (customer == null) {
            throw new RuntimeException("客户不存在");
        }
        
        // 验证消息内容
        if (message == null || !message.containsKey("content") || !message.containsKey("sender")) {
            throw new RuntimeException("消息内容不完整");
        }
        
        // 在实际项目中，这里应该将消息保存到数据库
        // 并可能触发消息通知等操作
        
        // 模拟消息发送成功的日志
        String sender = (String) message.get("sender");
        String content = (String) message.get("content");
        System.out.println("发送协作消息成功 - 客户ID: " + customerId + ", 发送者: " + sender + ", 内容: " + content);
        
        // 如果有消息通知系统，这里可以调用发送通知的方法
        // notificationService.sendNotification(customerId, message);
    }


    /**
     * 根据条件查询客户列表
     * @param customer 查询条件
     * @return 客户列表
     */
    @Override
    public List<Customer> selectCustomerList(Customer customer) {
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        if (customer != null) {
            // 根据客户编码模糊查询
            if (StringUtils.hasText(customer.getCustomerCode())) {
                queryWrapper.like(Customer::getCustomerCode, customer.getCustomerCode());
            }
            // 根据客户名称模糊查询
            if (StringUtils.hasText(customer.getCustomerName())) {
                queryWrapper.like(Customer::getCustomerName, customer.getCustomerName());
            }
            // 根据客户类型精确查询
            if (StringUtils.hasText(customer.getCustomerType())) {
                queryWrapper.eq(Customer::getCustomerType, customer.getCustomerType());
            }
            // 根据状态精确查询
            if (StringUtils.hasText(customer.getStatus())) {
                queryWrapper.eq(Customer::getStatus, customer.getStatus());
            }
        }
        // 按创建时间降序排序
        queryWrapper.orderByDesc(Customer::getCreateTime);
        return list(queryWrapper);
    }
    
//    /**
//     * 获取移动端客户列表
//     * @param params 查询参数，包含keyword、page、size
//     * @return 移动端客户列表
//     */
//    @Override
//    public List<MobileCustomerVO> getMobileCustomerList(Map<String, Object> params) {
//        // 获取查询参数
//        String keyword = (String) params.get("keyword");
//        Integer page = (Integer) params.get("page");
//        Integer size = (Integer) params.get("size");
//
//        // 构建查询条件
//        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
//        if (StringUtils.hasText(keyword)) {
//            queryWrapper.like(Customer::getCustomerName, keyword)
//                    .or()
//                    .like(Customer::getContactPerson, keyword)
//                    .or()
//                    .like(Customer::getContactPhone, keyword);
//        }
//
//        // 按创建时间降序排序
//        queryWrapper.orderByDesc(Customer::getCreateTime);
//
//        // 分页查询
//        int current = (page - 1) * size;
//        queryWrapper.last("limit " + current + ", " + size);
//
//        // 查询客户列表
//        List<Customer> customers = list(queryWrapper);
//
//        // 转换为移动端VO
//        List<MobileCustomerVO> mobileCustomers = new ArrayList<>();
//        for (Customer customer : customers) {
//            MobileCustomerVO mobileCustomer = convertToMobileVO(customer);
//            mobileCustomers.add(mobileCustomer);
//        }
//
//        return mobileCustomers;
//    }
    
//    /**
//     * 获取移动端客户详情
//     * @param customerId 客户ID
//     * @return 移动端客户详情
//     */
//    @Override
//    public MobileCustomerVO getMobileCustomerDetail(Long customerId) {
//        // 查询客户信息
//        Customer customer = getById(customerId);
//        if (customer == null) {
//            return null;
//        }
//
//        // 转换为移动端VO并返回
//        return convertToMobileVO(customer);
//    }
    
    /**
     * 移动端快速添加客户
     * @param customerVO 客户信息
     * @return 客户ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long quickAddCustomer(MobileCustomerVO customerVO) {
        // 转换为客户实体
        Customer customer = new Customer();
        customer.setCustomerName(customerVO.getCustomerName());
        customer.setContactPerson(customerVO.getContactPerson());
        customer.setContactPhone(customerVO.getContactPhone());
        // 默认状态为正常
        customer.setStatus("1");
        
        // 设置其他默认值
        // 生成临时编码
        customer.setCustomerCode("C" + System.currentTimeMillis());
        // 默认类型
        customer.setCustomerType("1");
        // 默认等级
        customer.setLevel(customerVO.getLevel() != null ? customerVO.getLevel() : "3");
        customer.setCreateTime(new Date());
        // 标记为移动端创建
        customer.setCreateBy("mobile");
        
        // 保存客户信息
        save(customer);
        
        return customer.getCustomerId();
    }
    
    /**
     * 将客户实体转换为移动端VO
     * @param customer 客户实体
     * @return 移动端客户VO
     */
    private MobileCustomerVO convertToMobileVO(Customer customer) {
        MobileCustomerVO mobileCustomer = new MobileCustomerVO();
        mobileCustomer.setCustomerId(customer.getCustomerId());
        mobileCustomer.setCustomerName(customer.getCustomerName());
        mobileCustomer.setContactPerson(customer.getContactPerson());
        mobileCustomer.setContactPhone(customer.getContactPhone());
        mobileCustomer.setStatus(customer.getStatus());
        mobileCustomer.setLevel(customer.getLevel());
        
        // 设置最近订单（模拟数据）
        Map<String, Object> recentOrder = new HashMap<>();
        recentOrder.put("orderId", 1000L + customer.getCustomerId());
        recentOrder.put("orderNo", "SO" + (System.currentTimeMillis() / 1000));
        recentOrder.put("orderAmount", new BigDecimal("1000.00"));
        recentOrder.put("orderTime", new Date());
        mobileCustomer.setRecentOrder(recentOrder);
        
        // 设置未结订单数（模拟数据）
        mobileCustomer.setPendingOrders(2L);
        
        // 设置最近跟进记录（模拟数据）
        List<Map<String, Object>> recentFollowups = new ArrayList<>();
        Map<String, Object> followup = new HashMap<>();
        followup.put("followupId", 1L);
        followup.put("followupType", "电话");
        followup.put("followupContent", "询问产品使用情况");
        followup.put("followupTime", new Date());
        recentFollowups.add(followup);
        mobileCustomer.setRecentFollowups(recentFollowups);
        
        return mobileCustomer;
    }
}