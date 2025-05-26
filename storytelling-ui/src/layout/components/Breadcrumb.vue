<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item v-for="(item, index) in levelList" :key="item.path">
        <span v-if="item.redirect === 'noRedirect' || index === levelList.length - 1" class="no-redirect">{{ item.meta.title }}</span>
        <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script setup>
import { ref, watch, onBeforeMount } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { compile } from 'path-to-regexp'

const route = useRoute()
const router = useRouter()

// 面包屑层级列表
const levelList = ref([])

// 监听路由变化
watch(
  () => route.path,
  () => {
    getBreadcrumb()
  }
)

// 获取面包屑数据
const getBreadcrumb = () => {
  // 只显示有meta.title的路由项
  let matched = route.matched.filter(item => item.meta && item.meta.title)
  
  // 如果第一个不是首页，则添加首页
  const first = matched[0]
  if (first && first.path !== '/') {
    matched = [{ path: '/', meta: { title: '首页' } }].concat(matched)
  }
  
  levelList.value = matched.filter(item => item.meta && item.meta.title && item.meta.breadcrumb !== false)
}

// 处理链接点击
const handleLink = (item) => {
  const { redirect, path } = item
  if (redirect) {
    router.push(redirect)
    return
  }
  router.push(path)
}

// 路径解析
const pathCompile = (path) => {
  const { params } = route
  const toPath = compile(path)
  return toPath(params)
}

onBeforeMount(() => {
  getBreadcrumb()
})
</script>

<style lang="scss" scoped>
.app-breadcrumb {
  display: inline-block;
  font-size: 14px;
  line-height: 60px;
  margin-left: 8px;
  
  .no-redirect {
    color: #97a8be;
    cursor: text;
  }
}
</style>