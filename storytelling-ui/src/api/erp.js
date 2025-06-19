import request from '@/utils/request'

// ========================= 客户管理 API =========================

/**
 * 客户分页查询
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getCustomerPage(params) {
  return request({
    url: '/erp/customer/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取客户详情
 * @param {Number} customerId 客户ID
 * @returns {Promise}
 */
export function getCustomerInfo(customerId) {
  return request({
    url: `/erp/customer/info/${customerId}`,
    method: 'post'
  })
}

/**
 * 新增客户
 * @param {Object} data 客户信息
 * @returns {Promise}
 */
export function addCustomer(data) {
  return request({
    url: '/erp/customer/add',
    method: 'post',
    data
  })
}

/**
 * 修改客户
 * @param {Object} data 客户信息
 * @returns {Promise}
 */
export function updateCustomer(data) {
  return request({
    url: '/erp/customer/update',
    method: 'post',
    data
  })
}

/**
 * 删除客户
 * @param {Array} customerIds 客户ID数组
 * @returns {Promise}
 */
export function deleteCustomer(customerIds) {
  return request({
    url: `/erp/customer/remove/${customerIds.join(',')}`,
    method: 'post'
  })
}

/**
 * 修改客户状态
 * @param {Object} data 客户状态信息
 * @returns {Promise}
 */
export function changeCustomerStatus(data) {
  return request({
    url: '/erp/customer/changeStatus',
    method: 'post',
    data
  })
}

/**
 * 获取客户统计分析
 * @returns {Promise}
 */
export function getCustomerStatistics() {
  return request({
    url: '/erp/customer/statistics',
    method: 'get'
  })
}

/**
 * 获取客户跟进记录
 * @param {Number} customerId 客户ID
 * @returns {Promise}
 */
export function getCustomerFollowup(customerId) {
  return request({
    url: `/erp/customer/followup/${customerId}`,
    method: 'get'
  })
}

// ========================= 产品管理 API =========================

/**
 * 产品分页查询
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getProductPage(params) {
  return request({
    url: '/erp/product/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取产品详情
 * @param {Number} productId 产品ID
 * @returns {Promise}
 */
export function getProductInfo(productId) {
  return request({
    url: `/erp/product/info/${productId}`,
    method: 'post'
  })
}

/**
 * 新增产品
 * @param {Object} data 产品信息
 * @returns {Promise}
 */
export function addProduct(data) {
  return request({
    url: '/erp/product/add',
    method: 'post',
    data
  })
}

/**
 * 修改产品
 * @param {Object} data 产品信息
 * @returns {Promise}
 */
export function updateProduct(data) {
  return request({
    url: '/erp/product/update',
    method: 'post',
    data
  })
}

/**
 * 删除产品
 * @param {Array} productIds 产品ID数组
 * @returns {Promise}
 */
export function deleteProduct(productIds) {
  return request({
    url: `/erp/product/remove/${productIds.join(',')}`,
    method: 'post'
  })
}

/**
 * 修改产品状态
 * @param {Object} data 产品状态信息
 * @returns {Promise}
 */
export function changeProductStatus(data) {
  return request({
    url: '/erp/product/changeStatus',
    method: 'post',
    data
  })
}

/**
 * 获取产品库存信息
 * @param {Number} productId 产品ID
 * @returns {Promise}
 */
export function getProductInventory(productId) {
  return request({
    url: `/erp/product/inventory/${productId}`,
    method: 'get'
  })
}

/**
 * 获取产品分类列表
 * @returns {Promise}
 */
export function getProductCategories() {
  return request({
    url: '/erp/product/categories',
    method: 'get'
  })
}

// ========================= 库存管理 API =========================

/**
 * 库存分页查询
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getInventoryPage(params) {
  return request({
    url: '/erp/inventory/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取库存详情
 * @param {Number} inventoryId 库存ID
 * @returns {Promise}
 */
export function getInventoryInfo(inventoryId) {
  return request({
    url: `/erp/inventory/info/${inventoryId}`,
    method: 'post'
  })
}

/**
 * 库存调整
 * @param {Object} data 库存调整信息
 * @returns {Promise}
 */
export function adjustInventory(data) {
  return request({
    url: '/erp/inventory/adjust',
    method: 'post',
    data
  })
}

/**
 * 库存盘点
 * @param {Array} inventoryList 库存盘点列表
 * @returns {Promise}
 */
export function stocktakeInventory(inventoryList) {
  return request({
    url: '/erp/inventory/stocktake',
    method: 'post',
    data: inventoryList
  })
}

/**
 * 获取库存预警列表
 * @returns {Promise}
 */
export function getInventoryWarningList() {
  return request({
    url: '/erp/inventory/warning/list',
    method: 'post'
  })
}

/**
 * 库存调拨
 * @param {Object} data 调拨信息
 * @returns {Promise}
 */
export function transferInventory(data) {
  return request({
    url: '/erp/inventory/transfer',
    method: 'post',
    data
  })
}

/**
 * 获取库存统计报表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getInventoryStatistics(params) {
  return request({
    url: '/erp/inventory/statistics',
    method: 'get',
    params
  })
}

/**
 * 获取库存流水记录
 * @param {Number} productId 产品ID
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getInventoryHistory(productId, params) {
  return request({
    url: `/erp/inventory/history/${productId}`,
    method: 'get',
    params
  })
}

// ========================= 供应商管理 API =========================

/**
 * 供应商分页查询
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getSupplierPage(params) {
  return request({
    url: '/erp/supplier/page',
    method: 'post',
    data: params
  })
}

/**
 * 获取供应商详情
 * @param {Number} supplierId 供应商ID
 * @returns {Promise}
 */
export function getSupplierInfo(supplierId) {
  return request({
    url: `/erp/supplier/info/${supplierId}`,
    method: 'post'
  })
}

/**
 * 新增供应商
 * @param {Object} data 供应商信息
 * @returns {Promise}
 */
export function addSupplier(data) {
  return request({
    url: '/erp/supplier/add',
    method: 'post',
    data
  })
}

/**
 * 修改供应商
 * @param {Object} data 供应商信息
 * @returns {Promise}
 */
export function updateSupplier(data) {
  return request({
    url: '/erp/supplier/update',
    method: 'post',
    data
  })
}

/**
 * 删除供应商
 * @param {Array} supplierIds 供应商ID数组
 * @returns {Promise}
 */
export function deleteSupplier(supplierIds) {
  return request({
    url: `/erp/supplier/remove/${supplierIds.join(',')}`,
    method: 'post'
  })
}

/**
 * 修改供应商状态
 * @param {Object} data 供应商状态信息
 * @returns {Promise}
 */
export function changeSupplierStatus(data) {
  return request({
    url: '/erp/supplier/changeStatus',
    method: 'post',
    data
  })
}