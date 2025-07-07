package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.PurchaseOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购订单Mapper接口
 * @author chennian
 * @date 2023/5/20
 */
@Mapper
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrder> {
    
}