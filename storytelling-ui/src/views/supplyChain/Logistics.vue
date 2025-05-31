<template>
  <div class="logistics-container">
    <div class="page-header">
      <h2>物流追踪</h2>
      <p>实时追踪货物运输状态和物流信息</p>
    </div>
    
    <div class="content-area">
      <!-- 物流概览 -->
      <div class="overview-cards">
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon size="24" color="#409EFF"><Truck /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-title">运输中</div>
              <div class="card-value">{{ inTransitCount }}</div>
            </div>
          </div>
        </el-card>
        
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon size="24" color="#67C23A"><CircleCheck /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-title">已送达</div>
              <div class="card-value">{{ deliveredCount }}</div>
            </div>
          </div>
        </el-card>
        
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon size="24" color="#E6A23C"><Clock /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-title">待发货</div>
              <div class="card-value">{{ pendingCount }}</div>
            </div>
          </div>
        </el-card>
        
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon size="24" color="#F56C6C"><Warning /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-title">异常订单</div>
              <div class="card-value">{{ exceptionCount }}</div>
            </div>
          </div>
        </el-card>
      </div>
      
      <!-- 物流订单列表 -->
      <el-card>
        <template #header>
          <div class="card-header">
            <span>物流订单</span>
            <div class="header-actions">
              <el-select v-model="statusFilter" placeholder="筛选状态" style="width: 120px; margin-right: 10px;" clearable>
                <el-option label="全部" value="" />
                <el-option label="待发货" value="pending" />
                <el-option label="运输中" value="in-transit" />
                <el-option label="已送达" value="delivered" />
                <el-option label="异常" value="exception" />
              </el-select>
              <el-input
                v-model="searchText"
                placeholder="搜索订单号"
                style="width: 200px; margin-right: 10px;"
                clearable
              >
                <template #prefix>
                  <el-icon><Search /></el-icon>
                </template>
              </el-input>
              <el-button type="primary" @click="createOrder">
                <el-icon><Plus /></el-icon>
                新建订单
              </el-button>
            </div>
          </div>
        </template>
        
        <el-table :data="filteredOrders" style="width: 100%">
          <el-table-column prop="orderNo" label="订单号" width="150" />
          <el-table-column prop="destination" label="目的地" width="150" />
          <el-table-column prop="carrier" label="承运商" width="120" />
          <el-table-column prop="trackingNo" label="运单号" width="150" />
          <el-table-column label="状态" width="120" align="center">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="estimatedDelivery" label="预计送达" width="150" />
          <el-table-column prop="createTime" label="创建时间" width="150" />
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button size="small" @click="trackOrder(scope.row)">追踪</el-button>
              <el-button size="small" type="primary" @click="viewDetails(scope.row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
    
    <!-- 物流追踪弹窗 -->
    <el-dialog v-model="trackingVisible" title="物流追踪" width="600px">
      <div v-if="selectedOrder" class="tracking-content">
        <div class="order-info">
          <h4>订单信息</h4>
          <p><strong>订单号：</strong>{{ selectedOrder.orderNo }}</p>
          <p><strong>运单号：</strong>{{ selectedOrder.trackingNo }}</p>
          <p><strong>承运商：</strong>{{ selectedOrder.carrier }}</p>
          <p><strong>目的地：</strong>{{ selectedOrder.destination }}</p>
        </div>
        
        <div class="tracking-timeline">
          <h4>物流轨迹</h4>
          <el-timeline>
            <el-timeline-item
              v-for="(track, index) in trackingHistory"
              :key="index"
              :timestamp="track.time"
              :type="index === 0 ? 'primary' : 'info'"
            >
              {{ track.description }}
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  Truck, 
  CircleCheck, 
  Clock, 
  Warning, 
  Search, 
  Plus 
} from '@element-plus/icons-vue'

const searchText = ref('')
const statusFilter = ref('')
const trackingVisible = ref(false)
const selectedOrder = ref(null)

const orders = ref([
  {
    id: 1,
    orderNo: 'LO2024001',
    destination: '北京市朝阳区',
    carrier: '顺丰速运',
    trackingNo: 'SF1234567890',
    status: 'in-transit',
    estimatedDelivery: '2024-01-16',
    createTime: '2024-01-15 09:00'
  },
  {
    id: 2,
    orderNo: 'LO2024002',
    destination: '上海市浦东新区',
    carrier: '中通快递',
    trackingNo: 'ZT9876543210',
    status: 'delivered',
    estimatedDelivery: '2024-01-15',
    createTime: '2024-01-14 14:30'
  },
  {
    id: 3,
    orderNo: 'LO2024003',
    destination: '广州市天河区',
    carrier: '圆通速递',
    trackingNo: 'YT5555666677',
    status: 'pending',
    estimatedDelivery: '2024-01-17',
    createTime: '2024-01-15 16:20'
  },
  {
    id: 4,
    orderNo: 'LO2024004',
    destination: '深圳市南山区',
    carrier: '韵达快递',
    trackingNo: 'YD1111222233',
    status: 'exception',
    estimatedDelivery: '2024-01-16',
    createTime: '2024-01-14 11:45'
  }
])

const trackingHistory = ref([
  {
    time: '2024-01-15 14:30',
    description: '货物已到达北京分拨中心，正在分拣'
  },
  {
    time: '2024-01-15 10:20',
    description: '货物已从上海转运中心发出'
  },
  {
    time: '2024-01-15 09:00',
    description: '货物已从发货地揽收'
  }
])

const filteredOrders = computed(() => {
  let filtered = orders.value
  
  if (statusFilter.value) {
    filtered = filtered.filter(order => order.status === statusFilter.value)
  }
  
  if (searchText.value) {
    filtered = filtered.filter(order => 
      order.orderNo.toLowerCase().includes(searchText.value.toLowerCase())
    )
  }
  
  return filtered
})

const inTransitCount = computed(() => {
  return orders.value.filter(order => order.status === 'in-transit').length
})

const deliveredCount = computed(() => {
  return orders.value.filter(order => order.status === 'delivered').length
})

const pendingCount = computed(() => {
  return orders.value.filter(order => order.status === 'pending').length
})

const exceptionCount = computed(() => {
  return orders.value.filter(order => order.status === 'exception').length
})

const getStatusText = (status) => {
  const statusMap = {
    'pending': '待发货',
    'in-transit': '运输中',
    'delivered': '已送达',
    'exception': '异常'
  }
  return statusMap[status] || status
}

const getStatusType = (status) => {
  const typeMap = {
    'pending': 'warning',
    'in-transit': 'primary',
    'delivered': 'success',
    'exception': 'danger'
  }
  return typeMap[status] || 'info'
}

const createOrder = () => {
  ElMessage.info('新建订单功能开发中...')
}

const trackOrder = (order) => {
  selectedOrder.value = order
  trackingVisible.value = true
}

const viewDetails = (order) => {
  ElMessage.info(`查看订单详情: ${order.orderNo}`)
}

onMounted(() => {
  // 初始化数据
})
</script>

<style scoped>
.logistics-container {
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

.tracking-content {
  padding: 10px 0;
}

.order-info {
  margin-bottom: 20px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 4px;
}

.order-info h4 {
  margin: 0 0 10px 0;
  color: #303133;
}

.order-info p {
  margin: 5px 0;
  color: #606266;
}

.tracking-timeline h4 {
  margin: 0 0 15px 0;
  color: #303133;
}
</style>