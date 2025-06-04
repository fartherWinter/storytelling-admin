package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.mall.MallSubOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商城子订单Mapper接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Mapper
public interface MallSubOrderMapper extends BaseMapper<MallSubOrder> {
    
}