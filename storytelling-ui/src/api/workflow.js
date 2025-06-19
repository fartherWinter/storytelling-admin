import request from '@/utils/request'

// API基础路径
const API_BASE = '/sys/v1/workflow'

// 管理员工作流相关接口

/**
 * 部署流程定义
 * @param {Object} processDefinition 流程定义信息
 * @returns {Promise}
 */
export function deployProcess(processDefinition) {
  return request({
    url: `${API_BASE}/deploy`,
    method: 'post',
    data: processDefinition
  })
}

/**
 * 部署管理员创建流程
 * @param {Object} data 流程定义数据
 * @returns {Promise}
 */
export function deployAdminProcess(data) {
  return request({
    url: '/sys/workflow/deploy/admin',
    method: 'post',
    data
  })
}

/**
 * 启动流程实例
 * @param {string} processKey 流程定义Key
 * @param {string} businessKey 业务Key
 * @param {Object} variables 流程变量
 * @returns {Promise}
 */
export function startProcess(processKey, businessKey, variables = {}) {
  return request({
    url: `${API_BASE}/start`,
    method: 'post',
    params: {
      processKey,
      businessKey
    },
    data: variables
  })
}

/**
 * 启动管理员创建流程
 * @param {Object} data 管理员用户信息
 * @returns {Promise}
 */
export function startAdminProcess(data) {
  return request({
    url: '/sys/workflow/start/admin',
    method: 'post',
    data
  })
}

/**
 * 获取待办任务
 * @param {string} assignee 任务处理人
 * @returns {Promise}
 */
export function getTodoTasks(assignee) {
  return request({
    url: `${API_BASE}/tasks/todo`,
    method: 'get',
    params: { assignee }
  })
}

/**
 * 获取我的待办任务
 * @returns {Promise}
 */
export function getMyTodoTasks() {
  return request({
    url: '/sys/workflow/tasks/todo',
    method: 'post'
  })
}

/**
 * 完成任务
 * @param {string} taskId 任务ID
 * @param {Object} variables 任务变量
 * @returns {Promise}
 */
export function completeTask(taskId, variables = {}) {
  return request({
    url: `${API_BASE}/tasks/${taskId}/complete`,
    method: 'post',
    data: { variables }
  })
}

/**
 * 审批任务
 * @param {string} taskId 任务ID
 * @param {boolean} approved 是否同意
 * @param {string} comment 审批意见
 * @returns {Promise}
 */
export function approveTask(taskId, approved, comment = '') {
  return request({
    url: `${API_BASE}/tasks/${taskId}/approve`,
    method: 'post',
    data: {
      approved,
      comment
    }
  })
}

/**
 * 拒绝任务
 * @param {string} taskId 任务ID
 * @param {string} reason 拒绝原因
 * @returns {Promise}
 */
export function rejectTask(taskId, reason = '') {
  return request({
    url: `${API_BASE}/tasks/${taskId}/reject`,
    method: 'post',
    data: { reason }
  })
}

/**
 * 获取流程图
 * @param {String} processInstanceId 流程实例ID
 * @returns {Promise}
 */
