package com.chennian.storytelling.service.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.mall.MallBrand;
import com.chennian.storytelling.bean.model.mall.MallProduct;
import com.chennian.storytelling.common.utils.PageParam;

import java.util.List;
import java.util.Map;

/**
 * 商城品牌Service接口
 * 
 * @author chennian
 * @date 2024-01-01
 */
public interface MallBrandService extends IService<MallBrand> {
    
    /**
     * 分页查询品牌列表
     * 
     * @param page 分页参数
     * @param mallBrand 查询条件
     * @return 品牌分页数据
     */
    IPage<MallBrand> findByPage(PageParam<MallBrand> page, MallBrand mallBrand);
    
    /**
     * 根据ID查询品牌详情
     * 
     * @param id 品牌ID
     * @return 品牌详情
     */
    MallBrand getBrandDetail(Long id);
    
    /**
     * 启用品牌
     * 
     * @param id 品牌ID
     * @return 操作结果
     */
    boolean enableBrand(Long id);
    
    /**
     * 禁用品牌
     * 
     * @param id 品牌ID
     * @return 操作结果
     */
    boolean disableBrand(Long id);
    
    /**
     * 获取推荐品牌列表
     * 
     * @param limit 限制数量
     * @return 推荐品牌列表
     */
    List<MallBrand> getRecommendBrands(Integer limit);
    
    /**
     * 根据品牌ID查询商品列表
     * 
     * @param brandId 品牌ID
     * @param page 分页参数
     * @return 商品分页数据
     */
    IPage<MallProduct> getProductsByBrand(Long brandId, PageParam<MallProduct> page);
    
    /**
     * 创建品牌
     * 
     * @param mallBrand 品牌信息
     * @return 操作结果
     */
    boolean createBrand(MallBrand mallBrand);
    
    /**
     * 更新品牌
     * 
     * @param mallBrand 品牌信息
     * @return 操作结果
     */
    boolean updateBrand(MallBrand mallBrand);
    
    /**
     * 删除品牌
     * 
     * @param id 品牌ID
     * @return 操作结果
     */
    boolean deleteBrand(Long id);
    
    /**
     * 获取品牌统计信息
     * 
     * @return 统计信息
     */
    Object getBrandStatistics();
    
    /**
     * 搜索品牌
     * 
     * @param keyword 搜索关键词
     * @param page 分页参数
     * @return 品牌分页数据
     */
    IPage<MallBrand> searchBrands(String keyword, PageParam<MallBrand> page);
    
    /**
     * 批量更新品牌状态
     * 
     * @param ids 品牌ID列表
     * @param status 状态
     * @return 操作结果
     */
    boolean batchUpdateStatus(List<Long> ids, Integer status);
    
    /**
     * 获取所有启用的品牌列表
     * 
     * @return 启用的品牌列表
     */
    List<MallBrand> getEnabledBrands();
    
    /**
     * 设置推荐品牌
     * 
     * @param brandId 品牌ID
     * @param isRecommend 是否推荐
     * @return 操作结果
     */
    boolean setRecommendBrand(Long brandId, Integer isRecommend);
    
    /**
     * 获取品牌统计信息（重载方法）
     * 
     * @param brandId 品牌ID
     * @return 统计信息
     */
    Map<String, Object> getBrandStatistics(Long brandId);
}