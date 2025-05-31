/**
 * 认证相关工具函数
 */

// Token在localStorage中的键名
const TokenKey = 'erp_token'

/**
 * 获取token
 * @returns {String} token值
 */
export function getToken() {
  return localStorage.getItem(TokenKey)
}

/**
 * 设置token
 * @param {String} token token值
 */
export function setToken(token) {
  return localStorage.setItem(TokenKey, token)
}

/**
 * 移除token
 */
export function removeToken() {
  return localStorage.removeItem(TokenKey)
}