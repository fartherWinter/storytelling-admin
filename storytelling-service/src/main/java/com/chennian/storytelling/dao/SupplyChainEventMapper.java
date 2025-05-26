package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.SupplyChainEvent;
import org.apache.ibatis.annotations.Mapper;

/**
 * 供应链事件Mapper接口
 * 
 * @author chennian
 * @date 2023/12/15
 */
@Mapper
public interface SupplyChainEventMapper extends BaseMapper<SupplyChainEvent> {
}