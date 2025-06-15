<template>
  <div class="workflow-config-container">
    <div class="page-header">
      <h2>工作流配置管理</h2>
      <p>管理工作流系统的配置、权限、角色等设置</p>
    </div>
    
    <div class="content-area">
      <!-- 配置导航 -->
      <el-tabs v-model="activeTab" type="card" class="config-tabs">
        <!-- 系统配置 -->
        <el-tab-pane label="系统配置" name="system">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>系统配置</span>
                <el-button type="primary" @click="showSystemConfigDialog = true">
                  <el-icon><Plus /></el-icon>
                  新增配置
                </el-button>
              </div>
            </template>
            
            <!-- 搜索区域 -->
            <div class="search-area">
              <el-form :model="systemConfigQuery" inline>
                <el-form-item label="配置键">
                  <el-input v-model="systemConfigQuery.configKey" placeholder="请输入配置键" clearable />
                </el-form-item>
                <el-form-item label="配置组">
                  <el-input v-model="systemConfigQuery.configGroup" placeholder="请输入配置组" clearable />
                </el-form-item>
                <el-form-item label="状态">
                  <el-select v-model="systemConfigQuery.status" placeholder="请选择状态" clearable>
                    <el-option label="启用" value="1" />
                    <el-option label="禁用" value="0" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="getSystemConfigList">
                    <el-icon><Search /></el-icon>
                    搜索
                  </el-button>
                  <el-button @click="resetSystemConfigQuery">
                    <el-icon><Refresh /></el-icon>
                    重置
                  </el-button>
                </el-form-item>
              </el-form>
            </div>
            
            <!-- 配置列表 -->
            <el-table :data="systemConfigList" style="width: 100%" v-loading="systemConfigLoading">
              <el-table-column prop="configKey" label="配置键" width="200" />
              <el-table-column prop="configValue" label="配置值" width="200" show-overflow-tooltip />
              <el-table-column prop="configGroup" label="配置组" width="150" />
              <el-table-column prop="configType" label="配置类型" width="120" />
              <el-table-column prop="description" label="描述" show-overflow-tooltip />
              <el-table-column label="状态" width="100" align="center">
                <template #default="scope">
                  <el-tag :type="scope.row.status === '1' ? 'success' : 'danger'">
                    {{ scope.row.status === '1' ? '启用' : '禁用' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="180" />
              <el-table-column label="操作" width="200" align="center">
                <template #default="scope">
                  <el-button type="primary" size="small" @click="editSystemConfig(scope.row)">
                    编辑
                  </el-button>
                  <el-button type="danger" size="small" @click="deleteSystemConfig(scope.row.id)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            
            <!-- 分页 -->
            <el-pagination
              v-model:current-page="systemConfigQuery.pageNum"
              v-model:page-size="systemConfigQuery.pageSize"
              :total="systemConfigTotal"
              :page-sizes="[10, 20, 50, 100]"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="getSystemConfigList"
              @current-change="getSystemConfigList"
              class="pagination"
            />
          </el-card>
        </el-tab-pane>
        
        <!-- 权限管理 -->
        <el-tab-pane label="权限管理" name="permission">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>权限管理</span>
                <el-button type="primary" @click="showPermissionDialog = true">
                  <el-icon><Plus /></el-icon>
                  新增权限
                </el-button>
              </div>
            </template>
            
            <!-- 权限树 -->
            <div class="permission-tree">
              <el-tree
                :data="permissionTree"
                :props="{ children: 'children', label: 'permissionName' }"
                show-checkbox
                node-key="id"
                :default-expand-all="true"
                class="permission-tree-component"
              >
                <template #default="{ node, data }">
                  <span class="custom-tree-node">
                    <span>{{ data.permissionName }}</span>
                    <span class="node-actions">
                      <el-button type="primary" size="small" @click="editPermission(data)">
                        编辑
                      </el-button>
                      <el-button type="success" size="small" @click="addChildPermission(data)">
                        添加子权限
                      </el-button>
                      <el-button type="danger" size="small" @click="deletePermission(data.id)">
                        删除
                      </el-button>
                    </span>
                  </span>
                </template>
              </el-tree>
            </div>
          </el-card>
        </el-tab-pane>
        
        <!-- 角色管理 -->
        <el-tab-pane label="角色管理" name="role">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>角色管理</span>
                <el-button type="primary" @click="showRoleDialog = true">
                  <el-icon><Plus /></el-icon>
                  新增角色
                </el-button>
              </div>
            </template>
            
            <!-- 搜索区域 -->
            <div class="search-area">
              <el-form :model="roleQuery" inline>
                <el-form-item label="角色名称">
                  <el-input v-model="roleQuery.roleName" placeholder="请输入角色名称" clearable />
                </el-form-item>
                <el-form-item label="角色编码">
                  <el-input v-model="roleQuery.roleCode" placeholder="请输入角色编码" clearable />
                </el-form-item>
                <el-form-item label="状态">
                  <el-select v-model="roleQuery.status" placeholder="请选择状态" clearable>
                    <el-option label="启用" value="1" />
                    <el-option label="禁用" value="0" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="getRoleList">
                    <el-icon><Search /></el-icon>
                    搜索
                  </el-button>
                  <el-button @click="resetRoleQuery">
                    <el-icon><Refresh /></el-icon>
                    重置
                  </el-button>
                </el-form-item>
              </el-form>
            </div>
            
            <!-- 角色列表 -->
            <el-table :data="roleList" style="width: 100%" v-loading="roleLoading">
              <el-table-column prop="roleName" label="角色名称" width="150" />
              <el-table-column prop="roleCode" label="角色编码" width="150" />
              <el-table-column prop="description" label="描述" show-overflow-tooltip />
              <el-table-column label="状态" width="100" align="center">
                <template #default="scope">
                  <el-tag :type="scope.row.status === '1' ? 'success' : 'danger'">
                    {{ scope.row.status === '1' ? '启用' : '禁用' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="180" />
              <el-table-column label="操作" width="250" align="center">
                <template #default="scope">
                  <el-button type="primary" size="small" @click="editRole(scope.row)">
                    编辑
                  </el-button>
                  <el-button type="success" size="small" @click="assignPermissions(scope.row)">
                    分配权限
                  </el-button>
                  <el-button type="danger" size="small" @click="deleteRole(scope.row.id)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            
            <!-- 分页 -->
            <el-pagination
              v-model:current-page="roleQuery.pageNum"
              v-model:page-size="roleQuery.pageSize"
              :total="roleTotal"
              :page-sizes="[10, 20, 50, 100]"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="getRoleList"
              @current-change="getRoleList"
              class="pagination"
            />
          </el-card>
        </el-tab-pane>
        
        <!-- 流程分类 -->
        <el-tab-pane label="流程分类" name="category">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>流程分类管理</span>
                <el-button type="primary" @click="showCategoryDialog = true">
                  <el-icon><Plus /></el-icon>
                  新增分类
                </el-button>
              </div>
            </template>
            
            <!-- 分类树 -->
            <div class="category-tree">
              <el-tree
                :data="categoryTree"
                :props="{ children: 'children', label: 'categoryName' }"
                node-key="id"
                :default-expand-all="true"
                class="category-tree-component"
              >
                <template #default="{ node, data }">
                  <span class="custom-tree-node">
                    <span>
                      <el-icon><Folder /></el-icon>
                      {{ data.categoryName }}
                      <el-tag v-if="data.status === '0'" type="danger" size="small">禁用</el-tag>
                    </span>
                    <span class="node-actions">
                      <el-button type="primary" size="small" @click="editCategory(data)">
                        编辑
                      </el-button>
                      <el-button type="success" size="small" @click="addChildCategory(data)">
                        添加子分类
                      </el-button>
                      <el-button type="danger" size="small" @click="deleteCategory(data.id)">
                        删除
                      </el-button>
                    </span>
                  </span>
                </template>
              </el-tree>
            </div>
          </el-card>
        </el-tab-pane>
        
        <!-- 通知模板 -->
        <el-tab-pane label="通知模板" name="notification">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>通知模板管理</span>
                <el-button type="primary" @click="showNotificationDialog = true">
                  <el-icon><Plus /></el-icon>
                  新增模板
                </el-button>
              </div>
            </template>
            
            <!-- 搜索区域 -->
            <div class="search-area">
              <el-form :model="notificationQuery" inline>
                <el-form-item label="模板名称">
                  <el-input v-model="notificationQuery.templateName" placeholder="请输入模板名称" clearable />
                </el-form-item>
                <el-form-item label="模板类型">
                  <el-select v-model="notificationQuery.templateType" placeholder="请选择模板类型" clearable>
                    <el-option label="邮件" value="EMAIL" />
                    <el-option label="短信" value="SMS" />
                    <el-option label="站内信" value="SYSTEM" />
                  </el-select>
                </el-form-item>
                <el-form-item label="事件类型">
                  <el-select v-model="notificationQuery.eventType" placeholder="请选择事件类型" clearable>
                    <el-option label="任务分配" value="TASK_ASSIGN" />
                    <el-option label="任务完成" value="TASK_COMPLETE" />
                    <el-option label="流程启动" value="PROCESS_START" />
                    <el-option label="流程结束" value="PROCESS_END" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="getNotificationList">
                    <el-icon><Search /></el-icon>
                    搜索
                  </el-button>
                  <el-button @click="resetNotificationQuery">
                    <el-icon><Refresh /></el-icon>
                    重置
                  </el-button>
                </el-form-item>
              </el-form>
            </div>
            
            <!-- 模板列表 -->
            <el-table :data="notificationList" style="width: 100%" v-loading="notificationLoading">
              <el-table-column prop="templateName" label="模板名称" width="200" />
              <el-table-column prop="templateCode" label="模板编码" width="150" />
              <el-table-column label="模板类型" width="120" align="center">
                <template #default="scope">
                  <el-tag :type="getTemplateTypeTag(scope.row.templateType)">
                    {{ getTemplateTypeText(scope.row.templateType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="事件类型" width="120" align="center">
                <template #default="scope">
                  <el-tag type="info">
                    {{ getEventTypeText(scope.row.eventType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="subject" label="主题" show-overflow-tooltip />
              <el-table-column label="状态" width="100" align="center">
                <template #default="scope">
                  <el-tag :type="scope.row.status === '1' ? 'success' : 'danger'">
                    {{ scope.row.status === '1' ? '启用' : '禁用' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200" align="center">
                <template #default="scope">
                  <el-button type="primary" size="small" @click="editNotification(scope.row)">
                    编辑
                  </el-button>
                  <el-button type="success" size="small" @click="previewNotification(scope.row)">
                    预览
                  </el-button>
                  <el-button type="danger" size="small" @click="deleteNotification(scope.row.id)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            
            <!-- 分页 -->
            <el-pagination
              v-model:current-page="notificationQuery.pageNum"
              v-model:page-size="notificationQuery.pageSize"
              :total="notificationTotal"
              :page-sizes="[10, 20, 50, 100]"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="getNotificationList"
              @current-change="getNotificationList"
              class="pagination"
            />
          </el-card>
        </el-tab-pane>
      </el-tabs>
    </div>
    
    <!-- 系统配置对话框 -->
    <el-dialog
      v-model="showSystemConfigDialog"
      :title="systemConfigForm.id ? '编辑系统配置' : '新增系统配置'"
      width="600px"
    >
      <el-form :model="systemConfigForm" :rules="systemConfigRules" ref="systemConfigFormRef" label-width="120px">
        <el-form-item label="配置键" prop="configKey">
          <el-input v-model="systemConfigForm.configKey" placeholder="请输入配置键" />
        </el-form-item>
        <el-form-item label="配置值" prop="configValue">
          <el-input v-model="systemConfigForm.configValue" type="textarea" :rows="3" placeholder="请输入配置值" />
        </el-form-item>
        <el-form-item label="配置组" prop="configGroup">
          <el-input v-model="systemConfigForm.configGroup" placeholder="请输入配置组" />
        </el-form-item>
        <el-form-item label="配置类型" prop="configType">
          <el-select v-model="systemConfigForm.configType" placeholder="请选择配置类型">
            <el-option label="字符串" value="STRING" />
            <el-option label="数字" value="NUMBER" />
            <el-option label="布尔值" value="BOOLEAN" />
            <el-option label="JSON" value="JSON" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="systemConfigForm.description" type="textarea" :rows="2" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="systemConfigForm.status">
            <el-radio label="1">启用</el-radio>
            <el-radio label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showSystemConfigDialog = false">取消</el-button>
          <el-button type="primary" @click="saveSystemConfig">确定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 权限对话框 -->
    <el-dialog
      v-model="showPermissionDialog"
      :title="permissionForm.id ? '编辑权限' : '新增权限'"
      width="600px"
    >
      <el-form :model="permissionForm" :rules="permissionRules" ref="permissionFormRef" label-width="120px">
        <el-form-item label="权限名称" prop="permissionName">
          <el-input v-model="permissionForm.permissionName" placeholder="请输入权限名称" />
        </el-form-item>
        <el-form-item label="权限编码" prop="permissionCode">
          <el-input v-model="permissionForm.permissionCode" placeholder="请输入权限编码" />
        </el-form-item>
        <el-form-item label="权限类型" prop="permissionType">
          <el-select v-model="permissionForm.permissionType" placeholder="请选择权限类型">
            <el-option label="菜单" value="MENU" />
            <el-option label="按钮" value="BUTTON" />
            <el-option label="接口" value="API" />
          </el-select>
        </el-form-item>
        <el-form-item label="父级权限">
          <el-tree-select
            v-model="permissionForm.parentId"
            :data="permissionTree"
            :props="{ children: 'children', label: 'permissionName', value: 'id' }"
            placeholder="请选择父级权限"
            clearable
          />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="permissionForm.sort" :min="0" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="permissionForm.description" type="textarea" :rows="2" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="permissionForm.status">
            <el-radio label="1">启用</el-radio>
            <el-radio label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showPermissionDialog = false">取消</el-button>
          <el-button type="primary" @click="savePermission">确定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 角色对话框 -->
    <el-dialog
      v-model="showRoleDialog"
      :title="roleForm.id ? '编辑角色' : '新增角色'"
      width="600px"
    >
      <el-form :model="roleForm" :rules="roleRules" ref="roleFormRef" label-width="120px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="roleForm.roleCode" placeholder="请输入角色编码" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="roleForm.sort" :min="0" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="roleForm.description" type="textarea" :rows="2" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="roleForm.status">
            <el-radio label="1">启用</el-radio>
            <el-radio label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showRoleDialog = false">取消</el-button>
          <el-button type="primary" @click="saveRole">确定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 权限分配对话框 -->
    <el-dialog
      v-model="showPermissionAssignDialog"
      title="分配权限"
      width="600px"
    >
      <el-tree
        ref="permissionTreeRef"
        :data="permissionTree"
        :props="{ children: 'children', label: 'permissionName' }"
        show-checkbox
        node-key="id"
        :default-expand-all="true"
      />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showPermissionAssignDialog = false">取消</el-button>
          <el-button type="primary" @click="saveRolePermissions">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus,
  Search,
  Refresh,
  Folder
} from '@element-plus/icons-vue'
import {
  getSystemConfigList as apiGetSystemConfigList,
  saveSystemConfig as apiSaveSystemConfig,
  deleteSystemConfig as apiDeleteSystemConfig,
  getPermissionTree as apiGetPermissionTree,
  savePermission as apiSavePermission,
  deletePermission as apiDeletePermission,
  getRoleList as apiGetRoleList,
  saveRole as apiSaveRole,
  deleteRole as apiDeleteRole,
  getRolePermissions as apiGetRolePermissions,
  saveRolePermissions as apiSaveRolePermissions,
  getCategoryTree as apiGetCategoryTree,
  saveCategory as apiSaveCategory,
  deleteCategory as apiDeleteCategory,
  getNotificationList as apiGetNotificationList,
  saveNotification as apiSaveNotification,
  deleteNotification as apiDeleteNotification
} from '@/api/workflowConfig'

// 当前激活的标签页
const activeTab = ref('system')

// 系统配置相关
const systemConfigList = ref([])
const systemConfigTotal = ref(0)
const systemConfigLoading = ref(false)
const showSystemConfigDialog = ref(false)
const systemConfigFormRef = ref()

const systemConfigQuery = reactive({
  configKey: '',
  configGroup: '',
  status: '',
  pageNum: 1,
  pageSize: 10
})

const systemConfigForm = reactive({
  id: null,
  configKey: '',
  configValue: '',
  configGroup: '',
  configType: 'STRING',
  description: '',
  status: '1'
})

const systemConfigRules = {
  configKey: [{ required: true, message: '请输入配置键', trigger: 'blur' }],
  configValue: [{ required: true, message: '请输入配置值', trigger: 'blur' }],
  configGroup: [{ required: true, message: '请输入配置组', trigger: 'blur' }],
  configType: [{ required: true, message: '请选择配置类型', trigger: 'change' }]
}

// 权限相关
const permissionTree = ref([])
const showPermissionDialog = ref(false)
const showPermissionAssignDialog = ref(false)
const permissionFormRef = ref()
const permissionTreeRef = ref()
const currentRole = ref(null)

const permissionForm = reactive({
  id: null,
  permissionName: '',
  permissionCode: '',
  permissionType: 'MENU',
  parentId: null,
  sort: 0,
  description: '',
  status: '1'
})

const permissionRules = {
  permissionName: [{ required: true, message: '请输入权限名称', trigger: 'blur' }],
  permissionCode: [{ required: true, message: '请输入权限编码', trigger: 'blur' }],
  permissionType: [{ required: true, message: '请选择权限类型', trigger: 'change' }]
}

// 角色相关
const roleList = ref([])
const roleTotal = ref(0)
const roleLoading = ref(false)
const showRoleDialog = ref(false)
const roleFormRef = ref()

const roleQuery = reactive({
  roleName: '',
  roleCode: '',
  status: '',
  pageNum: 1,
  pageSize: 10
})

const roleForm = reactive({
  id: null,
  roleName: '',
  roleCode: '',
  sort: 0,
  description: '',
  status: '1'
})

const roleRules = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  roleCode: [{ required: true, message: '请输入角色编码', trigger: 'blur' }]
}

// 流程分类相关
const categoryTree = ref([])
const showCategoryDialog = ref(false)

// 通知模板相关
const notificationList = ref([])
const notificationTotal = ref(0)
const notificationLoading = ref(false)
const showNotificationDialog = ref(false)

const notificationQuery = reactive({
  templateName: '',
  templateType: '',
  eventType: '',
  pageNum: 1,
  pageSize: 10
})

// 系统配置方法
const getSystemConfigList = async () => {
  systemConfigLoading.value = true
  try {
    const response = await apiGetSystemConfigList(systemConfigQuery)
    systemConfigList.value = response.data.list
    systemConfigTotal.value = response.data.total
  } catch (error) {
    ElMessage.error('获取系统配置列表失败')
  } finally {
    systemConfigLoading.value = false
  }
}

const resetSystemConfigQuery = () => {
  Object.assign(systemConfigQuery, {
    configKey: '',
    configGroup: '',
    status: '',
    pageNum: 1,
    pageSize: 10
  })
  getSystemConfigList()
}

const editSystemConfig = (row) => {
  Object.assign(systemConfigForm, row)
  showSystemConfigDialog.value = true
}

const saveSystemConfig = async () => {
  if (!systemConfigFormRef.value) return
  
  try {
    await systemConfigFormRef.value.validate()
    await apiSaveSystemConfig(systemConfigForm)
    ElMessage.success('保存成功')
    showSystemConfigDialog.value = false
    getSystemConfigList()
    resetSystemConfigForm()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const deleteSystemConfig = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个配置吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await apiDeleteSystemConfig(id)
    ElMessage.success('删除成功')
    getSystemConfigList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const resetSystemConfigForm = () => {
  Object.assign(systemConfigForm, {
    id: null,
    configKey: '',
    configValue: '',
    configGroup: '',
    configType: 'STRING',
    description: '',
    status: '1'
  })
}

// 权限方法
const getPermissionTree = async () => {
  try {
    const response = await apiGetPermissionTree()
    permissionTree.value = response.data
  } catch (error) {
    ElMessage.error('获取权限树失败')
  }
}

const editPermission = (row) => {
  Object.assign(permissionForm, row)
  showPermissionDialog.value = true
}

const addChildPermission = (parent) => {
  resetPermissionForm()
  permissionForm.parentId = parent.id
  showPermissionDialog.value = true
}

const savePermission = async () => {
  if (!permissionFormRef.value) return
  
  try {
    await permissionFormRef.value.validate()
    await apiSavePermission(permissionForm)
    ElMessage.success('保存成功')
    showPermissionDialog.value = false
    getPermissionTree()
    resetPermissionForm()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const deletePermission = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个权限吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await apiDeletePermission(id)
    ElMessage.success('删除成功')
    getPermissionTree()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const resetPermissionForm = () => {
  Object.assign(permissionForm, {
    id: null,
    permissionName: '',
    permissionCode: '',
    permissionType: 'MENU',
    parentId: null,
    sort: 0,
    description: '',
    status: '1'
  })
}

// 角色方法
const getRoleList = async () => {
  roleLoading.value = true
  try {
    const response = await apiGetRoleList(roleQuery)
    roleList.value = response.data.list
    roleTotal.value = response.data.total
  } catch (error) {
    ElMessage.error('获取角色列表失败')
  } finally {
    roleLoading.value = false
  }
}

const resetRoleQuery = () => {
  Object.assign(roleQuery, {
    roleName: '',
    roleCode: '',
    status: '',
    pageNum: 1,
    pageSize: 10
  })
  getRoleList()
}

const editRole = (row) => {
  Object.assign(roleForm, row)
  showRoleDialog.value = true
}

const saveRole = async () => {
  if (!roleFormRef.value) return
  
  try {
    await roleFormRef.value.validate()
    await apiSaveRole(roleForm)
    ElMessage.success('保存成功')
    showRoleDialog.value = false
    getRoleList()
    resetRoleForm()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const deleteRole = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个角色吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await apiDeleteRole(id)
    ElMessage.success('删除成功')
    getRoleList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const assignPermissions = async (role) => {
  currentRole.value = role
  showPermissionAssignDialog.value = true
  
  try {
    const response = await apiGetRolePermissions(role.id)
    const checkedKeys = response.data
    permissionTreeRef.value.setCheckedKeys(checkedKeys)
  } catch (error) {
    ElMessage.error('获取角色权限失败')
  }
}

const saveRolePermissions = async () => {
  try {
    const checkedKeys = permissionTreeRef.value.getCheckedKeys()
    const halfCheckedKeys = permissionTreeRef.value.getHalfCheckedKeys()
    const permissionIds = [...checkedKeys, ...halfCheckedKeys]
    
    await apiSaveRolePermissions({
      roleId: currentRole.value.id,
      permissionIds
    })
    
    ElMessage.success('权限分配成功')
    showPermissionAssignDialog.value = false
  } catch (error) {
    ElMessage.error('权限分配失败')
  }
}

const resetRoleForm = () => {
  Object.assign(roleForm, {
    id: null,
    roleName: '',
    roleCode: '',
    sort: 0,
    description: '',
    status: '1'
  })
}

// 流程分类方法
const getCategoryTree = async () => {
  try {
    const response = await apiGetCategoryTree()
    categoryTree.value = response.data
  } catch (error) {
    ElMessage.error('获取分类树失败')
  }
}

const editCategory = (row) => {
  // 编辑分类逻辑
}

const addChildCategory = (parent) => {
  // 添加子分类逻辑
}

const deleteCategory = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个分类吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await apiDeleteCategory(id)
    ElMessage.success('删除成功')
    getCategoryTree()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 通知模板方法
const getNotificationList = async () => {
  notificationLoading.value = true
  try {
    const response = await apiGetNotificationList(notificationQuery)
    notificationList.value = response.data.list
    notificationTotal.value = response.data.total
  } catch (error) {
    ElMessage.error('获取通知模板列表失败')
  } finally {
    notificationLoading.value = false
  }
}

const resetNotificationQuery = () => {
  Object.assign(notificationQuery, {
    templateName: '',
    templateType: '',
    eventType: '',
    pageNum: 1,
    pageSize: 10
  })
  getNotificationList()
}

const editNotification = (row) => {
  // 编辑通知模板逻辑
}

const previewNotification = (row) => {
  // 预览通知模板逻辑
}

const deleteNotification = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个模板吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await apiDeleteNotification(id)
    ElMessage.success('删除成功')
    getNotificationList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 辅助方法
const getTemplateTypeTag = (type) => {
  const typeMap = {
    EMAIL: 'primary',
    SMS: 'success',
    SYSTEM: 'warning'
  }
  return typeMap[type] || 'info'
}

const getTemplateTypeText = (type) => {
  const typeMap = {
    EMAIL: '邮件',
    SMS: '短信',
    SYSTEM: '站内信'
  }
  return typeMap[type] || type
}

const getEventTypeText = (type) => {
  const typeMap = {
    TASK_ASSIGN: '任务分配',
    TASK_COMPLETE: '任务完成',
    PROCESS_START: '流程启动',
    PROCESS_END: '流程结束'
  }
  return typeMap[type] || type
}

// 初始化
onMounted(() => {
  getSystemConfigList()
  getPermissionTree()
  getRoleList()
  getCategoryTree()
  getNotificationList()
})
</script>

<style scoped>
.workflow-config-container {
  padding: 20px;
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

.content-area {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.config-tabs {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-area {
  margin-bottom: 20px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.permission-tree,
.category-tree {
  padding: 20px;
}

.permission-tree-component,
.category-tree-component {
  background: #f5f7fa;
  border-radius: 4px;
  padding: 20px;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.node-actions {
  display: none;
}

.custom-tree-node:hover .node-actions {
  display: block;
}

.dialog-footer {
  text-align: right;
}

.el-form-item {
  margin-bottom: 18px;
}

.el-table {
  border-radius: 4px;
  overflow: hidden;
}

.el-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>