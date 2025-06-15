<template>
  <div class="workflow-analytics-container">
    <div class="page-header">
      <h2>工作流分析</h2>
      <p>工作流数据统计与分析报表</p>
    </div>
    
    <!-- 时间范围选择 -->
    <div class="filter-section">
      <el-card>
        <el-form :model="filterForm" :inline="true" label-width="80px">
          <el-form-item label="时间范围">
            <el-date-picker
              v-model="filterForm.dateRange"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 350px;"
            />
          </el-form-item>
          
          <el-form-item label="流程分类">
            <el-select
              v-model="filterForm.category"
              placeholder="请选择流程分类"
              clearable
              style="width: 150px;"
            >
              <el-option
                v-for="category in categoryOptions"
                :key="category.value"
                :label="category.label"
                :value="category.value"
              />
            </el-select>
          </el-form-item>
          
          <el-form-item label="流程定义">
            <el-select
              v-model="filterForm.processDefinitionKey"
              placeholder="请选择流程定义"
              clearable
              style="width: 200px;"
            >
              <el-option
                v-for="process in processDefinitions"
                :key="process.key"
                :label="process.name"
                :value="process.key"
              />
            </el-select>
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              查询
            </el-button>
            <el-button @click="handleReset">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
            <el-button @click="handleExport">
              <el-icon><Download /></el-icon>
              导出报表
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    
    <!-- 概览统计 -->
    <div class="overview-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="overview-content">
              <div class="overview-icon total">
                <el-icon><DataAnalysis /></el-icon>
              </div>
              <div class="overview-info">
                <div class="overview-number">{{ overviewData.totalInstances }}</div>
                <div class="overview-label">总流程实例</div>
                <div class="overview-trend" :class="overviewData.instancesTrend > 0 ? 'up' : 'down'">
                  <el-icon><TrendCharts /></el-icon>
                  {{ Math.abs(overviewData.instancesTrend) }}%
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="overview-content">
              <div class="overview-icon completed">
                <el-icon><CircleCheck /></el-icon>
              </div>
              <div class="overview-info">
                <div class="overview-number">{{ overviewData.completedInstances }}</div>
                <div class="overview-label">已完成实例</div>
                <div class="overview-trend" :class="overviewData.completedTrend > 0 ? 'up' : 'down'">
                  <el-icon><TrendCharts /></el-icon>
                  {{ Math.abs(overviewData.completedTrend) }}%
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="overview-content">
              <div class="overview-icon running">
                <el-icon><VideoPlay /></el-icon>
              </div>
              <div class="overview-info">
                <div class="overview-number">{{ overviewData.runningInstances }}</div>
                <div class="overview-label">运行中实例</div>
                <div class="overview-trend" :class="overviewData.runningTrend > 0 ? 'up' : 'down'">
                  <el-icon><TrendCharts /></el-icon>
                  {{ Math.abs(overviewData.runningTrend) }}%
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="overview-content">
              <div class="overview-icon avg-duration">
                <el-icon><Timer /></el-icon>
              </div>
              <div class="overview-info">
                <div class="overview-number">{{ overviewData.avgDuration }}</div>
                <div class="overview-label">平均处理时长(小时)</div>
                <div class="overview-trend" :class="overviewData.durationTrend > 0 ? 'down' : 'up'">
                  <el-icon><TrendCharts /></el-icon>
                  {{ Math.abs(overviewData.durationTrend) }}%
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <!-- 图表分析 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <!-- 流程实例趋势图 -->
        <el-col :span="12">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>流程实例趋势</span>
                <el-radio-group v-model="instanceTrendType" size="small">
                  <el-radio-button label="day">按天</el-radio-button>
                  <el-radio-button label="week">按周</el-radio-button>
                  <el-radio-button label="month">按月</el-radio-button>
                </el-radio-group>
              </div>
            </template>
            
            <div ref="instanceTrendChart" class="chart-container"></div>
          </el-card>
        </el-col>
        
        <!-- 任务状态分布 -->
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>任务状态分布</span>
            </template>
            
            <div ref="taskStatusChart" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" style="margin-top: 20px;">
        <!-- 流程定义统计 -->
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>流程定义统计</span>
            </template>
            
            <div ref="processDefChart" class="chart-container"></div>
          </el-card>
        </el-col>
        
        <!-- 用户任务处理量 -->
        <el-col :span="12">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>用户任务处理量</span>
                <el-select v-model="userTaskPeriod" size="small" style="width: 100px;">
                  <el-option label="本周" value="week" />
                  <el-option label="本月" value="month" />
                  <el-option label="本季" value="quarter" />
                </el-select>
              </div>
            </template>
            
            <div ref="userTaskChart" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" style="margin-top: 20px;">
        <!-- 平均处理时长 -->
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>平均处理时长分析</span>
            </template>
            
            <div ref="avgDurationChart" class="chart-container"></div>
          </el-card>
        </el-col>
        
        <!-- 流程效率分析 -->
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>流程效率分析</span>
            </template>
            
            <div ref="efficiencyChart" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <!-- 详细数据表格 -->
    <div class="table-section">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>详细数据</span>
            <el-tabs v-model="activeTab" type="card" size="small">
              <el-tab-pane label="流程实例" name="instances" />
              <el-tab-pane label="任务统计" name="tasks" />
              <el-tab-pane label="用户统计" name="users" />
              <el-tab-pane label="性能分析" name="performance" />
            </el-tabs>
          </div>
        </template>
        
        <!-- 流程实例表格 -->
        <div v-show="activeTab === 'instances'">
          <el-table :data="instanceTableData" stripe style="width: 100%">
            <el-table-column prop="processDefinitionName" label="流程定义" min-width="150" />
            <el-table-column prop="totalCount" label="总数" width="100" />
            <el-table-column prop="runningCount" label="运行中" width="100" />
            <el-table-column prop="completedCount" label="已完成" width="100" />
            <el-table-column prop="suspendedCount" label="已暂停" width="100" />
            <el-table-column prop="avgDuration" label="平均时长(小时)" width="120" />
            <el-table-column prop="completionRate" label="完成率" width="100">
              <template #default="{ row }">
                <el-progress :percentage="row.completionRate" :stroke-width="8" />
              </template>
            </el-table-column>
          </el-table>
        </div>
        
        <!-- 任务统计表格 -->
        <div v-show="activeTab === 'tasks'">
          <el-table :data="taskTableData" stripe style="width: 100%">
            <el-table-column prop="taskName" label="任务名称" min-width="150" />
            <el-table-column prop="totalCount" label="总数" width="100" />
            <el-table-column prop="pendingCount" label="待办" width="100" />
            <el-table-column prop="completedCount" label="已完成" width="100" />
            <el-table-column prop="avgProcessTime" label="平均处理时间(小时)" width="150" />
            <el-table-column prop="overdueCount" label="超时数量" width="100" />
            <el-table-column prop="overdueRate" label="超时率" width="100">
              <template #default="{ row }">
                <el-tag :type="row.overdueRate > 20 ? 'danger' : row.overdueRate > 10 ? 'warning' : 'success'">
                  {{ row.overdueRate }}%
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
        
        <!-- 用户统计表格 -->
        <div v-show="activeTab === 'users'">
          <el-table :data="userTableData" stripe style="width: 100%">
            <el-table-column prop="userName" label="用户名" width="120" />
            <el-table-column prop="completedTasks" label="已完成任务" width="120" />
            <el-table-column prop="pendingTasks" label="待办任务" width="120" />
            <el-table-column prop="avgProcessTime" label="平均处理时间(小时)" width="150" />
            <el-table-column prop="efficiency" label="效率评分" width="100">
              <template #default="{ row }">
                <el-rate v-model="row.efficiency" disabled show-score text-color="#ff9900" />
              </template>
            </el-table-column>
            <el-table-column prop="workload" label="工作负荷" width="100">
              <template #default="{ row }">
                <el-tag :type="getWorkloadType(row.workload)">
                  {{ getWorkloadText(row.workload) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
        
        <!-- 性能分析表格 -->
        <div v-show="activeTab === 'performance'">
          <el-table :data="performanceTableData" stripe style="width: 100%">
            <el-table-column prop="processDefinitionName" label="流程定义" min-width="150" />
            <el-table-column prop="avgStartTime" label="平均启动时间(秒)" width="150" />
            <el-table-column prop="avgExecutionTime" label="平均执行时间(小时)" width="150" />
            <el-table-column prop="throughput" label="吞吐量(个/天)" width="120" />
            <el-table-column prop="errorRate" label="错误率" width="100">
              <template #default="{ row }">
                <el-tag :type="row.errorRate > 5 ? 'danger' : row.errorRate > 2 ? 'warning' : 'success'">
                  {{ row.errorRate }}%
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="performanceScore" label="性能评分" width="120">
              <template #default="{ row }">
                <el-progress :percentage="row.performanceScore" :stroke-width="8" :color="getPerformanceColor(row.performanceScore)" />
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, watch } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Search,
  Refresh,
  Download,
  DataAnalysis,
  TrendCharts,
  CircleCheck,
  VideoPlay,
  Timer
} from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import {
  getWorkflowOverview,
  getInstanceTrendData,
  getTaskStatusData,
  getProcessDefStats,
  getUserTaskStats,
  getAvgDurationData,
  getEfficiencyData,
  getInstanceTableData,
  getTaskTableData,
  getUserTableData,
  getPerformanceTableData
} from '@/api/workflow'
import { getCategoryTree, getProcessDefinitionList } from '@/api/workflowConfig'

