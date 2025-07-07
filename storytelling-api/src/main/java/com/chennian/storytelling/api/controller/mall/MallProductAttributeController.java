package com.chennian.storytelling.api.controller.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.mall.MallProductAttribute;
import com.chennian.storytelling.bean.model.mall.MallProductAttributeValue;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.common.enums.MallResponseEnum;
import com.chennian.storytelling.service.mall.MallProductAttributeService;
import com.chennian.storytelling.service.mall.MallProductAttributeValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品属性Controller
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Api(tags = "商品属性管理")
@RestController
@RequestMapping("/mall/product-attribute")
public class MallProductAttributeController {
    
    private final MallProductAttributeService mallProductAttributeService;
    
    private final MallProductAttributeValueService mallProductAttributeValueService;

    public MallProductAttributeController(MallProductAttributeService mallProductAttributeService, MallProductAttributeValueService mallProductAttributeValueService) {
        this.mallProductAttributeService = mallProductAttributeService;
        this.mallProductAttributeValueService = mallProductAttributeValueService;
    }

    /**
     * 分页查询属性列表
     */
    @ApiOperation("分页查询属性列表")
    @PostMapping("/page")
    public ServerResponseEntity<IPage<MallProductAttribute>> getAttributePage(
            @RequestBody PageParam<MallProductAttribute> page,
            @ApiParam("查询条件") MallProductAttribute attribute) {
        IPage<MallProductAttribute> result = mallProductAttributeService.findByPage(page, attribute);
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 获取所有启用的属性
     */
    @ApiOperation("获取所有启用的属性")
    @GetMapping("/enabled")
    public ServerResponseEntity<List<MallProductAttribute>> getEnabledAttributes() {
        List<MallProductAttribute> attributes = mallProductAttributeService.getEnabledAttributes();
        return ServerResponseEntity.success(attributes);
    }
    
    /**
     * 根据属性类型获取属性列表
     */
    @ApiOperation("根据属性类型获取属性列表")
    @GetMapping("/type/{attributeType}")
    public ServerResponseEntity<List<MallProductAttribute>> getAttributesByType(
            @ApiParam("属性类型") @PathVariable Integer attributeType) {
        List<MallProductAttribute> attributes = mallProductAttributeService.getAttributesByType(attributeType);
        return ServerResponseEntity.success(attributes);
    }
    
    /**
     * 根据ID查询属性详情
     */
    @ApiOperation("根据ID查询属性详情")
    @GetMapping("/{attributeId}")
    public ServerResponseEntity<MallProductAttribute> getAttributeById(
            @ApiParam("属性ID") @PathVariable Long attributeId) {
        MallProductAttribute attribute = mallProductAttributeService.getById(attributeId);
        return ServerResponseEntity.success(attribute);
    }
    
    /**
     * 创建商品属性
     */
    @ApiOperation("创建商品属性")
    @PostMapping
    public ServerResponseEntity<String> createAttribute(@RequestBody MallProductAttribute attribute) {
        try {
            boolean success = mallProductAttributeService.createAttribute(attribute);
            if (success) {
                return ServerResponseEntity.success("创建成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_CREATE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_CREATE_FAIL);
        }
    }
    
    /**
     * 更新商品属性
     */
    @ApiOperation("更新商品属性")
    @PutMapping("/{attributeId}")
    public ServerResponseEntity<String> updateAttribute(
            @ApiParam("属性ID") @PathVariable Long attributeId,
            @RequestBody MallProductAttribute attribute) {
        try {
            attribute.setId(attributeId);
            boolean success = mallProductAttributeService.updateAttribute(attribute);
            if (success) {
                return ServerResponseEntity.success("更新成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_UPDATE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_UPDATE_FAIL);
        }
    }
    
    /**
     * 删除商品属性
     */
    @ApiOperation("删除商品属性")
    @DeleteMapping("/{attributeId}")
    public ServerResponseEntity<String> deleteAttribute(
            @ApiParam("属性ID") @PathVariable Long attributeId) {
        try {
            boolean success = mallProductAttributeService.deleteAttribute(attributeId);
            if (success) {
                return ServerResponseEntity.success("删除成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_DELETE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_DELETE_FAIL);
        }
    }
    
    /**
     * 批量删除属性
     */
    @ApiOperation("批量删除属性")
    @DeleteMapping("/batch")
    public ServerResponseEntity<String> batchDeleteAttributes(
            @ApiParam("属性ID列表") @RequestParam List<Long> attributeIds) {
        try {
            boolean success = mallProductAttributeService.batchDeleteAttributes(attributeIds);
            if (success) {
                return ServerResponseEntity.success("批量删除成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_BATCH_DELETE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_BATCH_DELETE_FAIL);
        }
    }
    
    /**
     * 启用/禁用属性
     */
    @ApiOperation("启用/禁用属性")
    @PutMapping("/{attributeId}/status")
    public ServerResponseEntity<String> updateAttributeStatus(
            @ApiParam("属性ID") @PathVariable Long attributeId,
            @ApiParam("状态") @RequestParam Integer status) {
        try {
            boolean success = mallProductAttributeService.updateAttributeStatus(attributeId, status);
            if (success) {
                return ServerResponseEntity.success("状态更新成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_STATUS_UPDATE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_STATUS_UPDATE_FAIL);
        }
    }
    
    /**
     * 批量更新属性状态
     */
    @ApiOperation("批量更新属性状态")
    @PutMapping("/batch/status")
    public ServerResponseEntity<String> batchUpdateAttributeStatus(
            @ApiParam("属性ID列表") @RequestParam List<Long> attributeIds,
            @ApiParam("状态") @RequestParam Integer status) {
        try {
            boolean success = mallProductAttributeService.batchUpdateAttributeStatus(attributeIds, status);
            if (success) {
                return ServerResponseEntity.success("批量状态更新成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_BATCH_STATUS_UPDATE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_BATCH_STATUS_UPDATE_FAIL);
        }
    }
    
    /**
     * 更新属性排序
     */
    @ApiOperation("更新属性排序")
    @PutMapping("/{attributeId}/sort")
    public ServerResponseEntity<String> updateAttributeSort(
            @ApiParam("属性ID") @PathVariable Long attributeId,
            @ApiParam("排序值") @RequestParam Integer sortOrder) {
        try {
            boolean success = mallProductAttributeService.updateAttributeSort(attributeId, sortOrder);
            if (success) {
                return ServerResponseEntity.success("排序更新成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_SORT_UPDATE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_SORT_UPDATE_FAIL);
        }
    }
    
    /**
     * 获取属性值列表
     */
    @ApiOperation("获取属性值列表")
    @PostMapping("/values/page")
    public ServerResponseEntity<IPage<MallProductAttributeValue>> getAttributeValuePage(
            @RequestBody PageParam<MallProductAttributeValue> page,
            @ApiParam("查询条件") MallProductAttributeValue attributeValue) {
        IPage<MallProductAttributeValue> result = mallProductAttributeValueService.findByPage(page, attributeValue);
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 根据商品ID获取属性值
     */
    @ApiOperation("根据商品ID获取属性值")
    @GetMapping("/values/product/{productId}")
    public ServerResponseEntity<List<MallProductAttributeValue>> getAttributeValuesByProductId(
            @ApiParam("商品ID") @PathVariable Long productId) {
        List<MallProductAttributeValue> attributeValues = mallProductAttributeValueService.getAttributeValuesByProductId(productId);
        return ServerResponseEntity.success(attributeValues);
    }
    
    /**
     * 根据属性ID获取属性值
     */
    @ApiOperation("根据属性ID获取属性值")
    @GetMapping("/values/attribute/{attributeId}")
    public ServerResponseEntity<List<MallProductAttributeValue>> getAttributeValuesByAttributeId(
            @ApiParam("属性ID") @PathVariable Long attributeId) {
        List<MallProductAttributeValue> attributeValues = mallProductAttributeValueService.getAttributeValuesByAttributeId(attributeId);
        return ServerResponseEntity.success(attributeValues);
    }
    
    /**
     * 设置商品属性值
     */
    @ApiOperation("设置商品属性值")
    @PostMapping("/values")
    public ServerResponseEntity<String> setProductAttributeValues(
            @ApiParam("商品ID") @RequestParam Long productId,
            @RequestBody List<MallProductAttributeValue> attributeValues) {
        try {
            boolean success = mallProductAttributeValueService.setProductAttributeValues(productId, attributeValues);
            if (success) {
                return ServerResponseEntity.success("设置成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_VALUE_SET_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_VALUE_SET_FAIL);
        }
    }
    
    /**
     * 批量设置商品属性值
     */
    @ApiOperation("批量设置商品属性值")
    @PostMapping("/values/batch")
    public ServerResponseEntity<String> batchSetProductAttributeValues(
            @RequestBody Map<Long, List<MallProductAttributeValue>> productAttributeMap) {
        try {
            boolean success = mallProductAttributeValueService.batchSetProductAttributeValues(productAttributeMap);
            if (success) {
                return ServerResponseEntity.success("批量设置成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_VALUE_BATCH_SET_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_VALUE_BATCH_SET_FAIL);
        }
    }
    
    /**
     * 删除商品属性值
     */
    @ApiOperation("删除商品属性值")
    @DeleteMapping("/values/{valueId}")
    public ServerResponseEntity<String> deleteAttributeValue(
            @ApiParam("属性值ID") @PathVariable Long valueId) {
        try {
            boolean success = mallProductAttributeValueService.deleteAttributeValue(valueId);
            if (success) {
                return ServerResponseEntity.success("删除成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_VALUE_DELETE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_VALUE_DELETE_FAIL);
        }
    }
    
    /**
     * 删除商品的所有属性值
     */
    @ApiOperation("删除商品的所有属性值")
    @DeleteMapping("/values/product/{productId}")
    public ServerResponseEntity<String> deleteProductAttributeValues(
            @ApiParam("商品ID") @PathVariable Long productId) {
        try {
            boolean success = mallProductAttributeValueService.deleteProductAttributeValues(productId);
            if (success) {
                return ServerResponseEntity.success("删除成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_VALUE_DELETE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_VALUE_DELETE_FAIL);
        }
    }
    
    /**
     * 复制属性配置
     */
    @ApiOperation("复制属性配置")
    @PostMapping("/copy")
    public ServerResponseEntity<String> copyAttributeConfig(
            @ApiParam("源商品ID") @RequestParam Long sourceProductId,
            @ApiParam("目标商品ID列表") @RequestParam List<Long> targetProductIds) {
        try {
            boolean success = mallProductAttributeValueService.copyAttributeConfig(sourceProductId, targetProductIds);
            if (success) {
                return ServerResponseEntity.success("复制成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_COPY_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_COPY_FAIL);
        }
    }
    
    /**
     * 获取属性使用统计
     */
    @ApiOperation("获取属性使用统计")
    @GetMapping("/stats")
    public ServerResponseEntity<List<Map<String, Object>>> getAttributeStats() {
        List<Map<String, Object>> stats = mallProductAttributeService.getAttributeUsageStats();
        return ServerResponseEntity.success(stats);
    }
    
    /**
     * 搜索属性
     */
    @ApiOperation("搜索属性")
    @GetMapping("/search")
    public ServerResponseEntity<List<MallProductAttribute>> searchAttributes(
            @ApiParam("搜索关键词") @RequestParam String keyword,
            @ApiParam("属性类型") @RequestParam(required = false) Integer attributeType) {
        List<MallProductAttribute> attributes = mallProductAttributeService.searchAttributes(keyword, attributeType);
        return ServerResponseEntity.success(attributes);
    }
    
    /**
     * 导入属性配置
     */
    @ApiOperation("导入属性配置")
    @PostMapping("/import")
    public ServerResponseEntity<String> importAttributes(
            @RequestBody List<MallProductAttribute> attributes) {
        try {
            boolean success = mallProductAttributeService.importAttributes(attributes);
            if (success) {
                return ServerResponseEntity.success("导入成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_IMPORT_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_IMPORT_FAIL);
        }
    }
    
    /**
     * 导出属性配置
     */
    @ApiOperation("导出属性配置")
    @GetMapping("/export")
    public ServerResponseEntity<List<MallProductAttribute>> exportAttributes(
            @ApiParam("属性ID列表") @RequestParam(required = false) List<Long> attributeIds) {
        try {
            List<MallProductAttribute> attributes = mallProductAttributeService.exportAttributes(attributeIds);
            return ServerResponseEntity.success(attributes);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_EXPORT_FAIL);
        }
    }
}