package com.chennian.storytelling.api.controller.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.mall.MallProductFavorite;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.mall.MallProductFavoriteService;
import com.chennian.storytelling.common.enums.MallResponseEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品收藏Controller
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Api(tags = "商品收藏管理")
@RestController
@RequestMapping("/mall/product-favorite")
public class MallProductFavoriteController {

    private final MallProductFavoriteService mallProductFavoriteService;

    public MallProductFavoriteController(MallProductFavoriteService mallProductFavoriteService) {
        this.mallProductFavoriteService = mallProductFavoriteService;
    }

    /**
     * 添加商品收藏
     */
    @ApiOperation("添加商品收藏")
    @PostMapping
    public ServerResponseEntity<String> addFavorite(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("商品ID") @RequestParam Long productId,
            @ApiParam("分组ID") @RequestParam(required = false) Long groupId,
            @ApiParam("备注") @RequestParam(required = false) String remark) {
        try {
            mallProductFavoriteService.addFavorite(userId, productId, groupId, remark);
            return ServerResponseEntity.success("收藏成功");
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.FAVORITE_ADD_FAIL);
        }
    }
    
    /**
     * 取消商品收藏
     */
    @ApiOperation("取消商品收藏")
    @DeleteMapping
    public ServerResponseEntity<String> removeFavorite(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("商品ID") @RequestParam Long productId) {
        try {
            mallProductFavoriteService.removeFavorite(userId, productId);
            return ServerResponseEntity.success("取消收藏成功");
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.FAVORITE_REMOVE_FAIL);
        }
    }
    
    /**
     * 根据收藏ID删除收藏
     */
    @ApiOperation("根据收藏ID删除收藏")
    @DeleteMapping("/{favoriteId}")
    public ServerResponseEntity<String> deleteFavoriteById(
            @ApiParam("收藏ID") @PathVariable Long favoriteId) {
        try {
            mallProductFavoriteService.deleteFavoriteById(favoriteId);
            return ServerResponseEntity.success("删除成功");
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.FAVORITE_DELETE_FAIL);
        }
    }
    
    /**
     * 批量删除收藏
     */
    @ApiOperation("批量删除收藏")
    @DeleteMapping("/batch")
    public ServerResponseEntity<String> batchDeleteFavorites(
            @ApiParam("收藏ID列表") @RequestParam List<Long> favoriteIds) {
        try {
            mallProductFavoriteService.batchDeleteFavorites(favoriteIds);
            return ServerResponseEntity.success("批量删除成功");
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.FAVORITE_BATCH_DELETE_FAIL);
        }
    }
    
    /**
     * 获取用户收藏列表
     */
    @ApiOperation("获取用户收藏列表")
    @PostMapping("/user/{userId}")
    public ServerResponseEntity<IPage<MallProductFavorite>> getUserFavorites(
            @ApiParam("用户ID") @PathVariable Long userId,
            @RequestBody PageParam<MallProductFavorite> page,
            @ApiParam("收藏分组ID") @RequestParam(required = false) Long groupId) {
        try {
            IPage<MallProductFavorite> favorites = mallProductFavoriteService.getUserFavorites(userId, page, groupId);
            return ServerResponseEntity.success(favorites);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.GET_LIST_FAIL);
        }
    }
    
    /**
     * 检查商品是否已收藏
     */
    @ApiOperation("检查商品是否已收藏")
    @GetMapping("/check")
    public ServerResponseEntity<Boolean> checkFavorite(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("商品ID") @RequestParam Long productId) {
        try {
            Boolean isFavorite = mallProductFavoriteService.checkFavorite(userId, productId);
            return ServerResponseEntity.success(isFavorite);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.FAVORITE_CHECK_FAIL);
        }
    }
    
    /**
     * 批量检查商品收藏状态
     */
    @ApiOperation("批量检查商品收藏状态")
    @PostMapping("/batch/check")
    public ServerResponseEntity<Map<Long, Boolean>> batchCheckFavorites(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("商品ID列表") @RequestParam List<Long> productIds) {
        try {
            Map<Long, Boolean> favoriteStatus = mallProductFavoriteService.batchCheckFavorites(userId, productIds);
            return ServerResponseEntity.success(favoriteStatus);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.FAVORITE_BATCH_CHECK_FAIL);
        }
    }
    
    /**
     * 获取收藏分组列表
     */
    @ApiOperation("获取收藏分组列表")
    @GetMapping("/groups/{userId}")
    public ServerResponseEntity<List<Map<String, Object>>> getFavoriteGroups(
            @ApiParam("用户ID") @PathVariable Long userId) {
        try {
            List<Map<String, Object>> groups = mallProductFavoriteService.getFavoriteGroups(userId);
            return ServerResponseEntity.success(groups);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.FAVORITE_GROUP_LIST_FAIL);
        }
    }
    
    /**
     * 创建收藏分组
     */
    @ApiOperation("创建收藏分组")
    @PostMapping("/groups")
    public ServerResponseEntity<String> createFavoriteGroup(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("分组名称") @RequestParam String groupName) {
        try {
            mallProductFavoriteService.createFavoriteGroup(userId, groupName);
            return ServerResponseEntity.success("创建分组成功");
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.FAVORITE_GROUP_CREATE_FAIL);
        }
    }
    
    /**
     * 更新收藏分组
     */
    @ApiOperation("更新收藏分组")
    @PutMapping("/groups/{groupId}")
    public ServerResponseEntity<String> updateFavoriteGroup(
            @ApiParam("分组ID") @PathVariable Long groupId,
            @ApiParam("分组名称") @RequestParam String groupName) {
        try {
            mallProductFavoriteService.updateFavoriteGroup(groupId, groupName);
            return ServerResponseEntity.success("更新分组成功");
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.FAVORITE_GROUP_UPDATE_FAIL);
        }
    }
    
    /**
     * 删除收藏分组
     */
    @ApiOperation("删除收藏分组")
    @DeleteMapping("/groups/{groupId}")
    public ServerResponseEntity<String> deleteFavoriteGroup(
            @ApiParam("分组ID") @PathVariable Long groupId) {
        try {
            mallProductFavoriteService.deleteFavoriteGroup(groupId);
            return ServerResponseEntity.success("删除分组成功");
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.FAVORITE_GROUP_DELETE_FAIL);
        }
    }
    
    /**
     * 移动收藏到分组
     */
    @ApiOperation("移动收藏到分组")
    @PutMapping("/move-to-group")
    public ServerResponseEntity<String> moveToGroup(
            @ApiParam("收藏ID列表") @RequestParam List<Long> favoriteIds,
            @ApiParam("目标分组ID") @RequestParam Long targetGroupId) {
        try {
            mallProductFavoriteService.moveToGroup(favoriteIds, targetGroupId);
            return ServerResponseEntity.success("移动成功");
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.FAVORITE_MOVE_FAIL);
        }
    }
    
    /**
     * 更新收藏备注
     */
    @ApiOperation("更新收藏备注")
    @PutMapping("/{favoriteId}/remark")
    public ServerResponseEntity<String> updateFavoriteRemark(
            @ApiParam("收藏ID") @PathVariable Long favoriteId,
            @ApiParam("收藏备注") @RequestParam String remark) {
        try {
            mallProductFavoriteService.updateFavoriteRemark(favoriteId, remark);
            return ServerResponseEntity.success("更新备注成功");
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.FAVORITE_REMARK_UPDATE_FAIL);
        }
    }
    
    /**
     * 获取用户收藏统计
     */
    @ApiOperation("获取用户收藏统计")
    @GetMapping("/stats/{userId}")
    public ServerResponseEntity<Map<String, Object>> getFavoriteStats(
            @ApiParam("用户ID") @PathVariable Long userId) {
        try {
            Map<String, Object> stats = mallProductFavoriteService.getFavoriteStats(userId);
            return ServerResponseEntity.success(stats);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.GET_STATS_FAIL);
        }
    }
    
    /**
     * 获取热门收藏商品
     */
    @ApiOperation("获取热门收藏商品")
    @GetMapping("/hot")
    public ServerResponseEntity<List<Map<String, Object>>> getHotFavoriteProducts(
            @ApiParam("数量限制") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<Map<String, Object>> hotProducts = mallProductFavoriteService.getHotFavoriteProducts(limit);
            return ServerResponseEntity.success(hotProducts);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.FAVORITE_HOT_FAIL);
        }
    }
    
    /**
     * 根据收藏推荐商品
     */
    @ApiOperation("根据收藏推荐商品")
    @GetMapping("/recommendations/{userId}")
    public ServerResponseEntity<List<Map<String, Object>>> getRecommendationsByFavorites(
            @ApiParam("用户ID") @PathVariable Long userId,
            @ApiParam("推荐数量") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<Map<String, Object>> recommendations = mallProductFavoriteService.getRecommendationsByFavorites(userId, limit);
            return ServerResponseEntity.success(recommendations);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.FAVORITE_RECOMMENDATION_FAIL);
        }
    }
    
    /**
     * 导出用户收藏列表
     */
    @ApiOperation("导出用户收藏列表")
    @GetMapping("/export/{userId}")
    public ServerResponseEntity<String> exportFavorites(
            @ApiParam("用户ID") @PathVariable Long userId,
            @ApiParam("导出格式") @RequestParam(defaultValue = "excel") String format) {
        try {
            String exportUrl = mallProductFavoriteService.exportFavorites(userId, format);
            return ServerResponseEntity.success(exportUrl);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.FAVORITE_EXPORT_FAIL);
        }
    }
    
    /**
     * 清空用户收藏
     */
    @ApiOperation("清空用户收藏")
    @DeleteMapping("/clear/{userId}")
    public ServerResponseEntity<String> clearUserFavorites(
            @ApiParam("用户ID") @PathVariable Long userId) {
        try {
            mallProductFavoriteService.clearUserFavorites(userId);
            return ServerResponseEntity.success("清空收藏成功");
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.FAVORITE_CLEAR_FAIL);
        }
    }
}