// 筛选表单
const filterForm = reactive({
  dateRange: [],
  category: '',
  processDefinitionKey: ''
})

// 选项数据
const categoryOptions = ref([])
const processDefinitions = ref([])

// 概览数据
const overviewData = ref({
  totalInstances: 0,
  completedInstances: 0,
  runningInstances: 0,
  avgDuration: 0,
  instancesTrend: 0,
  completedTrend: 0,
  runningTrend: 0,
  durationTrend: 0
})

// 图表配置
const instanceTrendType = ref('day')
const userTaskPeriod = ref('week')
const activeTab = ref('instances')

// 图表实例
const instanceTrendChart = ref()
const taskStatusChart = ref()
const processDefChart = ref()
const userTaskChart = ref()
const avgDurationChart = ref()
const efficiencyChart = ref()

let instanceTrendChartInstance = null
let taskStatusChartInstance = null
let processDefChartInstance = null
let userTaskChartInstance = null
let avgDurationChartInstance = null
let efficiencyChartInstance = null

// 表格数据
const instanceTableData = ref([])
const taskTableData = ref([])
const userTableData = ref([])
const performanceTableData = ref([])

// 获取工作负荷类型
const getWorkloadType = (workload) => {
  if (workload === 'high') return 'danger'
  if (workload === 'medium') return 'warning'
  return 'success'
}

