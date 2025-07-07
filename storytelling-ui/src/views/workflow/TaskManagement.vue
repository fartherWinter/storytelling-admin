<template>
  <div class="task-management-container">
    <div class="page-header">
      <h2>任务管理</h2>
      <p>管理和处理工作流任务</p>
    </div>
    
    <!-- 搜索和筛选 -->
    <div class="search-section">
      <el-card>
        <el-form :model="searchForm" :inline="true" label-width="80px">
          <el-form-item label="任务名称">
            <el-input
              v-model="searchForm.taskName"
              placeholder="请输入任务名称"
              clearable
              style="width: 200px;"
            />
          </el-form-item>
          
          <el-form-item label="任务状态">
            <el-select
              v-model="searchForm.taskStatus"
              placeholder="请选择任务状态"
              clearable
              style="width: 150px;"
            >
              <el-option label="待办" value="pending" />
              <el-option label="进行中" value="running" />
              <el-option label="已完成" value="completed" />
              <el-option label="已暂停" value="suspended" />
              <el-option label="已取消" value="cancelled" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="分配人">
            <el-input
              v-model="searchForm.assignee"
              placeholder="请输入分配人"
              clearable
              style="width: 150px;"
            />
          </el-form-item>
          
          <el-form-item label="流程定义">
            <el-select
              v-model="searchForm.processDefinitionKey"
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
          
          <el-form-item label="优先级">
            <el-select
              v-model="searchForm.priority"
              placeholder="请选择优先级"
              clearable
              style="width: 120px;"
            >
              <el-option label="低" value="low" />
              <el-option label="中" value="medium" />
              <el-option label="高" value="high" />
              <el-option label="紧急" value="urgent" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="创建时间">
            <el-date-picker
              v-model="searchForm.createTimeRange"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 350px;"
            />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="handleReset">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    
    <!-- 任务统计 -->
    <div class="stats-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon pending">
                <el-icon><Clock /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-number">{{ taskStats.pending }}</div>
                <div class="stats-label">待办任务</div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon running">
                <el-icon><VideoPlay /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-number">{{ taskStats.running }}</div>
                <div class="stats-label">进行中</div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon completed">
                <el-icon><CircleCheck /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-number">{{ taskStats.completed }}</div>
                <div class="stats-label">已完成</div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon overdue">
                <el-icon><Warning /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-number">{{ taskStats.overdue }}</div>
                <div class="stats-label">超时任务</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <!-- 任务列表 -->
    <div class="table-section">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>任务列表</span>
            <div class="header-actions">
              <el-button type="primary" @click="handleBatchAssign" :disabled="!selectedTasks.length">
                <el-icon><User /></el-icon>
                批量分配
              </el-button>
              <el-button type="warning" @click="handleBatchSuspend" :disabled="!selectedTasks.length">
                <el-icon><VideoPause /></el-icon>
                批量暂停
              </el-button>
              <el-button @click="handleExport">
                <el-icon><Download /></el-icon>
                导出
              </el-button>
            </div>
          </div>
        </template>
        
        <el-table
          v-loading="loading"
          :data="taskList"
          @selection-change="handleSelectionChange"
          stripe
          style="width: 100%"
        >
          <el-table-column type="selection" width="55" />
          
          <el-table-column prop="taskName" label="任务名称" min-width="150">
            <template #default="{ row }">
              <el-link type="primary" @click="handleTaskDetail(row)">
                {{ row.taskName }}
              </el-link>
            </template>
          </el-table-column>
          
          <el-table-column prop="processInstanceName" label="流程实例" min-width="150" />
          
          <el-table-column prop="assignee" label="分配人" width="120">
            <template #default="{ row }">
              <span v-if="row.assignee">{{ row.assignee }}</span>
              <el-tag v-else type="info" size="small">未分配</el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="candidateUsers" label="候选人" width="150">
            <template #default="{ row }">
              <el-tag
                v-for="user in (row.candidateUsers || '').split(',')"
                :key="user"
                size="small"
                style="margin-right: 4px;"
              >
                {{ user }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="taskStatus" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.taskStatus)" size="small">
                {{ getStatusText(row.taskStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="priority" label="优先级" width="100">
            <template #default="{ row }">
              <el-tag :type="getPriorityType(row.priority)" size="small">
                {{ getPriorityText(row.priority) }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="dueDate" label="到期时间" width="160">
            <template #default="{ row }">
              <span v-if="row.dueDate" :class="{ 'overdue': isOverdue(row.dueDate) }">
                {{ formatDate(row.dueDate) }}
              </span>
              <span v-else class="text-muted">-</span>
            </template>
          </el-table-column>
          
          <el-table-column prop="createTime" label="创建时间" width="160">
            <template #default="{ row }">
              {{ formatDate(row.createTime) }}
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button
                v-if="row.taskStatus === 'pending'"
                type="primary"
                size="small"
                @click="handleTaskProcess(row)"
              >
                处理
              </el-button>
              
              <el-button
                v-if="!row.assignee"
                type="success"
                size="small"
                @click="handleTaskClaim(row)"
              >
                认领
              </el-button>
              
              <el-dropdown @command="(command) => handleTaskAction(command, row)">
                <el-button size="small">
                  更多
                  <el-icon><ArrowDown /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="assign">分配</el-dropdown-item>
                    <el-dropdown-item command="delegate">委派</el-dropdown-item>
                    <el-dropdown-item command="transfer">转办</el-dropdown-item>
                    <el-dropdown-item command="suspend" v-if="row.taskStatus === 'running'">暂停</el-dropdown-item>
                    <el-dropdown-item command="resume" v-if="row.taskStatus === 'suspended'">恢复</el-dropdown-item>
                    <el-dropdown-item command="history">历史</el-dropdown-item>
                    <el-dropdown-item command="comment">评论</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="pagination.current"
            v-model:page-size="pagination.size"
            :total="pagination.total"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
    </div>
    
    <!-- 任务处理对话框 -->
    <el-dialog
      v-model="showProcessDialog"
      :title="'处理任务 - ' + (currentTask?.taskName || '')"
      width="800px"
      @close="handleProcessDialogClose"
    >
      <div class="process-content">
        <!-- 任务信息 -->
        <div class="task-info">
          <h4>任务信息</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="任务名称">{{ currentTask?.taskName }}</el-descriptions-item>
            <el-descriptions-item label="流程实例">{{ currentTask?.processInstanceName }}</el-descriptions-item>
            <el-descriptions-item label="分配人">{{ currentTask?.assignee || '未分配' }}</el-descriptions-item>
            <el-descriptions-item label="优先级">{{ getPriorityText(currentTask?.priority) }}</el-descriptions-item>
            <el-descriptions-item label="到期时间">{{ formatDate(currentTask?.dueDate) || '-' }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ formatDate(currentTask?.createTime) }}</el-descriptions-item>
          </el-descriptions>
        </div>
        
        <!-- 表单数据 -->
        <div class="form-data" v-if="taskFormData">
          <h4>表单数据</h4>
          <el-form :model="taskFormData" label-width="120px">
            <el-form-item
              v-for="field in formFields"
              :key="field.name"
              :label="field.label"
            >
              <el-input
                v-if="field.type === 'string'"
                v-model="taskFormData[field.name]"
                :placeholder="field.placeholder"
              />
              <el-input-number
                v-else-if="field.type === 'number'"
                v-model="taskFormData[field.name]"
                :placeholder="field.placeholder"
              />
              <el-date-picker
                v-else-if="field.type === 'date'"
                v-model="taskFormData[field.name]"
                type="date"
                :placeholder="field.placeholder"
              />
              <el-select
                v-else-if="field.type === 'select'"
                v-model="taskFormData[field.name]"
                :placeholder="field.placeholder"
              >
                <el-option
                  v-for="option in field.options"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
              <el-input
                v-else-if="field.type === 'textarea'"
                v-model="taskFormData[field.name]"
                type="textarea"
                :rows="3"
                :placeholder="field.placeholder"
              />
            </el-form-item>
          </el-form>
        </div>
        
        <!-- 审批意见 -->
        <div class="approval-section">
          <h4>审批意见</h4>
          <el-form :model="approvalForm" label-width="80px">
            <el-form-item label="审批结果">
              <el-radio-group v-model="approvalForm.result">
                <el-radio label="approve">同意</el-radio>
                <el-radio label="reject">拒绝</el-radio>
                <el-radio label="return">退回</el-radio>
              </el-radio-group>
            </el-form-item>
            
            <el-form-item label="审批意见">
              <el-input
                v-model="approvalForm.comment"
                type="textarea"
                :rows="4"
                placeholder="请输入审批意见"
              />
            </el-form-item>
            
            <el-form-item label="下一步处理人" v-if="approvalForm.result === 'approve'">
              <el-select
                v-model="approvalForm.nextAssignee"
                placeholder="请选择下一步处理人"
                clearable
                filterable
              >
                <el-option
                  v-for="user in userList"
                  :key="user.id"
                  :label="user.name"
                  :value="user.id"
                />
              </el-select>
            </el-form-item>
          </el-form>
        </div>
        
        <!-- 历史记录 -->
        <div class="history-section">
          <h4>处理历史</h4>
          <el-timeline>
            <el-timeline-item
              v-for="history in taskHistory"
              :key="history.id"
              :timestamp="formatDate(history.createTime)"
              placement="top"
            >
              <div class="history-item">
                <div class="history-header">
                  <span class="history-user">{{ history.assignee }}</span>
                  <el-tag :type="getHistoryType(history.action)" size="small">
                    {{ getHistoryText(history.action) }}
                  </el-tag>
                </div>
                <div class="history-comment" v-if="history.comment">
                  {{ history.comment }}
                </div>
              </div>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showProcessDialog = false">取消</el-button>
          <el-button type="primary" @click="handleTaskSubmit" :loading="submitting">
            提交
          </el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 任务分配对话框 -->
    <el-dialog v-model="showAssignDialog" title="任务分配" width="500px">
      <el-form :model="assignForm" label-width="80px">
        <el-form-item label="分配给">
          <el-select
            v-model="assignForm.assignee"
            placeholder="请选择分配人"
            filterable
            style="width: 100%;"
          >
            <el-option
              v-for="user in userList"
              :key="user.id"
              :label="user.name"
              :value="user.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="分配原因">
          <el-input
            v-model="assignForm.reason"
            type="textarea"
            :rows="3"
            placeholder="请输入分配原因"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAssignDialog = false">取消</el-button>
          <el-button type="primary" @click="handleAssignSubmit" :loading="submitting">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 任务委派对话框 -->
    <el-dialog v-model="showDelegateDialog" title="任务委派" width="500px">
      <el-form :model="delegateForm" label-width="80px">
        <el-form-item label="委派给">
          <el-select
            v-model="delegateForm.delegatee"
            placeholder="请选择委派人"
            filterable
            style="width: 100%;"
          >
            <el-option
              v-for="user in userList"
              :key="user.id"
              :label="user.name"
              :value="user.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="委派原因">
          <el-input
            v-model="delegateForm.reason"
            type="textarea"
            :rows="3"
            placeholder="请输入委派原因"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showDelegateDialog = false">取消</el-button>
          <el-button type="primary" @click="handleDelegateSubmit" :loading="submitting">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Search,
  Refresh,
  Clock,
  VideoPlay,
  CircleCheck,
  Warning,
  User,
  VideoPause,
  Download,
  ArrowDown
} from '@element-plus/icons-vue'
import {
  searchTasks,
  getTaskDetail,
  approveTask,
  rejectTask,
  completeTask,
  delegateTask,
  claimTask,
  transferTask,
  setTaskReminder,
  batchOperateTasks,
  getTaskList,
  getTaskStats,
  processTask,
  assignTask,
  suspendTask,
  resumeTask,
  getTaskHistory,
  getTaskFormData,
  batchAssignTasks,
  batchSuspendTasks,
  getProcessDefinitionList
} from '@/api/workflow'
import { getUserList } from '@/api/workflowConfig'

// 搜索表单
const searchForm = reactive({
  taskName: '',
  taskStatus: '',
  assignee: '',
  processDefinitionKey: '',
  priority: '',
  createTimeRange: []
})

// 任务统计
const taskStats = ref({
  pending: 0,
  running: 0,
  completed: 0,
  overdue: 0
})

// 表格数据
const loading = ref(false)
const taskList = ref([])
const selectedTasks = ref([])
const processDefinitions = ref([])
const userList = ref([])

// 分页
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

// 对话框
const showProcessDialog = ref(false)
const showAssignDialog = ref(false)
const showDelegateDialog = ref(false)
const submitting = ref(false)

// 当前任务
const currentTask = ref(null)
const taskFormData = ref(null)
const formFields = ref([])
const taskHistory = ref([])

// 表单数据
const approvalForm = reactive({
  result: 'approve',
  comment: '',
  nextAssignee: ''
})

const assignForm = reactive({
  assignee: '',
  reason: ''
})

const delegateForm = reactive({
  delegatee: '',
  reason: ''
})

// 状态映射
const getStatusType = (status) => {
  const typeMap = {
    pending: 'warning',
    running: 'primary',
    completed: 'success',
    suspended: 'info',
    cancelled: 'danger'
  }
  return typeMap[status] || 'info'
}

const getStatusText = (status) => {
  const textMap = {
    pending: '待办',
    running: '进行中',
    completed: '已完成',
    suspended: '已暂停',
    cancelled: '已取消'
  }
  return textMap[status] || status
}

const getPriorityType = (priority) => {
  const typeMap = {
    low: 'info',
    medium: 'warning',
    high: 'danger',
    urgent: 'danger'
  }
  return typeMap[priority] || 'info'
}

const getPriorityText = (priority) => {
  const textMap = {
    low: '低',
    medium: '中',
    high: '高',
    urgent: '紧急'
  }
  return textMap[priority] || priority
}

const getHistoryType = (action) => {
  const typeMap = {
    create: 'info',
    assign: 'primary',
    complete: 'success',
    reject: 'danger',
    delegate: 'warning'
  }
  return typeMap[action] || 'info'
}

const getHistoryText = (action) => {
  const textMap = {
    create: '创建',
    assign: '分配',
    complete: '完成',
    reject: '拒绝',
    delegate: '委派'
  }
  return textMap[action] || action
}

// 格式化日期
const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleString('zh-CN')
}

// 检查是否超期
const isOverdue = (dueDate) => {
  if (!dueDate) return false
  return new Date(dueDate) < new Date()
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getTaskListData()
}

// 重置
const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    if (Array.isArray(searchForm[key])) {
      searchForm[key] = []
    } else {
      searchForm[key] = ''
    }
  })
  handleSearch()
}

