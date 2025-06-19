import request from '@/utils/request'

// ==================== 系统配置相关接口 ====================

/**
 * 获取系统配置列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getSystemConfigList(params) {
  return request({
    url: '/sys/workflow/config/system/list',
    method: 'get',
    params
  })
}

/**
 * 保存系统配置
 * @param {Object} data 配置数据
 * @returns {Promise}
 */
export function saveSystemConfig(data) {
  return request({
    url: '/sys/workflow/config/system/save',
    method: 'post',
    data
  })
}

/**
 * 删除系统配置
 * @param {String} id 配置ID
 * @returns {Promise}
 */
export function deleteSystemConfig(id) {
  return request({
    url: `/sys/workflow/config/system/delete/${id}`,
    method: 'delete'
  })
}

/**
 * 批量更新系统配置状态
 * @param {Object} data 更新数据
 * @returns {Promise}
 */
export function batchUpdateSystemConfigStatus(data) {
  return request({
    url: '/sys/workflow/config/system/batch-status',
    method: 'put',
    data
  })
}

/**
 * 获取系统配置统计信息
 * @returns {Promise}
 */
export function getSystemConfigStats() {
  return request({
    url: '/sys/workflow/config/system/stats',
    method: 'get'
  })
}

// ==================== 权限相关接口 (统一使用UnifiedPermissionController) ====================

/**
 * 获取权限树
 * @param {String} permissionType 权限类型
 * @returns {Promise}
 */
export function getPermissionTree(permissionType) {
  return request({
    url: '/admin/unified-permission/permissions/tree',
    method: 'get',
    params: { permissionType }
  })
}

/**
 * 根据角色ID获取权限列表
 * @param {Number} roleId 角色ID
 * @returns {Promise}
 */
export function getPermissionsByRoleId(roleId) {
  return request({
    url: `/admin/unified-permission/permissions/role/${roleId}`,
    method: 'get'
  })
}

/**
 * 保存权限
 * @param {Object} data 权限数据
 * @returns {Promise}
 */
export function savePermission(data) {
  return request({
    url: '/admin/unified-permission/permissions',
    method: 'post',
    data
  })
}

/**
 * 更新权限
 * @param {Object} data 权限数据
 * @returns {Promise}
 */
export function updatePermission(data) {
  return request({
    url: '/admin/unified-permission/permissions',
    method: 'put',
    data
  })
}

/**
 * 删除权限
 * @param {Number} permissionId 权限ID
 * @returns {Promise}
 */
export function deletePermission(permissionId) {
  return request({
    url: `/admin/unified-permission/permissions/${permissionId}`,
    method: 'delete'
  })
}

// ==================== 角色相关接口 (统一使用UnifiedPermissionController) ====================

/**
 * 分页查询角色
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRoleList(params) {
  return request({
    url: '/admin/unified-permission/roles/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询角色
 * @param {Number} roleId 角色ID
 * @returns {Promise}
 */
export function getRoleById(roleId) {
  return request({
    url: `/admin/unified-permission/roles/${roleId}`,
    method: 'get'
  })
}

/**
 * 新增角色
 * @param {Object} data 角色数据
 * @returns {Promise}
 */
export function saveRole(data) {
  return request({
    url: '/admin/unified-permission/roles',
    method: 'post',
    data
  })
}

/**
 * 修改角色
 * @param {Object} data 角色数据
 * @returns {Promise}
 */
export function updateRole(data) {
  return request({
    url: '/admin/unified-permission/roles',
    method: 'put',
    data
  })
}

/**
 * 删除角色
 * @param {Number} roleId 角色ID
 * @returns {Promise}
 */
export function deleteRole(roleId) {
  return request({
    url: `/admin/unified-permission/roles/${roleId}`,
    method: 'delete'
  })
}

/**
 * 为角色分配权限
 * @param {Number} roleId 角色ID
 * @param {Array} permissionIds 权限ID列表
 * @returns {Promise}
 */
export function assignPermissionsToRole(roleId, permissionIds) {
  return request({
    url: '/admin/unified-permission/role-permissions/assign',
    method: 'post',
    data: { roleId, permissionIds }
  })
}

/**
 * 获取工作流角色列表
 * @param {String} processDefinitionKey 流程定义Key
 * @returns {Promise}
 */
export function getWorkflowRoles(processDefinitionKey) {
  return request({
    url: '/admin/unified-permission/sys/workflow/roles',
    method: 'get',
    params: { processDefinitionKey }
  })
}

