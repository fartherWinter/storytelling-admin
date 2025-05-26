import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import 'webrtc-adapter'

// 创建Vue应用实例
const app = createApp(App)
const pinia = createPinia()

// 注册Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 使用Pinia和路由
app.use(pinia).use(router)

// 使用Element Plus，并设置中文语言
app.use(ElementPlus, {
  locale: zhCn
})

// 挂载应用
app.mount('#app')