// 获取任务列表
const getTaskListData = async () => {
  try {
    loading.value = true
    const params = {
      ...searchForm,
      current: pagination.current,
      size: pagination.size
    }
    
    if (searchForm.createTimeRange?.length === 2) {
      params.startTime = searchForm.createTimeRange[0]
      params.endTime = searchForm.createTimeRange[1]
    }
    delete params.createTimeRange
    
    const response = await getTaskList(params)
    taskList.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    ElMessage.error('获取任务列表失败')
  } finally {
    loading.value = false
  }
}

// 获取任务统计
const getTaskStatsData = async () => {
  try {
    const response = await getTaskStats()
    taskStats.value = response.data
  } catch (error) {
    ElMessage.error('获取任务统计失败')
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

// 获取用户列表
const getUserListData = async () => {
  try {
    const response = await getUserList({ size: 1000 })
    userList.value = response.data.records
  } catch (error) {
    ElMessage.error('获取用户列表失败')
  }
}

// 表格选择变化
const handleSelectionChange = (selection) => {
  selectedTasks.value = selection
}

// 分页变化
const handleSizeChange = (size) => {
  pagination.size = size
  getTaskListData()
}

const handleCurrentChange = (current) => {
  pagination.current = current
  getTaskListData()
}

// 任务详情
const handleTaskDetail = async (task) => {
  try {
    const response = await getTaskFormData(task.id)
    currentTask.value = task
    taskFormData.value = response.data.formData
    formFields.value = response.data.formFields
    
    const historyResponse = await getTaskHistory(task.id)
    taskHistory.value = historyResponse.data
    
    showProcessDialog.value = true
  } catch (error) {
    ElMessage.error('获取任务详情失败')
  }
}

// 处理任务
const handleTaskProcess = async (task) => {
  try {
    currentTask.value = task
    
    // 获取任务表单数据
    const formResponse = await getTaskFormData(task.id)
    taskFormData.value = formResponse.data.formData
    formFields.value = formResponse.data.formFields
    
    // 获取任务历史
    const historyResponse = await getTaskHistory(task.id)
    taskHistory.value = historyResponse.data
    
    // 重置表单
    approvalForm.result = 'approve'
    approvalForm.comment = ''
    approvalForm.nextAssignee = ''
    
    showProcessDialog.value = true
  } catch (error) {
    ElMessage.error('获取任务信息失败')
  }
}

// 认领任务
const handleTaskClaim = async (task) => {
  try {
    await claimTask(task.id)
    ElMessage.success('任务认领成功')
    getTaskListData()
    getTaskStatsData()
  } catch (error) {
    ElMessage.error('任务认领失败')
  }
}

// 任务操作
const handleTaskAction = async (command, task) => {
  currentTask.value = task
  
  switch (command) {
    case 'assign':
      assignForm.assignee = ''
      assignForm.reason = ''
      showAssignDialog.value = true
      break
      
    case 'delegate':
      delegateForm.delegatee = ''
      delegateForm.reason = ''
      showDelegateDialog.value = true
      break
      
    case 'transfer':
      ElMessage.info('任务转办功能开发中...')
      break
      
    case 'suspend':
      try {
        await suspendTask(task.id)
        ElMessage.success('任务暂停成功')
        getTaskListData()
        getTaskStatsData()
      } catch (error) {
        ElMessage.error('任务暂停失败')
      }
      break
      
    case 'resume':
      try {
        await resumeTask(task.id)
        ElMessage.success('任务恢复成功')
        getTaskListData()
        getTaskStatsData()
      } catch (error) {
        ElMessage.error('任务恢复失败')
      }
      break
      
    case 'history':
      ElMessage.info('任务历史功能开发中...')
      break
      
    case 'comment':
      ElMessage.info('任务评论功能开发中...')
      break
  }
}

// 提交任务处理
const handleTaskSubmit = async () => {
  try {
    submitting.value = true
    
    const params = {
      taskId: currentTask.value.id,
      result: approvalForm.result,
      comment: approvalForm.comment,
      nextAssignee: approvalForm.nextAssignee,
      formData: taskFormData.value
    }
    
    await processTask(params)
    ElMessage.success('任务处理成功')
    
    showProcessDialog.value = false
    getTaskListData()
    getTaskStatsData()
  } catch (error) {
    ElMessage.error('任务处理失败')
  } finally {
    submitting.value = false
  }
}

// 关闭处理对话框
const handleProcessDialogClose = () => {
  currentTask.value = null
  taskFormData.value = null
  formFields.value = []
  taskHistory.value = []
}

// 提交任务分配
const handleAssignSubmit = async () => {
  if (!assignForm.assignee) {
    ElMessage.warning('请选择分配人')
    return
  }
  
  try {
    submitting.value = true
    
    await assignTask({
      taskId: currentTask.value.id,
      assignee: assignForm.assignee,
      reason: assignForm.reason
    })
    
    ElMessage.success('任务分配成功')
    showAssignDialog.value = false
    getTaskListData()
  } catch (error) {
    ElMessage.error('任务分配失败')
  } finally {
    submitting.value = false
  }
}

// 提交任务委派
const handleDelegateSubmit = async () => {
  if (!delegateForm.delegatee) {
    ElMessage.warning('请选择委派人')
    return
  }
  
  try {
    submitting.value = true
    
    await delegateTask({
      taskId: currentTask.value.id,
      delegatee: delegateForm.delegatee,
      reason: delegateForm.reason
    })
    
    ElMessage.success('任务委派成功')
    showDelegateDialog.value = false
    getTaskListData()
  } catch (error) {
    ElMessage.error('任务委派失败')
  } finally {
    submitting.value = false
  }
}

// 批量分配
const handleBatchAssign = () => {
  ElMessage.info('批量分配功能开发中...')
}

// 批量暂停
const handleBatchSuspend = async () => {
  try {
    await ElMessageBox.confirm('确定要暂停选中的任务吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const taskIds = selectedTasks.value.map(task => task.id)
    await batchSuspendTasks(taskIds)
    
    ElMessage.success('批量暂停成功')
    getTaskListData()
    getTaskStatsData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('批量暂停失败')
    }
  }
}

// 导出
const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

// 初始化
onMounted(() => {
  getTaskListData()
  getTaskStatsData()
  getProcessDefinitions()
  getUserListData()
})
</script>

<style scoped>
.task-management-container {
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

.search-section {
  margin-bottom: 20px;
}

.stats-section {
  margin-bottom: 20px;
}

.stats-card {
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.stats-content {
  display: flex;
  align-items: center;
  padding: 10px 0;
}

.stats-icon {
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

.stats-icon.pending {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stats-icon.running {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stats-icon.completed {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stats-icon.overdue {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.stats-info {
  flex: 1;
}

.stats-number {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  line-height: 1;
  margin-bottom: 8px;
}

.stats-label {
  font-size: 14px;
  color: #606266;
}

.table-section {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.overdue {
  color: #f56c6c;
  font-weight: 600;
}

.text-muted {
  color: #c0c4cc;
}

.process-content {
  max-height: 600px;
  overflow-y: auto;
}

.task-info,
.form-data,
.approval-section,
.history-section {
  margin-bottom: 30px;
}

.task-info h4,
.form-data h4,
.approval-section h4,
.history-section h4 {
  margin: 0 0 15px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
  border-bottom: 1px solid #e4e7ed;
  padding-bottom: 8px;
}

.history-item {
  background: #f8f9fa;
  padding: 12px;
  border-radius: 4px;
  border-left: 3px solid #409EFF;
}

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.history-user {
  font-weight: 600;
  color: #303133;
}

.history-comment {
  color: #606266;
  font-size: 14px;
  line-height: 1.5;
}

.dialog-footer {
  text-align: right;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-card {
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>