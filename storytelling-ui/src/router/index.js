import { createRouter, createWebHistory } from 'vue-router'

// 布局组件
import Layout from '@/layout/index.vue'

// 导入会议路由
import conferenceRoutes from './conference'

// 路由配置
const routes = [
  {
    path: '/login',
    name: 'ErpLogin',
    component: () => import('@/views/ErpLogin.vue'),
    meta: { title: '登录', hidden: true }
  },
  {    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'dashboard' }
      },
      {
        path: 'data-visualization',
        name: 'DataVisualization',
        component: () => import('@/views/dashboard/DataVisualization.vue'),
        meta: { title: '数据可视化', icon: 'chart' }
      },
      {
        path: 'enhanced-dashboard',
        name: 'EnhancedDashboard',
        component: () => import('@/views/dashboard/EnhancedDashboard.vue'),
        meta: { title: '增强型仪表盘', icon: 'dashboard' }
      }
    ]
  },
  
  // 工作流管理路由
  {
    path: '/workflow',
    component: Layout,
    redirect: '/workflow/admin',
    name: 'Workflow',
    meta: { title: '工作流管理', icon: 'workflow' },
    children: [
      {
        path: 'admin',
        name: 'AdminWorkflow',
        component: () => import('@/views/workflow/AdminWorkflow.vue'),
        meta: { title: '管理员创建流程', icon: 'user' }
      },
      {
        path: 'custom',
        name: 'CustomWorkflow',
        component: () => import('@/views/workflow/CustomWorkflow.vue'),
        meta: { title: '工作流自定义', icon: 'edit' }
      },
      {
        path: 'monitor',
        name: 'WorkflowMonitor',
        component: () => import('@/views/workflow/WorkflowMonitor.vue'),
        meta: { title: '流程监控', icon: 'monitor' }
      }
    ]
  },
  
  // 供应链协同平台路由
  {
    path: '/supply-chain',
    component: Layout,
    redirect: '/supply-chain/index',
    name: 'SupplyChain',
    meta: { title: '供应链协同', icon: 'link' },
    children: [
      {
        path: 'index',
        name: 'SupplyChainIndex',
        component: () => import('@/views/supplyChain/index.vue'),
        meta: { title: '供应链概览', icon: 'dashboard' }
      },
      {
        path: 'partners',
        name: 'SupplyChainPartners',
        component: () => import('@/views/supplyChain/Partners.vue'),
        meta: { title: '合作伙伴管理', icon: 'partners' }
      },
      {
        path: 'inventory',
        name: 'SupplyChainInventory',
        component: () => import('@/views/supplyChain/Inventory.vue'),
        meta: { title: '库存管理', icon: 'inventory' }
      },
      {
        path: 'logistics',
        name: 'SupplyChainLogistics',
        component: () => import('@/views/supplyChain/Logistics.vue'),
        meta: { title: '物流追踪', icon: 'truck' }
      }
    ]
  },
  
  // AI分析预测功能
  {
    path: '/ai-analysis',
    component: Layout,
    redirect: '/ai-analysis/prediction',
    name: 'AIAnalysis',
    meta: { title: 'AI分析预测', icon: 'ai' },
    children: [
      {
        path: 'prediction',
        name: 'AIPrediction',
        component: () => import('@/views/ai/Prediction.vue'),
        meta: { title: '预测分析', icon: 'chart' }
      },
      {
        path: 'insights',
        name: 'AIInsights',
        component: () => import('@/views/ai/Insights.vue'),
        meta: { title: '智能洞察', icon: 'bulb' }
      },
      {
        path: 'recommendation',
        name: 'AIRecommendation',
        component: () => import('@/views/ai/Recommendation.vue'),
        meta: { title: '智能推荐', icon: 'star' }
      }
    ]
  },
  
  // 移动端适配
  {
    path: '/mobile',
    component: Layout,
    redirect: '/mobile/dashboard',
    name: 'Mobile',
    meta: { title: '移动端管理', icon: 'mobile' },
    children: [
      {
        path: 'dashboard',
        name: 'MobileDashboard',
        component: () => import('@/views/mobile/Dashboard.vue'),
        meta: { title: '移动端概览', icon: 'dashboard' }
      },
      {
        path: 'settings',
        name: 'MobileSettings',
        component: () => import('@/views/mobile/Settings.vue'),
        meta: { title: '移动端设置', icon: 'setting' }
      }
    ]
  },
  
  // 多语言支持
  {
    path: '/language',
    component: Layout,
    redirect: '/language/settings',
    name: 'Language',
    meta: { title: '多语言支持', icon: 'language' },
    children: [
      {
        path: 'settings',
        name: 'LanguageSettings',
        component: () => import('@/views/language/Settings.vue'),
        meta: { title: '语言设置', icon: 'setting' }
      },
      {
        path: 'translation',
        name: 'Translation',
        component: () => import('@/views/language/Translation.vue'),
        meta: { title: '翻译管理', icon: 'translate' }
      }
    ]
  },
  
  // 添加会议路由
  ...conferenceRoutes,
  
  // 404页面必须放在最后
  { path: '/:pathMatch(.*)*', redirect: '/404', hidden: true }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫，未登录跳转到登录页
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('erp_token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router