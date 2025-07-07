package com.chennian.storytelling.dao.mall;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.mall.DeliveryMethod;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 配送方式Mapper接口
 */
@Mapper
public interface DeliveryMethodMapper extends BaseMapper<DeliveryMethod> {

    /**
     * 分页查询配送方式
     */
    IPage<DeliveryMethod> selectDeliveryMethodPage(Page<DeliveryMethod> page, @Param("name") String name, @Param("status") Integer status);

    /**
     * 获取启用的配送方式列表
     */
    List<DeliveryMethod> selectEnabledDeliveryMethods();

    /**
     * 根据地区获取可用的配送方式
     */
    List<DeliveryMethod> selectAvailableByRegion(@Param("provinceCode") String provinceCode, @Param("cityCode") String cityCode);

    /**
     * 批量更新状态
     */
    int updateStatusBatch(@Param("ids") List<Long> ids, @Param("status") Integer status);
}