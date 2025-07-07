package com.chennian.storytelling.service.impl.mall;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.mall.DeliveryMethod;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.mall.DeliveryMethodMapper;
import com.chennian.storytelling.service.mall.DeliveryMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 配送方式服务实现类
 */
@Service
public class DeliveryMethodServiceImpl extends ServiceImpl<DeliveryMethodMapper, DeliveryMethod> implements DeliveryMethodService {

    @Autowired
    private DeliveryMethodMapper deliveryMethodMapper;

    @Override
    public PageParam<DeliveryMethod> getDeliveryMethodPage(Integer pageNum, Integer pageSize, String name, Integer status) {
        LambdaQueryWrapper<DeliveryMethod> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(name)) {
            queryWrapper.like(DeliveryMethod::getName, name);
        }
        if (status != null) {
            queryWrapper.eq(DeliveryMethod::getStatus, status);
        }
        
        queryWrapper.orderByAsc(DeliveryMethod::getSort)
                   .orderByDesc(DeliveryMethod::getCreateTime);
        
        IPage<DeliveryMethod> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        
        // 转换为PageParam
        PageParam<DeliveryMethod> pageParam = new PageParam<>();
        pageParam.setRecords(page.getRecords());
        pageParam.setTotal(page.getTotal());
        pageParam.setSize(page.getSize());
        pageParam.setCurrent(page.getCurrent());
        pageParam.setPages(page.getPages());
        
        return pageParam;
    }

    @Override
    public List<DeliveryMethod> getEnabledDeliveryMethods() {
        LambdaQueryWrapper<DeliveryMethod> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeliveryMethod::getStatus, 1)
                   .orderByAsc(DeliveryMethod::getSort);
        return list(queryWrapper);
    }

    @Override
    public List<DeliveryMethod> getAvailableDeliveryMethods(String provinceCode, String cityCode) {
        LambdaQueryWrapper<DeliveryMethod> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeliveryMethod::getStatus, 1)
                   .and(wrapper -> wrapper
                       .isNull(DeliveryMethod::getSupportedRegions)
                       .or()
                       .like(DeliveryMethod::getSupportedRegions, provinceCode)
                       .or()
                       .like(DeliveryMethod::getSupportedRegions, cityCode)
                   )
                   .orderByAsc(DeliveryMethod::getSort);
        return list(queryWrapper);
    }

    @Override
    public BigDecimal calculateShippingFee(Long deliveryMethodId, Integer weight, BigDecimal orderAmount, String provinceCode, String cityCode) {
        DeliveryMethod deliveryMethod = getById(deliveryMethodId);
        if (deliveryMethod == null || deliveryMethod.getStatus() != 1) {
            return BigDecimal.ZERO;
        }
        
        // 检查是否满足免运费条件
        if (deliveryMethod.getFreeShippingThreshold() != null && 
            orderAmount.compareTo(deliveryMethod.getFreeShippingThreshold()) >= 0) {
            return BigDecimal.ZERO;
        }
        
        // 计算运费
        BigDecimal shippingFee = deliveryMethod.getBasePrice();
        
        // 如果有重量限制，计算续重费用
        if (deliveryMethod.getFirstWeight() != null && deliveryMethod.getAdditionalPrice() != null) {
            if (weight > deliveryMethod.getFirstWeight().intValue()) {
                // 计算续重
                int additionalWeight = weight - deliveryMethod.getFirstWeight().intValue();
                // 按500g为单位计算续重（向上取整）
                int additionalUnits = (int) Math.ceil(additionalWeight / 500.0);
                BigDecimal additionalFee = deliveryMethod.getAdditionalPrice().multiply(new BigDecimal(additionalUnits));
                shippingFee = shippingFee.add(additionalFee);
            }
        }
        
        return shippingFee;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createDeliveryMethod(DeliveryMethod deliveryMethod) {
        deliveryMethod.setCreateTime(LocalDateTime.now());
        deliveryMethod.setUpdateTime(LocalDateTime.now());
        return save(deliveryMethod);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDeliveryMethod(DeliveryMethod deliveryMethod) {
        deliveryMethod.setUpdateTime(LocalDateTime.now());
        return updateById(deliveryMethod);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDeliveryMethod(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long id, Integer status) {
        DeliveryMethod deliveryMethod = new DeliveryMethod();
        deliveryMethod.setId(id);
        deliveryMethod.setStatus(status);
        deliveryMethod.setUpdateTime(LocalDateTime.now());
        return updateById(deliveryMethod);
    }

    /**
     * 更新配送方式状态（控制器调用的方法名）
     */
    @Override
    public boolean updateDeliveryMethodStatus(Long id, Integer status) {
        return updateStatus(id, status);
    }

    /**
     * 计算配送费用（控制器调用的方法名）
     */
    @Override
    public BigDecimal calculateDeliveryFee(Long deliveryMethodId, Integer weight, BigDecimal orderAmount, String provinceCode, String cityCode) {
        return calculateShippingFee(deliveryMethodId, weight, orderAmount, provinceCode, cityCode);
    }
}