package com.chennian.storytelling.service.impl.mall;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.mall.LogisticsTracking;
import com.chennian.storytelling.bean.model.mall.LogisticsTrackingDetail;
import com.chennian.storytelling.dao.mall.LogisticsTrackingDetailMapper;
import com.chennian.storytelling.dao.mall.LogisticsTrackingMapper;
import com.chennian.storytelling.service.mall.LogisticsTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 物流跟踪服务实现类
 */
@Service
public class LogisticsTrackingServiceImpl extends ServiceImpl<LogisticsTrackingMapper, LogisticsTracking> implements LogisticsTrackingService {

    @Autowired
    private LogisticsTrackingMapper logisticsTrackingMapper;
    
    @Autowired
    private LogisticsTrackingDetailMapper logisticsTrackingDetailMapper;

    @Override
    public IPage<LogisticsTracking> getLogisticsTrackingPage(Integer pageNum, Integer pageSize, String orderSn, String trackingNumber, Integer status, String logisticsCompanyCode) {
        LambdaQueryWrapper<LogisticsTracking> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(orderSn)) {
            queryWrapper.like(LogisticsTracking::getOrderSn, orderSn);
        }
        if (StringUtils.hasText(trackingNumber)) {
            queryWrapper.like(LogisticsTracking::getTrackingNumber, trackingNumber);
        }
        if (status != null) {
            queryWrapper.eq(LogisticsTracking::getStatus, status);
        }
        if (StringUtils.hasText(logisticsCompanyCode)) {
            queryWrapper.eq(LogisticsTracking::getLogisticsCompanyCode, logisticsCompanyCode);
        }
        
        queryWrapper.orderByDesc(LogisticsTracking::getCreateTime);

