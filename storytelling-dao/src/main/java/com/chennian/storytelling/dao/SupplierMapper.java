package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.Supplier;
import org.apache.ibatis.annotations.Mapper;

/**
 * 供应商Mapper接口
 * @author chennian
 */
@Mapper
public interface SupplierMapper extends BaseMapper<Supplier> {
    
}