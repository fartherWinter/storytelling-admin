<template>
  <div class="supply-chain-container">
    <div class="page-header">
      <h2>{{ $t('supplyChain.title') }}</h2>
    </div>
    
    <!-- 功能卡片区域 -->
    <el-row :gutter="20" class="feature-cards">
      <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="(feature, index) in features" :key="index">
        <el-card shadow="hover" :body-style="{ padding: '20px' }" @click="navigateToFeature(feature.route)">
          <div class="feature-card">
            <div class="feature-icon" :style="{backgroundColor: feature.color}">
              <i :class="feature.icon"></i>
            </div>
            <div class="feature-info">
              <h3>{{ feature.title }}</h3>
              <p>{{ feature.description }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 协作平台概览 -->
    <el-card class="platform-overview" shadow="hover">
      <template #header>
        <div class="card-header">
          <span>{{ $t('supplyChain.collaboration') }}</span>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :xs="24" :md="12">
          <div class="overview-chart" ref="collaborationChart"></div>
        </el-col>
        <el-col :xs="24" :md="12">
          <div class="partner-list">
            <h3>{{ $t('supplyChain.partnerManagement') }}</h3>
            <el-table :data="partners" style="width: 100%" :max-height="300">
              <el-table-column prop="name" :label="$t('supplyChain.partnerManagement')" width="180"></el-table-column>
              <el-table-column prop="type" :label="$t('common.type')" width="100"></el-table-column>
              <el-table-column prop="status" :label="$t('supplyChain.partnerStatus')">
                <template #default="scope">
                  <el-tag :type="getStatusType(scope.row.status)">{{ getLocalizedStatus(scope.row.status) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column :label="$t('common.operation')" width="120">
                <template #default>
                  <el-button type="text" size="small">{{ $t('common.view') }}</el-button>
                  <el-button type="text" size="small">{{ $t('common.contact') }}</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-col>
      </el-row>
    </el-card>
    
    <!-- 实时物流追踪 -->
    <el-card class="logistics-tracking" shadow="hover">
      <template #header>
        <div class="card-header">
          <span>{{ $t('supplyChain.tracking') }}</span>
        </div>
      </template>
      <div class="map-container" ref="mapContainer">
        <!-- 地图将在这里渲染 -->
        <div class="map-placeholder">物流追踪地图将在这里显示</div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import * as echarts from 'echarts'

// 国际化
const { t } = useI18n()
const router = useRouter()

// 图表引用
const collaborationChart = ref(null)
const mapContainer = ref(null)

// 功能列表
const features = reactive([
  {
    title: t('supplyChain.supplier'),
    description: '管理供应商信息、评估和合同',
    icon: 'el-icon-s-custom',
    color: '#409EFF',
    route: '/supply-chain/supplier'
  },
  {
    title: t('supplyChain.inventory'),
    description: '库存管理、预警和分析',
    icon: 'el-icon-s-goods',
    color: '#67C23A',
    route: '/supply-chain/inventory'
  },
  {
    title: t('supplyChain.logistics'),
    description: '物流管理、路线优化',
    icon: 'el-icon-s-promotion',
    color: '#E6A23C',
    route: '/supply-chain/logistics'
  },
  {
    title: t('supplyChain.procurement'),
    description: '采购计划、订单管理',
    icon: 'el-icon-s-order',
    color: '#F56C6C',
    route: '/supply-chain/procurement'
  },
  {
    title: t('supplyChain.production'),
    description: '生产计划、进度跟踪',
    icon: 'el-icon-s-cooperation',
    color: '#909399',
    route: '/supply-chain/production'
  },
  {
    title: t('supplyChain.distribution'),
    description: '配送管理、区域规划',
    icon: 'el-icon-s-flag',
    color: '#9B59B6',
    route: '/supply-chain/distribution'
  },
  {
    title: t('supplyChain.forecast'),
    description: '需求预测、趋势分析',
    icon: 'el-icon-s-data',
    color: '#3498DB',
    route: '/supply-chain/forecast'
  },
  {
    title: t('supplyChain.alert'),
    description: '异常预警、风险管理',
    icon: 'el-icon-warning',
    color: '#E74C3C',
    route: '/supply-chain/alert'
  }
])

// 合作伙伴数据
const partners = reactive([
  { name: '供应商A公司', type: '原材料供应商', status: 'active' },
  { name: '物流B公司', type: '物流服务商', status: 'active' },
  { name: '分销C公司', type: '分销商', status: 'inactive' },
  { name: '零售D公司', type: '零售商', status: 'pending' },
  { name: '制造E公司', type: '制造商', status: 'processing' }
])

// 获取状态对应的类型
const getStatusType = (status) => {
  const statusMap = {
    'active': 'success',
    'inactive': 'info',
    'pending': 'warning',
    'completed': 'success',
    'processing': 'primary',
    'cancelled': 'danger'
  }
  return statusMap[status] || 'info'
}

// 获取本地化的状态文本
const getLocalizedStatus = (status) => {
  return t(`supplyChain.${status}`)
}


// 导航到功能页面
const navigateToFeature = (route) => {
  router.push(route)
}

// 初始化协作图表
const initCollaborationChart = () => {
  if (collaborationChart.value) {
    const chart = echarts.init(collaborationChart.value)
    
    const option = {
      title: {
        text: '供应链协作状态',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          name: '协作类型',
          type: 'pie',
          radius: '50%',
          data: [
            { value: 35, name: '采购协作' },
            { value: 20, name: '库存协作' },
            { value: 15, name: '物流协作' },
            { value: 25, name: '预测协作' },
            { value: 5, name: '其他' }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    }
    
    chart.setOption(option)
    
    // 响应式调整
    window.addEventListener('resize', () => {
      chart.resize()
    })
  }
}

// 初始化地图
const initMap = () => {
  // 这里应该集成地图API，如高德地图、百度地图等
  // 示例中仅展示占位符
}

onMounted(() => {
  initCollaborationChart()
  initMap()
})
</script>

<style scoped>
.supply-chain-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.feature-cards {
  margin-bottom: 20px;
}

.feature-card {
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  color: white;
  font-size: 24px;
}

.feature-info h3 {
  margin: 0 0 5px 0;
  font-size: 16px;
}

.feature-info p {
  margin: 0;
  font-size: 12px;
  color: #666;
}

.platform-overview,
.logistics-tracking {
  margin-bottom: 20px;
}

.overview-chart {
  height: 300px;
}

.map-container {
  height: 400px;
  background-color: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
}

.map-placeholder {
  color: #909399;
  font-size: 16px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .feature-cards .el-col {
    margin-bottom: 15px;
  }
  
  .overview-chart,
  .partner-list {
    margin-bottom: 20px;
  }
  
  .map-container {
    height: 300px;
  }
}
</style>