        return page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @Override
    public LogisticsTracking getByOrderId(Long orderId) {
        LambdaQueryWrapper<LogisticsTracking> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LogisticsTracking::getOrderId, orderId);
        return getOne(queryWrapper);
    }

    @Override
    public LogisticsTracking getByTrackingNumber(String trackingNumber) {
        LambdaQueryWrapper<LogisticsTracking> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LogisticsTracking::getTrackingNumber, trackingNumber);
        return getOne(queryWrapper);
    }

    @Override
    public List<LogisticsTrackingDetail> getTrackingDetails(Long trackingId) {
        LambdaQueryWrapper<LogisticsTrackingDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LogisticsTrackingDetail::getTrackingId, trackingId)
                   .orderByDesc(LogisticsTrackingDetail::getTrackTime);
        return logisticsTrackingDetailMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createLogisticsTracking(Long orderId, String orderSn, String logisticsCompanyCode, String logisticsCompanyName, String trackingNumber) {
        LogisticsTracking tracking = new LogisticsTracking();
        tracking.setOrderId(orderId);
        tracking.setOrderSn(orderSn);
        tracking.setLogisticsCompanyCode(logisticsCompanyCode);
        tracking.setLogisticsCompanyName(logisticsCompanyName);
        tracking.setTrackingNumber(trackingNumber);
        tracking.setStatus(0); // 待发货
        tracking.setStatusDescription("订单已创建，等待发货");
        tracking.setCreateTime(LocalDateTime.now());
        tracking.setUpdateTime(LocalDateTime.now());
        
        boolean saveResult = save(tracking);
        if (!saveResult) {
            return false;
        }
        
        // 创建初始跟踪详情
        LogisticsTrackingDetail detail = new LogisticsTrackingDetail();
        detail.setTrackingId(tracking.getId());
        detail.setTrackingNumber(trackingNumber);
        detail.setTrackTime(LocalDateTime.now());
        detail.setDescription("订单已创建，等待发货");
        detail.setOperationType(1); // 揽收
        detail.setCreateTime(LocalDateTime.now());
        
        return logisticsTrackingDetailMapper.insert(detail) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLogisticsStatus(Long trackingId, Integer status, String statusDescription, String currentLocation) {
        LogisticsTracking tracking = getById(trackingId);
        if (tracking == null) {
            return false;
        }
        
        tracking.setStatus(status);
        tracking.setStatusDescription(statusDescription);
        tracking.setCurrentLocation(currentLocation);
        tracking.setUpdateTime(LocalDateTime.now());
        
        // 如果是已签收状态，设置实际送达时间
        if (status == 4) {
            tracking.setActualDeliveryTime(LocalDateTime.now());
        }
        
        return updateById(tracking);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addTrackingDetail(LogisticsTrackingDetail trackingDetail) {
        trackingDetail.setCreateTime(LocalDateTime.now());
        return logisticsTrackingDetailMapper.insert(trackingDetail) > 0;
    }

    @Override
    public boolean syncLogisticsInfo(String trackingNumber) {
        // 这里应该调用第三方物流API获取最新的物流信息
        // 由于是示例代码，这里只是模拟实现
        LogisticsTracking tracking = getByTrackingNumber(trackingNumber);
        if (tracking == null) {
            return false;
        }
        
        try {
            // 模拟调用第三方API
            // LogisticsApiResponse response = logisticsApiClient.getTrackingInfo(trackingNumber);
            
            // 模拟更新物流状态
            updateLogisticsStatus(tracking.getId(), 2, "运输中", "北京分拣中心");
            
            // 模拟添加跟踪详情
            LogisticsTrackingDetail detail = new LogisticsTrackingDetail();
            detail.setTrackingId(tracking.getId());
            detail.setTrackingNumber(trackingNumber);
            detail.setTrackTime(LocalDateTime.now());
            detail.setDescription("快件已从北京分拣中心发出");
            detail.setLocation("北京分拣中心");
            detail.setOperationType(2); // 运输
            addTrackingDetail(detail);
            
            return true;
        } catch (Exception e) {
            // 记录日志
            return false;
        }
    }

    @Override
    public int batchSyncLogisticsInfo(List<String> trackingNumbers) {
        int successCount = 0;
        for (String trackingNumber : trackingNumbers) {
            if (syncLogisticsInfo(trackingNumber)) {
                successCount++;
            }
        }
        return successCount;
    }

    @Override
    public List<LogisticsTracking> getAbnormalLogistics() {
        LambdaQueryWrapper<LogisticsTracking> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LogisticsTracking::getStatus, 5) // 异常状态
                   .orderByDesc(LogisticsTracking::getUpdateTime);
        return list(queryWrapper);
    }

    @Override
    public List<LogisticsTracking> getOverdueDeliveries(int hours) {
        LocalDateTime overdueTime = LocalDateTime.now().minusHours(hours);
        
        LambdaQueryWrapper<LogisticsTracking> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(LogisticsTracking::getStatus, 1, 2, 3) // 已发货、运输中、派送中
                   .and(wrapper -> wrapper
                       .lt(LogisticsTracking::getEstimatedDeliveryTime, LocalDateTime.now())
                       .or()
                       .lt(LogisticsTracking::getCreateTime, overdueTime)
                   )
                   .orderByAsc(LogisticsTracking::getEstimatedDeliveryTime);
        
        return list(queryWrapper);
    }



    /**
     * 根据订单ID获取物流跟踪（控制器调用的方法名）
     */
    public LogisticsTracking getLogisticsTrackingByOrderId(Long orderId) {
        return getByOrderId(orderId);
    }

    /**
     * 根据运单号获取物流跟踪（控制器调用的方法名）
     */
    public LogisticsTracking getLogisticsTrackingByTrackingNumber(String trackingNumber) {
        return getByTrackingNumber(trackingNumber);
    }

    /**
     * 获取物流跟踪详情（控制器调用的方法名）
     */
    public List<LogisticsTrackingDetail> getLogisticsTrackingDetails(Long trackingId) {
        return getTrackingDetails(trackingId);
    }

    /**
     * 创建物流跟踪记录（控制器调用的方法名）
     */
    public boolean createLogisticsTrackingRecord(Long orderId, String orderSn, String logisticsCompanyCode, String logisticsCompanyName, String trackingNumber) {
        return createLogisticsTracking(orderId, orderSn, logisticsCompanyCode, logisticsCompanyName, trackingNumber);
    }

    /**
     * 更新物流状态（控制器调用的方法名）
     */
    public boolean updateLogisticsTrackingStatus(Long trackingId, Integer status, String statusDescription, String currentLocation) {
        return updateLogisticsStatus(trackingId, status, statusDescription, currentLocation);
    }

    /**
     * 添加物流跟踪详情（控制器调用的方法名）
     */
    @Override
    public boolean addLogisticsTrackingDetail(LogisticsTrackingDetail trackingDetail) {
        return addTrackingDetail(trackingDetail);
    }

    /**
     * 同步物流信息（控制器调用的方法名）
     */
    public boolean syncLogisticsTrackingInfo(String trackingNumber) {
        return syncLogisticsInfo(trackingNumber);
    }

    /**
     * 批量同步物流信息（控制器调用的方法名）
     */
    public int batchSyncLogisticsTrackingInfo(List<String> trackingNumbers) {
        return batchSyncLogisticsInfo(trackingNumbers);
    }

    /**
     * 获取异常物流订单（控制器调用的方法名）
     */
    public List<LogisticsTracking> getAbnormalLogisticsOrders() {
        return getAbnormalLogistics();
    }

    /**
     * 获取超时未送达订单（控制器调用的方法名）
     */
    public List<LogisticsTracking> getOverdueDeliveryOrders(Integer hours) {
        return getOverdueDeliveries(hours);
    }
}