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
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard/index',
    name: 'Dashboard',
    meta: { title: '仪表盘', icon: 'dashboard' },
    children: [
      {
        path: 'dashboard/index',
        name: 'DashboardIndex',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页概览', icon: 'home' }
      },
      {
        path: 'dashboard/data-visualization',
        name: 'DataVisualization',
        component: () => import('@/views/dashboard/DataVisualization.vue'),
        meta: { title: '数据可视化', icon: 'chart' }
      },
      {
        path: 'dashboard/enhanced',
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
  
  // 报表中心
  {
    path: '/report',
    component: Layout,
    redirect: '/report/center',
    name: 'Report',
    meta: { title: '报表中心', icon: 'document' },
    children: [
      {
        path: 'center',
        name: 'ReportCenter',
        component: () => import('@/views/report/ReportCenter.vue'),
        meta: { title: '报表管理', icon: 'folder' }
      },
      {
        path: 'designer',
        name: 'ReportDesigner',
        component: () => import('@/views/report/ReportDesigner.vue'),
        meta: { title: '报表设计', icon: 'edit' }
      },
      {
        path: 'preview',
        name: 'ReportPreview',
        component: () => import('@/views/report/ReportPreview.vue'),
        meta: { title: '报表预览', icon: 'view' }
      }
    ]
  },
  
  // 系统设置
  {
    path: '/system',
    component: Layout,
    redirect: '/system/settings',
    name: 'System',
    meta: { title: '系统设置', icon: 'setting' },
    children: [
      {
        path: 'settings',
        name: 'SystemSettings',
        component: () => import('@/views/system/Settings.vue'),
        meta: { title: '基础设置', icon: 'setting' }
      },
      {
        path: 'users',
        name: 'UserManagement',
        component: () => import('@/views/system/UserManagement.vue'),
        meta: { title: '用户管理', icon: 'user' }
      },
      {
        path: 'roles',
        name: 'RoleManagement',
        component: () => import('@/views/system/RoleManagement.vue'),
        meta: { title: '角色管理', icon: 'peoples' }
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

import { getToken } from '@/utils/auth'

// 路由守卫，未登录跳转到登录页
router.beforeEach((to, from, next) => {
  const token = getToken()
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router