const getWorkloadText = (workload) => {
  const textMap = {
    high: '高',
    medium: '中',
    low: '低'
  }
  return textMap[workload] || workload
}

// 获取性能评分颜色
const getPerformanceColor = (score) => {
  if (score >= 80) return '#67c23a'
  if (score >= 60) return '#e6a23c'
  return '#f56c6c'
}

// 搜索
const handleSearch = () => {
  loadAllData()
}

// 重置
const handleReset = () => {
  filterForm.dateRange = []
  filterForm.category = ''
  filterForm.processDefinitionKey = ''
  loadAllData()
}

// 导出报表
const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

// 获取概览数据
const getOverviewData = async () => {
  try {
    const params = {
      startTime: filterForm.dateRange[0],
      endTime: filterForm.dateRange[1],
      category: filterForm.category,
      processDefinitionKey: filterForm.processDefinitionKey
    }
    
    const response = await getWorkflowOverview(params)
    overviewData.value = response.data
  } catch (error) {
    ElMessage.error('获取概览数据失败')
  }
}

// 初始化流程实例趋势图
const initInstanceTrendChart = async () => {
  try {
    const params = {
      type: instanceTrendType.value,
      startTime: filterForm.dateRange[0],
      endTime: filterForm.dateRange[1],
      category: filterForm.category,
      processDefinitionKey: filterForm.processDefinitionKey
    }
    
    const response = await getInstanceTrendData(params)
    const data = response.data
    
    if (!instanceTrendChartInstance) {
      instanceTrendChartInstance = echarts.init(instanceTrendChart.value)
    }
    
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross'
        }
      },
      legend: {
        data: ['启动数量', '完成数量']
      },
      xAxis: {
        type: 'category',
        data: data.dates
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '启动数量',
          type: 'line',
          data: data.startCounts,
          smooth: true,
          itemStyle: {
            color: '#409EFF'
          }
        },
        {
          name: '完成数量',
          type: 'line',
          data: data.completeCounts,
          smooth: true,
          itemStyle: {
            color: '#67C23A'
          }
        }
      ]
    }
    
    instanceTrendChartInstance.setOption(option)
  } catch (error) {
    ElMessage.error('获取流程实例趋势数据失败')
  }
}

