<template>
  <div class="role-management-container">
    <div class="page-header">
      <h2>角色管理</h2>
      <p>管理系统角色和权限配置</p>
    </div>
    
    <div class="content-area">
      <el-row :gutter="20">
        <!-- 角色列表 -->
        <el-col :span="8">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>角色列表</span>
                <el-button type="primary" size="small" @click="addRole">
                  <el-icon><Plus /></el-icon>
                  添加角色
                </el-button>
              </div>
            </template>
            
            <div class="role-list">
              <div 
                v-for="role in roles" 
                :key="role.id"
                class="role-item"
                :class="{ active: selectedRole?.id === role.id }"
                @click="selectRole(role)"
              >
                <div class="role-info">
                  <div class="role-name">{{ role.name }}</div>
                  <div class="role-description">{{ role.description }}</div>
                  <div class="role-meta">
                    <span class="user-count">{{ role.userCount }} 个用户</span>
                    <el-tag :type="role.status === 'active' ? 'success' : 'danger'" size="small">
                      {{ role.status === 'active' ? '启用' : '禁用' }}
                    </el-tag>
                  </div>
                </div>
                <div class="role-actions">
                  <el-button size="small" text @click.stop="editRole(role)">
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button size="small" text type="danger" @click.stop="deleteRole(role)">
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <!-- 权限配置 -->
        <el-col :span="16">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>权限配置</span>
                <div v-if="selectedRole">
                  <el-button @click="expandAll">展开全部</el-button>
                  <el-button @click="collapseAll">收起全部</el-button>
                  <el-button type="primary" @click="savePermissions">保存权限</el-button>
                </div>
              </div>
            </template>
            
            <div v-if="!selectedRole" class="no-selection">
              <el-empty description="请选择一个角色来配置权限" />
            </div>
            
            <div v-else class="permission-config">
              <div class="role-info-header">
                <h3>{{ selectedRole.name }}</h3>
                <p>{{ selectedRole.description }}</p>
              </div>
              
              <el-tree
                ref="permissionTreeRef"
                :data="permissionTree"
                :props="treeProps"
                show-checkbox
                node-key="id"
                :default-checked-keys="selectedRole.permissions"
                :default-expand-all="false"
                class="permission-tree"
              >
                <template #default="{ node, data }">
                  <div class="tree-node">
                    <el-icon v-if="data.icon" class="node-icon">
                      <component :is="data.icon" />
                    </el-icon>
                    <span class="node-label">{{ node.label }}</span>
                    <span v-if="data.description" class="node-description">{{ data.description }}</span>
                  </div>
                </template>
              </el-tree>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <!-- 添加/编辑角色弹窗 -->
    <el-dialog v-model="roleDialogVisible" :title="isEdit ? '编辑角色' : '添加角色'" width="500px">
      <el-form :model="roleForm" :rules="roleRules" ref="roleFormRef" label-width="100px">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="roleForm.name" placeholder="请输入角色名称" />
        </el-form-item>
        
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="roleForm.code" placeholder="请输入角色编码" :disabled="isEdit" />
        </el-form-item>
        
        <el-form-item label="角色描述" prop="description">
          <el-input 
            v-model="roleForm.description" 
            type="textarea" 
            :rows="3"
            placeholder="请输入角色描述"
          />
        </el-form-item>
        
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="roleForm.status">
            <el-radio label="active">启用</el-radio>
            <el-radio label="inactive">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="roleDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveRole">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Plus, 
  Edit, 
  Delete,
  User,
  Setting,
  Document,
  Monitor,
  Link,
  VideoCamera
} from '@element-plus/icons-vue'

const selectedRole = ref(null)
const roleDialogVisible = ref(false)
const isEdit = ref(false)
const roleFormRef = ref()
const permissionTreeRef = ref()

const roles = ref([
  {
    id: 1,
    name: '超级管理员',
    code: 'super_admin',
    description: '拥有系统所有权限',
    status: 'active',
    userCount: 1,
    permissions: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]
  },
  {
    id: 2,
    name: '部门管理员',
    code: 'dept_admin',
    description: '管理部门内用户和基础功能',
    status: 'active',
    userCount: 5,
    permissions: [1, 2, 3, 7, 8, 9, 13, 14, 15]
  },
  {
    id: 3,
    name: '普通用户',
    code: 'user',
    description: '基础功能使用权限',
    status: 'active',
    userCount: 20,
    permissions: [1, 7, 13]
  },
  {
    id: 4,
    name: '访客',
    code: 'guest',
    description: '只读权限',
    status: 'active',
    userCount: 3,
    permissions: [1]
  }
])

const roleForm = ref({
  name: '',
  code: '',
  description: '',
  status: 'active'
})

