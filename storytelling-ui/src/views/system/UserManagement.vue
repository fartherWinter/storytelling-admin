<template>
  <div class="user-management">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>用户管理</h2>
      <div class="header-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增用户
        </el-button>
        <el-button type="success" @click="handleBatchImport">
          <el-icon><Upload /></el-icon>
          批量导入
        </el-button>
        <el-button type="info" @click="handleExport">
          <el-icon><Download /></el-icon>
          导出数据
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon total">
                <el-icon><User /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-value">{{ stats.total }}</div>
                <div class="stats-label">总用户数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon active">
                <el-icon><CircleCheck /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-value">{{ stats.active }}</div>
                <div class="stats-label">活跃用户</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon online">
                <el-icon><Connection /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-value">{{ stats.online }}</div>
                <div class="stats-label">在线用户</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon disabled">
                <el-icon><CircleClose /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-value">{{ stats.disabled }}</div>
                <div class="stats-label">已禁用</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <div class="search-area">
      <el-form :model="searchForm" inline>
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="searchForm.realName" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item label="部门">
          <el-tree-select
            v-model="searchForm.deptId"
            :data="deptTree"
            :props="{ label: 'name', value: 'id' }"
            placeholder="请选择部门"
            clearable
            check-strictly
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="正常" value="1" />
            <el-option label="禁用" value="0" />
          </el-select>
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
    </div>

    <!-- 批量操作 -->
    <div class="batch-actions" v-if="selectedRows.length > 0">
      <el-alert
        :title="`已选择 ${selectedRows.length} 项`"
        type="info"
        show-icon
        :closable="false"
      >
        <template #default>
          <div class="batch-buttons">
            <el-button size="small" @click="handleBatchEnable">批量启用</el-button>
            <el-button size="small" @click="handleBatchDisable">批量禁用</el-button>
            <el-button size="small" type="danger" @click="handleBatchDelete">批量删除</el-button>
          </div>
        </template>
      </el-alert>
    </div>

    <!-- 数据表格 -->
    <div class="table-area">
      <el-table 
        :data="tableData" 
        v-loading="loading"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="userId" label="用户ID" width="80" />
        <el-table-column prop="avatar" label="头像" width="80">
          <template #default="{ row }">
            <el-avatar :src="row.avatar" :size="40">
              <el-icon><User /></el-icon>
            </el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="姓名" width="100" />
        <el-table-column prop="deptName" label="部门" width="120" />
        <el-table-column prop="email" label="邮箱" width="150" show-overflow-tooltip />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="roles" label="角色" width="150">
          <template #default="{ row }">
            <el-tag 
              v-for="role in row.roles" 
              :key="role.roleId" 
              size="small" 
              style="margin-right: 5px;"
            >
              {{ role.roleName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === '1' ? 'success' : 'danger'">
              {{ row.status === '1' ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="loginDate" label="最后登录" width="150" />
        <el-table-column prop="createTime" label="创建时间" width="150" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleView(row)">
              详情
            </el-button>
            <el-button type="warning" size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-dropdown trigger="click">
              <el-button size="small">
                更多<el-icon><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleResetPassword(row)">
                    重置密码
                  </el-dropdown-item>
                  <el-dropdown-item @click="handleToggleStatus(row)">
                    {{ row.status === '1' ? '禁用' : '启用' }}
                  </el-dropdown-item>
                  <el-dropdown-item @click="handleAssignRole(row)">
                    分配角色
                  </el-dropdown-item>
                  <el-dropdown-item divided @click="handleDelete(row)">
                    <span style="color: #f56c6c">删除</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-area">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 用户表单对话框 -->
    <el-dialog
      v-model="formDialogVisible"
      :title="formMode === 'add' ? '新增用户' : '编辑用户'"
      width="600px"
      @close="handleFormClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="80px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="formData.username" placeholder="请输入用户名" :disabled="formMode === 'edit'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="realName">
              <el-input v-model="formData.realName" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="formMode === 'add'">
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input v-model="formData.password" type="password" placeholder="请输入密码" show-password />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="formData.confirmPassword" type="password" placeholder="请确认密码" show-password />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="formData.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="formData.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门" prop="deptId">
              <el-tree-select
                v-model="formData.deptId"
                :data="deptTree"
                :props="{ label: 'name', value: 'id' }"
                placeholder="请选择部门"
                check-strictly
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="岗位" prop="postIds">
              <el-select v-model="formData.postIds" multiple placeholder="请选择岗位">
                <el-option 
                  v-for="post in postList" 
                  :key="post.postId" 
                  :label="post.postName" 
                  :value="post.postId" 
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别">
              <el-radio-group v-model="formData.sex">
                <el-radio label="0">男</el-radio>
                <el-radio label="1">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="formData.status">
                <el-radio label="1">正常</el-radio>
                <el-radio label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="formDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleFormSubmit" :loading="formLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 用户详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="用户详情"
      width="600px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户ID">{{ detailData.userId }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ detailData.username }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ detailData.realName }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ detailData.sex === '0' ? '男' : '女' }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ detailData.email }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detailData.phone }}</el-descriptions-item>
        <el-descriptions-item label="部门">{{ detailData.deptName }}</el-descriptions-item>
        <el-descriptions-item label="岗位">
          <el-tag v-for="post in detailData.posts" :key="post.postId" size="small" style="margin-right: 5px;">
            {{ post.postName }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="角色">
          <el-tag v-for="role in detailData.roles" :key="role.roleId" size="small" style="margin-right: 5px;">
            {{ role.roleName }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="detailData.status === '1' ? 'success' : 'danger'">
            {{ detailData.status === '1' ? '正常' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="最后登录IP">{{ detailData.loginIp }}</el-descriptions-item>
        <el-descriptions-item label="最后登录时间">{{ detailData.loginDate }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 分配角色对话框 -->
    <el-dialog
      v-model="roleDialogVisible"
      title="分配角色"
      width="500px"
    >
      <el-form label-width="80px">
        <el-form-item label="用户名">
          <el-input :value="currentUser.username" disabled />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input :value="currentUser.realName" disabled />
        </el-form-item>
        <el-form-item label="角色">
          <el-checkbox-group v-model="selectedRoles">
            <el-checkbox 
              v-for="role in roleList" 
              :key="role.roleId" 
              :label="role.roleId"
            >
              {{ role.roleName }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="roleDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleRoleSubmit">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Search, Refresh, Plus, Upload, Download, ArrowDown,
  User, CircleCheck, CircleClose, Connection
} from '@element-plus/icons-vue'
import {
  getUserList,
  getUserById,
  createUser,
  updateUser,
  deleteUser,
  updateUserStatus,
  resetUserPassword,
  assignUserRoles,
  getDeptTree,
  getPostList,
  getRoleList
} from '@/api/system'

// 响应式数据
const loading = ref(false)
const formLoading = ref(false)
const tableData = ref([])
const selectedRows = ref([])
const formDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const roleDialogVisible = ref(false)
const formMode = ref('add')
const formRef = ref()
const deptTree = ref([])
const postList = ref([])
const roleList = ref([])
const selectedRoles = ref([])
const currentUser = ref({})

// 统计数据
const stats = reactive({
  total: 0,
  active: 0,
  online: 0,
  disabled: 0
})

// 搜索表单
const searchForm = reactive({
  username: '',
  realName: '',
  deptId: '',
  status: ''
})

// 分页
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 表单数据
const formData = reactive({
  userId: null,
  username: '',
  realName: '',
  password: '',
  confirmPassword: '',
  email: '',
  phone: '',
  deptId: '',
  postIds: [],
  sex: '0',
  status: '1',
  remark: ''
})

// 详情数据
const detailData = reactive({})

// 表单验证规则
const formRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== formData.password) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ],
  deptId: [{ required: true, message: '请选择部门', trigger: 'change' }]
}

// 获取用户列表
const getList = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      size: pagination.size,
      ...searchForm
    }
    const response = await getUserList(params)
    tableData.value = response.data.records
    pagination.total = response.data.total
    
    // 更新统计数据
    updateStats(response.data.records)
  } catch (error) {
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

// 获取部门树
const getDeptTreeData = async () => {
  try {
    const response = await getDeptTree()
    deptTree.value = response.data
  } catch (error) {
    ElMessage.error('获取部门树失败')
  }
}

// 获取岗位列表
const getPostListData = async () => {
  try {
    const response = await getPostList()
    postList.value = response.data
  } catch (error) {
    ElMessage.error('获取岗位列表失败')
  }
}

// 获取角色列表
const getRoleListData = async () => {
  try {
    const response = await getRoleList()
    roleList.value = response.data
  } catch (error) {
    ElMessage.error('获取角色列表失败')
  }
}

// 更新统计数据
const updateStats = (data) => {
  stats.total = data.length
  stats.active = data.filter(item => item.status === '1').length
  stats.online = Math.floor(stats.active * 0.3) // 模拟在线用户数
  stats.disabled = data.filter(item => item.status === '0').length
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  getList()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    username: '',
    realName: '',
    deptId: '',
    status: ''
  })
  pagination.page = 1
  getList()
}

