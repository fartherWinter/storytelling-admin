import request from '@/utils/request'

// ========================= 用户管理 API =========================

/**
 * 用户分页查询
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getUserPage(params) {
  return request({
    url: '/system/user/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取用户详情
 * @param {Number} userId 用户ID
 * @returns {Promise}
 */
export function getUserInfo(userId) {
  return request({
    url: `/system/user/info/${userId}`,
    method: 'post'
  })
}

/**
 * 新增用户
 * @param {Object} data 用户信息
 * @returns {Promise}
 */
export function addUser(data) {
  return request({
    url: '/system/user/add',
    method: 'post',
    data
  })
}

/**
 * 修改用户
 * @param {Object} data 用户信息
 * @returns {Promise}
 */
export function updateUser(data) {
  return request({
    url: '/system/user/update',
    method: 'post',
    data
  })
}

/**
 * 删除用户
 * @param {Array} userIds 用户ID数组
 * @returns {Promise}
 */
export function deleteUser(userIds) {
  return request({
    url: `/system/user/remove/${userIds.join(',')}`,
    method: 'post'
  })
}

/**
 * 修改用户状态
 * @param {Object} data 用户状态信息
 * @returns {Promise}
 */
export function changeUserStatus(data) {
  return request({
    url: '/system/user/changeStatus',
    method: 'post',
    data
  })
}

/**
 * 重置用户密码
 * @param {Number} userId 用户ID
 * @param {String} password 新密码
 * @returns {Promise}
 */
export function resetUserPassword(userId, password) {
  return request({
    url: '/system/user/resetPassword',
    method: 'post',
    data: { userId, password }
  })
}

/**
 * 获取用户个人信息
 * @returns {Promise}
 */
export function getUserProfile() {
  return request({
    url: '/system/user/profile',
    method: 'get'
  })
}

/**
 * 修改用户个人信息
 * @param {Object} data 用户信息
 * @returns {Promise}
 */
export function updateUserProfile(data) {
  return request({
    url: '/system/user/profile',
    method: 'put',
    data
  })
}

/**
 * 修改用户密码
 * @param {String} oldPassword 旧密码
 * @param {String} newPassword 新密码
 * @returns {Promise}
 */
export function updateUserPassword(oldPassword, newPassword) {
  return request({
    url: '/system/user/profile/updatePwd',
    method: 'put',
    data: { oldPassword, newPassword }
  })
}

// ========================= 角色管理 API =========================

/**
 * 角色分页查询
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getRolePage(params) {
  return request({
    url: '/system/role/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取角色详情
 * @param {Number} roleId 角色ID
 * @returns {Promise}
 */
export function getRoleInfo(roleId) {
  return request({
    url: `/system/role/info/${roleId}`,
    method: 'post'
  })
}

/**
 * 新增角色
 * @param {Object} data 角色信息
 * @returns {Promise}
 */
export function addRole(data) {
  return request({
    url: '/system/role/add',
    method: 'post',
    data
  })
}

/**
 * 修改角色
 * @param {Object} data 角色信息
 * @returns {Promise}
 */
export function updateRole(data) {
  return request({
    url: '/system/role/update',
    method: 'post',
    data
  })
}

/**
 * 删除角色
 * @param {Array} roleIds 角色ID数组
 * @returns {Promise}
 */
export function deleteRole(roleIds) {
  return request({
    url: `/system/role/remove/${roleIds.join(',')}`,
    method: 'post'
  })
}

/**
 * 修改角色状态
 * @param {Object} data 角色状态信息
 * @returns {Promise}
 */
export function changeRoleStatus(data) {
  return request({
    url: '/system/role/changeStatus',
    method: 'post',
    data
  })
}

/**
 * 获取角色菜单权限
 * @param {Number} roleId 角色ID
 * @returns {Promise}
 */
export function getRoleMenus(roleId) {
  return request({
    url: `/system/role/menus/${roleId}`,
    method: 'get'
  })
}

/**
 * 分配角色菜单权限
 * @param {Number} roleId 角色ID
 * @param {Array} menuIds 菜单ID数组
 * @returns {Promise}
 */
export function assignRoleMenus(roleId, menuIds) {
  return request({
    url: '/system/role/assignMenus',
    method: 'post',
    data: { roleId, menuIds }
  })
}

// ========================= 菜单管理 API =========================

/**
 * 获取菜单列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getMenuList(params) {
  return request({
    url: '/system/menu/list',
    method: 'post',
    data: params
  })
}

/**
 * 获取菜单详情
 * @param {Number} menuId 菜单ID
 * @returns {Promise}
 */
export function getMenuInfo(menuId) {
  return request({
    url: `/system/menu/info/${menuId}`,
    method: 'post'
  })
}

/**
 * 新增菜单
 * @param {Object} data 菜单信息
 * @returns {Promise}
 */
export function addMenu(data) {
  return request({
    url: '/system/menu/add',
    method: 'post',
    data
  })
}

/**
 * 修改菜单
 * @param {Object} data 菜单信息
 * @returns {Promise}
 */
export function updateMenu(data) {
  return request({
    url: '/system/menu/update',
    method: 'post',
    data
  })
}

/**
 * 删除菜单
 * @param {Number} menuId 菜单ID
 * @returns {Promise}
 */
