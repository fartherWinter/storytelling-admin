<template>
  <div class="admin-workflow-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>管理员创建流程</span>
        </div>
      </template>
      
      <!-- 流程部署区域 -->
      <el-collapse v-model="activeNames">
        <el-collapse-item title="流程部署" name="1">
          <el-form :model="deployForm" label-width="120px">
            <el-form-item label="流程名称">
              <el-input v-model="deployForm.name" placeholder="请输入流程名称"></el-input>
            </el-form-item>
            <el-form-item label="流程分类">
              <el-input v-model="deployForm.category" placeholder="请输入流程分类"></el-input>
            </el-form-item>
            <el-form-item label="资源名称">
              <el-input v-model="deployForm.resourceName" placeholder="请输入资源名称"></el-input>
            </el-form-item>
            <el-form-item label="流程定义文件">
              <el-upload
                class="upload-demo"
                action="#"
                :http-request="handleFileUpload"
                :limit="1"
                :on-exceed="handleExceed"
                :file-list="fileList">
                <el-button type="primary">选择文件</el-button>
                <template #tip>
                  <div class="el-upload__tip">
                    请上传BPMN流程定义文件
                  </div>
                </template>
              </el-upload>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="deployProcess">部署流程</el-button>
            </el-form-item>
          </el-form>
        </el-collapse-item>
        
        <!-- 启动流程区域 -->
        <el-collapse-item title="启动管理员创建流程" name="2">
          <el-form :model="adminForm" label-width="120px">
            <el-form-item label="用户名">
              <el-input v-model="adminForm.userName" placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item label="密码">
              <el-input v-model="adminForm.password" type="password" placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item label="昵称">
              <el-input v-model="adminForm.nickName" placeholder="请输入昵称"></el-input>
            </el-form-item>
            <el-form-item label="部门">
              <el-select v-model="adminForm.deptId" placeholder="请选择部门">
                <el-option
                  v-for="item in deptOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="adminForm.email" placeholder="请输入邮箱"></el-input>
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="adminForm.phoneNumber" placeholder="请输入手机号"></el-input>
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="adminForm.sex">
                <el-radio :label="'0'">男</el-radio>
                <el-radio :label="'1'">女</el-radio>
                <el-radio :label="'2'">未知</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="角色">
              <el-select v-model="adminForm.roleIds" multiple placeholder="请选择角色">
                <el-option
                  v-for="item in roleOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="岗位">
              <el-select v-model="adminForm.postIds" multiple placeholder="请选择岗位">
                <el-option
                  v-for="item in postOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="adminForm.remark" type="textarea" placeholder="请输入备注"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="startProcess">启动流程</el-button>
            </el-form-item>
          </el-form>
        </el-collapse-item>
        
        <!-- 我的待办任务 -->
        <el-collapse-item title="我的待办任务" name="3">
          <el-table :data="todoTasks" style="width: 100%" v-loading="taskTableLoading">
            <el-table-column prop="id" label="任务ID" width="180"></el-table-column>
            <el-table-column prop="name" label="任务名称"></el-table-column>
            <el-table-column prop="createTime" label="创建时间"></el-table-column>
            <el-table-column prop="processInstanceId" label="流程实例ID" width="180"></el-table-column>
            <el-table-column label="操作" width="280">
              <template #default="scope">
                <el-button size="small" type="success" @click="approveTask(scope.row)">审批通过</el-button>
                <el-button size="small" type="danger" @click="rejectTask(scope.row)">拒绝</el-button>
                <el-button size="small" type="primary" @click="viewProcessDiagram(scope.row.processInstanceId)">查看流程图</el-button>
                <el-button size="small" type="info" @click="viewTaskDetail(scope.row)">查看详情</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pagination-container">
            <el-pagination
              v-model:current-page="taskCurrentPage"
              v-model:page-size="taskPageSize"
              :page-sizes="[10, 20, 50, 100]"
              layout="total, sizes, prev, pager, next, jumper"
              :total="taskTotal"
              @size-change="handleTaskSizeChange"
              @current-change="handleTaskCurrentChange"
            />
          </div>
        </el-collapse-item>
        
        <!-- 流程实例列表 -->
        <el-collapse-item title="流程实例列表" name="4">
          <el-table :data="processInstances" style="width: 100%" v-loading="tableLoading">
            <el-table-column prop="id" label="流程实例ID" width="180"></el-table-column>
            <el-table-column prop="businessKey" label="业务键"></el-table-column>
            <el-table-column prop="startTime" label="开始时间"></el-table-column>
            <el-table-column prop="status" label="状态">
              <template #default="scope">
                <el-tag :type="scope.row.status === '进行中' ? 'primary' : (scope.row.status === '已完成' ? 'success' : 'info')">{{ scope.row.status }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button size="small" type="primary" @click="viewProcessDiagram(scope.row.id)">查看流程图</el-button>
                <el-button size="small" type="danger" @click="terminateProcess(scope.row)" :disabled="scope.row.status !== '进行中'">终止流程</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pagination-container">
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :page-sizes="[10, 20, 50, 100]"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </el-collapse-item>
      </el-collapse>
    </el-card>
    
    <!-- 流程图对话框 -->
    <el-dialog v-model="dialogVisible" title="流程图" width="70%" destroy-on-close>
      <div class="process-diagram">
        <img :src="processDiagramUrl" alt="流程图" style="width: 100%">
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="downloadProcessDiagram">下载流程图</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 审批对话框 -->
    <el-dialog v-model="approveDialogVisible" :title="approveDialogTitle" width="30%">
      <el-form :model="approveForm">
        <el-form-item label="审批意见" label-width="100px">
          <el-input v-model="approveForm.comment" type="textarea" :rows="3"></el-input>
        </el-form-item>
        <el-form-item label="手写签名" label-width="100px">
          <vue-signature-pad
            ref="signaturePadRef"
            :options="signaturePadOptions"
            style="border:1px solid #ccc;width:100%;height:150px;"
          />
          <el-button size="small" @click="clearSignature">清除签名</el-button>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="approveDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitApproval">确定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 终止流程对话框 -->
    <el-dialog v-model="terminateDialogVisible" title="终止流程" width="30%">
      <el-form :model="terminateForm">
        <el-form-item label="终止原因" label-width="100px">
          <el-input v-model="terminateForm.reason" type="textarea" :rows="3"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="terminateDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitTerminate">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, ElLoading } from 'element-plus'
