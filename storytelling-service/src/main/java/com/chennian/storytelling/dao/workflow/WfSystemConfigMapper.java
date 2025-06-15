package com.chennian.storytelling.dao.workflow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.workflow.WfSystemConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统配置Mapper接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface WfSystemConfigMapper extends BaseMapper<WfSystemConfig> {

    /**
     * 根据配置键查询配置
     * 
     * @param configKey 配置键
     * @return 系统配置
     */
    WfSystemConfig selectByConfigKey(@Param("configKey") String configKey);

    /**
     * 根据配置分组查询配置列表
     * 
     * @param configGroup 配置分组
     * @return 配置列表
     */
    List<WfSystemConfig> selectByConfigGroup(@Param("configGroup") String configGroup);

    /**
     * 根据配置类型查询配置列表
     * 
     * @param configType 配置类型
     * @return 配置列表
     */
    List<WfSystemConfig> selectByConfigType(@Param("configType") String configType);

    /**
     * 查询可编辑的配置列表
     * 
     * @return 配置列表
     */
    List<WfSystemConfig> selectEditableConfigs();

    /**
     * 根据配置键更新配置值
     * 
     * @param configKey 配置键
     * @param configValue 配置值
     * @param updatedBy 更新人
     * @return 更新行数
     */
    int updateConfigValueByKey(@Param("configKey") String configKey, 
                              @Param("configValue") String configValue, 
                              @Param("updatedBy") String updatedBy);

    /**
     * 批量更新配置
     * 
     * @param configs 配置列表
     * @return 更新行数
     */
    int batchUpdateConfigs(@Param("configs") List<WfSystemConfig> configs);

    /**
     * 查询所有配置分组
     * 
     * @return 分组列表
     */
    List<String> selectAllConfigGroups();

    /**
     * 查询配置统计信息
     * 
     * @return 统计信息
     */
    List<java.util.Map<String, Object>> selectConfigStatistics();
}