/**
 * 获取工作流权限列表
 * @param {String} processDefinitionKey 流程定义Key
 * @returns {Promise}
 */
export function getWorkflowPermissions(processDefinitionKey) {
  return request({
    url: '/admin/unified-permission/sys/workflow/permissions',
    method: 'get',
    params: { processDefinitionKey }
  })
}

// ==================== 用户角色相关接口 (统一使用UnifiedPermissionController) ====================

/**
 * 根据用户ID获取角色列表
 * @param {Number} userId 用户ID
 * @returns {Promise}
 */
export function getUserRoles(userId) {
  return request({
    url: `/admin/unified-permission/user-roles/user/${userId}`,
    method: 'get'
  })
}

/**
 * 为用户分配角色
 * @param {Number} userId 用户ID
 * @param {Array} roleIds 角色ID列表
 * @returns {Promise}
 */
export function assignRolesToUser(userId, roleIds) {
  return request({
    url: '/admin/unified-permission/user-roles/assign',
    method: 'post',
    data: { userId, roleIds }
  })
}

/**
 * 获取用户权限列表
 * @param {Number} userId 用户ID
 * @returns {Promise}
 */
export function getUserPermissions(userId) {
  return request({
    url: `/admin/unified-permission/user-permissions/${userId}`,
    method: 'get'
  })
}

/**
 * 检查用户是否有指定权限
 * @param {Number} userId 用户ID
 * @param {String} permission 权限标识
 * @returns {Promise}
 */
export function checkUserPermission(userId, permission) {
  return request({
    url: '/admin/unified-permission/user-permissions/check',
    method: 'get',
    params: { userId, permission }
  })
}

/**
 * 获取当前用户权限列表
 * @returns {Promise}
 */
export function getCurrentUserPermissions() {
  return request({
    url: '/admin/unified-permission/current-user/permissions',
    method: 'get'
  })
}

/**
 * 检查当前用户是否有指定权限
 * @param {String} permission 权限标识
 * @returns {Promise}
 */
export function checkCurrentUserPermission(permission) {
  return request({
    url: '/admin/unified-permission/current-user/permissions/check',
    method: 'get',
    params: { permission }
  })
}

/**
 * 获取当前用户角色列表
 * @returns {Promise}
 */
export function getCurrentUserRoles() {
  return request({
    url: '/admin/unified-permission/current-user/roles',
    method: 'get'
  })
}

// ==================== 流程分类相关接口 ====================

/**
 * 获取流程分类树
 * @returns {Promise}
 */
export function getCategoryTree() {
  return request({
    url: '/sys/workflow/config/category/tree',
    method: 'get'
  })
}

/**
 * 获取流程分类列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCategoryList(params) {
  return request({
    url: '/sys/workflow/config/category/list',
    method: 'get',
    params
  })
}

/**
 * 保存流程分类
 * @param {Object} data 分类数据
 * @returns {Promise}
 */
export function saveCategory(data) {
  return request({
    url: '/sys/workflow/config/category/save',
    method: 'post',
    data
  })
}

/**
 * 删除流程分类
 * @param {String} id 分类ID
 * @returns {Promise}
 */
export function deleteCategory(id) {
  return request({
    url: `/sys/workflow/config/category/delete/${id}`,
    method: 'delete'
  })
}

/**
 * 批量更新分类状态
 * @param {Object} data 更新数据
 * @returns {Promise}
 */
export function batchUpdateCategoryStatus(data) {
  return request({
    url: '/sys/workflow/config/category/batch-status',
    method: 'put',
    data
  })
}

/**
 * 获取分类统计信息
 * @returns {Promise}
 */
export function getCategoryStats() {
  return request({
    url: '/sys/workflow/config/category/stats',
    method: 'get'
  })
}

// ==================== 通知模板相关接口 ====================

/**
 * 获取通知模板列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getNotificationList(params) {
  return request({
    url: '/sys/workflow/config/notification/list',
    method: 'get',
    params
  })
}

/**
 * 保存通知模板
 * @param {Object} data 模板数据
 * @returns {Promise}
 */
export function saveNotification(data) {
  return request({
    url: '/sys/workflow/config/notification/save',
    method: 'post',
    data
  })
}

/**
 * 删除通知模板
 * @param {String} id 模板ID
 * @returns {Promise}
 */
export function deleteNotification(id) {
  return request({
    url: `/sys/workflow/config/notification/delete/${id}`,
    method: 'delete'
  })
}

/**
 * 批量更新模板状态
 * @param {Object} data 更新数据
 * @returns {Promise}
 */
