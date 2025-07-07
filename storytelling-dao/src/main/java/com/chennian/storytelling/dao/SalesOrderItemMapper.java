package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.SalesOrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 销售订单明细数据访问层
 * @author chennian
 * @date 2023/5/20
 */
@Mapper
public interface SalesOrderItemMapper extends BaseMapper<SalesOrderItem> {

}