// 新增
const handleAdd = () => {
  formMode.value = 'add'
  resetFormData()
  formDialogVisible.value = true
}

// 编辑
const handleEdit = async (row) => {
  formMode.value = 'edit'
  try {
    const response = await getUserById(row.userId)
    Object.assign(formData, response.data)
    formDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取用户信息失败')
  }
}

// 查看详情
const handleView = async (row) => {
  try {
    const response = await getUserById(row.userId)
    Object.assign(detailData, response.data)
    detailDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取用户详情失败')
  }
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除用户 "${row.username}" 吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await deleteUser(row.userId)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

// 切换状态
const handleToggleStatus = async (row) => {
  const newStatus = row.status === '1' ? '0' : '1'
  const action = newStatus === '1' ? '启用' : '禁用'
  
  try {
    await updateUserStatus(row.userId, newStatus)
    ElMessage.success(`${action}成功`)
    getList()
  } catch (error) {
    ElMessage.error(`${action}失败`)
  }
}

// 重置密码
const handleResetPassword = (row) => {
  ElMessageBox.confirm(
    `确定要重置用户 "${row.username}" 的密码吗？`,
    '确认重置',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await resetUserPassword(row.userId)
      ElMessage.success('密码重置成功，新密码为：123456')
    } catch (error) {
      ElMessage.error('密码重置失败')
    }
  })
}

