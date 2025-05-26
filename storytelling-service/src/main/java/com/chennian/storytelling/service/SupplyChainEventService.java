package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.vo.SupplyChainEventVO;

import java.util.List;
import java.util.Map;

/**
 * 供应链事件服务接口
 * 
 * @author chennian
 * @date 2023/12/15
 */
public interface SupplyChainEventService {
    
    /**
     * 获取最近的供应链事件
     * 
     * @param limit 限制数量
     * @return 供应链事件列表
     */
    List<SupplyChainEventVO> getRecentEvents(int limit);
    
    /**
     * 根据条件查询供应链事件
     * 
     * @param params 查询参数，包含eventType(事件类型)、dateRange(日期范围)、page(页码)、size(每页大小)
     * @return 供应链事件列表
     */
    List<SupplyChainEventVO> getEventsByCondition(Map<String, Object> params);
    
    /**
     * 创建供应链事件
     * 
     * @param event 供应链事件信息
     * @return 事件ID
     */
    Long createEvent(SupplyChainEventVO event);
    
    /**
     * 更新供应链事件状态
     * 
     * @param eventId 事件ID
     * @param status 状态
     * @param handler 处理人
     * @return 是否成功
     */
    boolean updateEventStatus(Long eventId, String status, String handler);
}