export function batchUpdateNotificationStatus(data) {
  return request({
    url: '/sys/workflow/config/notification/batch-status',
    method: 'put',
    data
  })
}

/**
 * 获取模板类型列表
 * @returns {Promise}
 */
export function getTemplateTypes() {
  return request({
    url: '/sys/workflow/config/notification/template-types',
    method: 'get'
  })
}

/**
 * 获取事件类型列表
 * @returns {Promise}
 */
export function getEventTypes() {
  return request({
    url: '/sys/workflow/config/notification/event-types',
    method: 'get'
  })
}

/**
 * 预览通知模板
 * @param {String} id 模板ID
 * @param {Object} data 预览数据
 * @returns {Promise}
 */
export function previewNotification(id, data) {
  return request({
    url: `/sys/workflow/config/notification/preview/${id}`,
    method: 'post',
    data
  })
}

/**
 * 获取通知模板统计信息
 * @returns {Promise}
 */
export function getNotificationStats() {
  return request({
    url: '/sys/workflow/config/notification/stats',
    method: 'get'
  })
}

// ==================== 报表配置相关接口 ====================

/**
 * 获取报表配置列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getReportConfigList(params) {
  return request({
    url: '/sys/workflow/config/report/list',
    method: 'get',
    params
  })
}

/**
 * 保存报表配置
 * @param {Object} data 报表数据
 * @returns {Promise}
 */
export function saveReportConfig(data) {
  return request({
    url: '/sys/workflow/config/report/save',
    method: 'post',
    data
  })
}

/**
 * 删除报表配置
 * @param {String} id 报表ID
 * @returns {Promise}
 */
export function deleteReportConfig(id) {
  return request({
    url: `/sys/workflow/config/report/delete/${id}`,
    method: 'delete'
  })
}

/**
 * 批量更新报表状态
 * @param {Object} data 更新数据
 * @returns {Promise}
 */
export function batchUpdateReportStatus(data) {
  return request({
    url: '/sys/workflow/config/report/batch-status',
    method: 'put',
    data
  })
}

/**
 * 获取报表类型列表
 * @returns {Promise}
 */
export function getReportTypes() {
  return request({
    url: '/sys/workflow/config/report/types',
    method: 'get'
  })
}

/**
 * 获取报表统计信息
 * @returns {Promise}
 */
export function getReportStats() {
  return request({
    url: '/sys/workflow/config/report/stats',
    method: 'get'
  })
}

/**
 * 更新报表使用次数
 * @param {String} id 报表ID
 * @returns {Promise}
 */
export function updateReportUsageCount(id) {
  return request({
    url: `/sys/workflow/config/report/usage/${id}`,
    method: 'put'
  })
}

// ==================== 工作流实例相关接口 ====================

/**
 * 获取工作流实例列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getInstanceList(params) {
  return request({
    url: '/sys/workflow/config/instance/list',
    method: 'get',
    params
  })
}

/**
 * 获取实例统计信息
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getInstanceStats(params) {
  return request({
    url: '/sys/workflow/config/instance/stats',
    method: 'get',
    params
  })
}

/**
 * 批量更新实例状态
 * @param {Object} data 更新数据
 * @returns {Promise}
 */
export function batchUpdateInstanceStatus(data) {
  return request({
    url: '/sys/workflow/config/instance/batch-status',
    method: 'put',
    data
  })
}

// ==================== 工作流任务相关接口 ====================

/**
 * 获取工作流任务列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getTaskList(params) {
  return request({
    url: '/sys/workflow/config/task/list',
    method: 'get',
    params
  })
}

/**
 * 获取任务统计信息
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getTaskStats(params) {
  return request({
    url: '/sys/workflow/config/task/stats',
    method: 'get',
    params
  })
}

/**
 * 批量更新任务状态
 * @param {Object} data 更新数据
 * @returns {Promise}
 */
export function batchUpdateTaskStatus(data) {
  return request({
    url: '/sys/workflow/config/task/batch-status',
    method: 'put',
    data
  })
}

/**
 * 批量分配任务
 * @param {Object} data 分配数据
 * @returns {Promise}
 */
/**
 * 获取流程定义列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getProcessDefinitionList(params) {
  return request({
    url: '/sys/workflow/config/process/definition/list',
    method: 'get',
    params
  })
}

export function batchAssignTask(data) {
  return request({
    url: '/sys/workflow/config/task/batch-assign',
    method: 'post',
    data
  })
}