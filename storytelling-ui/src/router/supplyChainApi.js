// 供应链相关API请求封装
import axios from 'axios'

// 示例：获取供应链概览数据
export function fetchSupplyChainOverview(params) {
  return axios.get('/api/supply-chain/overview', { params })
}

// 示例：获取合作伙伴列表
export function fetchPartners(params) {
  return axios.get('/api/supply-chain/partners', { params })
}

// 示例：获取库存信息
export function fetchInventory(params) {
  return axios.get('/api/supply-chain/inventory', { params })
}

// 示例：获取物流追踪信息
export function fetchLogistics(params) {
  return axios.get('/api/supply-chain/logistics', { params })
}

// 你可以根据实际需求继续添加其他供应链相关API方法