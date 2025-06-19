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

    /**
     * 根据配置键列表批量查询配置
     * 
     * @param configKeys 配置键列表
     * @return 配置列表
     */
    List<WfSystemConfig> selectByConfigKeys(@Param("configKeys") List<String> configKeys);

    /**
     * 查询系统配置详情
     * 
     * @param configId 配置ID
     * @return 系统配置
     */
    WfSystemConfig selectConfigDetail(@Param("configId") String configId);

    /**
     * 重置配置为默认值
     * 
     * @param configKey 配置键
     * @param updatedBy 更新人
     * @return 更新行数
     */
    int resetConfigToDefault(@Param("configKey") String configKey, 
                            @Param("updatedBy") String updatedBy);

    /**
     * 批量重置配置为默认值
     * 
     * @param configKeys 配置键列表
     * @param updatedBy 更新人
     * @return 更新行数
     */
    int batchResetConfigsToDefault(@Param("configKeys") List<String> configKeys, 
                                  @Param("updatedBy") String updatedBy);

    /**
     * 导出系统配置
     * 
     * @param configGroup 配置分组
     * @return 配置列表
     */
    List<WfSystemConfig> exportConfigs(@Param("configGroup") String configGroup);

    /**
     * 查询配置变更历史
     * 
     * @param configKey 配置键
     * @return 变更历史
     */
    List<java.util.Map<String, Object>> selectConfigHistory(@Param("configKey") String configKey);
}