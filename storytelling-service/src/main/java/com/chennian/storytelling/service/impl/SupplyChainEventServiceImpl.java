package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.SupplyChainEvent;
import com.chennian.storytelling.bean.vo.SupplyChainEventVO;
import com.chennian.storytelling.dao.SupplyChainEventMapper;
import com.chennian.storytelling.service.SupplyChainEventService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 供应链事件服务实现
 * 
 * @author chennian
 * @date 2023/12/15
 */
@Service
public class SupplyChainEventServiceImpl implements SupplyChainEventService {


    private final SupplyChainEventMapper supplyChainEventMapper;

    public SupplyChainEventServiceImpl(SupplyChainEventMapper supplyChainEventMapper) {
        this.supplyChainEventMapper = supplyChainEventMapper;
    }
    
    @Override
    public List<SupplyChainEventVO> getRecentEvents(int limit) {
        // 创建查询条件，按事件时间降序排序
        LambdaQueryWrapper<SupplyChainEvent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(SupplyChainEvent::getEventTime);
        
        // 创建分页对象
        Page<SupplyChainEvent> page = new Page<>(1, limit);
        
        // 执行查询
        IPage<SupplyChainEvent> eventPage = supplyChainEventMapper.selectPage(page, queryWrapper);
        
        // 转换为VO对象
        return convertToVOList(eventPage.getRecords());
    }
    
    @Override
    public List<SupplyChainEventVO> getEventsByCondition(Map<String, Object> params) {
        // 解析查询参数
        String eventType = (String) params.get("eventType");
        String dateRange = (String) params.get("dateRange");
        Integer pageNum = (Integer) params.get("page");
        Integer pageSize = (Integer) params.get("size");
        
        // 创建查询条件
        LambdaQueryWrapper<SupplyChainEvent> queryWrapper = new LambdaQueryWrapper<>();
        
        // 添加事件类型过滤条件
        if (eventType != null && !eventType.isEmpty()) {
            queryWrapper.eq(SupplyChainEvent::getEventType, eventType);
        }
        
        // 添加日期范围过滤条件
        if (dateRange != null && !dateRange.isEmpty()) {
            // 解析日期范围，格式如：2023-01-01,2023-12-31
            String[] dates = dateRange.split(",");
            if (dates.length == 2) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate startDate = LocalDate.parse(dates[0], formatter);
                LocalDate endDate = LocalDate.parse(dates[1], formatter);
                
                // 转换为毫秒时间戳
                long startTime = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                long endTime = endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1;
                
                queryWrapper.between(SupplyChainEvent::getEventTime, startTime, endTime);
            }
        }
        
        // 按事件时间降序排序
        queryWrapper.orderByDesc(SupplyChainEvent::getEventTime);
        
        // 创建分页对象
        Page<SupplyChainEvent> page = new Page<>(pageNum, pageSize);
        
        // 执行查询
        IPage<SupplyChainEvent> eventPage = supplyChainEventMapper.selectPage(page, queryWrapper);
        
        // 转换为VO对象
        return convertToVOList(eventPage.getRecords());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createEvent(SupplyChainEventVO eventVO) {
        // 创建实体对象
        SupplyChainEvent event = new SupplyChainEvent();
        BeanUtils.copyProperties(eventVO, event);
        
        // 设置事件时间（如果未设置）
        if (event.getEventTime() == null) {
            event.setEventTime(System.currentTimeMillis());
        }
        
        // 保存到数据库
        supplyChainEventMapper.insert(event);
        
        return event.getEventId();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateEventStatus(Long eventId, String status, String handler) {
        // 查询事件
        SupplyChainEvent event = supplyChainEventMapper.selectById(eventId);
        if (event == null) {
            return false;
        }
        
        // 更新状态
        event.setStatus(status);
        event.setHandler(handler);
        event.setHandleTime(System.currentTimeMillis());
        
        // 保存到数据库
        int rows = supplyChainEventMapper.updateById(event);
        
        return rows > 0;
    }
    
    /**
     * 将实体列表转换为VO列表
     */
    private List<SupplyChainEventVO> convertToVOList(List<SupplyChainEvent> events) {
        if (events == null || events.isEmpty()) {
            return new ArrayList<>();
        }
        
        return events.stream().map(event -> {
            SupplyChainEventVO vo = new SupplyChainEventVO();
            BeanUtils.copyProperties(event, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}