// 初始化任务状态分布图
const initTaskStatusChart = async () => {
  try {
    const params = {
      startTime: filterForm.dateRange[0],
      endTime: filterForm.dateRange[1],
      category: filterForm.category,
      processDefinitionKey: filterForm.processDefinitionKey
    }
    
    const response = await getTaskStatusData(params)
    const data = response.data
    
    if (!taskStatusChartInstance) {
      taskStatusChartInstance = echarts.init(taskStatusChart.value)
    }
    
    const option = {
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          name: '任务状态',
          type: 'pie',
          radius: '50%',
          data: [
            { value: data.pending, name: '待办', itemStyle: { color: '#E6A23C' } },
            { value: data.running, name: '进行中', itemStyle: { color: '#409EFF' } },
            { value: data.completed, name: '已完成', itemStyle: { color: '#67C23A' } },
            { value: data.suspended, name: '已暂停', itemStyle: { color: '#909399' } }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    }
    
    taskStatusChartInstance.setOption(option)
  } catch (error) {
    ElMessage.error('获取任务状态数据失败')
  }
}

// 初始化流程定义统计图
const initProcessDefChart = async () => {
  try {
    const params = {
      startTime: filterForm.dateRange[0],
      endTime: filterForm.dateRange[1],
      category: filterForm.category
    }
    
    const response = await getProcessDefStats(params)
    const data = response.data
    
    if (!processDefChartInstance) {
      processDefChartInstance = echarts.init(processDefChart.value)
    }
    
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      xAxis: {
        type: 'category',
        data: data.processNames,
        axisLabel: {
          rotate: 45
        }
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '实例数量',
          type: 'bar',
          data: data.instanceCounts,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' }
            ])
          }
        }
      ]
    }
    
    processDefChartInstance.setOption(option)
  } catch (error) {
    ElMessage.error('获取流程定义统计数据失败')
  }
}

// 初始化用户任务处理量图
const initUserTaskChart = async () => {
  try {
    const params = {
      period: userTaskPeriod.value,
      startTime: filterForm.dateRange[0],
      endTime: filterForm.dateRange[1],
      category: filterForm.category,
      processDefinitionKey: filterForm.processDefinitionKey
    }
    
    const response = await getUserTaskStats(params)
    const data = response.data
    
    if (!userTaskChartInstance) {
      userTaskChartInstance = echarts.init(userTaskChart.value)
    }
    
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      xAxis: {
        type: 'category',
        data: data.userNames
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '处理任务数',
          type: 'bar',
          data: data.taskCounts,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#ffecd2' },
              { offset: 1, color: '#fcb69f' }
            ])
          }
        }
      ]
    }
    
    userTaskChartInstance.setOption(option)
  } catch (error) {
    ElMessage.error('获取用户任务统计数据失败')
  }
}

// 初始化平均处理时长图
const initAvgDurationChart = async () => {
  try {
    const params = {
      startTime: filterForm.dateRange[0],
      endTime: filterForm.dateRange[1],
      category: filterForm.category,
      processDefinitionKey: filterForm.processDefinitionKey
    }
    
    const response = await getAvgDurationData(params)
    const data = response.data
    
    if (!avgDurationChartInstance) {
      avgDurationChartInstance = echarts.init(avgDurationChart.value)
    }
    
    const option = {
      tooltip: {
        trigger: 'axis'
      },
      xAxis: {
        type: 'category',
        data: data.processNames
      },
      yAxis: {
        type: 'value',
        name: '小时'
      },
      series: [
        {
          name: '平均处理时长',
          type: 'line',
          data: data.avgDurations,
          smooth: true,
          itemStyle: {
            color: '#FF6B6B'
          },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(255, 107, 107, 0.3)' },
              { offset: 1, color: 'rgba(255, 107, 107, 0.1)' }
            ])
          }
        }
      ]
    }
    
    avgDurationChartInstance.setOption(option)
  } catch (error) {
    ElMessage.error('获取平均处理时长数据失败')
  }
}

// 初始化流程效率分析图
const initEfficiencyChart = async () => {
  try {
    const params = {
      startTime: filterForm.dateRange[0],
      endTime: filterForm.dateRange[1],
      category: filterForm.category,
      processDefinitionKey: filterForm.processDefinitionKey
    }
    
    const response = await getEfficiencyData(params)
    const data = response.data
    
    if (!efficiencyChartInstance) {
      efficiencyChartInstance = echarts.init(efficiencyChart.value)
    }
    
    const option = {
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: ['完成率', '及时率']
      },
      xAxis: {
        type: 'category',
        data: data.processNames
      },
      yAxis: {
        type: 'value',
        max: 100,
        axisLabel: {
          formatter: '{value}%'
        }
      },
      series: [
        {
          name: '完成率',
          type: 'bar',
          data: data.completionRates,
          itemStyle: {
            color: '#67C23A'
          }
        },
        {
          name: '及时率',
          type: 'bar',
          data: data.timelyRates,
          itemStyle: {
            color: '#409EFF'
          }
        }
      ]
    }
    
    efficiencyChartInstance.setOption(option)
  } catch (error) {
    ElMessage.error('获取流程效率数据失败')
  }
}

