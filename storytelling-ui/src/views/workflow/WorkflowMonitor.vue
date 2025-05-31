<template>
  <div class="workflow-monitor-container">
    <div class="page-header">
      <h2>流程监控</h2>
      <p>实时监控工作流程执行状态和性能指标</p>
    </div>
    
    <div class="content-area">
      <!-- 监控概览 -->
      <div class="overview-cards">
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon size="24" color="#409EFF"><Monitor /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-title">运行中流程</div>
              <div class="card-value">{{ runningProcesses }}</div>
            </div>
          </div>
        </el-card>
        
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon size="24" color="#67C23A"><CircleCheck /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-title">已完成</div>
              <div class="card-value">{{ completedProcesses }}</div>
            </div>
          </div>
        </el-card>
        
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon size="24" color="#E6A23C"><Clock /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-title">待处理</div>
              <div class="card-value">{{ pendingProcesses }}</div>
            </div>
          </div>
        </el-card>
        
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon size="24" color="#F56C6C"><Warning /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-title">异常流程</div>
              <div class="card-value">{{ errorProcesses }}</div>
            </div>
          </div>
        </el-card>
      </div>
      
      <!-- 流程列表 -->
      <el-card>
        <template #header>
          <div class="card-header">
            <span>流程实例</span>
            <div class="header-actions">
              <el-select v-model="statusFilter" placeholder="筛选状态" style="width: 120px; margin-right: 10px;" clearable>
                <el-option label="全部" value="" />
                <el-option label="运行中" value="running" />
                <el-option label="已完成" value="completed" />
                <el-option label="待处理" value="pending" />
                <el-option label="异常" value="error" />
              </el-select>
              <el-input
                v-model="searchText"
                placeholder="搜索流程名称"
                style="width: 200px; margin-right: 10px;"
                clearable
              >
                <template #prefix>
                  <el-icon><Search /></el-icon>
                </template>
              </el-input>
              <el-button type="primary" @click="refreshData">
                <el-icon><Refresh /></el-icon>
                刷新
              </el-button>
            </div>
          </div>
        </template>
        
        <el-table :data="filteredProcesses" style="width: 100%">
          <el-table-column prop="id" label="流程ID" width="120" />
          <el-table-column prop="name" label="流程名称" width="200" />
          <el-table-column prop="initiator" label="发起人" width="120" />
          <el-table-column prop="currentStep" label="当前步骤" width="150" />
          <el-table-column label="状态" width="120" align="center">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="进度" width="150" align="center">
            <template #default="scope">
              <el-progress 
                :percentage="scope.row.progress" 
                :status="scope.row.status === 'error' ? 'exception' : (scope.row.status === 'completed' ? 'success' : '')"
                :stroke-width="8"
              />
            </template>
          </el-table-column>
          <el-table-column prop="startTime" label="开始时间" width="150" />
          <el-table-column prop="duration" label="耗时" width="100" />
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button size="small" @click="viewProcess(scope.row)">查看</el-button>
              <el-button 
                size="small" 
                type="warning" 
                @click="pauseProcess(scope.row)"
                :disabled="scope.row.status !== 'running'"
              >
                暂停
              </el-button>
              <el-button 
                size="small" 
                type="danger" 
                @click="terminateProcess(scope.row)"
                :disabled="scope.row.status === 'completed'"
              >
                终止
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      
      <!-- 性能监控图表 -->
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>流程执行趋势</span>
            </template>
            <div class="chart-container">
              <div class="chart-placeholder">
                📊 流程执行趋势图表
                <p>显示最近7天的流程执行数量变化</p>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>平均执行时间</span>
            </template>
            <div class="chart-container">
              <div class="chart-placeholder">
                ⏱️ 平均执行时间统计
                <p>不同类型流程的平均执行时间对比</p>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <!-- 流程详情弹窗 -->
    <el-dialog v-model="detailVisible" title="流程详情" width="800px">
      <div v-if="selectedProcess" class="process-detail">
        <div class="detail-info">
          <h4>基本信息</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="流程ID">{{ selectedProcess.id }}</el-descriptions-item>
            <el-descriptions-item label="流程名称">{{ selectedProcess.name }}</el-descriptions-item>
            <el-descriptions-item label="发起人">{{ selectedProcess.initiator }}</el-descriptions-item>
            <el-descriptions-item label="当前步骤">{{ selectedProcess.currentStep }}</el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="getStatusType(selectedProcess.status)">
                {{ getStatusText(selectedProcess.status) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="进度">
              <el-progress :percentage="selectedProcess.progress" />
            </el-descriptions-item>
            <el-descriptions-item label="开始时间">{{ selectedProcess.startTime }}</el-descriptions-item>
            <el-descriptions-item label="耗时">{{ selectedProcess.duration }}</el-descriptions-item>
          </el-descriptions>
        </div>
        
        <div class="step-timeline">
          <h4>执行步骤</h4>
          <el-timeline>
            <el-timeline-item
              v-for="(step, index) in processSteps"
              :key="index"
              :timestamp="step.time"
              :type="step.status === 'completed' ? 'success' : (step.status === 'current' ? 'primary' : 'info')"
            >
              <div class="step-content">
                <div class="step-title">{{ step.name }}</div>
                <div class="step-description">{{ step.description }}</div>
                <div class="step-assignee">处理人: {{ step.assignee }}</div>
              </div>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Monitor, 
  CircleCheck, 
  Clock, 
  Warning, 
  Search, 
  Refresh 
} from '@element-plus/icons-vue'

const searchText = ref('')
const statusFilter = ref('')
const detailVisible = ref(false)
const selectedProcess = ref(null)

const processes = ref([
  {
    id: 'WF001',
    name: '采购申请流程',
    initiator: '张三',
    currentStep: '部门经理审批',
    status: 'running',
    progress: 60,
    startTime: '2024-01-15 09:00',
    duration: '2小时30分'
  },
  {
    id: 'WF002',
    name: '请假申请流程',
    initiator: '李四',
    currentStep: '已完成',
    status: 'completed',
    progress: 100,
    startTime: '2024-01-15 08:30',
    duration: '1小时45分'
  },
  {
    id: 'WF003',
    name: '报销申请流程',
    initiator: '王五',
    currentStep: '财务审核',
    status: 'pending',
    progress: 40,
    startTime: '2024-01-15 10:15',
    duration: '1小时15分'
  },
  {
    id: 'WF004',
    name: '合同审批流程',
    initiator: '赵六',
    currentStep: '法务审核',
    status: 'error',
    progress: 30,
    startTime: '2024-01-15 07:45',
    duration: '3小时20分'
  }
])

const processSteps = ref([
  {
    name: '流程发起',
    description: '用户提交申请',
    assignee: '张三',
    time: '2024-01-15 09:00',
    status: 'completed'
  },
  {
    name: '直属领导审批',
    description: '直属领导审核申请内容',
    assignee: '李经理',
    time: '2024-01-15 09:30',
    status: 'completed'
  },
  {
    name: '部门经理审批',
    description: '部门经理进行最终审批',
    assignee: '王总监',
    time: '2024-01-15 10:00',
    status: 'current'
  },
  {
    name: '流程结束',
    description: '流程执行完成',
    assignee: '系统',
    time: '',
    status: 'pending'
  }
])

const filteredProcesses = computed(() => {
  let filtered = processes.value
  
  if (statusFilter.value) {
    filtered = filtered.filter(process => process.status === statusFilter.value)
  }
  
  if (searchText.value) {
    filtered = filtered.filter(process => 
      process.name.toLowerCase().includes(searchText.value.toLowerCase())
    )
  }
  
  return filtered
})

const runningProcesses = computed(() => {
  return processes.value.filter(process => process.status === 'running').length
})

const completedProcesses = computed(() => {
  return processes.value.filter(process => process.status === 'completed').length
})

const pendingProcesses = computed(() => {
  return processes.value.filter(process => process.status === 'pending').length
})

const errorProcesses = computed(() => {
  return processes.value.filter(process => process.status === 'error').length
})

const getStatusText = (status) => {
  const statusMap = {
    'running': '运行中',
    'completed': '已完成',
    'pending': '待处理',
    'error': '异常'
  }
  return statusMap[status] || status
}

const getStatusType = (status) => {
  const typeMap = {
    'running': 'primary',
    'completed': 'success',
    'pending': 'warning',
    'error': 'danger'
  }
  return typeMap[status] || 'info'
}

const refreshData = () => {
  ElMessage.success('数据已刷新')
}

const viewProcess = (process) => {
  selectedProcess.value = process
  detailVisible.value = true
}

const pauseProcess = async (process) => {
  try {
    await ElMessageBox.confirm(
      `确定要暂停流程 "${process.name}" 吗？`,
      '确认暂停',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    ElMessage.success('流程已暂停')
  } catch {
    ElMessage.info('已取消暂停')
  }
}

const terminateProcess = async (process) => {
  try {
    await ElMessageBox.confirm(
      `确定要终止流程 "${process.name}" 吗？此操作不可恢复！`,
      '确认终止',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    ElMessage.success('流程已终止')
  } catch {
    ElMessage.info('已取消终止')
  }
}

onMounted(() => {
  // 初始化数据
})
</script>

<style scoped>
.workflow-monitor-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.overview-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.overview-card {
  cursor: pointer;
  transition: transform 0.2s;
}

.overview-card:hover {
  transform: translateY(-2px);
}

.card-content {
  display: flex;
  align-items: center;
  padding: 10px;
}

.card-icon {
  margin-right: 15px;
}

.card-info {
  flex: 1;
}

.card-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.card-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  align-items: center;
}

.content-area {
  background: #f5f5f5;
  min-height: calc(100vh - 200px);
}

.chart-container {
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.chart-placeholder {
  text-align: center;
  color: #909399;
  font-size: 16px;
}

.chart-placeholder p {
  margin: 10px 0 0 0;
  font-size: 12px;
}

.process-detail {
  padding: 10px 0;
}

.detail-info {
  margin-bottom: 20px;
}

.detail-info h4 {
  margin: 0 0 15px 0;
  color: #303133;
}

.step-timeline h4 {
  margin: 0 0 15px 0;
  color: #303133;
}

.step-content {
  padding: 5px 0;
}

.step-title {
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.step-description {
  color: #606266;
  margin-bottom: 3px;
}

.step-assignee {
  color: #909399;
  font-size: 12px;
}
</style>