<template>
  <div class="user-management-container">
    <div class="page-header">
      <h2>用户管理</h2>
      <p>管理系统用户账户和权限</p>
    </div>
    
    <div class="content-area">
      <!-- 用户统计卡片 -->
      <div class="overview-cards">
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon size="24" color="#409EFF"><User /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-title">总用户数</div>
              <div class="card-value">{{ totalUsers }}</div>
            </div>
          </div>
        </el-card>
        
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon size="24" color="#67C23A"><CircleCheck /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-title">活跃用户</div>
              <div class="card-value">{{ activeUsers }}</div>
            </div>
          </div>
        </el-card>
        
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon size="24" color="#E6A23C"><Clock /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-title">待审核</div>
              <div class="card-value">{{ pendingUsers }}</div>
            </div>
          </div>
        </el-card>
        
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon size="24" color="#F56C6C"><Lock /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-title">已禁用</div>
              <div class="card-value">{{ disabledUsers }}</div>
            </div>
          </div>
        </el-card>
      </div>
      
      <!-- 用户列表 -->
      <el-card>
        <template #header>
          <div class="card-header">
            <span>用户列表</span>
            <div class="header-actions">
              <el-select v-model="statusFilter" placeholder="筛选状态" style="width: 120px; margin-right: 10px;" clearable>
                <el-option label="全部" value="" />
                <el-option label="活跃" value="active" />
                <el-option label="待审核" value="pending" />
                <el-option label="已禁用" value="disabled" />
              </el-select>
              <el-select v-model="roleFilter" placeholder="筛选角色" style="width: 120px; margin-right: 10px;" clearable>
                <el-option label="全部" value="" />
                <el-option label="管理员" value="admin" />
                <el-option label="普通用户" value="user" />
                <el-option label="访客" value="guest" />
              </el-select>
              <el-input
                v-model="searchText"
                placeholder="搜索用户名或邮箱"
                style="width: 200px; margin-right: 10px;"
                clearable
              >
                <template #prefix>
                  <el-icon><Search /></el-icon>
                </template>
              </el-input>
              <el-button type="primary" @click="addUser">
                <el-icon><Plus /></el-icon>
                添加用户
              </el-button>
            </div>
          </div>
        </template>
        
        <el-table :data="filteredUsers" style="width: 100%">
          <el-table-column type="selection" width="55" />
          <el-table-column label="头像" width="80" align="center">
            <template #default="scope">
              <el-avatar :size="40" :src="scope.row.avatar">
                {{ scope.row.username.charAt(0).toUpperCase() }}
              </el-avatar>
            </template>
          </el-table-column>
          <el-table-column prop="username" label="用户名" width="120" />
          <el-table-column prop="realName" label="真实姓名" width="120" />
          <el-table-column prop="email" label="邮箱" width="200" />
          <el-table-column prop="phone" label="手机号" width="130" />
          <el-table-column label="角色" width="100" align="center">
            <template #default="scope">
              <el-tag :type="getRoleType(scope.row.role)">
                {{ getRoleText(scope.row.role) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="状态" width="100" align="center">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="lastLogin" label="最后登录" width="150" />
          <el-table-column prop="createTime" label="创建时间" width="150" />
          <el-table-column label="操作" width="250">
            <template #default="scope">
              <el-button size="small" @click="editUser(scope.row)">编辑</el-button>
              <el-button 
                size="small" 
                :type="scope.row.status === 'active' ? 'warning' : 'success'"
                @click="toggleUserStatus(scope.row)"
              >
                {{ scope.row.status === 'active' ? '禁用' : '启用' }}
              </el-button>
              <el-button size="small" @click="resetPassword(scope.row)">重置密码</el-button>
              <el-button size="small" type="danger" @click="deleteUser(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="totalUsers"
            layout="total, sizes, prev, pager, next, jumper"
          />
        </div>
      </el-card>
    </div>
    
    <!-- 添加/编辑用户弹窗 -->
    <el-dialog v-model="userDialogVisible" :title="isEdit ? '编辑用户' : '添加用户'" width="600px">
      <el-form :model="userForm" :rules="userRules" ref="userFormRef" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名" :disabled="isEdit" />
        </el-form-item>
        
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="userForm.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        
        <el-form-item label="角色" prop="role">
          <el-select v-model="userForm.role" placeholder="请选择角色" style="width: 100%;">
            <el-option label="管理员" value="admin" />
            <el-option label="普通用户" value="user" />
            <el-option label="访客" value="guest" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="状态" prop="status">
          <el-select v-model="userForm.status" placeholder="请选择状态" style="width: 100%;">
            <el-option label="活跃" value="active" />
            <el-option label="待审核" value="pending" />
            <el-option label="已禁用" value="disabled" />
          </el-select>
        </el-form-item>
        
        <el-form-item v-if="!isEdit" label="密码" prop="password">
          <el-input v-model="userForm.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        
        <el-form-item v-if="!isEdit" label="确认密码" prop="confirmPassword">
          <el-input v-model="userForm.confirmPassword" type="password" placeholder="请确认密码" show-password />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="userDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveUser">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  User, 
  CircleCheck, 
  Clock, 
  Lock, 
  Search, 
  Plus 
} from '@element-plus/icons-vue'

const searchText = ref('')
const statusFilter = ref('')
const roleFilter = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const userDialogVisible = ref(false)
const isEdit = ref(false)
const userFormRef = ref()

const users = ref([
  {
    id: 1,
    username: 'admin',
    realName: '系统管理员',
    email: 'admin@example.com',
    phone: '13800138001',
    role: 'admin',
    status: 'active',
    avatar: '',
    lastLogin: '2024-01-15 10:30',
    createTime: '2024-01-01 09:00'
  },
  {
    id: 2,
    username: 'zhangsan',
    realName: '张三',
    email: 'zhangsan@example.com',
    phone: '13800138002',
    role: 'user',
    status: 'active',
    avatar: '',
    lastLogin: '2024-01-15 09:15',
    createTime: '2024-01-02 14:20'
  },
  {
    id: 3,
    username: 'lisi',
    realName: '李四',
    email: 'lisi@example.com',
    phone: '13800138003',
    role: 'user',
    status: 'pending',
    avatar: '',
    lastLogin: '从未登录',
    createTime: '2024-01-15 16:45'
  },
  {
    id: 4,
    username: 'wangwu',
    realName: '王五',
    email: 'wangwu@example.com',
    phone: '13800138004',
    role: 'guest',
    status: 'disabled',
    avatar: '',
    lastLogin: '2024-01-10 11:20',
    createTime: '2024-01-05 08:30'
  }
])

const userForm = ref({
  username: '',
  realName: '',
  email: '',
  phone: '',
  role: '',
  status: 'active',
  password: '',
  confirmPassword: ''
})

const userRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== userForm.value.password) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const filteredUsers = computed(() => {
  let filtered = users.value
  
  if (statusFilter.value) {
    filtered = filtered.filter(user => user.status === statusFilter.value)
  }
  
  if (roleFilter.value) {
    filtered = filtered.filter(user => user.role === roleFilter.value)
  }
  
  if (searchText.value) {
    filtered = filtered.filter(user => 
      user.username.toLowerCase().includes(searchText.value.toLowerCase()) ||
      user.email.toLowerCase().includes(searchText.value.toLowerCase()) ||
      user.realName.toLowerCase().includes(searchText.value.toLowerCase())
    )
  }
  
  return filtered
})

