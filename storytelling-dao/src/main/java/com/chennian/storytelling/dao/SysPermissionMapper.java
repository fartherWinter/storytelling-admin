package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.SysPermission;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 权限信息 数据层
 * 
 * @author chennian
 * @date 2024-01-20
 */
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    /**
     * 查询权限列表
     * 
     * @param permissionType 权限类型
     * @return 权限列表
     */
    List<SysPermission> selectPermissionList(@Param("permissionType") String permissionType);

    /**
     * 根据权限ID查询权限信息
     * 
     * @param permissionId 权限ID
     * @return 权限信息
     */
    SysPermission selectPermissionById(@Param("permissionId") Long permissionId);

    /**
     * 根据角色ID查询权限列表
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<SysPermission> selectPermissionsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查询权限编码列表
     * 
     * @param userId 用户ID
     * @return 权限编码列表
     */
    List<String> selectPermissionCodesByUserId(@Param("userId") Long userId);

    /**
     * 检查用户是否有指定权限
     * 
     * @param userId 用户ID
     * @param permissionCode 权限编码
     * @return 权限数量
     */
    int checkUserPermission(@Param("userId") Long userId, @Param("permissionCode") String permissionCode);

    /**
     * 查询工作流权限列表（支持分页和高级搜索）
     * 
     * @param page 分页参数
     * @param params 查询参数
     * @return 工作流权限列表
     */
    IPage<SysPermission> selectWorkflowPermissionsPage(IPage<SysPermission> page, @Param("params") Map<String, Object> params);

    /**
     * 查询工作流权限列表
     * 
     * @return 工作流权限列表
     */
    List<SysPermission> selectWorkflowPermissions();

    /**
     * 查询用户的工作流权限（支持分页和高级搜索）
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param params 查询参数
     * @return 工作流权限列表
     */
    IPage<SysPermission> selectWorkflowPermissionsByUserIdPage(IPage<SysPermission> page,
                                                             @Param("userId") Long userId,
                                                             @Param("params") Map<String, Object> params);

    /**
     * 查询用户的工作流权限
     * 
     * @param userId 用户ID
     * @return 工作流权限列表
     */
    List<SysPermission> selectWorkflowPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 查询用户在指定流程中的权限
     * 
     * @param userId 用户ID
     * @param processKey 流程标识
     * @return 权限列表
     */
    List<SysPermission> selectUserProcessPermissions(@Param("userId") Long userId, @Param("processKey") String processKey);

    /**
     * 查询用户在指定任务中的权限
     * 
     * @param userId 用户ID
     * @param taskId 任务ID
     * @return 权限列表
     */
    List<SysPermission> selectUserTaskPermissions(@Param("userId") Long userId, @Param("taskId") String taskId);

    /**
     * 批量授予工作流权限
     * 
     * @param userId 用户ID
     * @param permissionIds 权限ID列表
     * @param grantBy 授权人
     * @return 结果
     */
    int batchGrantWorkflowPermissions(@Param("userId") Long userId,
                                    @Param("permissionIds") List<Long> permissionIds,
                                    @Param("grantBy") String grantBy);

    /**
     * 批量撤销工作流权限
     * 
     * @param userId 用户ID
     * @param permissionIds 权限ID列表
     * @param revokeBy 撤销人
     * @return 结果
     */
    int batchRevokeWorkflowPermissions(@Param("userId") Long userId,
                                     @Param("permissionIds") List<Long> permissionIds,
                                     @Param("revokeBy") String revokeBy);

    /**
     * 查询用户的工作流权限历史
     * 
     * @param userId 用户ID
     * @return 权限历史列表
     */
    List<Map<String, Object>> selectUserWorkflowPermissionHistory(@Param("userId") Long userId);

    /**
     * 统计工作流权限分布
     * 
     * @return 统计结果
     */
    List<Map<String, Object>> countWorkflowPermissionDistribution();

    /**
     * 统计用户工作流权限使用情况
     * 
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    List<Map<String, Object>> countUserWorkflowPermissionUsage(@Param("userId") Long userId,
                                                             @Param("startTime") String startTime,
                                                             @Param("endTime") String endTime);

    /**
     * 查询工作流权限模板列表
     * 
     * @return 权限模板列表
     */
    List<SysPermission> selectWorkflowPermissionTemplates();

    /**
     * 根据模板批量创建工作流权限
     * 
     * @param templateId 模板ID
     * @param processKey 流程标识
     * @param createBy 创建人
     * @return 结果
     */
    int createWorkflowPermissionsByTemplate(@Param("templateId") Long templateId,
                                          @Param("processKey") String processKey,
                                          @Param("createBy") String createBy);

    /**
     * 检查用户是否有指定工作流权限
     * 
     * @param userId 用户ID
     * @param processKey 流程标识
     * @param permissionCode 权限编码
     * @return 权限数量
     */
    int checkUserWorkflowPermission(@Param("userId") Long userId,
                                  @Param("processKey") String processKey,
                                  @Param("permissionCode") String permissionCode);

    /**
     * 查询工作流权限继承关系
     * 
     * @param permissionId 权限ID
     * @return 继承关系列表
     */
    List<Map<String, Object>> selectWorkflowPermissionInheritance(@Param("permissionId") Long permissionId);

    /**
     * 更新工作流权限继承关系
     * 
     * @param permissionId 权限ID
     * @param parentIds 父权限ID列表
     * @param updateBy 更新人
     * @return 结果
     */
    int updateWorkflowPermissionInheritance(@Param("permissionId") Long permissionId,
                                          @Param("parentIds") List<Long> parentIds,
                                          @Param("updateBy") String updateBy);

    /**
     * 新增权限信息
     * 
     * @param permission 权限信息
     * @return 结果
     */
    int insertPermission(SysPermission permission);

    /**
     * 修改权限信息
     * 
     * @param permission 权限信息
     * @return 结果
     */
    int updatePermission(SysPermission permission);

    /**
     * 删除权限信息
     * 
     * @param permissionId 权限ID
     * @return 结果
     */
    int deletePermissionById(@Param("permissionId") Long permissionId);

    /**
     * 批量删除权限信息
     * 
     * @param permissionIds 需要删除的权限ID
     * @return 结果
     */
    int deletePermissionByIds(@Param("permissionIds") Long[] permissionIds);

    /**
     * 校验权限编码是否唯一
     * 
     * @param permissionCode 权限编码
     * @param permissionId 权限ID
     * @return 结果
     */
    int checkPermissionCodeUnique(@Param("permissionCode") String permissionCode, @Param("permissionId") Long permissionId);

    /**
     * 统计子权限数量
     * 
     * @param permissionId 权限ID
     * @return 子权限数量
     */
    int countChildPermissions(@Param("permissionId") Long permissionId);

    /**
     * 根据父权限ID查询子权限列表
     * 
     * @param parentId 父权限ID
     * @return 子权限列表
     */
    List<SysPermission> selectPermissionsByParentId(@Param("parentId") Long parentId);

    /**
     * 查询所有权限（用于构建权限树）
     * 
     * @return 权限列表
     */
    List<SysPermission> selectAllPermissions();

    /**
     * 根据权限编码查询权限信息
     * 
     * @param permissionCode 权限编码
     * @return 权限信息
     */
    SysPermission selectPermissionByCode(@Param("permissionCode") String permissionCode);

    /**
     * 根据权限类型和状态查询权限列表
     * 
     * @param permissionType 权限类型
     * @param status 状态
     * @return 权限列表
     */
    List<SysPermission> selectPermissionsByTypeAndStatus(@Param("permissionType") String permissionType, @Param("status") String status);

    /**
     * 查询用户的菜单权限
     * 
     * @param userId 用户ID
     * @return 菜单权限列表
     */
    List<SysPermission> selectMenuPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的按钮权限
     * 
     * @param userId 用户ID
     * @return 按钮权限列表
     */
    List<SysPermission> selectButtonPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的API权限
     * 
     * @param userId 用户ID
     * @return API权限列表
     */
    List<SysPermission> selectApiPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 根据资源URL查询权限信息
     * 
     * @param resourceUrl 资源URL
     * @return 权限列表
     */
    List<SysPermission> selectPermissionsByResourceUrl(@Param("resourceUrl") String resourceUrl);

    /**
     * 批量插入权限信息
     * 
     * @param permissions 权限列表
     * @return 结果
     */
    int batchInsertPermissions(@Param("permissions") List<SysPermission> permissions);

    /**
     * 批量更新权限状态
     * 
     * @param permissionIds 权限ID列表
     * @param status 状态
     * @param updateBy 更新者
     * @return 结果
     */
    int batchUpdatePermissionStatus(@Param("permissionIds") List<Long> permissionIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 查询权限统计信息
     * 
     * @return 统计信息
     */
    @MapKey("type")
    List<Map<String, Object>> selectPermissionStatistics();

    /**
     * 根据权限名称模糊查询
     * 
     * @param permissionName 权限名称
     * @return 权限列表
     */
    List<SysPermission> selectPermissionsByNameLike(@Param("permissionName") String permissionName);

    /**
     * 根据权限编码模糊查询
     * 
     * @param permissionCode 权限编码
     * @return 权限列表
     */
    List<SysPermission> selectPermissionsByCodeLike(@Param("permissionCode") String permissionCode);

    /**
     * 查询最大排序值
     * 
     * @param parentId 父权限ID
     * @return 最大排序值
     */
    Integer selectMaxSortOrder(@Param("parentId") Long parentId);

    /**
     * 更新权限排序
     * 
     * @param permissionId 权限ID
     * @param sortOrder 排序值
     * @return 结果
     */
    int updatePermissionSortOrder(@Param("permissionId") Long permissionId, @Param("sortOrder") Integer sortOrder);

    /**
     * 查询权限路径（从根到当前权限的完整路径）
     * 
     * @param permissionId 权限ID
     * @return 权限路径
     */
    List<SysPermission> selectPermissionPath(@Param("permissionId") Long permissionId);
}