// 分配角色
const handleAssignRole = async (row) => {
  currentUser.value = row
  selectedRoles.value = row.roles ? row.roles.map(role => role.roleId) : []
  roleDialogVisible.value = true
}

// 批量导入
const handleBatchImport = () => {
  ElMessage.info('批量导入功能开发中')
}

// 导出数据
const handleExport = () => {
  ElMessage.info('导出功能开发中')
}

// 表格选择变化
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

// 批量启用
const handleBatchEnable = async () => {
  const userIds = selectedRows.value.map(row => row.userId)
  try {
    // 这里应该调用批量更新状态的API
    ElMessage.success('批量启用成功')
    getList()
  } catch (error) {
    ElMessage.error('批量启用失败')
  }
}

// 批量禁用
const handleBatchDisable = async () => {
  const userIds = selectedRows.value.map(row => row.userId)
  try {
    // 这里应该调用批量更新状态的API
    ElMessage.success('批量禁用成功')
    getList()
  } catch (error) {
    ElMessage.error('批量禁用失败')
  }
}

// 批量删除
const handleBatchDelete = () => {
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedRows.value.length} 个用户吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const userIds = selectedRows.value.map(row => row.userId)
      // 这里应该调用批量删除的API
      ElMessage.success('批量删除成功')
      getList()
    } catch (error) {
      ElMessage.error('批量删除失败')
    }
  })
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.page = 1
  getList()
}

// 当前页变化
const handleCurrentChange = (page) => {
  pagination.page = page
  getList()
}

// 表单关闭
const handleFormClose = () => {
  formRef.value?.resetFields()
  resetFormData()
}

// 表单提交
const handleFormSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      formLoading.value = true
      try {
        if (formMode.value === 'add') {
          await createUser(formData)
          ElMessage.success('新增成功')
        } else {
          await updateUser(formData.userId, formData)
          ElMessage.success('更新成功')
        }
        formDialogVisible.value = false
        getList()
      } catch (error) {
        ElMessage.error(formMode.value === 'add' ? '新增失败' : '更新失败')
      } finally {
        formLoading.value = false
      }
    }
  })
}

// 角色分配提交
const handleRoleSubmit = async () => {
  try {
    await assignUserRoles(currentUser.value.userId, selectedRoles.value)
    ElMessage.success('角色分配成功')
    roleDialogVisible.value = false
    getList()
  } catch (error) {
    ElMessage.error('角色分配失败')
  }
}

// 重置表单数据
const resetFormData = () => {
  Object.assign(formData, {
    userId: null,
    username: '',
    realName: '',
    password: '',
    confirmPassword: '',
    email: '',
    phone: '',
    deptId: '',
    postIds: [],
    sex: '0',
    status: '1',
    remark: ''
  })
}

// 页面加载时获取数据
onMounted(() => {
  getList()
  getDeptTreeData()
  getPostListData()
  getRoleListData()
})
</script>

<style scoped>
.user-management {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.stats-cards {
  margin-bottom: 20px;
}

.stats-card {
  border-radius: 8px;
  overflow: hidden;
}

.stats-content {
  display: flex;
  align-items: center;
  padding: 10px;
}

.stats-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.stats-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stats-icon.active {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stats-icon.online {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stats-icon.disabled {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stats-info {
  flex: 1;
}

.stats-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.stats-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.search-area {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.batch-actions {
  margin-bottom: 20px;
}

.batch-buttons {
  margin-top: 10px;
}

.table-area {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.pagination-area {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-table) {
  font-size: 14px;
}

:deep(.el-table th) {
  background-color: #fafafa;
}

:deep(.el-card__body) {
  padding: 15px;
}
</style>