const roleRules = {
  name: [
    { required: true, message: '请输入角色名称', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入角色编码', trigger: 'blur' },
    { pattern: /^[a-zA-Z_][a-zA-Z0-9_]*$/, message: '角色编码只能包含字母、数字和下划线，且以字母或下划线开头', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入角色描述', trigger: 'blur' }
  ]
}

const treeProps = {
  children: 'children',
  label: 'label'
}

const permissionTree = ref([
  {
    id: 1,
    label: '仪表盘',
    icon: 'Monitor',
    description: '查看系统概览和数据统计',
    children: [
      {
        id: 2,
        label: '首页概览',
        description: '查看首页概览信息'
      },
      {
        id: 3,
        label: '数据可视化',
        description: '查看数据可视化图表'
      }
    ]
  },
  {
    id: 4,
    label: '系统管理',
    icon: 'Setting',
    description: '系统配置和管理功能',
    children: [
      {
        id: 5,
        label: '用户管理',
        description: '管理系统用户'
      },
      {
        id: 6,
        label: '角色管理',
        description: '管理系统角色和权限'
      },
      {
        id: 19,
        label: '系统设置',
        description: '配置系统参数'
      }
    ]
  },
  {
    id: 7,
    label: '工作流管理',
    icon: 'Document',
    description: '工作流程管理功能',
    children: [
      {
        id: 8,
        label: '流程设计',
        description: '设计和配置工作流程'
      },
      {
        id: 9,
        label: '流程监控',
        description: '监控流程执行状态'
      },
      {
        id: 10,
        label: '流程审批',
        description: '处理流程审批任务'
      }
    ]
  },
  {
    id: 11,
    label: '供应链管理',
    icon: 'Link',
    description: '供应链协同管理',
    children: [
      {
        id: 12,
        label: '合作伙伴管理',
        description: '管理供应链合作伙伴'
      },
      {
        id: 16,
        label: '库存管理',
        description: '管理库存信息'
      },
      {
        id: 17,
        label: '物流追踪',
        description: '追踪物流信息'
      }
    ]
  },
  {
    id: 13,
    label: '视频会议',
    icon: 'VideoCamera',
    description: '视频会议功能',
    children: [
      {
        id: 14,
        label: '创建会议',
        description: '创建和管理视频会议'
      },
      {
        id: 15,
        label: '参与会议',
        description: '参与视频会议'
      }
    ]
  },
  {
    id: 18,
    label: '报表中心',
    icon: 'Document',
    description: '报表管理功能',
    children: [
      {
        id: 20,
        label: '报表查看',
        description: '查看系统报表'
      }
    ]
  }
])

const selectRole = (role) => {
  selectedRole.value = role
  // 设置权限树的选中状态
  setTimeout(() => {
    if (permissionTreeRef.value) {
      permissionTreeRef.value.setCheckedKeys(role.permissions)
    }
  }, 100)
}

const addRole = () => {
  isEdit.value = false
  roleForm.value = {
    name: '',
    code: '',
    description: '',
    status: 'active'
  }
  roleDialogVisible.value = true
}

const editRole = (role) => {
  isEdit.value = true
  roleForm.value = {
    ...role
  }
  roleDialogVisible.value = true
}

const saveRole = async () => {
  try {
    await roleFormRef.value.validate()
    if (isEdit.value) {
      ElMessage.success('角色更新成功')
    } else {
      ElMessage.success('角色添加成功')
    }
    roleDialogVisible.value = false
  } catch {
    ElMessage.error('请检查表单信息')
  }
}

const deleteRole = async (role) => {
  if (role.userCount > 0) {
    ElMessage.warning('该角色下还有用户，无法删除')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要删除角色 "${role.name}" 吗？此操作不可恢复！`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const index = roles.value.findIndex(r => r.id === role.id)
    if (index > -1) {
      roles.value.splice(index, 1)
      if (selectedRole.value?.id === role.id) {
        selectedRole.value = null
      }
      ElMessage.success('角色删除成功')
    }
  } catch {
    ElMessage.info('已取消删除')
  }
}

const expandAll = () => {
  const allKeys = []
  const collectKeys = (nodes) => {
    nodes.forEach(node => {
      allKeys.push(node.id)
      if (node.children) {
        collectKeys(node.children)
      }
    })
  }
  collectKeys(permissionTree.value)
  permissionTreeRef.value.setExpandedKeys(allKeys)
}

const collapseAll = () => {
  permissionTreeRef.value.setExpandedKeys([])
}

const savePermissions = () => {
  if (!selectedRole.value) return
  
  const checkedKeys = permissionTreeRef.value.getCheckedKeys()
  const halfCheckedKeys = permissionTreeRef.value.getHalfCheckedKeys()
  const allPermissions = [...checkedKeys, ...halfCheckedKeys]
  
  selectedRole.value.permissions = allPermissions
  ElMessage.success('权限保存成功')
}

onMounted(() => {
  // 默认选择第一个角色
  if (roles.value.length > 0) {
    selectRole(roles.value[0])
  }
})
</script>

<style scoped>
.role-management-container {
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

.content-area {
  background: #f5f5f5;
  min-height: calc(100vh - 200px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.role-list {
  max-height: 600px;
  overflow-y: auto;
}

.role-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  margin-bottom: 10px;
  background: #f8f9fa;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;
  border: 2px solid transparent;
}

.role-item:hover {
  background: #e9ecef;
}

.role-item.active {
  background: #e7f3ff;
  border-color: #409EFF;
}

.role-info {
  flex: 1;
}

.role-name {
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.role-description {
  color: #606266;
  font-size: 14px;
  margin-bottom: 8px;
}

.role-meta {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-count {
  color: #909399;
  font-size: 12px;
}

.role-actions {
  display: flex;
  gap: 5px;
}

.no-selection {
  height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.permission-config {
  padding: 10px 0;
}

.role-info-header {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

.role-info-header h3 {
  margin: 0 0 5px 0;
  color: #303133;
}

.role-info-header p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.permission-tree {
  max-height: 500px;
  overflow-y: auto;
}

.tree-node {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.node-icon {
  color: #409EFF;
}

.node-label {
  font-weight: 500;
  color: #303133;
}

.node-description {
  color: #909399;
  font-size: 12px;
  margin-left: auto;
}

.dialog-footer {
  text-align: right;
}
</style>