export function deleteMenu(menuId) {
  return request({
    url: `/system/menu/remove/${menuId}`,
    method: 'post'
  })
}

/**
 * 获取菜单树形结构
 * @returns {Promise}
 */
export function getMenuTree() {
  return request({
    url: '/system/menu/tree',
    method: 'get'
  })
}

/**
 * 获取用户菜单权限
 * @returns {Promise}
 */
export function getUserMenus() {
  return request({
    url: '/system/menu/user',
    method: 'get'
  })
}

// ========================= 部门管理 API =========================

/**
 * 获取部门列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getDeptList(params) {
  return request({
    url: '/system/dept/list',
    method: 'post',
    data: params
  })
}

/**
 * 获取部门详情
 * @param {Number} deptId 部门ID
 * @returns {Promise}
 */
export function getDeptInfo(deptId) {
  return request({
    url: `/system/dept/info/${deptId}`,
    method: 'post'
  })
}

/**
 * 新增部门
 * @param {Object} data 部门信息
 * @returns {Promise}
 */
export function addDept(data) {
  return request({
    url: '/system/dept/add',
    method: 'post',
    data
  })
}

/**
 * 修改部门
 * @param {Object} data 部门信息
 * @returns {Promise}
 */
export function updateDept(data) {
  return request({
    url: '/system/dept/update',
    method: 'post',
    data
  })
}

/**
 * 删除部门
 * @param {Number} deptId 部门ID
 * @returns {Promise}
 */
export function deleteDept(deptId) {
  return request({
    url: `/system/dept/remove/${deptId}`,
    method: 'post'
  })
}

/**
 * 获取部门树形结构
 * @returns {Promise}
 */
export function getDeptTree() {
  return request({
    url: '/system/dept/tree',
    method: 'get'
  })
}

// ========================= 岗位管理 API =========================

/**
 * 岗位分页查询
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getPostPage(params) {
  return request({
    url: '/system/post/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取岗位详情
 * @param {Number} postId 岗位ID
 * @returns {Promise}
 */
export function getPostInfo(postId) {
  return request({
    url: `/system/post/info/${postId}`,
    method: 'post'
  })
}

/**
 * 新增岗位
 * @param {Object} data 岗位信息
 * @returns {Promise}
 */
export function addPost(data) {
  return request({
    url: '/system/post/add',
    method: 'post',
    data
  })
}

/**
 * 修改岗位
 * @param {Object} data 岗位信息
 * @returns {Promise}
 */
export function updatePost(data) {
  return request({
    url: '/system/post/update',
    method: 'post',
    data
  })
}

/**
 * 删除岗位
 * @param {Array} postIds 岗位ID数组
 * @returns {Promise}
 */
export function deletePost(postIds) {
  return request({
    url: `/system/post/remove/${postIds.join(',')}`,
    method: 'post'
  })
}

/**
 * 修改岗位状态
 * @param {Object} data 岗位状态信息
 * @returns {Promise}
 */
export function changePostStatus(data) {
  return request({
    url: '/system/post/changeStatus',
    method: 'post',
    data
  })
}

/**
 * 获取所有岗位列表
 * @returns {Promise}
 */
export function getAllPosts() {
  return request({
    url: '/system/post/all',
    method: 'get'
  })
}

// ========================= 字典管理 API =========================

/**
 * 字典类型分页查询
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getDictTypePage(params) {
  return request({
    url: '/system/dict/type/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取字典数据列表
 * @param {String} dictType 字典类型
 * @returns {Promise}
 */
export function getDictDataList(dictType) {
  return request({
    url: `/system/dict/data/list/${dictType}`,
    method: 'get'
  })
}

/**
 * 新增字典类型
 * @param {Object} data 字典类型信息
 * @returns {Promise}
 */
export function addDictType(data) {
  return request({
    url: '/system/dict/type/add',
    method: 'post',
    data
  })
}

/**
 * 新增字典数据
 * @param {Object} data 字典数据信息
 * @returns {Promise}
 */
export function addDictData(data) {
  return request({
    url: '/system/dict/data/add',
    method: 'post',
    data
  })
}

// ========================= 参数配置 API =========================

/**
 * 参数配置分页查询
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getConfigPage(params) {
  return request({
    url: '/system/config/page',
    method: 'post',
    data: params
  })
}

/**
 * 根据参数键名查询参数值
 * @param {String} configKey 参数键名
 * @returns {Promise}
 */
export function getConfigByKey(configKey) {
  return request({
    url: `/system/config/configKey/${configKey}`,
    method: 'get'
  })
}

/**
 * 新增参数配置
 * @param {Object} data 参数配置信息
 * @returns {Promise}
 */
export function addConfig(data) {
  return request({
    url: '/system/config/add',
    method: 'post',
    data
  })
}

/**
 * 修改参数配置
 * @param {Object} data 参数配置信息
 * @returns {Promise}
 */
export function updateConfig(data) {
  return request({
    url: '/system/config/update',
    method: 'post',
    data
  })
}

/**
 * 删除参数配置
 * @param {Array} configIds 参数配置ID数组
 * @returns {Promise}
 */
export function deleteConfig(configIds) {
  return request({
    url: `/system/config/remove/${configIds.join(',')}`,
    method: 'post'
  })
}