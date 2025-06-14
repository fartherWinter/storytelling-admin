package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;

/**
 * 基础Mapper接口，继承MyBatis-Plus的BaseMapper
 * 提供通用的CRUD操作方法
 * 
 * @param <T> 实体类型
 * @author storytelling
 * @date 2024-01-01
 */
public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

    // 继承MyBatis-Plus的BaseMapper，无需重新声明基础CRUD方法

    /**
     * 批量插入实体
     * 注意：此方法为逐条插入，如需真正的批量插入请使用MyBatis-Plus的批量插入功能
     * 
     * @param entityList 实体列表
     * @return 影响行数
     */
    default int insertBatch(List<T> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (T entity : entityList) {
            count += insert(entity);
        }
        return count;
    }

    /**
     * 批量更新实体
     * 注意：此方法为逐条更新，如需真正的批量更新请使用MyBatis-Plus的批量更新功能
     * 
     * @param entityList 实体列表
     * @return 影响行数
     */
    default int updateBatch(List<T> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (T entity : entityList) {
            count += updateById(entity);
        }
        return count;
    }

    /**
     * 保存或更新实体
     * 如果实体ID为空则插入，否则更新
     * 
     * @param entity 实体对象
     * @return 影响行数
     */
    default int saveOrUpdate(T entity) {
        if (entity == null) {
            return 0;
        }
        try {
            // 通过反射获取ID字段
            java.lang.reflect.Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Object id = idField.get(entity);
            
            if (id == null) {
                return insert(entity);
            } else {
                return updateById(entity);
            }
        } catch (Exception e) {
            // 如果获取ID失败，默认执行插入操作
            return insert(entity);
        }
    }

    /**
 * 根据条件查询是否存在记录
 * 
 * @param queryWrapper 查询条件
 * @return 是否存在
 */
    default boolean exists(Wrapper<T> queryWrapper) {
        return selectCount(queryWrapper) > 0;
    }

    /**
 * 分页查询所有记录
 * 
 * @param current 当前页
 * @param size 每页大小
 * @return 分页结果
 */
    default IPage<T> selectPage(long current, long size) {
        Page<T> page = new Page<>(current, size);
        return selectPage(page, null);
    }

    /**
 * 查询所有记录
 * 
 * @return 所有记录列表
 */
    default List<T> selectAll() {
        return selectList(null);
    }

    /**
 * 统计所有记录数
 * 
 * @return 总记录数
 */
    default Long countAll() {
        return selectCount(null);
    }

    /**
 * 清空表数据
 * 
 * @return 影响行数
 */
    default int deleteAll() {
        return delete(null);
    }

    /**
 * 根据字段值查询实体
 * 
 * @param column 字段名
 * @param value 字段值
 * @return 实体对象
 */
    default T selectByColumn(String column, Object value) {
        Map<String, Object> columnMap = new java.util.HashMap<>();
        columnMap.put(column, value);
        List<T> list = selectByMap(columnMap);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
 * 根据字段值查询实体列表
 * 
 * @param column 字段名
 * @param value 字段值
 * @return 实体列表
 */
    default List<T> selectListByColumn(String column, Object value) {
        Map<String, Object> columnMap = new java.util.HashMap<>();
        columnMap.put(column, value);
        return selectByMap(columnMap);
    }

    /**
 * 根据字段值删除记录
 * 
 * @param column 字段名
 * @param value 字段值
 * @return 影响行数
 */
    default int deleteByColumn(String column, Object value) {
        Map<String, Object> columnMap = new java.util.HashMap<>();
        columnMap.put(column, value);
        return deleteByMap(columnMap);
    }

    /**
     * 根据字段值统计记录数
     * 
     * @param column 字段名
     * @param value 字段值
     * @return 记录数
     */
    default Long countByColumn(String column, Object value) {
        if (column == null || column.trim().isEmpty()) {
            return 0L;
        }
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(column, value);
        return selectCount(queryWrapper);
    }
}