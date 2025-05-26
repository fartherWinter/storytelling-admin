<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="welcome-card">
          <template #header>
            <div class="card-header">
              <span>欢迎使用ERP系统</span>
            </div>
          </template>
          <div class="welcome-content">
            <h2>管理员工作流模块</h2>
            <p>本模块提供以下功能：</p>
            <el-divider></el-divider>
            <el-row :gutter="20">
              <el-col :xs="24" :sm="12" :md="8" :lg="6">
                <el-card shadow="hover" class="feature-card">
                  <template #header>
                    <div class="feature-header">
                      <el-icon><Document /></el-icon>
                      <span>流程部署</span>
                    </div>
                  </template>
                  <div class="feature-content">
                    支持上传BPMN流程定义文件，部署管理员创建审批流程
                  </div>
                </el-card>
              </el-col>
              <el-col :xs="24" :sm="12" :md="8" :lg="6">
                <el-card shadow="hover" class="feature-card">
                  <template #header>
                    <div class="feature-header">
                      <el-icon><Plus /></el-icon>
                      <span>启动流程</span>
                    </div>
                  </template>
                  <div class="feature-content">
                    填写管理员信息，启动管理员创建审批流程
                  </div>
                </el-card>
              </el-col>
              <el-col :xs="24" :sm="12" :md="8" :lg="6">
                <el-card shadow="hover" class="feature-card">
                  <template #header>
                    <div class="feature-header">
                      <el-icon><List /></el-icon>
                      <span>任务管理</span>
                    </div>
                  </template>
                  <div class="feature-content">
                    查看待办任务列表，进行审批或拒绝操作
                  </div>
                </el-card>
              </el-col>
              <el-col :xs="24" :sm="12" :md="8" :lg="6">
                <el-card shadow="hover" class="feature-card">
                  <template #header>
                    <div class="feature-header">
                      <el-icon><PictureRounded /></el-icon>
                      <span>流程图</span>
                    </div>
                  </template>
                  <div class="feature-content">
                    查看流程实例的流程图，直观了解流程执行情况
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { Document, Plus, List, PictureRounded } from '@element-plus/icons-vue'
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.welcome-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-content {
  padding: 10px 0;
}

.feature-card {
  height: 100%;
  margin-bottom: 20px;
  transition: all 0.3s;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.feature-header {
  display: flex;
  align-items: center;
}

.feature-header .el-icon {
  margin-right: 8px;
  font-size: 18px;
}

.feature-content {
  color: #606266;
  font-size: 14px;
  line-height: 1.5;
}
</style>