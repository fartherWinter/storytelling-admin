// 仪表盘相关API请求封装
import axios from 'axios'

// 示例：获取仪表盘数据
export function fetchDashboardData(params) {
  return axios.get('/api/dashboard/data', { params })
}

// 你可以根据实际需求继续添加其他仪表盘相关API方法