// 获取表格数据
const getTableData = async () => {
  try {
    const params = {
      startTime: filterForm.dateRange[0],
      endTime: filterForm.dateRange[1],
      category: filterForm.category,
      processDefinitionKey: filterForm.processDefinitionKey
    }
    
    // 并行获取所有表格数据
    const [instanceRes, taskRes, userRes, performanceRes] = await Promise.all([
      getInstanceTableData(params),
      getTaskTableData(params),
      getUserTableData(params),
      getPerformanceTableData(params)
    ])
    
    instanceTableData.value = instanceRes.data
    taskTableData.value = taskRes.data
    userTableData.value = userRes.data
    performanceTableData.value = performanceRes.data
  } catch (error) {
    ElMessage.error('获取表格数据失败')
  }
}

// 获取分类选项
const getCategoryOptions = async () => {
  try {
    const response = await getCategoryTree()
    categoryOptions.value = response.data.map(item => ({
      label: item.categoryName,
      value: item.id
    }))
  } catch (error) {
    ElMessage.error('获取分类列表失败')
  }
}

// 获取流程定义列表
const getProcessDefinitions = async () => {
  try {
    const response = await getProcessDefinitionList({ size: 1000 })
    processDefinitions.value = response.data.records
  } catch (error) {
    ElMessage.error('获取流程定义列表失败')
  }
}

// 加载所有数据
const loadAllData = async () => {
  await getOverviewData()
  await nextTick()
  
  // 初始化所有图表
  await Promise.all([
    initInstanceTrendChart(),
    initTaskStatusChart(),
    initProcessDefChart(),
    initUserTaskChart(),
    initAvgDurationChart(),
    initEfficiencyChart()
  ])
  
  await getTableData()
}

// 监听图表类型变化
watch(instanceTrendType, () => {
  initInstanceTrendChart()
})

watch(userTaskPeriod, () => {
  initUserTaskChart()
})

// 窗口大小变化时重新调整图表
const handleResize = () => {
  instanceTrendChartInstance?.resize()
  taskStatusChartInstance?.resize()
  processDefChartInstance?.resize()
  userTaskChartInstance?.resize()
  avgDurationChartInstance?.resize()
  efficiencyChartInstance?.resize()
}

// 初始化
onMounted(async () => {
  // 设置默认时间范围（最近30天）
  const endDate = new Date()
  const startDate = new Date()
  startDate.setDate(startDate.getDate() - 30)
  
  filterForm.dateRange = [
    startDate.toISOString().slice(0, 19).replace('T', ' '),
    endDate.toISOString().slice(0, 19).replace('T', ' ')
  ]
  
  await getCategoryOptions()
  await getProcessDefinitions()
  await loadAllData()
  
  // 监听窗口大小变化
  window.addEventListener('resize', handleResize)
})

// 组件卸载时清理
onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  
  instanceTrendChartInstance?.dispose()
  taskStatusChartInstance?.dispose()
  processDefChartInstance?.dispose()
  userTaskChartInstance?.dispose()
  avgDurationChartInstance?.dispose()
  efficiencyChartInstance?.dispose()
})
</script>

<style scoped>
.workflow-analytics-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.filter-section {
  margin-bottom: 20px;
}

.overview-section {
  margin-bottom: 20px;
}

.overview-card {
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.overview-content {
  display: flex;
  align-items: center;
  padding: 10px 0;
}

.overview-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 24px;
  color: #fff;
}

.overview-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.overview-icon.completed {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.overview-icon.running {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.overview-icon.avg-duration {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.overview-info {
  flex: 1;
}

.overview-number {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  line-height: 1;
  margin-bottom: 8px;
}

.overview-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 4px;
}

.overview-trend {
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 2px;
}

.overview-trend.up {
  color: #67c23a;
}

.overview-trend.down {
  color: #f56c6c;
}

.charts-section {
  margin-bottom: 20px;
}

.table-section {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  width: 100%;
  height: 300px;
}

.el-card {
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.el-form-item {
  margin-bottom: 0;
}

.el-table {
  margin-top: 10px;
}

.el-tabs {
  margin-left: 20px;
}

.el-tabs :deep(.el-tabs__header) {
  margin: 0;
}

.el-tabs :deep(.el-tabs__nav-wrap) {
  padding: 0;
}
</style>