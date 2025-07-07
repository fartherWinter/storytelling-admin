package com.chennian.storytelling.dao.mall;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.mall.MallLogisticsTrackingDetail;
import com.chennian.storytelling.bean.vo.MallLogisticsTrackingDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 物流跟踪详情Mapper接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Mapper
public interface MallLogisticsTrackingDetailMapper extends BaseMapper<MallLogisticsTrackingDetail> {

    /**
     * 分页查询物流跟踪详情列表
     *
     * @param page 分页参数
     * @param trackingId 跟踪ID
     * @param logisticsNo 物流单号
     * @param operationType 操作类型
     * @param operationResult 操作结果
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 物流跟踪详情VO分页列表
     */
    IPage<MallLogisticsTrackingDetailVO> selectDetailPage(
            @Param("page") Page<MallLogisticsTrackingDetailVO> page,
            @Param("trackingId") Long trackingId,
            @Param("logisticsNo") String logisticsNo,
            @Param("operationType") Integer operationType,
            @Param("operationResult") Integer operationResult,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    /**
     * 根据跟踪ID查询物流跟踪详情列表
     *
     * @param trackingId 跟踪ID
     * @return 物流跟踪详情VO列表
     */
    List<MallLogisticsTrackingDetailVO> selectByTrackingId(@Param("trackingId") Long trackingId);

    /**
     * 根据物流单号查询物流跟踪详情列表
     *
     * @param logisticsNo 物流单号
     * @return 物流跟踪详情VO列表
     */
    List<MallLogisticsTrackingDetailVO> selectByLogisticsNo(@Param("logisticsNo") String logisticsNo);

    /**
     * 批量插入物流跟踪详情
     *
     * @param detailList 物流跟踪详情列表
     * @return 插入数量
     */
    int batchInsert(@Param("list") List<MallLogisticsTrackingDetail> detailList);

    /**
     * 查询异常物流跟踪详情列表
     *
     * @param trackingId 跟踪ID
     * @return 物流跟踪详情VO列表
     */
    List<MallLogisticsTrackingDetailVO> selectExceptionDetails(@Param("trackingId") Long trackingId);

    /**
     * 查询最新物流跟踪详情
     *
     * @param trackingId 跟踪ID
     * @return 物流跟踪详情VO
     */
    MallLogisticsTrackingDetailVO selectLatestDetail(@Param("trackingId") Long trackingId);

    /**
     * 查询指定时间范围内的物流跟踪详情统计
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计信息
     */
    List<MallLogisticsTrackingDetailVO> selectDetailStatistics(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    /**
     * 更新异常处理信息
     *
     * @param id 详情ID
     * @param exceptionReason 异常原因
     * @param handleSuggestion 处理建议
     * @param operationResult 操作结果
     * @return 更新数量
     */
    int updateExceptionInfo(
            @Param("id") Long id,
            @Param("exceptionReason") String exceptionReason,
            @Param("handleSuggestion") String handleSuggestion,
            @Param("operationResult") Integer operationResult
    );

    /**
     * 更新位置信息
     *
     * @param id 详情ID
     * @param location 位置信息
     * @param longitude 经度
     * @param latitude 纬度
     * @return 更新数量
     */
    int updateLocationInfo(
            @Param("id") Long id,
            @Param("location") String location,
            @Param("longitude") Double longitude,
            @Param("latitude") Double latitude
    );

    /**
     * 删除指定时间之前的物流跟踪详情
     *
     * @param beforeTime 指定时间
     * @return 删除数量
     */
    int deleteBeforeTime(@Param("beforeTime") LocalDateTime beforeTime);
} 