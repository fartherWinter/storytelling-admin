// 工作流相关API请求封装
import axios from 'axios'

// 示例：获取工作流列表
export function fetchWorkflowList(params) {
  return axios.get('/api/workflow/list', { params })
}

// 示例：创建新工作流
export function createWorkflow(data) {
  return axios.post('/api/workflow/create', data)
}

// 你可以根据实际需求继续添加其他工作流相关API方法