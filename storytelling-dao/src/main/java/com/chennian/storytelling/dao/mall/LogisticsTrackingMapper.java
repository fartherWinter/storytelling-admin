package com.chennian.storytelling.dao.mall;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.mall.LogisticsTracking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物流跟踪Mapper接口
 */
@Mapper
public interface LogisticsTrackingMapper extends BaseMapper<LogisticsTracking> {

    /**
     * 分页查询物流跟踪信息
     */
    IPage<LogisticsTracking> selectLogisticsTrackingPage(Page<LogisticsTracking> page, 
                                                        @Param("orderNumber") String orderNumber, 
                                                        @Param("trackingNumber") String trackingNumber,
                                                        @Param("status") Integer status);

    /**
     * 根据订单号查询物流跟踪信息
     */
    LogisticsTracking selectByOrderNumber(@Param("orderNumber") String orderNumber);

    /**
     * 根据物流单号查询物流跟踪信息
     */
    LogisticsTracking selectByTrackingNumber(@Param("trackingNumber") String trackingNumber);

    /**
     * 批量更新物流状态
     */
    int updateStatusBatch(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 根据物流公司查询跟踪信息
     */
    List<LogisticsTracking> selectByLogisticsCompany(@Param("logisticsCompany") String logisticsCompany);
}