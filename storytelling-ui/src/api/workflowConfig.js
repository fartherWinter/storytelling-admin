import request from '@/utils/request'

// ==================== 系统配置相关接口 ====================

/**
 * 获取系统配置列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getSystemConfigList(params) {
  return request({
    url: '/workflow/config/system/list',
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
    url: '/workflow/config/system/save',
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
    url: `/workflow/config/system/delete/${id}`,
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
    url: '/workflow/config/system/batch-status',
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
    url: '/workflow/config/system/stats',
    method: 'get'
  })
}

// ==================== 权限相关接口 ====================

/**
 * 获取权限树
 * @returns {Promise}
 */
export function getPermissionTree() {
  return request({
    url: '/workflow/config/permission/tree',
    method: 'get'
  })
}

/**
 * 获取权限列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getPermissionList(params) {
  return request({
    url: '/workflow/config/permission/list',
    method: 'get',
    params
  })
}

/**
 * 保存权限
 * @param {Object} data 权限数据
 * @returns {Promise}
 */
export function savePermission(data) {
  return request({
    url: '/workflow/config/permission/save',
    method: 'post',
    data
  })
}

/**
 * 删除权限
 * @param {String} id 权限ID
 * @returns {Promise}
 */
export function deletePermission(id) {
  return request({
    url: `/workflow/config/permission/delete/${id}`,
    method: 'delete'
  })
}

/**
 * 批量更新权限状态
 * @param {Object} data 更新数据
 * @returns {Promise}
 */
export function batchUpdatePermissionStatus(data) {
  return request({
    url: '/workflow/config/permission/batch-status',
    method: 'put',
    data
  })
}

// ==================== 角色相关接口 ====================

/**
 * 获取角色列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRoleList(params) {
  return request({
    url: '/workflow/config/role/list',
    method: 'get',
    params
  })
}

/**
 * 保存角色
 * @param {Object} data 角色数据
 * @returns {Promise}
 */
export function saveRole(data) {
  return request({
    url: '/workflow/config/role/save',
    method: 'post',
    data
  })
}

/**
 * 删除角色
 * @param {String} id 角色ID
 * @returns {Promise}
 */
export function deleteRole(id) {
  return request({
    url: `/workflow/config/role/delete/${id}`,
    method: 'delete'
  })
}

/**
 * 获取角色权限
 * @param {String} roleId 角色ID
 * @returns {Promise}
 */
export function getRolePermissions(roleId) {
  return request({
    url: `/workflow/config/role/${roleId}/permissions`,
    method: 'get'
  })
}

/**
 * 保存角色权限
 * @param {Object} data 角色权限数据
 * @returns {Promise}
 */
export function saveRolePermissions(data) {
  return request({
    url: '/workflow/config/role/permissions',
    method: 'post',
    data
  })
}

/**
 * 批量删除角色权限关联
 * @param {Object} data 删除数据
 * @returns {Promise}
 */
export function batchDeleteRolePermissions(data) {
  return request({
    url: '/workflow/config/role/permissions/batch-delete',
    method: 'delete',
    data
  })
}

/**
 * 获取角色统计信息
 * @returns {Promise}
 */
export function getRoleStats() {
  return request({
    url: '/workflow/config/role/stats',
    method: 'get'
  })
}

// ==================== 用户角色相关接口 ====================

/**
 * 获取用户角色列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getUserRoleList(params) {
  return request({
    url: '/workflow/config/user-role/list',
    method: 'get',
    params
  })
}

/**
 * 保存用户角色关联
 * @param {Object} data 用户角色数据
 * @returns {Promise}
 */
export function saveUserRole(data) {
  return request({
    url: '/workflow/config/user-role/save',
    method: 'post',
    data
  })
}

/**
 * 删除用户角色关联
 * @param {String} userId 用户ID
 * @param {String} roleId 角色ID
 * @returns {Promise}
 */
export function deleteUserRole(userId, roleId) {
  return request({
    url: `/workflow/config/user-role/delete/${userId}/${roleId}`,
    method: 'delete'
  })
}

/**
 * 批量保存用户角色关联
 * @param {Object} data 批量数据
 * @returns {Promise}
 */
export function batchSaveUserRole(data) {
  return request({
    url: '/workflow/config/user-role/batch-save',
    method: 'post',
    data
  })
}

// ==================== 流程分类相关接口 ====================

/**
 * 获取流程分类树
 * @returns {Promise}
 */
export function getCategoryTree() {
  return request({
    url: '/workflow/config/category/tree',
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
    url: '/workflow/config/category/list',
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
    url: '/workflow/config/category/save',
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
    url: `/workflow/config/category/delete/${id}`,
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
    url: '/workflow/config/category/batch-status',
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
    url: '/workflow/config/category/stats',
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
    url: '/workflow/config/notification/list',
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
    url: '/workflow/config/notification/save',
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
    url: `/workflow/config/notification/delete/${id}`,
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
    url: '/workflow/config/notification/batch-status',
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
    url: '/workflow/config/notification/template-types',
    method: 'get'
  })
}

/**
 * 获取事件类型列表
 * @returns {Promise}
 */
export function getEventTypes() {
  return request({
    url: '/workflow/config/notification/event-types',
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
    url: `/workflow/config/notification/preview/${id}`,
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
    url: '/workflow/config/notification/stats',
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
    url: '/workflow/config/report/list',
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
    url: '/workflow/config/report/save',
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
    url: `/workflow/config/report/delete/${id}`,
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
    url: '/workflow/config/report/batch-status',
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
    url: '/workflow/config/report/types',
    method: 'get'
  })
}

/**
 * 获取报表统计信息
 * @returns {Promise}
 */
export function getReportStats() {
  return request({
    url: '/workflow/config/report/stats',
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
    url: `/workflow/config/report/usage/${id}`,
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
    url: '/workflow/config/instance/list',
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
    url: '/workflow/config/instance/stats',
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
    url: '/workflow/config/instance/batch-status',
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
    url: '/workflow/config/task/list',
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
    url: '/workflow/config/task/stats',
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
    url: '/workflow/config/task/batch-status',
    method: 'put',
    data
  })
}

/**
 * 批量分配任务
 * @param {Object} data 分配数据
 * @returns {Promise}
 */
export function batchAssignTask(data) {
  return request({
    url: '/workflow/config/task/batch-assign',
    method: 'post',
    data
  })
}