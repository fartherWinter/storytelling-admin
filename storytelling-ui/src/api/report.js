import request from '@/utils/request'

// ========================= 报表管理 API =========================

/**
 * 创建报告
 * @param {Object} data 报告信息
 * @returns {Promise}
 */
export function createReport(data) {
  return request({
    url: '/report',
    method: 'post',
    data
  })
}

/**
 * 更新报告
 * @param {Number} id 报告ID
 * @param {Object} data 报告信息
 * @returns {Promise}
 */
export function updateReport(id, data) {
  return request({
    url: `/report/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除报告
 * @param {Number} id 报告ID
 * @returns {Promise}
 */
export function deleteReport(id) {
  return request({
    url: `/report/${id}`,
    method: 'delete'
  })
}

/**
 * 获取报告详情
 * @param {Number} id 报告ID
 * @returns {Promise}
 */
export function getReportById(id) {
  return request({
    url: `/report/${id}`,
    method: 'get'
  })
}

/**
 * 根据用户ID和类型获取报告列表
 * @param {Number} userId 用户ID
 * @param {String} type 报告类型（daily/weekly）
 * @returns {Promise}
 */
export function getReportsByUserId(userId, type) {
  return request({
    url: `/report/user/${userId}`,
    method: 'get',
    params: { type }
  })
}

/**
 * 获取指定用户在指定日期的日报
 * @param {Number} userId 用户ID
 * @param {String} reportDate 报告日期（YYYY-MM-DD）
 * @returns {Promise}
 */
export function getDailyReportByUserIdAndDate(userId, reportDate) {
  return request({
    url: `/report/user/${userId}/date/${reportDate}`,
    method: 'get'
  })
}

/**
 * 获取指定用户在指定周的周报
 * @param {Number} userId 用户ID
 * @param {String} weekStartDate 周开始日期（YYYY-MM-DD）
 * @returns {Promise}
 */
export function getWeeklyReportByUserIdAndDateRange(userId, weekStartDate) {
  return request({
    url: `/report/user/${userId}/week/${weekStartDate}`,
    method: 'get'
  })
}

/**
 * 提交报告并发起审批流程
 * @param {Object} data 报告信息
 * @returns {Promise}
 */
export function submitReportWithWorkflow(data) {
  return request({
    url: '/report/submit',
    method: 'post',
    data
  })
}

/**
 * 审批报告
 * @param {Number} id 报告ID
 * @param {Boolean} approve 是否通过（true/false）
 * @param {String} comment 审批意见
 * @returns {Promise}
 */
export function approveReport(id, approve, comment) {
  return request({
    url: `/report/approve/${id}`,
    method: 'post',
    params: { approve, comment }
  })
}

/**
 * 获取我的报告列表（分页）
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getMyReports(params) {
  return request({
    url: '/report/my/list',
    method: 'get',
    params
  })
}

/**
 * 获取待审批的报告列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getPendingReports(params) {
  return request({
    url: '/report/pending/list',
    method: 'get',
    params
  })
}

/**
 * 获取已审批的报告列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getApprovedReports(params) {
  return request({
    url: '/report/approved/list',
    method: 'get',
    params
  })
}

/**
 * 获取报告统计数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getReportStatistics(params) {
  return request({
    url: '/report/statistics',
    method: 'get',
    params
  })
}

/**
 * 导出报告
 * @param {Object} params 导出参数
 * @returns {Promise}
 */
export function exportReport(params) {
  return request({
    url: '/report/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 批量删除报告
 * @param {Array} ids 报告ID数组
 * @returns {Promise}
 */
export function batchDeleteReports(ids) {
  return request({
    url: '/report/batch/delete',
    method: 'post',
    data: { ids }
  })
}

/**
 * 复制报告
 * @param {Number} id 报告ID
 * @returns {Promise}
 */
export function copyReport(id) {
  return request({
    url: `/report/copy/${id}`,
    method: 'post'
  })
}

/**
 * 获取报告模板列表
 * @returns {Promise}
 */
export function getReportTemplates() {
  return request({
    url: '/report/templates',
    method: 'get'
  })
}

/**
 * 根据模板创建报告
 * @param {Number} templateId 模板ID
 * @param {Object} data 报告数据
 * @returns {Promise}
 */
export function createReportFromTemplate(templateId, data) {
  return request({
    url: `/report/template/${templateId}/create`,
    method: 'post',
    data
  })
}

/**
 * 获取报告审批历史
 * @param {Number} id 报告ID
 * @returns {Promise}
 */
export function getReportApprovalHistory(id) {
  return request({
    url: `/report/${id}/approval/history`,
    method: 'get'
  })
}

/**
 * 撤回报告审批
 * @param {Number} id 报告ID
 * @returns {Promise}
 */
export function withdrawReportApproval(id) {
  return request({
    url: `/report/${id}/withdraw`,
    method: 'post'
  })
}

/**
 * 获取报告评论列表
 * @param {Number} id 报告ID
 * @returns {Promise}
 */
export function getReportComments(id) {
  return request({
    url: `/report/${id}/comments`,
    method: 'get'
  })
}

/**
 * 添加报告评论
 * @param {Number} id 报告ID
 * @param {String} content 评论内容
 * @returns {Promise}
 */
export function addReportComment(id, content) {
  return request({
    url: `/report/${id}/comments`,
    method: 'post',
    data: { content }
  })
}

/**
 * 获取部门报告汇总
 * @param {Number} deptId 部门ID
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getDepartmentReportSummary(deptId, params) {
  return request({
    url: `/report/department/${deptId}/summary`,
    method: 'get',
    params
  })
}

/**
 * 获取报告趋势分析
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getReportTrendAnalysis(params) {
  return request({
    url: '/report/trend/analysis',
    method: 'get',
    params
  })
}

/**
 * 设置报告提醒
 * @param {Object} data 提醒设置
 * @returns {Promise}
 */
export function setReportReminder(data) {
  return request({
    url: '/report/reminder/set',
    method: 'post',
    data
  })
}

/**
 * 获取报告提醒设置
 * @returns {Promise}
 */
export function getReportReminderSettings() {
  return request({
    url: '/report/reminder/settings',
    method: 'get'
  })
}