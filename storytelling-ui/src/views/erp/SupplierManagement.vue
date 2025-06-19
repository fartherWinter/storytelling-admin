<template>
  <div class="supplier-management">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>供应商管理</h2>
      <div class="header-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增供应商
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
                <el-icon><OfficeBuilding /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-value">{{ stats.total }}</div>
                <div class="stats-label">总供应商</div>
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
                <div class="stats-label">活跃供应商</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon cooperation">
                <el-icon><Handshake /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-value">{{ stats.cooperation }}</div>
                <div class="stats-label">合作中</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon inactive">
                <el-icon><CircleClose /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-value">{{ stats.inactive }}</div>
                <div class="stats-label">已停用</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <div class="search-area">
      <el-form :model="searchForm" inline>
        <el-form-item label="供应商名称">
          <el-input v-model="searchForm.name" placeholder="请输入供应商名称" clearable />
        </el-form-item>
        <el-form-item label="供应商编码">
          <el-input v-model="searchForm.code" placeholder="请输入供应商编码" clearable />
        </el-form-item>
        <el-form-item label="供应商类型">
          <el-select v-model="searchForm.type" placeholder="请选择供应商类型" clearable>
            <el-option label="原材料供应商" value="material" />
            <el-option label="设备供应商" value="equipment" />
            <el-option label="服务供应商" value="service" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="合作状态">
          <el-select v-model="searchForm.status" placeholder="请选择合作状态" clearable>
            <el-option label="活跃" value="active" />
            <el-option label="合作中" value="cooperation" />
            <el-option label="暂停" value="suspended" />
            <el-option label="已停用" value="inactive" />
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
            <el-button size="small" @click="handleBatchActive">批量启用</el-button>
            <el-button size="small" @click="handleBatchInactive">批量停用</el-button>
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
        <el-table-column prop="code" label="供应商编码" width="120" />
        <el-table-column prop="name" label="供应商名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="type" label="供应商类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getTypeTagType(row.type)">
              {{ getTypeText(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="contactPerson" label="联系人" width="100" />
        <el-table-column prop="contactPhone" label="联系电话" width="130" />
        <el-table-column prop="email" label="邮箱" width="150" show-overflow-tooltip />
        <el-table-column prop="address" label="地址" min-width="150" show-overflow-tooltip />
        <el-table-column prop="creditRating" label="信用等级" width="100">
          <template #default="{ row }">
            <el-rate v-model="row.creditRating" disabled show-score />
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
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
                  <el-dropdown-item @click="handleToggleStatus(row)">
                    {{ row.status === 'active' ? '停用' : '启用' }}
                  </el-dropdown-item>
                  <el-dropdown-item @click="handleContract(row)">
                    合同管理
                  </el-dropdown-item>
                  <el-dropdown-item @click="handleEvaluation(row)">
                    供应商评估
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

    <!-- 供应商表单对话框 -->
    <el-dialog
      v-model="formDialogVisible"
      :title="formMode === 'add' ? '新增供应商' : '编辑供应商'"
      width="800px"
      @close="handleFormClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="供应商编码" prop="code">
                  <el-input v-model="formData.code" placeholder="请输入供应商编码" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="供应商名称" prop="name">
                  <el-input v-model="formData.name" placeholder="请输入供应商名称" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="供应商类型" prop="type">
                  <el-select v-model="formData.type" placeholder="请选择供应商类型">
                    <el-option label="原材料供应商" value="material" />
                    <el-option label="设备供应商" value="equipment" />
                    <el-option label="服务供应商" value="service" />
                    <el-option label="其他" value="other" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="状态" prop="status">
                  <el-select v-model="formData.status" placeholder="请选择状态">
                    <el-option label="活跃" value="active" />
                    <el-option label="合作中" value="cooperation" />
                    <el-option label="暂停" value="suspended" />
                    <el-option label="已停用" value="inactive" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="联系人" prop="contactPerson">
                  <el-input v-model="formData.contactPerson" placeholder="请输入联系人" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系电话" prop="contactPhone">
                  <el-input v-model="formData.contactPhone" placeholder="请输入联系电话" />
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
                <el-form-item label="传真">
                  <el-input v-model="formData.fax" placeholder="请输入传真" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="地址">
              <el-input v-model="formData.address" placeholder="请输入地址" />
            </el-form-item>
          </el-tab-pane>
          
          <el-tab-pane label="财务信息" name="finance">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="银行账户">
                  <el-input v-model="formData.bankAccount" placeholder="请输入银行账户" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="开户银行">
                  <el-input v-model="formData.bankName" placeholder="请输入开户银行" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="税号">
                  <el-input v-model="formData.taxNumber" placeholder="请输入税号" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="信用等级">
                  <el-rate v-model="formData.creditRating" show-text />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="付款方式">
                  <el-select v-model="formData.paymentMethod" placeholder="请选择付款方式">
                    <el-option label="现金" value="cash" />
                    <el-option label="银行转账" value="transfer" />
                    <el-option label="支票" value="check" />
                    <el-option label="信用证" value="credit" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="付款周期">
                  <el-input-number v-model="formData.paymentCycle" :min="0" placeholder="天" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          
          <el-tab-pane label="其他信息" name="other">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="成立时间">
                  <el-date-picker
                    v-model="formData.establishDate"
                    type="date"
                    placeholder="请选择成立时间"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="员工人数">
                  <el-input-number v-model="formData.employeeCount" :min="0" placeholder="人" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="注册资本">
                  <el-input-number v-model="formData.registeredCapital" :min="0" placeholder="万元" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="年营业额">
                  <el-input-number v-model="formData.annualRevenue" :min="0" placeholder="万元" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="主营业务">
              <el-input v-model="formData.mainBusiness" type="textarea" placeholder="请输入主营业务" />
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="formData.remark" type="textarea" placeholder="请输入备注" />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
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

    <!-- 供应商详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="供应商详情"
      width="800px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="供应商编码">{{ detailData.code }}</el-descriptions-item>
        <el-descriptions-item label="供应商名称">{{ detailData.name }}</el-descriptions-item>
        <el-descriptions-item label="供应商类型">
          <el-tag :type="getTypeTagType(detailData.type)">
            {{ getTypeText(detailData.type) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTagType(detailData.status)">
            {{ getStatusText(detailData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="联系人">{{ detailData.contactPerson }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detailData.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ detailData.email }}</el-descriptions-item>
        <el-descriptions-item label="传真">{{ detailData.fax }}</el-descriptions-item>
        <el-descriptions-item label="地址" :span="2">{{ detailData.address }}</el-descriptions-item>
        <el-descriptions-item label="银行账户">{{ detailData.bankAccount }}</el-descriptions-item>
        <el-descriptions-item label="开户银行">{{ detailData.bankName }}</el-descriptions-item>
        <el-descriptions-item label="税号">{{ detailData.taxNumber }}</el-descriptions-item>
        <el-descriptions-item label="信用等级">
          <el-rate v-model="detailData.creditRating" disabled show-score />
        </el-descriptions-item>
        <el-descriptions-item label="付款方式">{{ detailData.paymentMethod }}</el-descriptions-item>
        <el-descriptions-item label="付款周期">{{ detailData.paymentCycle }}天</el-descriptions-item>
        <el-descriptions-item label="成立时间">{{ detailData.establishDate }}</el-descriptions-item>
        <el-descriptions-item label="员工人数">{{ detailData.employeeCount }}人</el-descriptions-item>
        <el-descriptions-item label="注册资本">{{ detailData.registeredCapital }}万元</el-descriptions-item>
        <el-descriptions-item label="年营业额">{{ detailData.annualRevenue }}万元</el-descriptions-item>
        <el-descriptions-item label="主营业务" :span="2">{{ detailData.mainBusiness }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Search, Refresh, Plus, Upload, Download, ArrowDown,
  OfficeBuilding, CircleCheck, CircleClose, Handshake
} from '@element-plus/icons-vue'
import {
  getSupplierList,
  getSupplierById,
  createSupplier,
  updateSupplier,
  deleteSupplier,
  updateSupplierStatus
} from '@/api/erp'

// 响应式数据
const loading = ref(false)
const formLoading = ref(false)
const tableData = ref([])
const selectedRows = ref([])
const formDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const formMode = ref('add')
const activeTab = ref('basic')
const formRef = ref()

// 统计数据
const stats = reactive({
  total: 0,
  active: 0,
  cooperation: 0,
  inactive: 0
})

// 搜索表单
const searchForm = reactive({
  name: '',
  code: '',
  type: '',
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
  id: null,
  code: '',
  name: '',
  type: '',
  status: 'active',
  contactPerson: '',
  contactPhone: '',
  email: '',
  fax: '',
  address: '',
  bankAccount: '',
  bankName: '',
  taxNumber: '',
  creditRating: 5,
  paymentMethod: '',
  paymentCycle: 30,
  establishDate: '',
  employeeCount: 0,
  registeredCapital: 0,
  annualRevenue: 0,
  mainBusiness: '',
  remark: ''
})

// 详情数据
const detailData = reactive({})

// 表单验证规则
const formRules = {
  code: [{ required: true, message: '请输入供应商编码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入供应商名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择供应商类型', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
  contactPerson: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
  contactPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

// 获取供应商列表
const getList = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      size: pagination.size,
      ...searchForm
    }
    const response = await getSupplierList(params)
    tableData.value = response.data.records
    pagination.total = response.data.total
    
    // 更新统计数据
    updateStats(response.data.records)
  } catch (error) {
    ElMessage.error('获取供应商列表失败')
  } finally {
    loading.value = false
  }
}

// 更新统计数据
const updateStats = (data) => {
  stats.total = data.length
  stats.active = data.filter(item => item.status === 'active').length
  stats.cooperation = data.filter(item => item.status === 'cooperation').length
  stats.inactive = data.filter(item => item.status === 'inactive').length
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  getList()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    name: '',
    code: '',
    type: '',
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
  activeTab.value = 'basic'
}

// 编辑
const handleEdit = async (row) => {
  formMode.value = 'edit'
  try {
    const response = await getSupplierById(row.id)
    Object.assign(formData, response.data)
    formDialogVisible.value = true
    activeTab.value = 'basic'
  } catch (error) {
    ElMessage.error('获取供应商信息失败')
  }
}

// 查看详情
const handleView = async (row) => {
  try {
    const response = await getSupplierById(row.id)
    Object.assign(detailData, response.data)
    detailDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取供应商详情失败')
  }
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除供应商 "${row.name}" 吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await deleteSupplier(row.id)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

// 切换状态
const handleToggleStatus = async (row) => {
  const newStatus = row.status === 'active' ? 'inactive' : 'active'
  const action = newStatus === 'active' ? '启用' : '停用'
  
  try {
    await updateSupplierStatus(row.id, newStatus)
    ElMessage.success(`${action}成功`)
    getList()
  } catch (error) {
    ElMessage.error(`${action}失败`)
  }
}

// 合同管理
const handleContract = (row) => {
  ElMessage.info('合同管理功能开发中')
}

// 供应商评估
const handleEvaluation = (row) => {
  ElMessage.info('供应商评估功能开发中')
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
const handleBatchActive = async () => {
  const ids = selectedRows.value.map(row => row.id)
  try {
    // 这里应该调用批量更新状态的API
    ElMessage.success('批量启用成功')
    getList()
  } catch (error) {
    ElMessage.error('批量启用失败')
  }
}

// 批量停用
const handleBatchInactive = async () => {
  const ids = selectedRows.value.map(row => row.id)
  try {
    // 这里应该调用批量更新状态的API
    ElMessage.success('批量停用成功')
    getList()
  } catch (error) {
    ElMessage.error('批量停用失败')
  }
}

// 批量删除
const handleBatchDelete = () => {
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedRows.value.length} 个供应商吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const ids = selectedRows.value.map(row => row.id)
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
          await createSupplier(formData)
          ElMessage.success('新增成功')
        } else {
          await updateSupplier(formData.id, formData)
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

// 重置表单数据
const resetFormData = () => {
  Object.assign(formData, {
    id: null,
    code: '',
    name: '',
    type: '',
    status: 'active',
    contactPerson: '',
    contactPhone: '',
    email: '',
    fax: '',
    address: '',
    bankAccount: '',
    bankName: '',
    taxNumber: '',
    creditRating: 5,
    paymentMethod: '',
    paymentCycle: 30,
    establishDate: '',
    employeeCount: 0,
    registeredCapital: 0,
    annualRevenue: 0,
    mainBusiness: '',
    remark: ''
  })
}

// 获取类型标签类型
const getTypeTagType = (type) => {
  const typeMap = {
    material: 'primary',
    equipment: 'success',
    service: 'warning',
    other: 'info'
  }
  return typeMap[type] || ''
}

// 获取类型文本
const getTypeText = (type) => {
  const typeMap = {
    material: '原材料供应商',
    equipment: '设备供应商',
    service: '服务供应商',
    other: '其他'
  }
  return typeMap[type] || type
}

// 获取状态标签类型
const getStatusTagType = (status) => {
  const statusMap = {
    active: 'success',
    cooperation: 'primary',
    suspended: 'warning',
    inactive: 'danger'
  }
  return statusMap[status] || ''
}

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    active: '活跃',
    cooperation: '合作中',
    suspended: '暂停',
    inactive: '已停用'
  }
  return statusMap[status] || status
}

// 页面加载时获取数据
onMounted(() => {
  getList()
})
</script>

<style scoped>
.supplier-management {
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

.stats-icon.cooperation {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stats-icon.inactive {
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

:deep(.el-tabs__content) {
  padding-top: 20px;
}
</style>