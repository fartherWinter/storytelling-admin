// 登录相关API请求封装
import axios from 'axios'

// 示例：用户登录
export function login(data) {
  return axios.post('/api/login', data)
}

// 示例：用户登出
export function logout() {
  return axios.post('/api/logout')
}

// 你可以根据实际需求继续添加其他登录相关API方法