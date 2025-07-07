package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.InventoryAlert;

import java.util.List;

/**
 * 库存预警服务接口
 */
public interface InventoryAlertService extends IService<InventoryAlert> {

    /**
     * 分页查询库存预警
     *
     * @param pageNum     页码
     * @param pageSize    页大小
     * @param alertType   预警类型（可选）
     * @param alertLevel  预警级别（可选）
     * @param status      处理状态（可选）
     * @param warehouseId 仓库ID（可选）
     * @return 分页结果
     */
    IPage<InventoryAlert> getInventoryAlertPage(Integer pageNum, Integer pageSize, Integer alertType, Integer alertLevel, Integer status, Long warehouseId);

    /**
     * 获取未处理的预警列表
     *
     * @return 未处理预警列表
     */
    List<InventoryAlert> getUnhandledAlerts();

    /**
     * 获取高优先级预警列表
     *
     * @return 高优先级预警列表
     */
    List<InventoryAlert> getHighPriorityAlerts();

    /**
     * 检查库存并生成预警
     *
     * @param productId   产品ID（可选，为空则检查所有产品）
     * @param warehouseId 仓库ID（可选，为空则检查所有仓库）
     * @return 生成的预警数量
     */
    int checkInventoryAndGenerateAlerts(Long productId, Long warehouseId);

    /**
     * 创建库存预警
     *
     * @param productId           产品ID
     * @param warehouseId         仓库ID
     * @param currentStock        当前库存
     * @param minStockThreshold   最小库存阈值
     * @param maxStockThreshold   最大库存阈值
     * @param alertType           预警类型
     * @param alertLevel          预警级别
     * @return 是否成功
     */
    boolean createInventoryAlert(Long productId, Long warehouseId, Integer currentStock, Integer minStockThreshold, Integer maxStockThreshold, Integer alertType, Integer alertLevel);

    /**
     * 处理预警
     *
     * @param alertId      预警ID
     * @param handledBy    处理人
     * @param handledRemark 处理备注
     * @return 是否成功
     */
    boolean handleAlert(Long alertId, Long handledBy, String handledRemark);

    /**
     * 忽略预警
     *
     * @param alertId      预警ID
     * @param handledBy    处理人
     * @param handledRemark 处理备注
     * @return 是否成功
     */
    boolean ignoreAlert(Long alertId, Long handledBy, String handledRemark);

    /**
     * 批量处理预警
     *
     * @param alertIds     预警ID列表
     * @param handledBy    处理人
     * @param handledRemark 处理备注
     * @return 处理成功的数量
     */
    int batchHandleAlerts(List<Long> alertIds, Long handledBy, String handledRemark);

    /**
     * 获取预警统计信息
     *
     * @param warehouseId 仓库ID（可选）
     * @return 预警统计
     */
    Object getAlertStatistics(Long warehouseId);

    /**
     * 删除过期的已处理预警
     *
     * @param days 保留天数
     * @return 删除的数量
     */
    int cleanupExpiredAlerts(int days);

    /**
     * 发送预警通知
     *
     * @param alertId 预警ID
     * @return 是否成功
     */
    boolean sendAlertNotification(Long alertId);

    /**
     * 批量发送预警通知
     *
     * @param alertLevel 预警级别（可选，为空则发送所有未处理预警）
     * @return 发送成功的数量
     */
    int batchSendAlertNotifications(Integer alertLevel);
}