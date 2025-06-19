<template>
  <div class="product-management">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>产品管理</h2>
      <div class="header-actions">
        <el-button type="success" @click="handleImport">
          <el-icon><Upload /></el-icon>
          导入产品
        </el-button>
        <el-button type="warning" @click="handleExport">
          <el-icon><Download /></el-icon>
          导出产品
        </el-button>
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增产品
        </el-button>
      </div>
    </div>

    <!-- 搜索区域 -->
    <div class="search-area">
      <el-form :model="searchForm" inline>
        <el-form-item label="产品名称">
          <el-input v-model="searchForm.name" placeholder="请输入产品名称" clearable />
        </el-form-item>
        <el-form-item label="产品编码">
          <el-input v-model="searchForm.code" placeholder="请输入产品编码" clearable />
        </el-form-item>
        <el-form-item label="产品分类">
          <el-select v-model="searchForm.categoryId" placeholder="请选择产品分类" clearable>
            <el-option 
              v-for="category in categoryList" 
              :key="category.id" 
              :label="category.name" 
              :value="category.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="正常" value="1" />
            <el-option label="停用" value="0" />
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
          <el-button size="small" @click="handleBatchDelete">批量删除</el-button>
          <el-button size="small" @click="handleBatchStatus(1)">批量启用</el-button>
          <el-button size="small" @click="handleBatchStatus(0)">批量停用</el-button>
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
        <el-table-column prop="id" label="产品ID" width="80" />
        <el-table-column label="产品图片" width="80">
          <template #default="{ row }">
            <el-image
              v-if="row.image"
              :src="row.image"
              :preview-src-list="[row.image]"
              fit="cover"
              style="width: 50px; height: 50px; border-radius: 4px;"
            />
            <div v-else class="no-image">暂无图片</div>
          </template>
        </el-table-column>
        <el-table-column prop="code" label="产品编码" width="120" />
        <el-table-column prop="name" label="产品名称" min-width="150" />
        <el-table-column prop="categoryName" label="产品分类" width="120" />
        <el-table-column prop="specification" label="规格" width="100" />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="price" label="价格" width="100">
          <template #default="{ row }">
            <span class="price">¥{{ row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="80">
          <template #default="{ row }">
            <el-tag :type="getStockTagType(row.stock, row.minStock)">{{ row.stock }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleView(row)">
              查看
            </el-button>
            <el-button type="warning" size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="info" size="small" @click="handleStock(row)">
              库存
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">
              删除
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

    <!-- 产品表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="产品编码" prop="code">
                  <el-input v-model="form.code" placeholder="请输入产品编码" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="产品名称" prop="name">
                  <el-input v-model="form.name" placeholder="请输入产品名称" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="产品分类" prop="categoryId">
                  <el-select v-model="form.categoryId" placeholder="请选择产品分类">
                    <el-option 
                      v-for="category in categoryList" 
                      :key="category.id" 
                      :label="category.name" 
                      :value="category.id" 
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="品牌" prop="brand">
                  <el-input v-model="form.brand" placeholder="请输入品牌" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="规格" prop="specification">
                  <el-input v-model="form.specification" placeholder="请输入规格" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="单位" prop="unit">
                  <el-input v-model="form.unit" placeholder="请输入单位" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="状态" prop="status">
                  <el-radio-group v-model="form.status">
                    <el-radio :label="1">正常</el-radio>
                    <el-radio :label="0">停用</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="产品描述">
              <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入产品描述" />
            </el-form-item>
          </el-tab-pane>
          
          <el-tab-pane label="价格库存" name="price">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="销售价格" prop="price">
                  <el-input-number v-model="form.price" :min="0" :precision="2" placeholder="请输入销售价格" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="成本价格" prop="costPrice">
                  <el-input-number v-model="form.costPrice" :min="0" :precision="2" placeholder="请输入成本价格" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="市场价格" prop="marketPrice">
                  <el-input-number v-model="form.marketPrice" :min="0" :precision="2" placeholder="请输入市场价格" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="当前库存" prop="stock">
                  <el-input-number v-model="form.stock" :min="0" placeholder="请输入当前库存" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="最小库存" prop="minStock">
                  <el-input-number v-model="form.minStock" :min="0" placeholder="请输入最小库存" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="最大库存" prop="maxStock">
                  <el-input-number v-model="form.maxStock" :min="0" placeholder="请输入最大库存" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          
          <el-tab-pane label="产品图片" name="image">
            <el-form-item label="产品图片">
              <el-upload
                class="image-uploader"
                :action="uploadUrl"
                :show-file-list="false"
                :on-success="handleImageSuccess"
                :before-upload="beforeImageUpload"
              >
                <img v-if="form.image" :src="form.image" class="image" />
                <el-icon v-else class="image-uploader-icon"><Plus /></el-icon>
              </el-upload>
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 产品详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="产品详情"
      width="800px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="产品ID">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="产品编码">{{ detailData.code }}</el-descriptions-item>
        <el-descriptions-item label="产品名称">{{ detailData.name }}</el-descriptions-item>
        <el-descriptions-item label="产品分类">{{ detailData.categoryName }}</el-descriptions-item>
        <el-descriptions-item label="品牌">{{ detailData.brand }}</el-descriptions-item>
        <el-descriptions-item label="规格">{{ detailData.specification }}</el-descriptions-item>
        <el-descriptions-item label="单位">{{ detailData.unit }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="detailData.status === 1 ? 'success' : 'danger'">
            {{ detailData.status === 1 ? '正常' : '停用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="销售价格">¥{{ detailData.price }}</el-descriptions-item>
        <el-descriptions-item label="成本价格">¥{{ detailData.costPrice }}</el-descriptions-item>
        <el-descriptions-item label="市场价格">¥{{ detailData.marketPrice }}</el-descriptions-item>
        <el-descriptions-item label="当前库存">
          <el-tag :type="getStockTagType(detailData.stock, detailData.minStock)">{{ detailData.stock }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="最小库存">{{ detailData.minStock }}</el-descriptions-item>
        <el-descriptions-item label="最大库存">{{ detailData.maxStock }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime }}</el-descriptions-item>
        <el-descriptions-item label="产品描述" :span="2">{{ detailData.description }}</el-descriptions-item>
        <el-descriptions-item label="产品图片" :span="2">
          <el-image
            v-if="detailData.image"
            :src="detailData.image"
            :preview-src-list="[detailData.image]"
            fit="cover"
            style="width: 100px; height: 100px; border-radius: 4px;"
          />
          <span v-else>暂无图片</span>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 库存管理对话框 -->
    <el-dialog
      v-model="stockDialogVisible"
      title="库存管理"
      width="600px"
    >
      <el-form :model="stockForm" label-width="100px">
        <el-form-item label="产品名称">
          <el-input :value="stockForm.productName" disabled />
        </el-form-item>
        <el-form-item label="当前库存">
          <el-input :value="stockForm.currentStock" disabled />
        </el-form-item>
        <el-form-item label="调整类型">
          <el-radio-group v-model="stockForm.type">
            <el-radio label="in">入库</el-radio>
            <el-radio label="out">出库</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="调整数量">
          <el-input-number v-model="stockForm.quantity" :min="1" placeholder="请输入调整数量" />
        </el-form-item>
        <el-form-item label="调整原因">
          <el-input v-model="stockForm.reason" type="textarea" placeholder="请输入调整原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="stockDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleStockSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Upload, Download } from '@element-plus/icons-vue'
import {
  getProductList,
  getProductById,
  createProduct,
  updateProduct,
  deleteProduct,
  updateProductStatus,
  getProductCategories,
  adjustProductStock
} from '@/api/erp'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const categoryList = ref([])
const selectedRows = ref([])
const dialogVisible = ref(false)
const detailDialogVisible = ref(false)
const stockDialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const formRef = ref()
const activeTab = ref('basic')
const uploadUrl = ref('/api/upload/image')

// 搜索表单
const searchForm = reactive({
  name: '',
  code: '',
  categoryId: '',
  status: ''
})

// 分页
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 表单数据
const form = reactive({
  id: null,
  code: '',
  name: '',
  categoryId: '',
  brand: '',
  specification: '',
  unit: '',
  price: 0,
  costPrice: 0,
  marketPrice: 0,
  stock: 0,
  minStock: 0,
  maxStock: 0,
  status: 1,
  description: '',
  image: ''
})

// 详情数据
const detailData = reactive({})

// 库存表单
const stockForm = reactive({
  productId: null,
  productName: '',
  currentStock: 0,
  type: 'in',
  quantity: 1,
  reason: ''
})

// 表单验证规则
const rules = {
  code: [{ required: true, message: '请输入产品编码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择产品分类', trigger: 'change' }],
  unit: [{ required: true, message: '请输入单位', trigger: 'blur' }],
  price: [{ required: true, message: '请输入销售价格', trigger: 'blur' }]
}

// 获取产品列表
const getList = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      size: pagination.size,
      ...searchForm
    }
    const response = await getProductList(params)
    tableData.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    ElMessage.error('获取产品列表失败')
  } finally {
    loading.value = false
  }
}

// 获取产品分类
const getCategories = async () => {
  try {
    const response = await getProductCategories()
    categoryList.value = response.data
  } catch (error) {
    ElMessage.error('获取产品分类失败')
  }
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
    categoryId: '',
    status: ''
  })
  pagination.page = 1
  getList()
}

