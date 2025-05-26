import request from '@/utils/request'

// 管理员工作流相关接口

/**
 * 部署管理员创建流程
 * @param {Object} data 流程定义数据
 * @returns {Promise}
 */
export function deployAdminProcess(data) {
  return request({
    url: '/admin/workflow/deploy/admin',
    method: 'post',
    data
  })
}

/**
 * 启动管理员创建流程
 * @param {Object} data 管理员用户信息
 * @returns {Promise}
 */
export function startAdminProcess(data) {
  return request({
    url: '/admin/workflow/start/admin',
    method: 'post',
    data
  })
}

/**
 * 获取我的待办任务
 * @returns {Promise}
 */
export function getMyTodoTasks() {
  return request({
    url: '/admin/workflow/tasks/todo',
    method: 'post'
  })
}

/**
 * 审批通过任务
 * @param {String} taskId 任务ID
 * @param {String} comment 审批意见
 * @returns {Promise}
 */
export function approveTask(taskId, comment) {
  return request({
    url: `/admin/workflow/task/approve/${taskId}`,
    method: 'post',
    params: { comment }
  })
}

/**
 * 拒绝任务
 * @param {String} taskId 任务ID
 * @param {String} comment 拒绝意见
 * @returns {Promise}
 */
export function rejectTask(taskId, comment) {
  return request({
    url: `/admin/workflow/task/reject/${taskId}`,
    method: 'post',
    params: { comment }
  })
}

/**
 * 获取流程图
 * @param {String} processInstanceId 流程实例ID
 * @returns {Promise}
 */
export function getProcessDiagram(processInstanceId) {
  return request({
    url: `/admin/workflow/diagram/${processInstanceId}`,
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
    url: `/admin/workflow/status/${processInstanceId}`,
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
    url: `/admin/workflow/terminate/${processInstanceId}`,
    method: 'post',
    params: { reason }
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