const totalUsers = computed(() => users.value.length)
const activeUsers = computed(() => users.value.filter(user => user.status === 'active').length)
const pendingUsers = computed(() => users.value.filter(user => user.status === 'pending').length)
const disabledUsers = computed(() => users.value.filter(user => user.status === 'disabled').length)

const getRoleText = (role) => {
  const roleMap = {
    'admin': '管理员',
    'user': '普通用户',
    'guest': '访客'
  }
  return roleMap[role] || role
}

const getRoleType = (role) => {
  const typeMap = {
    'admin': 'danger',
    'user': 'primary',
    'guest': 'info'
  }
  return typeMap[role] || 'info'
}

const getStatusText = (status) => {
  const statusMap = {
    'active': '活跃',
    'pending': '待审核',
    'disabled': '已禁用'
  }
  return statusMap[status] || status
}

const getStatusType = (status) => {
  const typeMap = {
    'active': 'success',
    'pending': 'warning',
    'disabled': 'danger'
  }
  return typeMap[status] || 'info'
}

const addUser = () => {
  isEdit.value = false
  userForm.value = {
    username: '',
    realName: '',
    email: '',
    phone: '',
    role: '',
    status: 'active',
    password: '',
    confirmPassword: ''
  }
  userDialogVisible.value = true
}

const editUser = (user) => {
  isEdit.value = true
  userForm.value = {
    ...user,
    password: '',
    confirmPassword: ''
  }
  userDialogVisible.value = true
}

const saveUser = async () => {
  try {
    await userFormRef.value.validate()
    if (isEdit.value) {
      ElMessage.success('用户信息更新成功')
    } else {
      ElMessage.success('用户添加成功')
    }
    userDialogVisible.value = false
  } catch {
    ElMessage.error('请检查表单信息')
  }
}

const toggleUserStatus = async (user) => {
  const action = user.status === 'active' ? '禁用' : '启用'
  try {
    await ElMessageBox.confirm(
      `确定要${action}用户 "${user.realName}" 吗？`,
      `确认${action}`,
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    user.status = user.status === 'active' ? 'disabled' : 'active'
    ElMessage.success(`用户${action}成功`)
  } catch {
    ElMessage.info(`已取消${action}`)
  }
}

const resetPassword = async (user) => {
  try {
    await ElMessageBox.confirm(
      `确定要重置用户 "${user.realName}" 的密码吗？`,
      '确认重置',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    ElMessage.success('密码重置成功，新密码已发送到用户邮箱')
  } catch {
    ElMessage.info('已取消重置')
  }
}

const deleteUser = async (user) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除用户 "${user.realName}" 吗？此操作不可恢复！`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const index = users.value.findIndex(u => u.id === user.id)
    if (index > -1) {
      users.value.splice(index, 1)
      ElMessage.success('用户删除成功')
    }
  } catch {
    ElMessage.info('已取消删除')
  }
}

onMounted(() => {
  // 初始化数据
})
</script>

<style scoped>
.user-management-container {
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

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>