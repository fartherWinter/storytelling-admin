package com.chennian.storytelling.service.mall.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.mall.MallProductSku;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.mapper.mall.MallProductSkuMapper;
import com.chennian.storytelling.service.mall.MallProductSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品SKU Service实现类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Slf4j
@Service
public class MallProductSkuServiceImpl extends ServiceImpl<MallProductSkuMapper, MallProductSku> implements MallProductSkuService {
    
    @Autowired
    private MallProductSkuMapper mallProductSkuMapper;
    
    @Override
    public IPage<MallProductSku> findByPage(PageParam<MallProductSku> page, MallProductSku sku) {
        try {
            Page<MallProductSku> pageParam = new Page<>(page.getPageNum(), page.getPageSize());
            return mallProductSkuMapper.selectSkuPage(pageParam, sku);
        } catch (Exception e) {
            log.error("分页查询SKU列表失败", e);
            throw new RuntimeException("分页查询SKU列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<MallProductSku> getSkusByProductId(Long productId) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            return mallProductSkuMapper.selectSkusByProductId(productId);
        } catch (Exception e) {
            log.error("根据商品ID获取SKU列表失败, productId: {}", productId, e);
            throw new RuntimeException("获取SKU列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<MallProductSku> getEnabledSkusByProductId(Long productId) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            return mallProductSkuMapper.selectEnabledSkusByProductId(productId);
        } catch (Exception e) {
            log.error("根据商品ID获取启用SKU列表失败, productId: {}", productId, e);
            throw new RuntimeException("获取启用SKU列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public MallProductSku getSkuByCode(String skuCode) {
        try {
            if (!StringUtils.hasText(skuCode)) {
                throw new IllegalArgumentException("SKU编码不能为空");
            }
            return mallProductSkuMapper.selectSkuByCode(skuCode);
        } catch (Exception e) {
            log.error("根据SKU编码获取SKU失败, skuCode: {}", skuCode, e);
            throw new RuntimeException("获取SKU失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createSku(MallProductSku sku) {
        try {
            if (sku == null) {
                throw new IllegalArgumentException("SKU信息不能为空");
            }
            if (sku.getProductId() == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            if (!StringUtils.hasText(sku.getSkuCode())) {
                throw new IllegalArgumentException("SKU编码不能为空");
            }
            
            // 检查SKU编码是否已存在
            MallProductSku existingSku = getSkuByCode(sku.getSkuCode());
            if (existingSku != null) {
                throw new IllegalArgumentException("SKU编码已存在: " + sku.getSkuCode());
            }
            
            sku.setCreateTime(LocalDateTime.now());
            sku.setUpdateTime(LocalDateTime.now());
            
            return save(sku);
        } catch (Exception e) {
            log.error("创建SKU失败", e);
            throw new RuntimeException("创建SKU失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSku(MallProductSku sku) {
        try {
            if (sku == null || sku.getId() == null) {
                throw new IllegalArgumentException("SKU信息或ID不能为空");
            }
            
            sku.setUpdateTime(LocalDateTime.now());
            return updateById(sku);
        } catch (Exception e) {
            log.error("更新SKU失败, skuId: {}", sku != null ? sku.getId() : null, e);
            throw new RuntimeException("更新SKU失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSku(Long skuId) {
        try {
            if (skuId == null) {
                throw new IllegalArgumentException("SKU ID不能为空");
            }
            return removeById(skuId);
        } catch (Exception e) {
            log.error("删除SKU失败, skuId: {}", skuId, e);
            throw new RuntimeException("删除SKU失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteSkus(List<Long> skuIds) {
        try {
            if (skuIds == null || skuIds.isEmpty()) {
                throw new IllegalArgumentException("SKU ID列表不能为空");
            }
            return removeByIds(skuIds);
        } catch (Exception e) {
            log.error("批量删除SKU失败, skuIds: {}", skuIds, e);
            throw new RuntimeException("批量删除SKU失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSkuStatus(Long skuId, Integer status) {
        try {
            if (skuId == null) {
                throw new IllegalArgumentException("SKU ID不能为空");
            }
            if (status == null) {
                throw new IllegalArgumentException("状态不能为空");
            }
            
            MallProductSku sku = new MallProductSku();
            sku.setId(skuId);
            sku.setStatus(status);
            sku.setUpdateTime(LocalDateTime.now());
            
            return updateById(sku);
        } catch (Exception e) {
            log.error("更新SKU状态失败, skuId: {}, status: {}", skuId, status, e);
            throw new RuntimeException("更新SKU状态失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateSkuStatus(List<Long> skuIds, Integer status) {
        try {
            if (skuIds == null || skuIds.isEmpty()) {
                throw new IllegalArgumentException("SKU ID列表不能为空");
            }
            if (status == null) {
                throw new IllegalArgumentException("状态不能为空");
            }
            
            int result = mallProductSkuMapper.batchUpdateSkuStatus(skuIds, status);
            return result > 0;
        } catch (Exception e) {
            log.error("批量更新SKU状态失败, skuIds: {}, status: {}", skuIds, status, e);
            throw new RuntimeException("批量更新SKU状态失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSkuStock(Long skuId, Integer stock) {
        try {
            if (skuId == null) {
                throw new IllegalArgumentException("SKU ID不能为空");
            }
            if (stock == null || stock < 0) {
                throw new IllegalArgumentException("库存数量不能为空且不能小于0");
            }
            
            int result = mallProductSkuMapper.updateSkuStock(skuId, stock);
            return result > 0;
        } catch (Exception e) {
            log.error("更新SKU库存失败, skuId: {}, stock: {}", skuId, stock, e);
            throw new RuntimeException("更新SKU库存失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateSkuStock(Map<Long, Integer> stockUpdates) {
        try {
            if (stockUpdates == null || stockUpdates.isEmpty()) {
                throw new IllegalArgumentException("库存更新数据不能为空");
            }
            
            List<Map<String, Object>> updateList = stockUpdates.entrySet().stream()
                    .map(entry -> {
                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put("skuId", entry.getKey());
                        updateData.put("stock", entry.getValue());
                        return updateData;
                    })
                    .collect(Collectors.toList());
            
            int result = mallProductSkuMapper.batchUpdateSkuStock(updateList);
            return result > 0;
        } catch (Exception e) {
            log.error("批量更新SKU库存失败, stockUpdates: {}", stockUpdates, e);
            throw new RuntimeException("批量更新SKU库存失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSkuPrice(Long skuId, BigDecimal salePrice) {
        try {
            if (skuId == null) {
                throw new IllegalArgumentException("SKU ID不能为空");
            }
            if (salePrice == null || salePrice.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("销售价格不能为空且不能小于0");
            }
            
            int result = mallProductSkuMapper.updateSkuPrice(skuId, salePrice);
            return result > 0;
        } catch (Exception e) {
            log.error("更新SKU价格失败, skuId: {}, salePrice: {}", skuId, salePrice, e);
            throw new RuntimeException("更新SKU价格失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateSkuPrice(Map<Long, BigDecimal> priceUpdates) {
        try {
            if (priceUpdates == null || priceUpdates.isEmpty()) {
                throw new IllegalArgumentException("价格更新数据不能为空");
            }
            
            List<Map<String, Object>> updateList = priceUpdates.entrySet().stream()
                    .map(entry -> {
                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put("skuId", entry.getKey());
                        updateData.put("salePrice", entry.getValue());
                        return updateData;
                    })
                    .collect(Collectors.toList());
            
            int result = mallProductSkuMapper.batchUpdateSkuPrice(updateList);
            return result > 0;
        } catch (Exception e) {
            log.error("批量更新SKU价格失败, priceUpdates: {}", priceUpdates, e);
            throw new RuntimeException("批量更新SKU价格失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<MallProductSku> getStockWarningSku() {
        try {
            return mallProductSkuMapper.selectStockWarningSku();
        } catch (Exception e) {
            log.error("获取库存预警SKU列表失败", e);
            throw new RuntimeException("获取库存预警SKU列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public Map<String, Object> getSkuSalesStats(Long skuId) {
        try {
            if (skuId == null) {
                throw new IllegalArgumentException("SKU ID不能为空");
            }
            return mallProductSkuMapper.selectSkuSalesStats(skuId);
        } catch (Exception e) {
            log.error("获取SKU销售统计失败, skuId: {}", skuId, e);
            throw new RuntimeException("获取SKU销售统计失败: " + e.getMessage());
        }
    }
    
    @Override
    public MallProductSku getDefaultSkuByProductId(Long productId) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            return mallProductSkuMapper.selectDefaultSkuByProductId(productId);
        } catch (Exception e) {
            log.error("根据商品ID获取默认SKU失败, productId: {}", productId, e);
            throw new RuntimeException("获取默认SKU失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefaultSku(Long productId, Long skuId) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            if (skuId == null) {
                throw new IllegalArgumentException("SKU ID不能为空");
            }
            
            // 先清除该商品的所有默认SKU标记
            mallProductSkuMapper.clearDefaultSkuByProductId(productId);
            
            // 设置新的默认SKU
            int result = mallProductSkuMapper.setDefaultSku(skuId);
            return result > 0;
        } catch (Exception e) {
            log.error("设置默认SKU失败, productId: {}, skuId: {}", productId, skuId, e);
            throw new RuntimeException("设置默认SKU失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copySkuConfig(Long sourceProductId, Long targetProductId) {
        try {
            if (sourceProductId == null) {
                throw new IllegalArgumentException("源商品ID不能为空");
            }
            if (targetProductId == null) {
                throw new IllegalArgumentException("目标商品ID不能为空");
            }
            
            // 获取源商品的SKU配置
            List<MallProductSku> sourceSkus = getSkusByProductId(sourceProductId);
            if (sourceSkus.isEmpty()) {
                return true; // 源商品没有SKU配置，直接返回成功
            }
            
            // 先删除目标商品的现有SKU
            mallProductSkuMapper.deleteSkusByProductId(targetProductId);
            
            // 复制SKU配置
            for (MallProductSku sourceSku : sourceSkus) {
                MallProductSku newSku = new MallProductSku();
                newSku.setProductId(targetProductId);
                newSku.setSkuCode(generateNewSkuCode(targetProductId, sourceSku.getSkuCode()));
                newSku.setSkuName(sourceSku.getSkuName());
                newSku.setSkuAttributes(sourceSku.getSkuAttributes());
                newSku.setSkuImage(sourceSku.getSkuImage());
                newSku.setMarketPrice(sourceSku.getMarketPrice());
                newSku.setSalePrice(sourceSku.getSalePrice());
                newSku.setCostPrice(sourceSku.getCostPrice());
                newSku.setStock(0); // 新SKU库存设为0
                newSku.setStockWarning(sourceSku.getStockWarning());
                newSku.setSales(0); // 新SKU销量设为0
                newSku.setWeight(sourceSku.getWeight());
                newSku.setVolume(sourceSku.getVolume());
                newSku.setBarcode(sourceSku.getBarcode());
                newSku.setStatus(sourceSku.getStatus());
                newSku.setIsDefault(sourceSku.getIsDefault());
                newSku.setSort(sourceSku.getSort());
                newSku.setRemark(sourceSku.getRemark());
                newSku.setCreateTime(LocalDateTime.now());
                newSku.setUpdateTime(LocalDateTime.now());
                
                save(newSku);
            }
            
            return true;
        } catch (Exception e) {
            log.error("复制SKU配置失败, sourceProductId: {}, targetProductId: {}", sourceProductId, targetProductId, e);
            throw new RuntimeException("复制SKU配置失败: " + e.getMessage());
        }
    }
    
    /**
     * 生成新的SKU编码
     */
    private String generateNewSkuCode(Long productId, String originalSkuCode) {
        // 简单的SKU编码生成策略：商品ID + 时间戳 + 随机数
        return "SKU" + productId + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
}