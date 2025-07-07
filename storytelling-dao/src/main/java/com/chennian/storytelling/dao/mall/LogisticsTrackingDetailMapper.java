package com.chennian.storytelling.dao.mall;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.mall.LogisticsTrackingDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物流跟踪详情Mapper接口
 */
@Mapper
public interface LogisticsTrackingDetailMapper extends BaseMapper<LogisticsTrackingDetail> {

    /**
     * 根据物流跟踪ID查询详情列表
     */
    List<LogisticsTrackingDetail> selectByTrackingId(@Param("trackingId") Long trackingId);

    /**
     * 批量插入物流跟踪详情
     */
    int insertBatch(@Param("details") List<LogisticsTrackingDetail> details);

    /**
     * 根据物流跟踪ID删除详情
     */
    int deleteByTrackingId(@Param("trackingId") Long trackingId);

    /**
     * 获取最新的物流详情
     */
    LogisticsTrackingDetail selectLatestByTrackingId(@Param("trackingId") Long trackingId);
}