<template>
  <div class="app-wrapper">
    <!-- 侧边栏 -->
    <div class="sidebar-container">
      <div class="logo-container">
        <img src="@/assets/logo.png" alt="logo" class="logo-img" v-if="false">
        <h1 class="logo-title">ERP系统</h1>
      </div>
      <el-scrollbar>
        <el-menu
          :default-active="activeMenu"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
          :collapse="isCollapse"
          :unique-opened="false"
          :collapse-transition="false"
          mode="vertical"
          router
        >
          <sidebar-item 
            v-for="route in routes" 
            :key="route.path" 
            :item="route" 
            :base-path="route.path" 
          />
        </el-menu>
      </el-scrollbar>
    </div>
    
    <!-- 主区域 -->
    <div class="main-container">
      <!-- 顶部导航栏 -->
      <div class="navbar">
        <div class="left-menu">
          <div class="hamburger-container" @click="toggleSideBar">
            <i :class="isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'"></i>
          </div>
          <breadcrumb />
        </div>
        <div class="right-menu">
          <el-dropdown trigger="click">
            <div class="avatar-wrapper">
              <img src="@/assets/avatar.jpg" class="user-avatar" v-if="false">
              <i class="el-icon-user-solid" v-else></i>
              <span class="user-name">{{ userName }}</span>
              <i class="el-icon-caret-bottom"></i>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人中心</el-dropdown-item>
                <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
      
      <!-- 主内容区 -->
      <app-main />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { removeToken } from '@/utils/auth'
import { ElMessageBox, ElMessage } from 'element-plus'
import request from '@/utils/request'
import SidebarItem from './components/SidebarItem.vue'
import Breadcrumb from './components/Breadcrumb.vue'
import AppMain from './components/AppMain.vue'

// 路由信息
const route = useRoute()
const router = useRouter()

// 侧边栏折叠状态
const isCollapse = ref(false)

// 菜单数据
const menuList = ref([])
const loading = ref(false)

// 切换侧边栏折叠状态
const toggleSideBar = () => {
  isCollapse.value = !isCollapse.value
}

// 当前激活的菜单
const activeMenu = computed(() => {
  const { meta, path } = route
  if (meta.activeMenu) {
    return meta.activeMenu
  }
  return path
})

// 获取菜单列表
const getMenuList = async () => {
  try {
    loading.value = true
    const response = await request({
      url: '/sys/menu/list',
      method: 'post',
      data: {}
    })
    
    if (response.code === '200') {
      menuList.value = response.data || []
      // 将菜单数据转换为路由格式
      convertMenuToRoutes(response.data || [])
    } else {
      ElMessage.error(response.msg || '获取菜单失败')
    }
  } catch (error) {
    console.error('获取菜单失败:', error)
    ElMessage.error('获取菜单失败')
  } finally {
    loading.value = false
  }
}

// 将菜单数据转换为路由格式
const convertMenuToRoutes = (menus) => {
  const convertedRoutes = menus.map(menu => {
    const route = {
      path: menu.path || '/',
      name: menu.componentName || menu.name,
      component: menu.component,
      meta: {
        title: menu.name,
        icon: menu.icon,
        hidden: !menu.visible,
        keepAlive: menu.keepAlive,
        alwaysShow: menu.alwaysShow
      },
      children: menu.children && menu.children.length > 0 ? convertMenuToRoutes(menu.children) : []
    }
    return route
  }).filter(route => !route.meta.hidden && route.meta.title)
  
  // 动态添加路由
  convertedRoutes.forEach(route => {
    if (!router.hasRoute(route.name)) {
      router.addRoute(route)
    }
  })
}

// 路由列表 - 现在从菜单数据获取
const routes = computed(() => {
  return menuList.value.filter(menu => {
    return menu.visible && menu.type !== 2 && menu.status === 0 // 过滤隐藏菜单和按钮，只显示启用状态
  })
})

// 用户信息
const userName = ref('管理员')

// 退出登录
const logout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    removeToken()
    router.push('/login')
  }).catch(() => {
    // 用户取消退出
  })
}

// 组件挂载时获取菜单
onMounted(() => {
  getMenuList()
})
</script>

<style scoped>
.app-wrapper {
  display: flex;
  height: 100vh;
  width: 100%;
}

.sidebar-container {
  width: 210px;
  height: 100%;
  background-color: #304156;
  transition: width 0.28s;
  overflow: hidden;
}

.sidebar-container.is-collapse {
  width: 64px;
}

.logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #2b3649;
  padding: 0 16px;
}

.logo-img {
  width: 32px;
  height: 32px;
  margin-right: 12px;
}

.logo-title {
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  white-space: nowrap;
  overflow: hidden;
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.navbar {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  background-color: #fff;
}

.left-menu {
  display: flex;
  align-items: center;
}

.hamburger-container {
  padding: 0 16px;
  cursor: pointer;
  font-size: 20px;
}

.right-menu {
  display: flex;
  align-items: center;
}

.avatar-wrapper {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.user-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-right: 8px;
}

.user-name {
  margin-right: 8px;
  font-size: 14px;
}
</style>