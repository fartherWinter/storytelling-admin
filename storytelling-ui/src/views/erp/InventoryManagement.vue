<template>
  <div class="inventory-management">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>库存管理</h2>
      <div class="header-actions">
        <el-button type="warning" @click="handleStockAlert">
          <el-icon><Warning /></el-icon>
          库存预警
        </el-button>
        <el-button type="info" @click="handleStockCheck">
          <el-icon><Document /></el-icon>
          库存盘点
        </el-button>
        <el-button type="success" @click="handleStockTransfer">
          <el-icon><Switch /></el-icon>
          库存调拨
        </el-button>
        <el-button type="primary" @click="handleStockAdjust">
          <el-icon><Edit /></el-icon>
          库存调整
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
                <el-icon><Box /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-value">{{ stats.totalProducts }}</div>
                <div class="stats-label">总产品数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon normal">
                <el-icon><CircleCheck /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-value">{{ stats.normalStock }}</div>
                <div class="stats-label">正常库存</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon warning">
                <el-icon><Warning /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-value">{{ stats.lowStock }}</div>
                <div class="stats-label">库存不足</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon danger">
                <el-icon><CircleClose /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-value">{{ stats.outOfStock }}</div>
                <div class="stats-label">缺货</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <div class="search-area">
      <el-form :model="searchForm" inline>
        <el-form-item label="产品名称">
          <el-input v-model="searchForm.productName" placeholder="请输入产品名称" clearable />
        </el-form-item>
        <el-form-item label="产品编码">
          <el-input v-model="searchForm.productCode" placeholder="请输入产品编码" clearable />
        </el-form-item>
        <el-form-item label="仓库">
          <el-select v-model="searchForm.warehouseId" placeholder="请选择仓库" clearable>
            <el-option 
              v-for="warehouse in warehouseList" 
              :key="warehouse.id" 
              :label="warehouse.name" 
              :value="warehouse.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="库存状态">
          <el-select v-model="searchForm.stockStatus" placeholder="请选择库存状态" clearable>
            <el-option label="正常" value="normal" />
            <el-option label="不足" value="low" />
            <el-option label="缺货" value="out" />
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
        <el-table-column prop="productCode" label="产品编码" width="120" />
        <el-table-column prop="productName" label="产品名称" min-width="150" />
        <el-table-column prop="warehouseName" label="仓库" width="120" />
        <el-table-column prop="currentStock" label="当前库存" width="100">
          <template #default="{ row }">
            <el-tag :type="getStockTagType(row.currentStock, row.minStock, row.maxStock)">
              {{ row.currentStock }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="minStock" label="最小库存" width="100" />
        <el-table-column prop="maxStock" label="最大库存" width="100" />
        <el-table-column prop="availableStock" label="可用库存" width="100" />
        <el-table-column prop="reservedStock" label="预留库存" width="100" />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="lastUpdateTime" label="最后更新" width="150" />
        <el-table-column label="库存状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.currentStock, row.minStock)">
              {{ getStatusText(row.currentStock, row.minStock) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleView(row)">
              详情
            </el-button>
            <el-button type="warning" size="small" @click="handleAdjust(row)">
              调整
            </el-button>
            <el-button type="info" size="small" @click="handleHistory(row)">
              记录
            </el-button>
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

    <!-- 库存调整对话框 -->
    <el-dialog
      v-model="adjustDialogVisible"
      title="库存调整"
      width="600px"
    >
      <el-form
        ref="adjustFormRef"
        :model="adjustForm"
        :rules="adjustRules"
        label-width="100px"
      >
        <el-form-item label="产品名称">
          <el-input :value="adjustForm.productName" disabled />
        </el-form-item>
        <el-form-item label="当前库存">
          <el-input :value="adjustForm.currentStock" disabled />
        </el-form-item>
        <el-form-item label="调整类型" prop="type">
          <el-radio-group v-model="adjustForm.type">
            <el-radio label="in">入库</el-radio>
            <el-radio label="out">出库</el-radio>
            <el-radio label="set">设置</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="调整数量" prop="quantity" v-if="adjustForm.type !== 'set'">
          <el-input-number v-model="adjustForm.quantity" :min="1" placeholder="请输入调整数量" />
        </el-form-item>
        <el-form-item label="设置数量" prop="quantity" v-if="adjustForm.type === 'set'">
          <el-input-number v-model="adjustForm.quantity" :min="0" placeholder="请输入设置数量" />
        </el-form-item>
        <el-form-item label="调整原因" prop="reason">
          <el-select v-model="adjustForm.reason" placeholder="请选择调整原因">
            <el-option label="采购入库" value="purchase" />
            <el-option label="销售出库" value="sale" />
            <el-option label="盘点调整" value="check" />
            <el-option label="损耗" value="loss" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="adjustForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="adjustDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAdjustSubmit" :loading="adjustLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 库存详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="库存详情"
      width="800px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="产品编码">{{ detailData.productCode }}</el-descriptions-item>
        <el-descriptions-item label="产品名称">{{ detailData.productName }}</el-descriptions-item>
        <el-descriptions-item label="仓库">{{ detailData.warehouseName }}</el-descriptions-item>
        <el-descriptions-item label="库位">{{ detailData.location }}</el-descriptions-item>
        <el-descriptions-item label="当前库存">
          <el-tag :type="getStockTagType(detailData.currentStock, detailData.minStock, detailData.maxStock)">
            {{ detailData.currentStock }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="可用库存">{{ detailData.availableStock }}</el-descriptions-item>
        <el-descriptions-item label="预留库存">{{ detailData.reservedStock }}</el-descriptions-item>
        <el-descriptions-item label="在途库存">{{ detailData.inTransitStock }}</el-descriptions-item>
        <el-descriptions-item label="最小库存">{{ detailData.minStock }}</el-descriptions-item>
        <el-descriptions-item label="最大库存">{{ detailData.maxStock }}</el-descriptions-item>
        <el-descriptions-item label="安全库存">{{ detailData.safetyStock }}</el-descriptions-item>
        <el-descriptions-item label="单位">{{ detailData.unit }}</el-descriptions-item>
        <el-descriptions-item label="最后更新时间">{{ detailData.lastUpdateTime }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 库存记录对话框 -->
    <el-dialog
      v-model="historyDialogVisible"
      title="库存变动记录"
      width="1000px"
    >
      <el-table :data="historyData" border stripe>
        <el-table-column prop="operationType" label="操作类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getOperationTagType(row.operationType)">
              {{ getOperationText(row.operationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="beforeStock" label="变动前" width="100" />
        <el-table-column prop="changeQuantity" label="变动数量" width="100">
          <template #default="{ row }">
            <span :class="row.changeQuantity > 0 ? 'positive' : 'negative'">
              {{ row.changeQuantity > 0 ? '+' : '' }}{{ row.changeQuantity }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="afterStock" label="变动后" width="100" />
        <el-table-column prop="reason" label="变动原因" width="120" />
        <el-table-column prop="operator" label="操作人" width="100" />
        <el-table-column prop="operationTime" label="操作时间" width="150" />
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
      </el-table>
    </el-dialog>

    <!-- 库存预警对话框 -->
    <el-dialog
      v-model="alertDialogVisible"
      title="库存预警"
      width="800px"
    >
      <el-table :data="alertData" border stripe>
        <el-table-column prop="productCode" label="产品编码" width="120" />
        <el-table-column prop="productName" label="产品名称" min-width="150" />
        <el-table-column prop="warehouseName" label="仓库" width="120" />
        <el-table-column prop="currentStock" label="当前库存" width="100">
          <template #default="{ row }">
            <el-tag type="danger">{{ row.currentStock }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="minStock" label="最小库存" width="100" />
        <el-table-column prop="alertLevel" label="预警级别" width="100">
          <template #default="{ row }">
            <el-tag :type="getAlertTagType(row.alertLevel)">
              {{ getAlertText(row.alertLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleQuickAdjust(row)">
              快速补货
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 库存盘点对话框 -->
    <el-dialog
      v-model="checkDialogVisible"
      title="库存盘点"
      width="600px"
    >
      <el-form :model="checkForm" label-width="100px">
        <el-form-item label="盘点名称">
          <el-input v-model="checkForm.name" placeholder="请输入盘点名称" />
        </el-form-item>
        <el-form-item label="盘点仓库">
          <el-select v-model="checkForm.warehouseId" placeholder="请选择盘点仓库">
            <el-option 
              v-for="warehouse in warehouseList" 
              :key="warehouse.id" 
              :label="warehouse.name" 
              :value="warehouse.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="盘点类型">
          <el-radio-group v-model="checkForm.type">
            <el-radio label="full">全盘</el-radio>
            <el-radio label="partial">抽盘</el-radio>
            <el-radio label="cycle">循环盘点</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="计划时间">
          <el-date-picker
            v-model="checkForm.planTime"
            type="datetime"
            placeholder="请选择计划时间"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="checkForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="checkDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleCheckSubmit">
            创建盘点
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 库存调拨对话框 -->
    <el-dialog
      v-model="transferDialogVisible"
      title="库存调拨"
      width="600px"
    >
      <el-form :model="transferForm" label-width="100px">
        <el-form-item label="调拨单号">
          <el-input v-model="transferForm.transferNo" placeholder="系统自动生成" disabled />
        </el-form-item>
        <el-form-item label="调出仓库">
          <el-select v-model="transferForm.fromWarehouseId" placeholder="请选择调出仓库">
            <el-option 
              v-for="warehouse in warehouseList" 
              :key="warehouse.id" 
              :label="warehouse.name" 
              :value="warehouse.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="调入仓库">
          <el-select v-model="transferForm.toWarehouseId" placeholder="请选择调入仓库">
            <el-option 
              v-for="warehouse in warehouseList" 
              :key="warehouse.id" 
              :label="warehouse.name" 
              :value="warehouse.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="调拨原因">
          <el-input v-model="transferForm.reason" placeholder="请输入调拨原因" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="transferForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="transferDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleTransferSubmit">
            创建调拨
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
  Search, Refresh, Warning, Document, Switch, Edit, 
  Box, CircleCheck, CircleClose, Plus 
} from '@element-plus/icons-vue'
import {
  getInventoryList,
  getInventoryById,
  adjustInventory,
  getInventoryHistory,
  getInventoryAlerts,
  createStockCheck,
  createStockTransfer,
  getWarehouses
} from '@/api/erp'

// 响应式数据
const loading = ref(false)
const adjustLoading = ref(false)
const tableData = ref([])
const warehouseList = ref([])
const selectedRows = ref([])
const adjustDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const historyDialogVisible = ref(false)
const alertDialogVisible = ref(false)
const checkDialogVisible = ref(false)
const transferDialogVisible = ref(false)
const adjustFormRef = ref()

// 统计数据
const stats = reactive({
  totalProducts: 0,
  normalStock: 0,
  lowStock: 0,
  outOfStock: 0
})

// 搜索表单
const searchForm = reactive({
  productName: '',
  productCode: '',
  warehouseId: '',
  stockStatus: ''
})

// 分页
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 调整表单
const adjustForm = reactive({
  inventoryId: null,
  productName: '',
  currentStock: 0,
  type: 'in',
  quantity: 1,
  reason: '',
  remark: ''
})

// 详情数据
const detailData = reactive({})

// 历史记录数据
const historyData = ref([])

// 预警数据
const alertData = ref([])

// 盘点表单
const checkForm = reactive({
  name: '',
  warehouseId: '',
  type: 'full',
  planTime: '',
  remark: ''
})

// 调拨表单
const transferForm = reactive({
  transferNo: '',
  fromWarehouseId: '',
  toWarehouseId: '',
  reason: '',
  remark: ''
})

// 调整表单验证规则
const adjustRules = {
  type: [{ required: true, message: '请选择调整类型', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }],
  reason: [{ required: true, message: '请选择调整原因', trigger: 'change' }]
}

// 获取库存列表
const getList = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      size: pagination.size,
      ...searchForm
    }
    const response = await getInventoryList(params)
    tableData.value = response.data.records
    pagination.total = response.data.total
    
    // 更新统计数据
    updateStats(response.data.records)
  } catch (error) {
    ElMessage.error('获取库存列表失败')
  } finally {
    loading.value = false
  }
}

// 获取仓库列表
const getWarehouseList = async () => {
  try {
    const response = await getWarehouses()
    warehouseList.value = response.data
  } catch (error) {
    ElMessage.error('获取仓库列表失败')
  }
}

// 更新统计数据
const updateStats = (data) => {
  stats.totalProducts = data.length
  stats.normalStock = data.filter(item => item.currentStock > item.minStock).length
  stats.lowStock = data.filter(item => item.currentStock <= item.minStock && item.currentStock > 0).length
  stats.outOfStock = data.filter(item => item.currentStock <= 0).length
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  getList()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    productName: '',
    productCode: '',
    warehouseId: '',
    stockStatus: ''
  })
  pagination.page = 1
  getList()
}

// 查看详情
const handleView = async (row) => {
  try {
    const response = await getInventoryById(row.id)
    Object.assign(detailData, response.data)
    detailDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取库存详情失败')
  }
}

// 库存调整
const handleAdjust = (row) => {
  Object.assign(adjustForm, {
    inventoryId: row.id,
    productName: row.productName,
    currentStock: row.currentStock,
    type: 'in',
    quantity: 1,
    reason: '',
    remark: ''
  })
  adjustDialogVisible.value = true
}

// 查看历史记录
const handleHistory = async (row) => {
  try {
    const response = await getInventoryHistory(row.id)
    historyData.value = response.data
    historyDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取库存记录失败')
  }
}

// 库存预警
const handleStockAlert = async () => {
  try {
    const response = await getInventoryAlerts()
    alertData.value = response.data
    alertDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取库存预警失败')
  }
}

// 库存盘点
const handleStockCheck = () => {
  Object.assign(checkForm, {
    name: '',
    warehouseId: '',
    type: 'full',
    planTime: '',
    remark: ''
  })
  checkDialogVisible.value = true
}

// 库存调拨
const handleStockTransfer = () => {
  Object.assign(transferForm, {
    transferNo: 'TF' + Date.now(),
    fromWarehouseId: '',
    toWarehouseId: '',
    reason: '',
    remark: ''
  })
  transferDialogVisible.value = true
}

// 快速库存调整
const handleStockAdjust = () => {
  ElMessage.info('请选择具体产品进行库存调整')
}

// 快速补货
const handleQuickAdjust = (row) => {
  Object.assign(adjustForm, {
    inventoryId: row.id,
    productName: row.productName,
    currentStock: row.currentStock,
    type: 'in',
    quantity: row.minStock - row.currentStock,
    reason: 'purchase',
    remark: '库存预警自动补货'
  })
  adjustDialogVisible.value = true
}

// 表格选择变化
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
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

// 提交调整
const handleAdjustSubmit = () => {
  adjustFormRef.value.validate(async (valid) => {
    if (valid) {
      adjustLoading.value = true
      try {
        await adjustInventory(adjustForm.inventoryId, {
          type: adjustForm.type,
          quantity: adjustForm.quantity,
          reason: adjustForm.reason,
          remark: adjustForm.remark
        })
        ElMessage.success('库存调整成功')
        adjustDialogVisible.value = false
        getList()
      } catch (error) {
        ElMessage.error('库存调整失败')
      } finally {
        adjustLoading.value = false
      }
    }
  })
}

// 提交盘点
const handleCheckSubmit = async () => {
  try {
    await createStockCheck(checkForm)
    ElMessage.success('盘点任务创建成功')
    checkDialogVisible.value = false
  } catch (error) {
    ElMessage.error('盘点任务创建失败')
  }
}

// 提交调拨
const handleTransferSubmit = async () => {
  try {
    await createStockTransfer(transferForm)
    ElMessage.success('调拨单创建成功')
    transferDialogVisible.value = false
  } catch (error) {
    ElMessage.error('调拨单创建失败')
  }
}

// 获取库存标签类型
const getStockTagType = (current, min, max) => {
  if (current <= 0) return 'danger'
  if (current <= min) return 'warning'
  if (current >= max) return 'info'
  return 'success'
}

// 获取状态标签类型
const getStatusTagType = (current, min) => {
  if (current <= 0) return 'danger'
  if (current <= min) return 'warning'
  return 'success'
}

// 获取状态文本
const getStatusText = (current, min) => {
  if (current <= 0) return '缺货'
  if (current <= min) return '不足'
  return '正常'
}

// 获取操作标签类型
const getOperationTagType = (type) => {
  const typeMap = {
    in: 'success',
    out: 'warning',
    set: 'info',
    transfer: 'primary'
  }
  return typeMap[type] || ''
}

// 获取操作文本
const getOperationText = (type) => {
  const typeMap = {
    in: '入库',
    out: '出库',
    set: '设置',
    transfer: '调拨'
  }
  return typeMap[type] || type
}

// 获取预警标签类型
const getAlertTagType = (level) => {
  const levelMap = {
    high: 'danger',
    medium: 'warning',
    low: 'info'
  }
  return levelMap[level] || ''
}

// 获取预警文本
const getAlertText = (level) => {
  const levelMap = {
    high: '严重',
    medium: '警告',
    low: '提醒'
  }
  return levelMap[level] || level
}

// 页面加载时获取数据
onMounted(() => {
  getList()
  getWarehouseList()
})
</script>

<style scoped>
.inventory-management {
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

.stats-icon.normal {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stats-icon.warning {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stats-icon.danger {
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

.positive {
  color: #67c23a;
  font-weight: bold;
}

.negative {
  color: #f56c6c;
  font-weight: bold;
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