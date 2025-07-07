package com.chennian.storytelling.dao.mapper.mall;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.mall.MallProductFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品收藏 Mapper接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Mapper
public interface MallProductFavoriteMapper extends BaseMapper<MallProductFavorite> {
    
    /**
     * 分页查询收藏列表
     * 
     * @param page 分页参数
     * @param favorite 查询条件
     * @return 收藏分页数据
     */
    IPage<MallProductFavorite> selectFavoritePage(Page<MallProductFavorite> page, @Param("favorite") MallProductFavorite favorite);
    
    /**
     * 检查是否已收藏商品
     * 
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 收藏数量
     */
    int checkFavorited(@Param("userId") Long userId, @Param("productId") Long productId);
    
    /**
     * 批量检查是否已收藏商品
     * 
     * @param userId 用户ID
     * @param productIds 商品ID列表
     * @return 已收藏的商品ID列表
     */
    List<Long> batchCheckFavorited(@Param("userId") Long userId, @Param("productIds") List<Long> productIds);
    
    /**
     * 获取用户收藏列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param groupId 收藏分组ID
     * @return 收藏分页数据
     */
    IPage<MallProductFavorite> selectUserFavorites(Page<MallProductFavorite> page, 
                                                    @Param("userId") Long userId,
                                                    @Param("groupId") Long groupId);
    
    /**
     * 获取用户收藏分组列表
     * 
     * @param userId 用户ID
     * @return 收藏分组列表
     */
    List<Map<String, Object>> selectUserFavoriteGroups(@Param("userId") Long userId);
    
    /**
     * 检查分组名称是否存在
     * 
     * @param userId 用户ID
     * @param groupName 分组名称
     * @return 分组数量
     */
    int checkGroupNameExists(@Param("userId") Long userId, @Param("groupName") String groupName);
    
    /**
     * 创建收藏分组
     * 
     * @param userId 用户ID
     * @param groupName 分组名称
     * @return 插入数量
     */
    int insertFavoriteGroup(@Param("userId") Long userId, @Param("groupName") String groupName);
    
    /**
     * 获取最新创建的分组ID
     * 
     * @param userId 用户ID
     * @param groupName 分组名称
     * @return 分组ID
     */
    Long selectLatestGroupId(@Param("userId") Long userId, @Param("groupName") String groupName);
    
    /**
     * 更新收藏分组
     * 
     * @param userId 用户ID
     * @param groupId 分组ID
     * @param groupName 分组名称
     * @return 更新数量
     */
    int updateFavoriteGroup(@Param("userId") Long userId, @Param("groupId") Long groupId, @Param("groupName") String groupName);
    
    /**
     * 删除收藏分组
     * 
     * @param userId 用户ID
     * @param groupId 分组ID
     * @return 删除数量
     */
    int deleteFavoriteGroup(@Param("userId") Long userId, @Param("groupId") Long groupId);
    
    /**
     * 移动收藏到分组
     * 
     * @param favoriteIds 收藏ID列表
     * @param groupId 目标分组ID
     * @param groupName 分组名称
     * @return 更新数量
     */
    int moveFavoritesToGroup(@Param("favoriteIds") List<Long> favoriteIds, 
                             @Param("groupId") Long groupId,
                             @Param("groupName") String groupName);
    
    /**
     * 更新收藏备注
     * 
     * @param favoriteId 收藏ID
     * @param remark 备注
     * @return 更新数量
     */
    int updateFavoriteRemark(@Param("favoriteId") Long favoriteId, @Param("remark") String remark);
    
    /**
     * 获取用户收藏统计
     * 
     * @param userId 用户ID
     * @return 收藏统计数据
     */
    Map<String, Object> selectUserFavoriteStats(@Param("userId") Long userId);
    
    /**
     * 获取热门收藏商品
     * 
     * @param limit 数量限制
     * @return 热门收藏商品列表
     */
    List<Map<String, Object>> selectHotFavoriteProducts(@Param("limit") Integer limit);
    
    /**
     * 根据收藏推荐商品
     * 
     * @param userId 用户ID
     * @param limit 数量限制
     * @return 推荐商品列表
     */
    List<Map<String, Object>> selectRecommendProductsByFavorites(@Param("userId") Long userId, @Param("limit") Integer limit);
    
    /**
     * 导出用户收藏列表
     * 
     * @param userId 用户ID
     * @return 收藏列表数据
     */
    List<MallProductFavorite> selectUserFavoritesForExport(@Param("userId") Long userId);
    
    /**
     * 清空用户收藏列表
     * 
     * @param userId 用户ID
     * @return 删除数量
     */
    int clearUserFavorites(@Param("userId") Long userId);
    
    /**
     * 获取商品收藏数量
     * 
     * @param productId 商品ID
     * @return 收藏数量
     */
    Long selectProductFavoriteCount(@Param("productId") Long productId);
    
    /**
     * 批量获取商品收藏数量
     * 
     * @param productIds 商品ID列表
     * @return 收藏数量列表
     */
    List<Map<String, Object>> batchSelectProductFavoriteCount(@Param("productIds") List<Long> productIds);
    
    /**
     * 同步收藏商品信息
     * 
     * @param productId 商品ID
     * @param productName 商品名称
     * @param productImage 商品图片
     * @param productPrice 商品价格
     * @return 更新数量
     */
    int syncFavoriteProductInfo(@Param("productId") Long productId,
                                @Param("productName") String productName,
                                @Param("productImage") String productImage,
                                @Param("productPrice") java.math.BigDecimal productPrice);
    
    /**
     * 根据商品ID删除收藏
     * 
     * @param productId 商品ID
     * @return 删除数量
     */
    int deleteFavoritesByProductId(@Param("productId") Long productId);
    
    /**
     * 根据分组ID清空收藏
     * 
     * @param groupId 分组ID
     * @return 更新数量
     */
    int clearFavoritesByGroupId(@Param("groupId") Long groupId);
    
    /**
     * 根据收藏获取推荐商品（分页）
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @return 推荐商品分页数据
     */
    IPage<Map<String, Object>> selectRecommendationsByFavorites(Page<Map<String, Object>> page, @Param("userId") Long userId);
    
    /**
     * 根据收藏获取推荐商品（限制数量）
     * 
     * @param userId 用户ID
     * @param limit 数量限制
     * @param offset 偏移量
     * @return 推荐商品列表
     */
    List<Map<String, Object>> selectRecommendationsByFavorites(@Param("userId") Long userId, @Param("limit") Integer limit, @Param("offset") Integer offset);
    
    /**
     * 导出收藏
     * 
     * @param userId 用户ID
     * @param groupId 分组ID（可选）
     * @return 导出数据
     */
    List<Map<String, Object>> selectFavoritesForExport(@Param("userId") Long userId, @Param("groupId") Long groupId);
    
    /**
     * 根据分组名称获取分组ID
     * 
     * @param groupId 分组ID
     * @return 分组名称
     */
    String selectGroupNameById(@Param("groupId") Long groupId);
}