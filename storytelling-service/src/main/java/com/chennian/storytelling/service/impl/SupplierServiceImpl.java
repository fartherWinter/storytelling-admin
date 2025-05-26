package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.Supplier;
import com.chennian.storytelling.bean.vo.SupplierCollaborationMessageVO;
import com.chennian.storytelling.bean.vo.SupplierPerformanceVO;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.SupplierMapper;
import com.chennian.storytelling.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应商服务实现类
 * @author chennian
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

    /**
     * 分页查询供应商列表
     * @param page 分页参数
     * @param supplier 查询条件
     * @return 供应商分页数据
     */
    @Override
    public IPage<Supplier> findByPage(PageParam<Supplier> page, Supplier supplier) {
        LambdaQueryWrapper<Supplier> queryWrapper = new LambdaQueryWrapper<>();
        if (supplier != null) {
            // 根据供应商编码模糊查询
            if (StringUtils.hasText(supplier.getSupplierCode())) {
                queryWrapper.like(Supplier::getSupplierCode, supplier.getSupplierCode());
            }
            // 根据供应商名称模糊查询
            if (StringUtils.hasText(supplier.getSupplierName())) {
                queryWrapper.like(Supplier::getSupplierName, supplier.getSupplierName());
            }
            // 根据供应商类型精确查询
            if (StringUtils.hasText(supplier.getSupplierType())) {
                queryWrapper.eq(Supplier::getSupplierType, supplier.getSupplierType());
            }
            // 根据状态精确查询
            if (StringUtils.hasText(supplier.getStatus())) {
                queryWrapper.eq(Supplier::getStatus, supplier.getStatus());
            }
        }
        // 按创建时间降序排序
        queryWrapper.orderByDesc(Supplier::getCreateTime);
        return page(page, queryWrapper);
    }

    /**
     * 根据ID查询供应商
     * @param supplierId 供应商ID
     * @return 供应商信息
     */
    @Override
    public Supplier selectSupplierById(Long supplierId) {
        return getById(supplierId);
    }

    /**
     * 新增供应商
     * @param supplier 供应商信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSupplier(Supplier supplier) {
        return save(supplier) ? 1 : 0;
    }

    /**
     * 修改供应商
     * @param supplier 供应商信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSupplier(Supplier supplier) {
        return updateById(supplier) ? 1 : 0;
    }

    /**
     * 批量删除供应商
     * @param supplierIds 需要删除的供应商ID数组
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSupplierByIds(Long[] supplierIds) {
        return supplierMapper.deleteBatchIds(Arrays.asList(supplierIds));
    }

    /**
     * 更新供应商状态
     * @param supplier 供应商信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSupplierStatus(Supplier supplier) {
        Supplier updateSupplier = new Supplier();
        updateSupplier.setSupplierId(supplier.getSupplierId());
        updateSupplier.setStatus(supplier.getStatus());
        return updateById(updateSupplier) ? 1 : 0;
    }

    /**
     * 获取供应商评估记录
     * @param supplierId 供应商ID
     * @return 评估记录列表
     */
    @Override
    public List<Map<String, Object>> getEvaluationRecords(Long supplierId) {
        // 模拟数据，实际项目中应该从数据库获取
        List<Map<String, Object>> records = new ArrayList<>();
        Map<String, Object> record1 = new HashMap<>();
        record1.put("id", 1);
        record1.put("supplierId", supplierId);
        record1.put("evaluationDate", "2023-05-20");
        record1.put("evaluator", "张三");
        record1.put("qualityScore", 90);
        record1.put("deliveryScore", 85);
        record1.put("priceScore", 80);
        record1.put("serviceScore", 95);
        record1.put("totalScore", 87.5);
        record1.put("comments", "服务态度好，质量稳定");
        records.add(record1);
        return records;
    }

    /**
     * 添加供应商评估记录
     * @param evaluation 评估信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addEvaluationRecord(Map<String, Object> evaluation) {
        // 实际项目中应该保存到数据库
        return 1;
    }

    /**
     * 获取供应商合作历史
     * @param supplierId 供应商ID
     * @return 合作历史列表
     */
    @Override
    public List<Map<String, Object>> getCooperationHistory(Long supplierId) {
        // 模拟数据，实际项目中应该从数据库获取
        List<Map<String, Object>> history = new ArrayList<>();
        Map<String, Object> record1 = new HashMap<>();
        record1.put("id", 1);
        record1.put("supplierId", supplierId);
        record1.put("orderNo", "PO-2023-001");
        record1.put("orderDate", "2023-01-15");
        record1.put("amount", 50000.00);
        record1.put("status", "已完成");
        record1.put("deliveryDate", "2023-01-25");
        record1.put("comments", "按时交付");
        history.add(record1);
        return history;
    }
    
    /**
     * 根据条件查询供应商列表
     * @param supplier 查询条件
     * @return 供应商列表
     */
    @Override
    public List<Supplier> selectSupplierList(Supplier supplier) {
        LambdaQueryWrapper<Supplier> queryWrapper = new LambdaQueryWrapper<>();
        if (supplier != null) {
            // 根据供应商编码模糊查询
            if (StringUtils.hasText(supplier.getSupplierCode())) {
                queryWrapper.like(Supplier::getSupplierCode, supplier.getSupplierCode());
            }
            // 根据供应商名称模糊查询
            if (StringUtils.hasText(supplier.getSupplierName())) {
                queryWrapper.like(Supplier::getSupplierName, supplier.getSupplierName());
            }
            // 根据供应商类型精确查询
            if (StringUtils.hasText(supplier.getSupplierType())) {
                queryWrapper.eq(Supplier::getSupplierType, supplier.getSupplierType());
            }
            // 根据状态精确查询
            if (StringUtils.hasText(supplier.getStatus())) {
                queryWrapper.eq(Supplier::getStatus, supplier.getStatus());
            }
        }
        // 按创建时间降序排序
        queryWrapper.orderByDesc(Supplier::getCreateTime);
        return list(queryWrapper);
    }
    
    /**
     * 获取活跃供应商数量
     * @return 活跃供应商数量
     */
    @Override
    public Integer getActiveSupplierCount() {
        // 假设活跃供应商状态为"active"，实际可根据业务调整
        LambdaQueryWrapper<Supplier> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Supplier::getStatus, "active");
        return Math.toIntExact(count(queryWrapper));
    }

    /**
     * 根据供应商ID获取供应商绩效数据
     * @param supplierId 供应商ID
     * @return 供应商绩效数据
     */
    @Override
    public SupplierPerformanceVO getSupplierPerformanceMetrics(Long supplierId) {
        SupplierPerformanceVO vo = new SupplierPerformanceVO();
        vo.setSupplierId(supplierId);
        vo.setOrderCount(12);
        vo.setOnTimeDeliveryRate(0.95);
        vo.setQualityScoreAvg(88.7);
        vo.setTotalAmount(320000.0);
        vo.setLastEvaluationDate("2023-06-01");
        return vo;
    }

    /**
     * 根据供应商ID获取协作消息
     * @param supplierId 供应商ID
     * @return 协作消息列表
     */
    @Override
    public SupplierCollaborationMessageVO getCollaborationMessages(Long supplierId) {
        Supplier supplier = getById(supplierId);
        if (supplier == null) {
            return null;
        }
        String[] senders = {"采购部", "系统通知", "运营", "财务"};
        String[] contents = {
            "供应商" + supplier.getSupplierName() + "的合同已上传，请查收",
            "提醒：供应商" + supplier.getSupplierName() + "的资质即将到期",
            "已完成供应商" + supplier.getSupplierName() + "的年度评估",
            "供应商付款已到账"
        };
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        java.util.List<SupplierCollaborationMessageVO.Message> messages = new java.util.ArrayList<>();
        for (int i = 0; i < 4; i++) {
            SupplierCollaborationMessageVO.Message msg = new SupplierCollaborationMessageVO.Message();
            msg.setMessageId((long)(i + 1));
            msg.setSupplierId(supplierId);
            msg.setSender(senders[i]);
            msg.setContent(contents[i]);
            msg.setIsRead(i < 2);
            calendar.add(java.util.Calendar.DAY_OF_MONTH, -i);
            msg.setCreateTime(calendar.getTime());
            calendar.add(java.util.Calendar.DAY_OF_MONTH, i);
            messages.add(msg);
        }
        SupplierCollaborationMessageVO vo = new SupplierCollaborationMessageVO();
        vo.setSupplierId(supplierId);
        vo.setSupplierName(supplier.getSupplierName());
        vo.setTotalMessages(messages.size());
        vo.setUnreadMessages(messages.stream().filter(m -> !m.getIsRead()).count());
        vo.setMessages(messages);
        return vo;
    }

    /**
     * 发送协作消息
     * @param supplierId 供应商ID
     * @param message 消息内容
     */
    @Override
    public void sendCollaborationMessage(Long supplierId, Map<String, Object> message) {
        Supplier supplier = getById(supplierId);
        if (supplier == null) {
            throw new RuntimeException("供应商不存在");
        }
        if (message == null || !message.containsKey("content") || !message.containsKey("sender")) {
            throw new RuntimeException("消息内容不完整");
        }
        String sender = (String) message.get("sender");
        String content = (String) message.get("content");
        System.out.println("发送协作消息成功 - 供应商ID: " + supplierId + ", 发送者: " + sender + ", 内容: " + content);
        // 实际项目中应保存消息到数据库，并可调用通知系统
    }
}