export function getProcessDiagram(processInstanceId) {
  return request({
    url: `/sys/workflow/diagram/${processInstanceId}`,
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 获取流程状态
 * @param {String} processInstanceId 流程实例ID
 * @returns {Promise}
 */
export function getProcessStatus(processInstanceId) {
  return request({
    url: `/sys/workflow/status/${processInstanceId}`,
    method: 'get'
  })
}

/**
 * 终止流程
 * @param {String} processInstanceId 流程实例ID
 * @param {String} reason 终止原因
 * @returns {Promise}
 */
export function terminateProcess(processInstanceId, reason) {
  return request({
    url: `/sys/workflow/terminate/${processInstanceId}`,
    method: 'post',
    params: { reason }
  })
}

// 工作流定义管理接口

/**
 * 获取所有流程定义
 * @returns {Promise}
 */
export function getAllDefinitions() {
  return request({
    url: '/workflow/definitions',
    method: 'get'
  })
}

/**
 * 分页查询流程定义
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getDefinitionsPage(params = {}) {
  return request({
    url: '/workflow/definitions/page',
    method: 'get',
    params: {
      page: 0,
      size: 20,
      ...params
    }
  })
}

/**
 * 获取流程定义详情
 * @param {string} definitionId 流程定义ID
 * @returns {Promise}
 */
export function getDefinitionDetail(definitionId) {
  return request({
    url: `/workflow/definitions/${definitionId}`,
    method: 'get'
  })
}

/**
 * 删除流程定义
 * @param {string} definitionId 流程定义ID
 * @returns {Promise}
 */
export function deleteDefinition(definitionId) {
  return request({
    url: `/workflow/definitions/${definitionId}`,
    method: 'delete'
  })
}

// 工作流设计器接口

/**
 * 获取设计器模型集合
 * @returns {Promise}
 */
export function getStencilset() {
  return request({
    url: '/workflow/designer/editor/stencilset',
    method: 'get'
  })
}

/**
 * 获取模型编辑器数据
 * @param {string} modelId 模型ID
 * @returns {Promise}
 */
export function getModelJson(modelId) {
  return request({
    url: `/workflow/designer/model/${modelId}/json`,
    method: 'get'
  })
}

/**
 * 保存模型数据
 * @param {string} modelId 模型ID
 * @param {Object} modelData 模型数据
 * @returns {Promise}
 */
export function saveModel(modelId, modelData) {
  return request({
    url: `/workflow/designer/model/${modelId}/save`,
    method: 'post',
    data: modelData
  })
}

// 工作流模型管理接口

/**
 * 创建工作流模型
 * @param {Object} model 模型信息
 * @returns {Promise}
 */
export function createModel(model) {
  return request({
    url: '/workflow/model',
    method: 'post',
    data: model
  })
}

/**
 * 更新工作流模型
 * @param {string} modelId 模型ID
 * @param {Object} model 模型信息
 * @returns {Promise}
 */
export function updateModel(modelId, model) {
  return request({
    url: `/workflow/model/${modelId}`,
    method: 'put',
    data: model
  })
}

/**
 * 获取模型列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getModelList(params = {}) {
  return request({
    url: '/workflow/model',
    method: 'get',
    params: {
      page: 1,
      size: 10,
      ...params
    }
  })
}

/**
 * 获取模型详情
 * @param {string} modelId 模型ID
 * @returns {Promise}
 */
export function getModelDetail(modelId) {
  return request({
    url: `/workflow/model/${modelId}`,
    method: 'get'
  })
}

/**
 * 删除模型
 * @param {string} modelId 模型ID
 * @returns {Promise}
 */
export function deleteModel(modelId) {
  return request({
    url: `/workflow/model/${modelId}`,
    method: 'delete'
  })
}

/**
 * 部署模型
 * @param {string} modelId 模型ID
 * @returns {Promise}
 */
export function deployModel(modelId) {
  return request({
    url: `/workflow/model/${modelId}/deploy`,
    method: 'post'
  })
}

/**
 * 导出模型
 * @param {string} modelId 模型ID
 * @param {string} type 导出类型 (xml|bpmn|json)
 * @returns {Promise}
 */
export function exportModel(modelId, type = 'xml') {
  return request({
    url: `/workflow/model/${modelId}/export`,
    method: 'get',
    params: { type },
    responseType: 'blob'
  })
}

// 工作流任务管理接口

/**
 * 高级任务查询
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function searchTasks(params = {}) {
  return request({
    url: '/workflow/tasks/search',
    method: 'get',
    params: {
      page: 0,
      size: 20,
      ...params
    }
  })
}

/**
 * 获取任务详情
 * @param {string} taskId 任务ID
 * @returns {Promise}
 */
export function getTaskDetail(taskId) {
  return request({
    url: `/workflow/tasks/${taskId}/detail`,
    method: 'get'
  })
}

/**
 * 设置任务提醒
 * @param {string} taskId 任务ID
 * @param {string} reminderTime 提醒时间
 * @param {string} message 提醒消息
 * @returns {Promise}
 */
export function setTaskReminder(taskId, reminderTime, message = '') {
  return request({
    url: `/workflow/tasks/${taskId}/reminder`,
    method: 'post',
    params: {
      reminderTime,
      message
    }
  })
}

/**
 * 委派任务
 * @param {string} taskId 任务ID
 * @param {string} assignee 委派人
 * @returns {Promise}
 */
export function delegateTask(taskId, assignee) {
  return request({
    url: `/workflow/tasks/${taskId}/delegate`,
    method: 'post',
    data: { assignee }
  })
}

/**
 * 认领任务
 * @param {string} taskId 任务ID
 * @param {string} userId 用户ID
 * @returns {Promise}
 */
export function claimTask(taskId, userId) {
  return request({
    url: `/workflow/tasks/${taskId}/claim`,
    method: 'post',
    data: { userId }
  })
}

/**
 * 转办任务
 * @param {string} taskId 任务ID
 * @param {string} assignee 转办人
 * @returns {Promise}
 */
export function transferTask(taskId, assignee) {
  return request({
    url: `/workflow/tasks/${taskId}/transfer`,
    method: 'post',
    data: { assignee }
  })
}

/**
 * 获取任务历史
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getTaskHistory(params = {}) {
  return request({
    url: '/workflow/tasks/history',
    method: 'get',
    params
  })
}

/**
 * 批量操作任务
 * @param {Object} operation 批量操作信息
 * @returns {Promise}
 */
export function batchOperateTasks(operation) {
  return request({
    url: '/workflow/tasks/batch',
    method: 'post',
    data: operation
  })
}

// 工作流流程管理接口

/**
 * 高级流程实例查询
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function searchProcesses(params = {}) {
  return request({
    url: '/workflow/processes/search',
    method: 'get',
    params: {
      page: 0,
      size: 20,
      ...params
    }
  })
}

/**
 * 启动流程实例（增强版）
 * @param {Object} processInfo 流程信息
 * @returns {Promise}
 */
export function startProcessEnhanced(processInfo) {
  return request({
    url: '/workflow/processes/start',
    method: 'post',
    params: {
      processKey: processInfo.processKey,
      businessKey: processInfo.businessKey,
      businessType: processInfo.businessType,
      title: processInfo.title,
      startUserId: processInfo.startUserId
    },
    data: processInfo.variables || {}
  })
}

/**
 * 获取流程实例详情
 * @param {string} processInstanceId 流程实例ID
 * @returns {Promise}
 */
export function getProcessDetail(processInstanceId) {
  return request({
    url: `/workflow/processes/${processInstanceId}/detail`,
    method: 'get'
  })
}

/**
 * 暂停流程实例
 * @param {string} processInstanceId 流程实例ID
 * @returns {Promise}
 */
export function suspendProcess(processInstanceId) {
  return request({
    url: `/workflow/processes/${processInstanceId}/suspend`,
    method: 'post'
  })
}

/**
 * 激活流程实例
 * @param {string} processInstanceId 流程实例ID
 * @returns {Promise}
 */
export function activateProcess(processInstanceId) {
  return request({
    url: `/workflow/processes/${processInstanceId}/activate`,
    method: 'post'
  })
}

// 工作流表单管理接口

/**
 * 获取所有表单模板
 * @returns {Promise}
 */
export function getAllForms() {
  return request({
    url: '/workflow/forms',
    method: 'get'
  })
}

/**
 * 获取表单模板详情
 * @param {string} formId 表单ID
 * @returns {Promise}
 */
export function getFormDetail(formId) {
  return request({
    url: `/workflow/forms/${formId}`,
    method: 'get'
  })
}

/**
 * 创建表单模板
 * @param {Object} form 表单信息
 * @returns {Promise}
 */
export function createForm(form) {
  return request({
    url: '/workflow/forms',
    method: 'post',
    data: form
  })
}

/**
 * 更新表单模板
 * @param {string} formId 表单ID
 * @param {Object} form 表单信息
 * @returns {Promise}
 */
export function updateForm(formId, form) {
  return request({
    url: `/workflow/forms/${formId}`,
    method: 'put',
    data: form
  })
}

/**
 * 删除表单模板
 * @param {string} formId 表单ID
 * @returns {Promise}
 */
export function deleteForm(formId) {
  return request({
    url: `/workflow/forms/${formId}`,
    method: 'delete'
  })
}

// 工作流监控管理接口

/**
 * 获取工作流仪表板数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getDashboardData(params = {}) {
  return request({
    url: '/workflow/monitor/dashboard',
    method: 'get',
    params
  })
}

/**
 * 获取详细统计报告
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getDetailedReport(params = {}) {
  return request({
    url: '/workflow/monitor/report',
    method: 'get',
    params: {
      groupBy: 'day',
      ...params
    }
  })
}

/**
 * 获取流程性能分析
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getPerformanceAnalysis(params = {}) {
  return request({
    url: '/workflow/monitor/performance',
    method: 'get',
    params
  })
}

/**
 * 获取实时监控数据
 * @returns {Promise}
 */
export function getRealTimeData() {
  return request({
    url: '/workflow/monitor/realtime',
    method: 'get'
  })
}

// 自定义工作流相关接口

/**
 * 保存工作流模板
 * @param {Object} data 工作流模板数据
 * @returns {Promise}
 */
export function saveWorkflowTemplate(data) {
  return request({
    url: '/workflow/template/save',
    method: 'post',
    data
  })
}

/**
 * 获取工作流模板列表
 * @returns {Promise}
 */
export function getWorkflowTemplates() {
  return request({
    url: '/workflow/template/list',
    method: 'get'
  })
}

/**
 * 获取工作流模板详情
 * @param {String} templateId 模板ID
 * @returns {Promise}
 */
export function getWorkflowTemplateDetail(templateId) {
  return request({
    url: `/workflow/template/${templateId}`,
    method: 'get'
  })
}

/**
 * 删除工作流模板
 * @param {String} templateId 模板ID
 * @returns {Promise}
 */
export function deleteWorkflowTemplate(templateId) {
  return request({
    url: `/workflow/template/${templateId}`,
    method: 'delete'
  })
}

/**
 * 导出工作流为XML
 * @param {Object} data 工作流数据
 * @returns {Promise}
 */
export function exportWorkflowXML(data) {
  return request({
    url: '/workflow/export/xml',
    method: 'post',
    data,
    responseType: 'blob'
  })
}

/**
 * 导入工作流XML
 * @param {FormData} formData 包含XML文件的表单数据
 * @returns {Promise}
 */
export function importWorkflowXML(formData) {
  return request({
    url: '/workflow/import/xml',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 验证工作流
 * @param {Object} data 工作流数据
 * @returns {Promise}
 */
export function validateWorkflow(data) {
  return request({
    url: '/workflow/validate',
    method: 'post',
    data
  })
}

/**
 * 获取流程历史记录
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getProcessHistory(params) {
  return request({
    url: '/workflow/history',
    method: 'get',
    params
  })
}

// 工作流分析相关API

/**
 * 获取工作流概览数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getWorkflowOverview(params) {
  return request({
    url: '/workflow/analytics/overview',
    method: 'get',
    params
  })
}

/**
 * 获取流程实例趋势数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getInstanceTrendData(params) {
  return request({
    url: '/workflow/analytics/instance-trend',
    method: 'get',
    params
  })
}

/**
 * 获取任务状态分布数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getTaskStatusData(params) {
  return request({
    url: '/workflow/analytics/task-status',
    method: 'get',
    params
  })
}

/**
 * 获取流程定义统计数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getProcessDefStats(params) {
  return request({
    url: '/workflow/analytics/process-definition-stats',
    method: 'get',
    params
  })
}

/**
 * 获取用户任务统计数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getUserTaskStats(params) {
  return request({
    url: '/workflow/analytics/user-task-stats',
    method: 'get',
    params
  })
}

/**
 * 获取平均处理时长数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getAvgDurationData(params) {
  return request({
    url: '/workflow/analytics/avg-duration',
    method: 'get',
    params
  })
}

/**
 * 获取流程效率数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getEfficiencyData(params) {
  return request({
    url: '/workflow/analytics/efficiency',
    method: 'get',
    params
  })
}

/**
 * 获取流程实例表格数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getInstanceTableData(params) {
  return request({
    url: '/workflow/analytics/instance-table',
    method: 'get',
    params
  })
}

/**
 * 获取任务统计表格数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getTaskTableData(params) {
  return request({
    url: '/workflow/analytics/task-table',
    method: 'get',
    params
  })
}

/**
 * 获取用户统计表格数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getUserTableData(params) {
  return request({
    url: '/workflow/analytics/user-table',
    method: 'get',
    params
  })
}

/**
 * 获取性能分析表格数据
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getPerformanceTableData(params) {
  return request({
    url: '/workflow/analytics/performance-table',
    method: 'get',
    params
  })
}

/**
 * 分配任务
 * @param {Object} data 任务分配数据
 * @returns {Promise}
 */
export function assignTask(data) {
  return request({
    url: '/workflow/task/assign',
    method: 'post',
    data
  })
}

/**
 * 工作流配置相关功能
 */
export const workflowConfig = {
  /**
   * 保存表单模板
   * @param {Object} data 表单模板数据
   * @returns {Promise}
   */
  saveFormTemplate(data) {
    return request({
      url: '/workflow/config/form/template/save',
      method: 'post',
      data
    })
  },

  /**
   * 获取流程定义列表
   * @param {Object} params 查询参数
   * @returns {Promise}
   */
  getProcessDefinitionList(params) {
    return request({
      url: '/workflow/config/process/definition/list',
      method: 'get',
      params
    })
  }
}