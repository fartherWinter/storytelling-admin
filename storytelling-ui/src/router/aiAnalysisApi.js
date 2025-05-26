// AI分析预测相关API请求封装
import axios from 'axios'

// 示例：获取预测分析数据
export function fetchPrediction(params) {
  return axios.get('/api/ai-analysis/prediction', { params })
}

// 示例：获取智能洞察数据
export function fetchInsights(params) {
  return axios.get('/api/ai-analysis/insights', { params })
}

// 示例：获取智能推荐数据
export function fetchRecommendation(params) {
  return axios.get('/api/ai-analysis/recommendation', { params })
}

// 你可以根据实际需求继续添加其他AI分析相关API方法