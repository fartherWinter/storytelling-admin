package com.chennian.storytelling.service.mall.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.mall.MallProduct;
import com.chennian.storytelling.bean.model.mall.MallProductFavorite;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.mapper.mall.MallProductFavoriteMapper;
import com.chennian.storytelling.service.mall.MallProductFavoriteService;
import com.chennian.storytelling.service.mall.MallProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品收藏 Service实现类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Slf4j
@Service
public class MallProductFavoriteServiceImpl extends ServiceImpl<MallProductFavoriteMapper, MallProductFavorite> implements MallProductFavoriteService {
    
    @Autowired
    private MallProductFavoriteMapper mallProductFavoriteMapper;
    
    @Autowired
    private MallProductService mallProductService;
    
    @Override
    public IPage<MallProductFavorite> findByPage(PageParam<MallProductFavorite> page, MallProductFavorite favorite) {
        try {
            Page<MallProductFavorite> pageParam = new Page<>(page.getPageNum(), page.getPageSize());
            return mallProductFavoriteMapper.selectFavoritePage(pageParam, favorite);
        } catch (Exception e) {
            log.error("分页查询收藏列表失败", e);
            throw new RuntimeException("分页查询收藏列表失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addFavorite(Long userId, Long productId, Long groupId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            
            // 检查是否已收藏
            if (isFavorited(userId, productId)) {
                throw new IllegalArgumentException("商品已收藏，请勿重复收藏");
            }
            
            // 获取商品信息
            MallProduct product = mallProductService.getById(productId);
            if (product == null) {
                throw new IllegalArgumentException("商品不存在");
            }
            
            MallProductFavorite favorite = new MallProductFavorite();
            favorite.setUserId(userId);
            favorite.setProductId(productId);
            favorite.setProductName(product.getProductName());
            favorite.setProductImage(product.getMainImage());
            favorite.setProductPrice(product.getSalePrice());
            favorite.setGroupId(groupId);
            
            // 如果指定了分组，获取分组名称
            if (groupId != null) {
                // 从分组表获取分组名称
                String groupName = mallProductFavoriteMapper.selectGroupNameById(groupId);
                if (groupName != null) {
                    favorite.setGroupName(groupName);
                } else {
                    favorite.setGroupName("默认分组");
                }
            }
            
            favorite.setCreateTime(LocalDateTime.now());
            favorite.setUpdateTime(LocalDateTime.now());
            
            return save(favorite);
        } catch (Exception e) {
            log.error("添加商品收藏失败, userId: {}, productId: {}", userId, productId, e);
            throw new RuntimeException("添加商品收藏失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeFavorite(Long userId, Long productId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            
            QueryWrapper<MallProductFavorite> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .eq("product_id", productId);
            
            return remove(queryWrapper);
        } catch (Exception e) {
            log.error("取消商品收藏失败, userId: {}, productId: {}", userId, productId, e);
            throw new RuntimeException("取消商品收藏失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFavorite(Long favoriteId) {
        try {
            if (favoriteId == null) {
                throw new IllegalArgumentException("收藏ID不能为空");
            }
            return removeById(favoriteId);
        } catch (Exception e) {
            log.error("删除收藏失败, favoriteId: {}", favoriteId, e);
            throw new RuntimeException("删除收藏失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteFavorites(List<Long> favoriteIds) {
        try {
            if (favoriteIds == null || favoriteIds.isEmpty()) {
                throw new IllegalArgumentException("收藏ID列表不能为空");
            }
            return removeByIds(favoriteIds);
        } catch (Exception e) {
            log.error("批量删除收藏失败, favoriteIds: {}", favoriteIds, e);
            throw new RuntimeException("批量删除收藏失败: " + e.getMessage());
        }
    }
    
    @Override
    public boolean isFavorited(Long userId, Long productId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            
            int count = mallProductFavoriteMapper.checkFavorited(userId, productId);
            return count > 0;
        } catch (Exception e) {
            log.error("检查是否已收藏失败, userId: {}, productId: {}", userId, productId, e);
            throw new RuntimeException("检查是否已收藏失败: " + e.getMessage());
        }
    }
    
    @Override
    public Map<Long, Boolean> batchCheckFavorited(Long userId, List<Long> productIds) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            if (productIds == null || productIds.isEmpty()) {
                throw new IllegalArgumentException("商品ID列表不能为空");
            }
            
            List<Long> favoritedProductIds = mallProductFavoriteMapper.batchCheckFavorited(userId, productIds);
            
            return productIds.stream()
                    .collect(Collectors.toMap(
                            productId -> productId,
                            productId -> favoritedProductIds.contains(productId)
                    ));
        } catch (Exception e) {
            log.error("批量检查是否已收藏失败, userId: {}, productIds: {}", userId, productIds, e);
            throw new RuntimeException("批量检查是否已收藏失败: " + e.getMessage());
        }
    }
    
    @Override
    public IPage<MallProductFavorite> getUserFavorites(Long userId, Long groupId, Integer pageNum, Integer pageSize) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            
            Page<MallProductFavorite> page = new Page<>(pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 10);
            return mallProductFavoriteMapper.selectUserFavorites(page, userId, groupId);
        } catch (Exception e) {
            log.error("获取用户收藏列表失败, userId: {}", userId, e);
            throw new RuntimeException("获取用户收藏列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<Map<String, Object>> getUserFavoriteGroups(Long userId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            
            return mallProductFavoriteMapper.selectUserFavoriteGroups(userId);
        } catch (Exception e) {
            log.error("获取用户收藏分组列表失败, userId: {}", userId, e);
            throw new RuntimeException("获取用户收藏分组列表失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createFavoriteGroup(Long userId, String groupName) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            if (!StringUtils.hasText(groupName)) {
                throw new IllegalArgumentException("分组名称不能为空");
            }
            
            // 检查分组名称是否已存在
            int existCount = mallProductFavoriteMapper.checkGroupNameExists(userId, groupName);
            if (existCount > 0) {
                throw new IllegalArgumentException("分组名称已存在");
            }
            
            int result = mallProductFavoriteMapper.insertFavoriteGroup(userId, groupName);
            if (result > 0) {
                return mallProductFavoriteMapper.selectLatestGroupId(userId, groupName);
            }
            
            throw new RuntimeException("创建收藏分组失败");
        } catch (Exception e) {
            log.error("创建收藏分组失败, userId: {}, groupName: {}", userId, groupName, e);
            throw new RuntimeException("创建收藏分组失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateFavoriteGroup(Long userId, Long groupId, String groupName) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            if (groupId == null) {
                throw new IllegalArgumentException("分组ID不能为空");
            }
            if (!StringUtils.hasText(groupName)) {
                throw new IllegalArgumentException("分组名称不能为空");
            }
            
            int result = mallProductFavoriteMapper.updateFavoriteGroup(userId, groupId, groupName);
            return result > 0;
        } catch (Exception e) {
            log.error("更新收藏分组失败, userId: {}, groupId: {}, groupName: {}", userId, groupId, groupName, e);
            throw new RuntimeException("更新收藏分组失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFavoriteGroup(Long userId, Long groupId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            if (groupId == null) {
                throw new IllegalArgumentException("分组ID不能为空");
            }
            
            // 先清空该分组下的收藏
            mallProductFavoriteMapper.clearFavoritesByGroupId(groupId);
            
            // 删除分组
            int result = mallProductFavoriteMapper.deleteFavoriteGroup(userId, groupId);
            return result > 0;
        } catch (Exception e) {
            log.error("删除收藏分组失败, userId: {}, groupId: {}", userId, groupId, e);
            throw new RuntimeException("删除收藏分组失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean moveFavoritesToGroup(List<Long> favoriteIds, Long groupId) {
        try {
            if (favoriteIds == null || favoriteIds.isEmpty()) {
                throw new IllegalArgumentException("收藏ID列表不能为空");
            }
            
            String groupName = null;
            if (groupId != null) {
                // 从分组表获取分组名称
                groupName = mallProductFavoriteMapper.selectGroupNameById(groupId);
                if (groupName == null) {
                    groupName = "默认分组";
                }
            }
            
            int result = mallProductFavoriteMapper.moveFavoritesToGroup(favoriteIds, groupId, groupName);
            return result > 0;
        } catch (Exception e) {
            log.error("移动收藏到分组失败, favoriteIds: {}, groupId: {}", favoriteIds, groupId, e);
            throw new RuntimeException("移动收藏到分组失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateFavoriteRemark(Long favoriteId, String remark) {
        try {
            if (favoriteId == null) {
                throw new IllegalArgumentException("收藏ID不能为空");
            }
            
            int result = mallProductFavoriteMapper.updateFavoriteRemark(favoriteId, remark);
            return result > 0;
        } catch (Exception e) {
            log.error("更新收藏备注失败, favoriteId: {}", favoriteId, e);
            throw new RuntimeException("更新收藏备注失败: " + e.getMessage());
        }
    }
    
    @Override
    public Map<String, Object> getUserFavoriteStats(Long userId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            
            Map<String, Object> stats = mallProductFavoriteMapper.selectUserFavoriteStats(userId);
            if (stats == null) {
                stats = new HashMap<>();
                stats.put("totalCount", 0L);
                stats.put("groupCount", 0L);
                stats.put("todayCount", 0L);
                stats.put("weekCount", 0L);
                stats.put("monthCount", 0L);
            }
            
            return stats;
        } catch (Exception e) {
            log.error("获取用户收藏统计失败, userId: {}", userId, e);
            throw new RuntimeException("获取用户收藏统计失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<Map<String, Object>> getHotFavoriteProducts(Integer limit) {
        try {
            return mallProductFavoriteMapper.selectHotFavoriteProducts(limit != null ? limit : 10);
        } catch (Exception e) {
            log.error("获取热门收藏商品失败", e);
            throw new RuntimeException("获取热门收藏商品失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<Map<String, Object>> getRecommendProductsByFavorites(Long userId, Integer limit) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            
            return mallProductFavoriteMapper.selectRecommendProductsByFavorites(userId, limit != null ? limit : 10);
        } catch (Exception e) {
            log.error("根据收藏推荐商品失败, userId: {}", userId, e);
            throw new RuntimeException("根据收藏推荐商品失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<MallProductFavorite> exportUserFavorites(Long userId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            
            return mallProductFavoriteMapper.selectUserFavoritesForExport(userId);
        } catch (Exception e) {
            log.error("导出用户收藏列表失败, userId: {}", userId, e);
            throw new RuntimeException("导出用户收藏列表失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean clearUserFavorites(Long userId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            
            int result = mallProductFavoriteMapper.clearUserFavorites(userId);
            return result >= 0; // 即使没有收藏记录也算成功
        } catch (Exception e) {
            log.error("清空用户收藏列表失败, userId: {}", userId, e);
            throw new RuntimeException("清空用户收藏列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public Long getProductFavoriteCount(Long productId) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            
            Long count = mallProductFavoriteMapper.selectProductFavoriteCount(productId);
            return count != null ? count : 0L;
        } catch (Exception e) {
            log.error("获取商品收藏数量失败, productId: {}", productId, e);
            throw new RuntimeException("获取商品收藏数量失败: " + e.getMessage());
        }
    }
    
    @Override
    public Map<Long, Long> batchGetProductFavoriteCount(List<Long> productIds) {
        try {
            if (productIds == null || productIds.isEmpty()) {
                throw new IllegalArgumentException("商品ID列表不能为空");
            }
            
            List<Map<String, Object>> countList = mallProductFavoriteMapper.batchSelectProductFavoriteCount(productIds);
            
            Map<Long, Long> countMap = new HashMap<>();
            for (Long productId : productIds) {
                countMap.put(productId, 0L);
            }
            
            for (Map<String, Object> item : countList) {
                Long productId = (Long) item.get("productId");
                Long count = ((Number) item.get("count")).longValue();
                countMap.put(productId, count);
            }
            
            return countMap;
        } catch (Exception e) {
            log.error("批量获取商品收藏数量失败, productIds: {}", productIds, e);
            throw new RuntimeException("批量获取商品收藏数量失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean syncFavoriteProductInfo(Long productId) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            
            // 获取商品信息
            MallProduct product = mallProductService.getById(productId);
            if (product == null) {
                throw new IllegalArgumentException("商品不存在");
            }
            
            int result = mallProductFavoriteMapper.syncFavoriteProductInfo(
                    productId,
                    product.getProductName(),
                    product.getMainImage(),
                    product.getSalePrice()
            );
            
            return result >= 0;
        } catch (Exception e) {
            log.error("同步收藏商品信息失败, productId: {}", productId, e);
            throw new RuntimeException("同步收藏商品信息失败: " + e.getMessage());
        }
    }
    
    @Override
    public IPage<Map<String, Object>> getRecommendationsByFavorites(Long userId, PageParam<Map<String, Object>> page) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            
            Page<Map<String, Object>> pageParam = new Page<>(page.getPageNum(), page.getPageSize());
            return mallProductFavoriteMapper.selectRecommendationsByFavorites(pageParam, userId);
        } catch (Exception e) {
            log.error("根据收藏获取推荐商品失败, userId: {}", userId, e);
            throw new RuntimeException("根据收藏获取推荐商品失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<Map<String, Object>> getRecommendationsByFavorites(Long userId, Integer limit) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            
            return mallProductFavoriteMapper.selectRecommendationsByFavorites(userId, limit != null ? limit : 10, 0);
        } catch (Exception e) {
            log.error("根据收藏获取推荐商品失败, userId: {}, limit: {}", userId, limit, e);
            throw new RuntimeException("根据收藏获取推荐商品失败: " + e.getMessage());
        }
    }
    
    @Override
    public String exportFavorites(Long userId, String format) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            
            // 获取用户收藏数据
            List<Map<String, Object>> favorites = mallProductFavoriteMapper.selectFavoritesForExport(userId, null);
            
            // 根据格式导出文件
            String fileName = "user_" + userId + "_favorites_" + System.currentTimeMillis();
            String filePath;
            
            if ("excel".equalsIgnoreCase(format)) {
                filePath = exportToExcel(favorites, fileName);
            } else if ("csv".equalsIgnoreCase(format)) {
                filePath = exportToCsv(favorites, fileName);
            } else {
                throw new IllegalArgumentException("不支持的导出格式: " + format);
            }
            
            return filePath;
        } catch (Exception e) {
            log.error("导出收藏列表失败, userId: {}, format: {}", userId, format, e);
            throw new RuntimeException("导出收藏列表失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> exportFavorites(Long userId, Long groupId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            
            return mallProductFavoriteMapper.selectFavoritesForExport(userId, groupId);
        } catch (Exception e) {
            log.error("导出收藏失败, userId: {}, groupId: {}", userId, groupId, e);
            throw new RuntimeException("导出收藏失败: " + e.getMessage());
        }
    }
    
    private String exportToExcel(List<Map<String, Object>> data, String fileName) {
        // 这里应该实现Excel导出逻辑
        // 返回文件下载URL或文件路径
        return "/exports/" + fileName + ".xlsx";
    }
    
    private String exportToCsv(List<Map<String, Object>> data, String fileName) {
        // 这里应该实现CSV导出逻辑
        // 返回文件下载URL或文件路径
        return "/exports/" + fileName + ".csv";
    }
}