import axios from 'axios'
import VueSignaturePad from 'vue-signature-pad'

// 折叠面板激活项
const activeNames = ref(['1', '3'])

// 流程部署表单
const deployForm = reactive({
  name: '',
  category: '',
  resourceName: '',
  deploymentFile: null
})

// 文件上传相关
const fileList = ref([])
const handleFileUpload = (params) => {
  const file = params.file
  // 将文件转为base64
  const reader = new FileReader()
  reader.readAsDataURL(file)
  reader.onload = () => {
    const base64 = reader.result.split(',')[1]
    deployForm.deploymentFile = base64
  }
}

const handleExceed = () => {
  ElMessage.warning('只能上传一个文件')
}

// 管理员表单
const adminForm = reactive({
  userName: '',
  password: '',
  nickName: '',
  deptId: null,
  email: '',
  phoneNumber: '',
  sex: '0',
  roleIds: [],
  postIds: [],
  remark: ''
})

// 部门、角色、岗位选项
const deptOptions = ref([])
const roleOptions = ref([])
const postOptions = ref([])

// 待办任务列表
const todoTasks = ref([])

// 任务分页相关
const taskCurrentPage = ref(1)
const taskPageSize = ref(10)
const taskTotal = ref(0)
const taskTableLoading = ref(false)

// 流程实例列表
const processInstances = ref([])

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 流程图对话框
const dialogVisible = ref(false)
const processDiagramUrl = ref('')

// 审批对话框
const approveDialogVisible = ref(false)
const approveDialogTitle = ref('审批')
const approveForm = reactive({
  taskId: '',
  comment: '',
  type: 'approve' // approve或reject
})

// 终止流程对话框
const terminateDialogVisible = ref(false)
const terminateForm = reactive({
  processInstanceId: '',
  reason: ''
})

// 当前选中的任务
const currentTask = ref(null)

// 当前选中的流程实例
const currentProcessInstance = ref(null)

