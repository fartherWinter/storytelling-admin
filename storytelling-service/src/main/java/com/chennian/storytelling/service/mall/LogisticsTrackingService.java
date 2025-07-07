package com.chennian.storytelling.service.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.mall.LogisticsTracking;
import com.chennian.storytelling.bean.model.mall.LogisticsTrackingDetail;


import java.util.List;

/**
 * 物流跟踪服务接口
 * @author chen
 */
public interface LogisticsTrackingService extends IService<LogisticsTracking> {

    /**
     * 分页查询物流跟踪信息
     *
     * @param pageNum        页码
     * @param pageSize       页大小
     * @param orderSn        订单编号（可选）
     * @param trackingNumber 运单号（可选）
     * @param status         状态（可选）
     * @return 分页结果
     */
    IPage<LogisticsTracking> getLogisticsTrackingPage(Integer pageNum, Integer pageSize, String orderSn, String trackingNumber, Integer status, String logisticsCompanyCode);

    /**
     * 根据订单ID获取物流跟踪信息
     *
     * @param orderId 订单ID
     * @return 物流跟踪信息
     */
    LogisticsTracking getByOrderId(Long orderId);

    /**
     * 根据运单号获取物流跟踪信息
     *
     * @param trackingNumber 运单号
     * @return 物流跟踪信息
     */
    LogisticsTracking getByTrackingNumber(String trackingNumber);

    /**
     * 获取物流跟踪详情
     *
     * @param trackingId 物流跟踪ID
     * @return 物流跟踪详情列表
     */
    List<LogisticsTrackingDetail> getTrackingDetails(Long trackingId);

    /**
     * 创建物流跟踪记录
     *
     * @param orderId              订单ID
     * @param orderSn              订单编号
     * @param logisticsCompanyCode 物流公司编码
     * @param logisticsCompanyName 物流公司名称
     * @param trackingNumber       运单号
     * @return 是否成功
     */
    boolean createLogisticsTracking(Long orderId, String orderSn, String logisticsCompanyCode, String logisticsCompanyName, String trackingNumber);

    /**
     * 更新物流状态
     *
     * @param trackingId        物流跟踪ID
     * @param status            状态
     * @param statusDescription 状态描述
     * @param currentLocation   当前位置
     * @return 是否成功
     */
    boolean updateLogisticsStatus(Long trackingId, Integer status, String statusDescription, String currentLocation);

    /**
     * 添加物流跟踪详情
     *
     * @param trackingDetail 物流跟踪详情
     * @return 是否成功
     */
    boolean addTrackingDetail(LogisticsTrackingDetail trackingDetail);

    /**
     * 添加物流跟踪详情（别名方法）
     *
     * @param trackingDetail 物流跟踪详情
     * @return 是否成功
     */
    boolean addLogisticsTrackingDetail(LogisticsTrackingDetail trackingDetail);

    /**
     * 同步物流信息（从第三方物流API获取）
     *
     * @param trackingNumber 运单号
     * @return 是否成功
     */
    boolean syncLogisticsInfo(String trackingNumber);

    /**
     * 批量同步物流信息
     *
     * @param trackingNumbers 运单号列表
     * @return 成功同步的数量
     */
    int batchSyncLogisticsInfo(List<String> trackingNumbers);

    /**
     * 获取异常物流订单
     *
     * @return 异常物流订单列表
     */
    List<LogisticsTracking> getAbnormalLogistics();

    /**
     * 获取超时未送达订单
     *
     * @param hours 超时小时数
     * @return 超时订单列表
     */
    List<LogisticsTracking> getOverdueDeliveries(int hours);
}