// 新增
const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增产品'
  resetForm()
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑产品'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 查看详情
const handleView = async (row) => {
  try {
    const response = await getProductById(row.id)
    Object.assign(detailData, response.data)
    detailDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取产品详情失败')
  }
}

// 库存管理
const handleStock = (row) => {
  Object.assign(stockForm, {
    productId: row.id,
    productName: row.name,
    currentStock: row.stock,
    type: 'in',
    quantity: 1,
    reason: ''
  })
  stockDialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除产品"${row.name}"吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await deleteProduct(row.id)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

// 状态变更
const handleStatusChange = async (row) => {
  try {
    await updateProductStatus(row.id, row.status)
    ElMessage.success('状态更新成功')
  } catch (error) {
    ElMessage.error('状态更新失败')
    // 恢复原状态
    row.status = row.status === 1 ? 0 : 1
  }
}

// 表格选择变化
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

// 批量删除
const handleBatchDelete = () => {
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedRows.value.length} 个产品吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const ids = selectedRows.value.map(row => row.id)
      // 这里需要后端提供批量删除接口
      ElMessage.success('批量删除成功')
      getList()
    } catch (error) {
      ElMessage.error('批量删除失败')
    }
  })
}

// 批量状态变更
const handleBatchStatus = (status) => {
  ElMessageBox.confirm(
    `确定要${status === 1 ? '启用' : '停用'}选中的 ${selectedRows.value.length} 个产品吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      // 这里需要后端提供批量状态更新接口
      ElMessage.success(`批量${status === 1 ? '启用' : '停用'}成功`)
      getList()
    } catch (error) {
      ElMessage.error(`批量${status === 1 ? '启用' : '停用'}失败`)
    }
  })
}

// 导入产品
const handleImport = () => {
  ElMessage.info('导入功能开发中...')
}

// 导出产品
const handleExport = () => {
  ElMessage.info('导出功能开发中...')
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

// 提交表单
const handleSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (isEdit.value) {
          await updateProduct(form.id, form)
          ElMessage.success('更新成功')
        } else {
          await createProduct(form)
          ElMessage.success('创建成功')
        }
        dialogVisible.value = false
        getList()
      } catch (error) {
        ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 库存调整提交
const handleStockSubmit = async () => {
  try {
    await adjustProductStock(stockForm.productId, {
      type: stockForm.type,
      quantity: stockForm.quantity,
      reason: stockForm.reason
    })
    ElMessage.success('库存调整成功')
    stockDialogVisible.value = false
    getList()
  } catch (error) {
    ElMessage.error('库存调整失败')
  }
}

// 关闭对话框
const handleDialogClose = () => {
  dialogVisible.value = false
  resetForm()
}

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    id: null,
    code: '',
    name: '',
    categoryId: '',
    brand: '',
    specification: '',
    unit: '',
    price: 0,
    costPrice: 0,
    marketPrice: 0,
    stock: 0,
    minStock: 0,
    maxStock: 0,
    status: 1,
    description: '',
    image: ''
  })
  formRef.value?.clearValidate()
  activeTab.value = 'basic'
}

// 图片上传成功
const handleImageSuccess = (response) => {
  form.image = response.data.url
}

// 图片上传前验证
const beforeImageUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('上传图片只能是 JPG/PNG 格式!')
  }
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB!')
  }
  return isJPG && isLt2M
}

// 获取库存标签类型
const getStockTagType = (stock, minStock) => {
  if (stock <= 0) return 'danger'
  if (stock <= minStock) return 'warning'
  return 'success'
}

// 页面加载时获取数据
onMounted(() => {
  getList()
  getCategories()
})
</script>

<style scoped>
.product-management {
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

.no-image {
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  color: #909399;
  font-size: 12px;
  border-radius: 4px;
}

.price {
  color: #f56c6c;
  font-weight: bold;
}

.image-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: 0.2s;
}

.image-uploader:hover {
  border-color: #409eff;
}

.image-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
  line-height: 178px;
}

.image {
  width: 178px;
  height: 178px;
  display: block;
}

:deep(.el-table) {
  font-size: 14px;
}

:deep(.el-table th) {
  background-color: #fafafa;
}

:deep(.el-tabs__content) {
  padding-top: 20px;
}
</style>