// 部署流程
const deployProcess = async () => {
  // 表单验证
  if (!deployForm.name) {
    ElMessage.warning('请输入流程名称')
    return
  }
  if (!deployForm.deploymentFile) {
    ElMessage.warning('请上传流程定义文件')
    return
  }
  
  try {
    const loading = ElLoading.service({
      lock: true,
      text: '正在部署流程...',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    
    const response = await axios.post('/admin/workflow/deploy/admin', deployForm)
    
    loading.close()
    
    if (response.data.code === 200) {
      ElMessage.success('流程部署成功')
      // 清空表单
      deployForm.name = ''
      deployForm.category = ''
      deployForm.resourceName = ''
      deployForm.deploymentFile = null
      fileList.value = []
      
      // 切换到启动流程标签页
      activeNames.value = ['2']
    } else {
      ElMessage.error(response.data.msg || '流程部署失败')
    }
  } catch (error) {
    console.error('部署流程出错:', error)
    ElMessage.error(error.response?.data?.msg || '部署流程出错，请检查网络连接')
  }
}

// 启动流程
const startProcess = async () => {
  // 表单验证
  if (!adminForm.userName) {
    ElMessage.warning('请输入用户名')
    return
  }
  if (!adminForm.password) {
    ElMessage.warning('请输入密码')
    return
  }
  if (!adminForm.nickName) {
    ElMessage.warning('请输入昵称')
    return
  }
  if (!adminForm.deptId) {
    ElMessage.warning('请选择部门')
    return
  }
  
  try {
    const loading = ElLoading.service({
      lock: true,
      text: '正在启动流程...',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    
    const response = await axios.post('/admin/workflow/start/admin', adminForm)
    
    loading.close()
    
    if (response.data.code === 200) {
      ElMessage.success('流程启动成功')
      // 清空表单
      Object.keys(adminForm).forEach(key => {
        if (Array.isArray(adminForm[key])) {
          adminForm[key] = []
        } else if (typeof adminForm[key] === 'string') {
          adminForm[key] = ''
        } else if (typeof adminForm[key] === 'number') {
          adminForm[key] = null
        }
      })
      adminForm.sex = '0'
      
      // 刷新流程实例列表和待办任务
      fetchProcessInstances()
      fetchTodoTasks()
      
      // 切换到待办任务标签页
      activeNames.value = ['3']
    } else {
      ElMessage.error(response.data.msg || '流程启动失败')
    }
  } catch (error) {
    console.error('启动流程出错:', error)
    ElMessage.error(error.response?.data?.msg || '启动流程出错，请检查网络连接')
  }
}

// 获取待办任务
const fetchTodoTasks = async () => {
  try {
    taskTableLoading.value = true
    
    const response = await axios.post('/admin/workflow/tasks/todo', {
      pageNum: taskCurrentPage.value,
      pageSize: taskPageSize.value
    })
    
    if (response.data.code === 200) {
      todoTasks.value = response.data.data.list || []
      taskTotal.value = response.data.data.total || 0
    } else {
      ElMessage.error(response.data.msg || '获取待办任务失败')
    }
  } catch (error) {
    console.error('获取待办任务出错:', error)
    ElMessage.error('获取待办任务出错')
  } finally {
    taskTableLoading.value = false
  }
}

// 获取流程实例列表
const fetchProcessInstances = async () => {
  try {
    const loading = ElLoading.service({
      lock: true,
      text: '加载流程实例中...',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    
    const response = await axios.post('/admin/workflow/instances', {
      pageNum: currentPage.value,
      pageSize: pageSize.value
    })
    
    loading.close()
    
    if (response.data.code === 200) {
      processInstances.value = response.data.data.list || []
      total.value = response.data.data.total || 0
    } else {
      ElMessage.error(response.data.msg || '获取流程实例列表失败')
    }
  } catch (error) {
    console.error('获取流程实例列表出错:', error)
    ElMessage.error('获取流程实例列表出错')
  }
}

// 查看流程图
const viewProcessDiagram = async (processInstanceId) => {
  try {
    const loading = ElLoading.service({
      lock: true,
      text: '正在加载流程图...',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    
    // 设置图片URL，调用后端接口获取流程图
    processDiagramUrl.value = `/admin/workflow/diagram/${processInstanceId}`
    
    // 预加载图片
    const img = new Image()
    img.src = processDiagramUrl.value
    img.onload = () => {
      loading.close()
      dialogVisible.value = true
    }
    img.onerror = () => {
      loading.close()
      ElMessage.error('流程图加载失败')
    }
    
    // 如果图片加载时间过长，5秒后自动关闭loading
    setTimeout(() => {
      loading.close()
    }, 5000)
  } catch (error) {
    console.error('获取流程图出错:', error)
    ElMessage.error('获取流程图出错，请稍后再试')
  }
}

// 审批任务
const approveTask = (task) => {
  currentTask.value = task
  approveDialogTitle.value = '审批通过'
  approveForm.taskId = task.id
  approveForm.comment = ''
  approveForm.type = 'approve'
  approveDialogVisible.value = true
}

// 拒绝任务
const rejectTask = (task) => {
  currentTask.value = task
  approveDialogTitle.value = '拒绝任务'
  approveForm.taskId = task.id
  approveForm.comment = ''
  approveForm.type = 'reject'
  approveDialogVisible.value = true
}

// 提交审批
const submitApproval = async () => {
  try {
    // 验证审批意见
    if (!approveForm.comment) {
      ElMessage.warning('请输入审批意见')
      return
    }
    
    const loading = ElLoading.service({
      lock: true,
      text: '正在提交审批...',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    
    let url = ''
    if (approveForm.type === 'approve') {
      url = `/admin/workflow/task/approve/${approveForm.taskId}`
    } else {
      url = `/admin/workflow/task/reject/${approveForm.taskId}`
    }
    
    const response = await axios.post(url, null, {
      params: {
        comment: approveForm.comment
      }
    })
    
    loading.close()
    
    if (response.data.code === 200) {
      ElMessage.success(approveForm.type === 'approve' ? '审批通过成功' : '拒绝任务成功')
      approveDialogVisible.value = false
      // 刷新待办任务列表
      fetchTodoTasks()
      // 刷新流程实例列表
      fetchProcessInstances()
    } else {
      ElMessage.error(response.data.msg || '操作失败')
    }
  } catch (error) {
    console.error('审批出错:', error)
    ElMessage.error(error.response?.data?.msg || '审批出错，请检查网络连接')
  }
}

// 终止流程
const terminateProcess = (processInstance) => {
  currentProcessInstance.value = processInstance
  terminateForm.processInstanceId = processInstance.id
  terminateForm.reason = ''
  terminateDialogVisible.value = true
}

// 提交终止
const submitTerminate = async () => {
  // 验证终止原因
  if (!terminateForm.reason) {
    ElMessage.warning('请输入终止原因')
    return
  }
  
  try {
    const loading = ElLoading.service({
      lock: true,
      text: '正在终止流程...',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    
    const response = await axios.post(`/admin/workflow/terminate/${terminateForm.processInstanceId}`, null, {
      params: {
        reason: terminateForm.reason
      }
    })
    
    loading.close()
    
    if (response.data.code === 200) {
      ElMessage.success('终止流程成功')
      terminateDialogVisible.value = false
      // 刷新流程实例列表和待办任务
      fetchProcessInstances()
      fetchTodoTasks()
    } else {
      ElMessage.error(response.data.msg || '终止流程失败')
    }
  } catch (error) {
    console.error('终止流程出错:', error)
    ElMessage.error(error.response?.data?.msg || '终止流程出错，请检查网络连接')
  }
}

// 获取部门列表
const fetchDeptOptions = async () => {
  try {
    const response = await axios.get('/admin/system/dept/list')
    if (response.data.code === 200) {
      deptOptions.value = response.data.data.map(item => ({
        value: item.deptId,
        label: item.deptName
      })) || []
    } else {
      // 如果接口调用失败，使用默认数据
      deptOptions.value = [
        { value: 1, label: '总公司' },
        { value: 2, label: '研发部' },
        { value: 3, label: '市场部' },
        { value: 4, label: '财务部' }
      ]
    }
  } catch (error) {
    console.error('获取部门列表出错:', error)
    // 使用默认数据
    deptOptions.value = [
      { value: 1, label: '总公司' },
      { value: 2, label: '研发部' },
      { value: 3, label: '市场部' },
      { value: 4, label: '财务部' }
    ]
  }
}

// 获取角色列表
const fetchRoleOptions = async () => {
  try {
    const response = await axios.get('/admin/system/role/list')
    if (response.data.code === 200) {
      roleOptions.value = response.data.data.map(item => ({
        value: item.roleId,
        label: item.roleName
      })) || []
    } else {
      // 如果接口调用失败，使用默认数据
      roleOptions.value = [
        { value: 1, label: '超级管理员' },
        { value: 2, label: '普通管理员' },
        { value: 3, label: '系统管理员' },
        { value: 4, label: '财务管理员' }
      ]
    }
  } catch (error) {
    console.error('获取角色列表出错:', error)
    // 使用默认数据
    roleOptions.value = [
      { value: 1, label: '超级管理员' },
      { value: 2, label: '普通管理员' },
      { value: 3, label: '系统管理员' },
      { value: 4, label: '财务管理员' }
    ]
  }
}

// 获取岗位列表
const fetchPostOptions = async () => {
  try {
    const response = await axios.get('/admin/system/post/list')
    if (response.data.code === 200) {
      postOptions.value = response.data.data.map(item => ({
        value: item.postId,
        label: item.postName
      })) || []
    } else {
      // 如果接口调用失败，使用默认数据
      postOptions.value = [
        { value: 1, label: '董事长' },
        { value: 2, label: '项目经理' },
        { value: 3, label: '开发工程师' },
        { value: 4, label: '测试工程师' }
      ]
    }
  } catch (error) {
    console.error('获取岗位列表出错:', error)
    // 使用默认数据
    postOptions.value = [
      { value: 1, label: '董事长' },
      { value: 2, label: '项目经理' },
      { value: 3, label: '开发工程师' },
      { value: 4, label: '测试工程师' }
    ]
  }
}

// 表格加载状态
const tableLoading = ref(false)

// 流程实例分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchProcessInstances()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchProcessInstances()
}

// 任务分页处理
const handleTaskSizeChange = (val) => {
  taskPageSize.value = val
  fetchTodoTasks()
}

const handleTaskCurrentChange = (val) => {
  taskCurrentPage.value = val
  fetchTodoTasks()
}

// 查看任务详情
const viewTaskDetail = (task) => {
  ElMessageBox.alert(
    `<div>
      <p><strong>任务ID:</strong> ${task.id}</p>
      <p><strong>任务名称:</strong> ${task.name}</p>
      <p><strong>创建时间:</strong> ${task.createTime}</p>
      <p><strong>流程实例ID:</strong> ${task.processInstanceId}</p>
      <p><strong>任务描述:</strong> ${task.description || '无'}</p>
      <p><strong>表单数据:</strong> ${task.formData || '无'}</p>
    </div>`,
    '任务详情',
    {
      dangerouslyUseHTMLString: true,
      confirmButtonText: '关闭'
    }
  )
}

// 下载流程图
const downloadProcessDiagram = () => {
  if (!processDiagramUrl.value) {
    ElMessage.warning('流程图不存在')
    return
  }
  
  try {
    // 创建一个a标签用于下载
    const link = document.createElement('a')
    link.href = processDiagramUrl.value
    link.download = `流程图_${new Date().getTime()}.png`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    
    ElMessage.success('流程图下载成功')
  } catch (error) {
    console.error('下载流程图出错:', error)
    ElMessage.error('下载流程图失败')
  }
}

// 页面加载时执行
onMounted(() => {
  tableLoading.value = true
  Promise.all([
    fetchTodoTasks(),
    fetchProcessInstances(),
    fetchDeptOptions(),
    fetchRoleOptions(),
    fetchPostOptions()
  ]).finally(() => {
    tableLoading.value = false
  })
})
</script>

<style scoped>
.admin-workflow-container {
  padding: 20px;
}

.box-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.process-diagram {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
  max-height: 70vh;
  overflow: auto;
}

.el-form-item {
  margin-bottom: 18px;
}

.pagination-container {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}
</style>