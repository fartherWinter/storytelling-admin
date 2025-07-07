import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getToken, removeToken } from '@/utils/auth'

// 创建axios实例
const service = axios.create({
  baseURL: 'http://localhost:8100', // url基础地址
  timeout: 10000, // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
  config => {
      
    // 在发送请求之前做些什么
    const token = getToken()
    if (token) {
      // sa-token认证，将token添加到请求头
      config.headers['token'] = token
    }

    return config
  },
  error => {
    // 对请求错误做些什么
    console.error(error)
    // Token过期处理（401状态码）
  if (error.response.status === 401) {
    localStorage.removeItem('token')
    router.push('/login')
  }
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    
    // 如果是二进制数据，直接返回
    if (response.config.responseType === 'blob') {
      return response
    }
    
    // 如果返回的状态码不是200，说明接口有问题，需要提示
    if (res.code !== '200') {
      ElMessage({
        message: res.msg || '系统错误',
        type: 'error',
        duration: 5 * 1000
      })
      
      // sa-token特定状态码处理
      // 401: 未登录; 402: token无效; 403: 没有权限
      if (res.code === 401 || res.code === 402) {
        // 重新登录
        ElMessageBox.confirm('您已被登出，请重新登录', '确认登出', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 清除用户信息并跳转到登录页
          // store.dispatch('user/resetToken').then(() => {
          //   location.reload()
          // })
          location.reload()
        })
      }
      return Promise.reject(new Error(res.msg || '系统错误'))
    } else {
      return res
    }
  },
  error => {
    console.error('请求错误:', error)
    // 处理sa-token特定错误
    if (error.response) {
      const status = error.response.status
      if (status === 401) {
        // 未登录或token已过期
        ElMessageBox.confirm('您的登录已过期，请重新登录', '登录提示', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 清除token并跳转到登录页
          removeToken()
          location.href = '/login'
        })
      } else {
        ElMessage({
          message: error.message || '请求失败',
          type: 'error',
          duration: 5 * 1000
        })
      }
    } else {
      ElMessage({
        message: error.message || '请求失败',
        type: 'error',
        duration: 5 * 1000
      })
    }
    return Promise.reject(error)
  }
)

export default service