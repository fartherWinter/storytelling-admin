package com.chennian.storytelling.dao.mall;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.mall.MallDeliveryArea;
import com.chennian.storytelling.bean.vo.MallDeliveryAreaVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 物流区域Mapper接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
public interface MallDeliveryAreaMapper extends BaseMapper<MallDeliveryArea> {

    /**
     * 获取区域树形结构
     *
     * @param parentCode 父级区域编码
     * @return 区域树
     */
    List<MallDeliveryAreaVO> selectAreaTree(@Param("parentCode") String parentCode);

    /**
     * 批量更新区域状态
     *
     * @param areaCodes 区域编码列表
     * @param status 状态
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("areaCodes") List<String> areaCodes, @Param("status") Integer status);

    /**
     * 获取区域配送统计信息
     *
     * @param areaCode 区域编码
     * @return 统计信息
     */
    MallDeliveryAreaVO.AreaStatistics selectAreaStatistics(@Param("areaCode") String areaCode);

    /**
     * 查询指定区域的所有子区域
     *
     * @param areaCode 区域编码
     * @return 子区域列表
     */
    List<MallDeliveryArea> selectChildAreas(@Param("areaCode") String areaCode);

    /**
     * 获取区域完整路径
     *
     * @param areaCode 区域编码
     * @return 区域路径
     */
    String selectAreaPath(@Param("areaCode") String areaCode);

    /**
     * 查询配送范围内的区域
     *
     * @param longitude 经度
     * @param latitude 纬度
     * @param radius 半径（公里）
     * @return 区域列表
     */
    List<MallDeliveryArea> selectAreasInRange(
            @Param("longitude") Double longitude,
            @Param("latitude") Double latitude,
            @Param("radius") Double radius
    );

    /**
     * 批量插入区域
     *
     * @param areas 区域列表
     * @return 插入数量
     */
    int batchInsert(@Param("areas") List<MallDeliveryArea> areas);

    /**
     * 更新区域配送信息
     *
     * @param areaCode 区域编码
     * @param deliveryInfo 配送信息
     * @return 更新结果
     */
    int updateDeliveryInfo(
            @Param("areaCode") String areaCode,
            @Param("deliveryInfo") Map<String, Object> deliveryInfo
    );

    /**
     * 获取区域配送覆盖率统计
     *
     * @param areaLevel 区域级别
     * @return 覆盖率统计
     */
    List<Map<String, Object>> selectDeliveryCoverageStats(@Param("areaLevel") Integer areaLevel);

    /**
     * 查询特定配送方式支持的区域
     *
     * @param deliveryMethodId 配送方式ID
     * @return 支持的区域列表
     */
    List<MallDeliveryArea> selectAreasByDeliveryMethod(@Param("deliveryMethodId") Long deliveryMethodId);
} 