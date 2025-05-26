package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.Supplier;
import com.chennian.storytelling.bean.vo.SupplierCollaborationMessageVO;
import com.chennian.storytelling.bean.vo.SupplierPerformanceVO;
import com.chennian.storytelling.common.utils.PageParam;

import java.util.List;
import java.util.Map;

/**
 * 供应商服务接口
 * @author chennian
 */
public interface SupplierService extends IService<Supplier> {

    /**
     * 分页查询供应商列表
     * @param page 分页参数
     * @param supplier 查询条件
     * @return 供应商分页数据
     */
    IPage<Supplier> findByPage(PageParam<Supplier> page, Supplier supplier);

    /**
     * 根据ID查询供应商
     * @param supplierId 供应商ID
     * @return 供应商信息
     */
    Supplier selectSupplierById(Long supplierId);

    /**
     * 新增供应商
     * @param supplier 供应商信息
     * @return 结果
     */
    int insertSupplier(Supplier supplier);

    /**
     * 修改供应商
     * @param supplier 供应商信息
     * @return 结果
     */
    int updateSupplier(Supplier supplier);

    /**
     * 批量删除供应商
     * @param supplierIds 需要删除的供应商ID数组
     * @return 结果
     */
    int deleteSupplierByIds(Long[] supplierIds);

    /**
     * 更新供应商状态
     * @param supplier 供应商信息
     * @return 结果
     */
    int updateSupplierStatus(Supplier supplier);
    
    /**
     * 获取供应商评估记录
     * @param supplierId 供应商ID
     * @return 评估记录列表
     */
    List<Map<String, Object>> getEvaluationRecords(Long supplierId);
    
    /**
     * 添加供应商评估记录
     * @param evaluation 评估信息
     * @return 结果
     */
    int addEvaluationRecord(Map<String, Object> evaluation);
    
    /**
     * 获取供应商合作历史
     * @param supplierId 供应商ID
     * @return 合作历史列表
     */
    List<Map<String, Object>> getCooperationHistory(Long supplierId);
    
    /**
     * 根据条件查询供应商列表
     * @param supplier 查询条件
     * @return 供应商列表
     */
    List<Supplier> selectSupplierList(Supplier supplier);

    Integer getActiveSupplierCount();

    SupplierPerformanceVO getSupplierPerformanceMetrics(Long supplierId);

    SupplierCollaborationMessageVO getCollaborationMessages(Long supplierId);

    void sendCollaborationMessage(Long supplierId, Map<String, Object> message);
}