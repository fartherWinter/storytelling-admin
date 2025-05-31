<template>
  <div class="inventory-container">
    <div class="page-header">
      <h2>库存管理</h2>
      <p>实时监控和管理库存状态</p>
    </div>
    
    <div class="content-area">
      <!-- 库存概览卡片 -->
      <div class="overview-cards">
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon size="24" color="#409EFF"><Box /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-title">总库存</div>
              <div class="card-value">{{ totalInventory }}</div>
            </div>
          </div>
        </el-card>
        
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon size="24" color="#67C23A"><CircleCheck /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-title">正常库存</div>
              <div class="card-value">{{ normalInventory }}</div>
            </div>
          </div>
        </el-card>
        
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon size="24" color="#E6A23C"><Warning /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-title">低库存预警</div>
              <div class="card-value">{{ lowStockItems }}</div>
            </div>
          </div>
        </el-card>
        
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon size="24" color="#F56C6C"><CircleClose /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-title">缺货商品</div>
              <div class="card-value">{{ outOfStockItems }}</div>
            </div>
          </div>
        </el-card>
      </div>
      
      <!-- 库存列表 -->
      <el-card>
        <template #header>
          <div class="card-header">
            <span>库存明细</span>
            <div class="header-actions">
              <el-input
                v-model="searchText"
                placeholder="搜索商品名称或编码"
                style="width: 200px; margin-right: 10px;"
                clearable
              >
                <template #prefix>
                  <el-icon><Search /></el-icon>
                </template>
              </el-input>
              <el-button type="primary" @click="addInventory">
                <el-icon><Plus /></el-icon>
                添加库存
              </el-button>
            </div>
          </div>
        </template>
        
        <el-table :data="filteredInventory" style="width: 100%">
          <el-table-column prop="code" label="商品编码" width="120" />
          <el-table-column prop="name" label="商品名称" width="200" />
          <el-table-column prop="category" label="分类" width="120" />
          <el-table-column prop="currentStock" label="当前库存" width="100" align="center" />
          <el-table-column prop="minStock" label="最低库存" width="100" align="center" />
          <el-table-column prop="maxStock" label="最高库存" width="100" align="center" />
          <el-table-column label="库存状态" width="120" align="center">
            <template #default="scope">
              <el-tag :type="getStockStatusType(scope.row)">
                {{ getStockStatus(scope.row) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="lastUpdated" label="最后更新" width="150" />
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button size="small" @click="adjustStock(scope.row)">调整库存</el-button>
              <el-button size="small" type="primary" @click="viewDetails(scope.row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  Box, 
  CircleCheck, 
  Warning, 
  CircleClose, 
  Search, 
  Plus 
} from '@element-plus/icons-vue'

const searchText = ref('')

const inventory = ref([
  {
    id: 1,
    code: 'P001',
    name: '原材料A',
    category: '原材料',
    currentStock: 150,
    minStock: 50,
    maxStock: 500,
    lastUpdated: '2024-01-15 10:30'
  },
  {
    id: 2,
    code: 'P002',
    name: '零部件B',
    category: '零部件',
    currentStock: 25,
    minStock: 30,
    maxStock: 200,
    lastUpdated: '2024-01-15 09:15'
  },
  {
    id: 3,
    code: 'P003',
    name: '成品C',
    category: '成品',
    currentStock: 0,
    minStock: 10,
    maxStock: 100,
    lastUpdated: '2024-01-14 16:45'
  },
  {
    id: 4,
    code: 'P004',
    name: '包装材料D',
    category: '包装',
    currentStock: 300,
    minStock: 100,
    maxStock: 1000,
    lastUpdated: '2024-01-15 11:20'
  }
])

const filteredInventory = computed(() => {
  if (!searchText.value) return inventory.value
  return inventory.value.filter(item => 
    item.name.toLowerCase().includes(searchText.value.toLowerCase()) ||
    item.code.toLowerCase().includes(searchText.value.toLowerCase())
  )
})

const totalInventory = computed(() => {
  return inventory.value.reduce((sum, item) => sum + item.currentStock, 0)
})

const normalInventory = computed(() => {
  return inventory.value.filter(item => 
    item.currentStock >= item.minStock && item.currentStock > 0
  ).length
})

const lowStockItems = computed(() => {
  return inventory.value.filter(item => 
    item.currentStock < item.minStock && item.currentStock > 0
  ).length
})

const outOfStockItems = computed(() => {
  return inventory.value.filter(item => item.currentStock === 0).length
})

const getStockStatus = (item) => {
  if (item.currentStock === 0) return '缺货'
  if (item.currentStock < item.minStock) return '低库存'
  if (item.currentStock > item.maxStock) return '超库存'
  return '正常'
}

const getStockStatusType = (item) => {
  if (item.currentStock === 0) return 'danger'
  if (item.currentStock < item.minStock) return 'warning'
  if (item.currentStock > item.maxStock) return 'info'
  return 'success'
}

const addInventory = () => {
  ElMessage.info('添加库存功能开发中...')
}

const adjustStock = (item) => {
  ElMessage.info(`调整库存: ${item.name}`)
}

const viewDetails = (item) => {
  ElMessage.info(`查看详情: ${item.name}`)
}

onMounted(() => {
  // 初始化数据
})
</script>

<style scoped>
.inventory-container {
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
</style>