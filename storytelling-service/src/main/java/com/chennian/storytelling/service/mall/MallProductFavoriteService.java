package com.chennian.storytelling.service.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.mall.MallProductFavorite;
import com.chennian.storytelling.common.utils.PageParam;

import java.util.List;
import java.util.Map;

/**
 * 商品收藏 Service接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
public interface MallProductFavoriteService extends IService<MallProductFavorite> {
    
    /**
     * 分页查询收藏列表
     * 
     * @param page 分页参数
     * @param favorite 查询条件
     * @return 收藏分页数据
     */
    IPage<MallProductFavorite> findByPage(PageParam<MallProductFavorite> page, MallProductFavorite favorite);
    
    /**
     * 添加商品收藏
     * 
     * @param userId 用户ID
     * @param productId 商品ID
     * @param groupId 收藏分组ID（可选）
     * @return 是否成功
     */
    boolean addFavorite(Long userId, Long productId, Long groupId);
    
    /**
     * 添加商品收藏
     * 
     * @param userId 用户ID
     * @param productId 商品ID
     * @param groupId 收藏分组ID（可选）
     * @param remark 备注
     * @return 是否成功
     */
    boolean addFavorite(Long userId, Long productId, Long groupId, String remark);
    
    /**
     * 取消商品收藏
     * 
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 是否成功
     */
    boolean removeFavorite(Long userId, Long productId);
    
    /**
     * 删除收藏（根据收藏ID）
     * 
     * @param favoriteId 收藏ID
     * @return 是否成功
     */
    boolean deleteFavorite(Long favoriteId);
    
    /**
     * 删除收藏（根据收藏ID）
     * 
     * @param favoriteId 收藏ID
     * @return 是否成功
     */
    boolean deleteFavoriteById(Long favoriteId);
    
    /**
     * 批量删除收藏
     * 
     * @param favoriteIds 收藏ID列表
     * @return 是否成功
     */
    boolean batchDeleteFavorites(List<Long> favoriteIds);
    
    /**
     * 检查是否已收藏商品
     * 
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 是否已收藏
     */
    boolean isFavorited(Long userId, Long productId);
    
    /**
     * 检查是否已收藏商品
     * 
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 是否已收藏
     */
    Boolean checkFavorite(Long userId, Long productId);
    
    /**
     * 批量检查是否已收藏商品
     * 
     * @param userId 用户ID
     * @param productIds 商品ID列表
     * @return 收藏状态映射，key为商品ID，value为是否已收藏
     */
    Map<Long, Boolean> batchCheckFavorited(Long userId, List<Long> productIds);
    
    /**
     * 批量检查是否已收藏商品
     * 
     * @param userId 用户ID
     * @param productIds 商品ID列表
     * @return 收藏状态映射，key为商品ID，value为是否已收藏
     */
    Map<Long, Boolean> batchCheckFavorites(Long userId, List<Long> productIds);
    
    /**
     * 获取用户收藏列表
     * 
     * @param userId 用户ID
     * @param groupId 收藏分组ID（可选）
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 收藏分页数据
     */
    IPage<MallProductFavorite> getUserFavorites(Long userId, Long groupId, Long pageNum, Long pageSize);
    
    /**
     * 获取用户收藏列表
     * 
     * @param userId 用户ID
     * @param page 分页参数
     * @param groupId 收藏分组ID（可选）
     * @return 收藏分页数据
     */
    IPage<MallProductFavorite> getUserFavorites(Long userId, PageParam<MallProductFavorite> page, Long groupId);
    
    /**
     * 获取用户收藏分组列表
     * 
     * @param userId 用户ID
     * @return 收藏分组列表
     */
    List<Map<String, Object>> getUserFavoriteGroups(Long userId);
    
    /**
     * 获取收藏分组列表
     * 
     * @param userId 用户ID
     * @return 收藏分组列表
     */
    List<Map<String, Object>> getFavoriteGroups(Long userId);
    
    /**
     * 创建收藏分组
     * 
     * @param userId 用户ID
     * @param groupName 分组名称
     * @return 分组ID
     */
    Long createFavoriteGroup(Long userId, String groupName);

    
    /**
     * 更新收藏分组
     * 
     * @param userId 用户ID
     * @param groupId 分组ID
     * @param groupName 分组名称
     * @return 是否成功
     */
    boolean updateFavoriteGroup(Long userId, Long groupId, String groupName);
    
    /**
     * 更新收藏分组
     * 
     * @param groupId 分组ID
     * @param groupName 分组名称
     * @return 是否成功
     */
    boolean updateFavoriteGroup(Long groupId, String groupName);
    
    /**
     * 删除收藏分组
     * 
     * @param userId 用户ID
     * @param groupId 分组ID
     * @return 是否成功
     */
    boolean deleteFavoriteGroup(Long userId, Long groupId);
    
    /**
     * 删除收藏分组
     * 
     * @param groupId 分组ID
     * @return 是否成功
     */
    boolean deleteFavoriteGroup(Long groupId);
    
    /**
     * 移动收藏到分组
     * 
     * @param favoriteIds 收藏ID列表
     * @param groupId 目标分组ID
     * @return 是否成功
     */
    boolean moveFavoritesToGroup(List<Long> favoriteIds, Long groupId);
    
    /**
     * 移动收藏到分组
     * 
     * @param favoriteIds 收藏ID列表
     * @param targetGroupId 目标分组ID
     * @return 是否成功
     */
    boolean moveToGroup(List<Long> favoriteIds, Long targetGroupId);
    
    /**
     * 更新收藏备注
     * 
     * @param favoriteId 收藏ID
     * @param remark 备注
     * @return 是否成功
     */
    boolean updateFavoriteRemark(Long favoriteId, String remark);
    
    /**
     * 更新收藏备注
     * 
     * @param favoriteId 收藏ID
     * @param remark 备注
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean updateFavoriteRemark(Long favoriteId, String remark, Long userId);
    
    /**
     * 获取用户收藏统计
     * 
     * @param userId 用户ID
     * @return 收藏统计数据
     */
    Map<String, Object> getUserFavoriteStats(Long userId);
    
    /**
     * 获取收藏统计
     * 
     * @param userId 用户ID
     * @return 收藏统计信息
     */
    Map<String, Object> getFavoriteStats(Long userId);
    
    /**
     * 获取热门收藏商品
     * 
     * @param limit 数量限制
     * @return 热门收藏商品列表
     */
    List<Map<String, Object>> getHotFavoriteProducts(Integer limit);
    
    /**
     * 获取热门收藏商品
     * 
     * @param page 分页参数
     * @return 热门收藏商品列表
     */
    IPage<Map<String, Object>> getHotFavoriteProducts(PageParam<Map<String, Object>> page);
    
    /**
     * 根据收藏推荐商品
     * 
     * @param userId 用户ID
     * @param limit 数量限制
     * @return 推荐商品列表
     */
    List<Map<String, Object>> getRecommendProductsByFavorites(Long userId, Integer limit);
    
    /**
     * 根据收藏获取推荐商品
     * 
     * @param userId 用户ID
     * @param page 分页参数
     * @return 推荐商品列表
     */
    IPage<Map<String, Object>> getRecommendationsByFavorites(Long userId, PageParam<Map<String, Object>> page);
    
    /**
     * 根据收藏推荐商品
     * 
     * @param userId 用户ID
     * @param limit 数量限制
     * @return 推荐商品列表
     */
    List<Map<String, Object>> getRecommendationsByFavorites(Long userId, Integer limit);
    
    /**
     * 导出用户收藏列表
     * 
     * @param userId 用户ID
     * @return 收藏列表数据
     */
    List<MallProductFavorite> exportUserFavorites(Long userId);
    
    /**
     * 导出用户收藏
     * 
     * @param userId 用户ID
     * @param groupId 分组ID（可选）
     * @return 导出数据
     */
    List<Map<String, Object>> exportUserFavorites(Long userId, Long groupId);
    
    /**
     * 导出收藏
     * 
     * @param userId 用户ID
     * @param groupId 分组ID（可选）
     * @return 导出数据
     */
    List<Map<String, Object>> exportFavorites(Long userId, Long groupId);
    
    /**
     * 导出用户收藏列表
     * 
     * @param userId 用户ID
     * @param format 导出格式（excel/csv）
     * @return 导出文件路径
     */
    String exportFavorites(Long userId, String format);
    
    /**
     * 清空用户收藏列表
     * 
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean clearUserFavorites(Long userId);
    
    /**
     * 获取商品收藏数量
     * 
     * @param productId 商品ID
     * @return 收藏数量
     */
    Long getProductFavoriteCount(Long productId);
    
    /**
     * 批量获取商品收藏数量
     * 
     * @param productIds 商品ID列表
     * @return 收藏数量映射，key为商品ID，value为收藏数量
     */
    Map<Long, Long> batchGetProductFavoriteCount(List<Long> productIds);
    
    /**
     * 同步收藏商品信息
     * 
     * @param productId 商品ID
     * @return 是否成功
     */
    boolean syncFavoriteProductInfo(Long productId);
}