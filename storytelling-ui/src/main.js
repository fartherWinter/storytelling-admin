import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import 'webrtc-adapter'
import { createI18n } from 'vue-i18n'

// 创建 i18n 实例
const i18n = createI18n({
  legacy: false, // 使用 Composition API
  locale: 'zh-cn', // 默认语言
  fallbackLocale: 'en', // 回退语言
  messages: {
    'zh-cn': {
      // 在这里添加你的中文翻译
      // 例如: common: { save: '保存', cancel: '取消' }
    },
    'en': {
      // 在这里添加你的英文翻译
    }
  }
})

// 创建Vue应用实例
const app = createApp(App)
const pinia = createPinia()

// 注册Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 使用Pinia和路由
app.use(pinia).use(router).use(i18n)

// 使用Element Plus，并设置中文语言
app.use(ElementPlus, {
  locale: zhCn
})

// 挂载应用
app.mount('#app')