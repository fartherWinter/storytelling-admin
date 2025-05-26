<template>
  <div class="dashboard-container">
    <div class="dashboard-header">
      <h2>{{ $t('dashboard.title') }}</h2>
      <div class="dashboard-controls">
        <el-select v-model="timeRange" placeholder="时间范围" size="small">
          <el-option label="今日" value="today"></el-option>
          <el-option label="本周" value="week"></el-option>
          <el-option label="本月" value="month"></el-option>
          <el-option label="本季度" value="quarter"></el-option>
          <el-option label="本年" value="year"></el-option>
        </el-select>
        <el-button type="primary" size="small" @click="refreshData">刷新数据</el-button>
      </div>
    </div>
    
    <!-- 数据概览卡片 -->
    <el-row :gutter="20" class="data-overview">
      <el-col :xs="24" :sm="12" :md="6" v-for="(card, index) in overviewCards" :key="index">
        <el-card shadow="hover" :body-style="{ padding: '20px' }">
          <div class="card-content">
            <div class="card-icon" :style="{backgroundColor: card.color}">
              <i :class="card.icon"></i>
            </div>
            <div class="card-info">
              <div class="card-title">{{ card.title }}</div>
              <div class="card-value">{{ card.value }}</div>
              <div class="card-trend" :class="card.trend > 0 ? 'up' : 'down'">
                <i :class="card.trend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                {{ Math.abs(card.trend) }}% {{ card.trend > 0 ? '增长' : '下降' }}
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-area">
      <el-col :xs="24" :md="12">
        <el-card shadow="hover" class="chart-card">
          <div slot="header" class="chart-header">
            <span>{{ $t('dashboard.salesTrend') }}</span>
            <el-dropdown trigger="click" @command="handleChartTypeChange">
              <span class="el-dropdown-link">
                图表类型<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="line">折线图</el-dropdown-item>
                <el-dropdown-item command="bar">柱状图</el-dropdown-item>
                <el-dropdown-item command="area">面积图</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
          <div class="chart-container" ref="salesChart"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="12">
        <el-card shadow="hover" class="chart-card">
          <div slot="header" class="chart-header">
            <span>{{ $t('dashboard.productDistribution') }}</span>
          </div>
          <div class="chart-container" ref="productChart"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="chart-area">
      <el-col :xs="24" :md="12">
        <el-card shadow="hover" class="chart-card">
          <div slot="header" class="chart-header">
            <span>{{ $t('dashboard.customerAnalysis') }}</span>
          </div>
          <div class="chart-container" ref="customerChart"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="12">
        <el-card shadow="hover" class="chart-card">
          <div slot="header" class="chart-header">
            <span>{{ $t('dashboard.supplyChainStatus') }}</span>
          </div>
          <div class="chart-container" ref="supplyChainChart"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- AI分析预测区域 -->
    <el-card shadow="hover" class="ai-analysis">
      <div slot="header" class="ai-header">
        <span>{{ $t('dashboard.aiAnalysis') }}</span>
        <el-tooltip content="基于历史数据和AI算法的预测分析" placement="top">
          <i class="el-icon-question"></i>
        </el-tooltip>
      </div>
      <div class="ai-content">
        <div class="ai-insights">
          <h4>{{ $t('dashboard.insights') }}</h4>
          <ul>
            <li v-for="(insight, index) in aiInsights" :key="index">
              <i class="el-icon-info"></i> {{ insight }}
            </li>
          </ul>
        </div>
        <div class="ai-predictions">
          <h4>{{ $t('dashboard.predictions') }}</h4>
          <div class="prediction-chart" ref="predictionChart"></div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, computed } from 'vue'
import * as echarts from 'echarts'
import { useI18n } from 'vue-i18n'

// 国际化
const { t } = useI18n()

// 状态变量
const timeRange = ref('month')
const salesChart = ref(null)
const productChart = ref(null)
const customerChart = ref(null)
const supplyChainChart = ref(null)
const predictionChart = ref(null)

// 图表实例
const chartInstances = reactive({
  sales: null,
  product: null,
  customer: null,
  supplyChain: null,
  prediction: null
})

// 数据概览卡片
const overviewCards = reactive([
  {
    title: t('dashboard.totalSales'),
    value: '¥1,234,567',
    trend: 12.5,
    icon: 'el-icon-money',
    color: '#409EFF'
  },
  {
    title: t('dashboard.totalOrders'),
    value: '1,234',
    trend: 8.3,
    icon: 'el-icon-s-order',
    color: '#67C23A'
  },
  {
    title: t('dashboard.totalCustomers'),
    value: '5,678',
    trend: 5.2,
    icon: 'el-icon-user',
    color: '#E6A23C'
  },
  {
    title: t('dashboard.inventoryValue'),
    value: '¥987,654',
    trend: -3.7,
    icon: 'el-icon-s-goods',
    color: '#F56C6C'
  }
])

// AI分析洞察
const aiInsights = reactive([
  '根据历史数据分析，预计下个月销售额将增长15%',
  '客户A的订单频率正在下降，建议进行客户关怀',
  '产品B的库存周转率低于平均水平，建议调整采购计划',
  '新市场区域C显示出强劲的增长潜力，建议增加营销投入'
])

// 监听时间范围变化
watch(timeRange, (newValue) => {
  refreshData()
})

// 刷新数据
const refreshData = () => {
  // 这里应该调用API获取最新数据
  // 模拟数据更新
  setTimeout(() => {
    initCharts()
  }, 500)
}

// 初始化所有图表
const initCharts = () => {
  initSalesChart()
  initProductChart()
  initCustomerChart()
  initSupplyChainChart()
  initPredictionChart()
}

// 初始化销售趋势图表
const initSalesChart = (type = 'line') => {
  if (!salesChart.value) return
  
  // 销毁旧图表实例
  if (chartInstances.sales) {
    chartInstances.sales.dispose()
  }
  
  // 创建新图表实例
  chartInstances.sales = echarts.init(salesChart.value)
  
  // 模拟数据
  const months = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
  const salesData = [120, 132, 101, 134, 90, 230, 210, 182, 191, 234, 290, 330]
  
  // 图表配置
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: months
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      data: salesData,
      type: type,
      smooth: true,
      areaStyle: type === 'area' ? {} : null,
      name: '销售额'
    }]
  }
  
  // 设置图表选项并渲染
  chartInstances.sales.setOption(option)
}

// 初始化产品分布图表
const initProductChart = () => {
  if (!productChart.value) return
  
  // 销毁旧图表实例
  if (chartInstances.product) {
    chartInstances.product.dispose()
  }
  
  // 创建新图表实例
  chartInstances.product = echarts.init(productChart.value)
  
  // 模拟数据
  const productData = [
    { value: 335, name: '产品A' },
    { value: 310, name: '产品B' },
    { value: 234, name: '产品C' },
    { value: 135, name: '产品D' },
    { value: 154, name: '产品E' }
  ]
  
  // 图表配置
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      right: 10,
      top: 'center',
      data: productData.map(item => item.name)
    },
    series: [
      {
        name: '产品销售',
        type: 'pie',
        radius: ['50%', '70%'],
        avoidLabelOverlap: false,
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '16',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: productData
      }
    ]
  }
  
  // 设置图表选项并渲染
  chartInstances.product.setOption(option)
}

// 初始化客户分析图表
const initCustomerChart = () => {
  if (!customerChart.value) return
  
  // 销毁旧图表实例
  if (chartInstances.customer) {
    chartInstances.customer.dispose()
  }
  
  // 创建新图表实例
  chartInstances.customer = echarts.init(customerChart.value)
  
  // 模拟数据
  const customerTypes = ['新客户', '老客户', 'VIP客户', '流失客户']
  const customerData = [30, 40, 20, 10]
  
  // 图表配置
  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      bottom: '5%',
      left: 'center'
    },
    series: [
      {
        name: '客户类型',
        type: 'pie',
        radius: '50%',
        data: customerTypes.map((type, index) => ({
          value: customerData[index],
          name: type
        })),
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
  
  // 设置图表选项并渲染
  chartInstances.customer.setOption(option)
}

// 初始化供应链状态图表
const initSupplyChainChart = () => {
  if (!supplyChainChart.value) return
  
  // 销毁旧图表实例
  if (chartInstances.supplyChain) {
    chartInstances.supplyChain.dispose()
  }
  
  // 创建新图表实例
  chartInstances.supplyChain = echarts.init(supplyChainChart.value)
  
  // 模拟数据
  const categories = ['原材料', '生产', '仓储', '物流', '销售']
  const normalData = [80, 85, 90, 78, 82]
  const warningData = [20, 15, 10, 22, 18]
  
  // 图表配置
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['正常', '警告']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: [
      {
        type: 'category',
        data: categories
      }
    ],
    yAxis: [
      {
        type: 'value',
        max: 100
      }
    ],
    series: [
      {
        name: '正常',
        type: 'bar',
        stack: 'total',
        emphasis: {
          focus: 'series'
        },
        data: normalData,
        itemStyle: {
          color: '#67C23A'
        }
      },
      {
        name: '警告',
        type: 'bar',
        stack: 'total',
        emphasis: {
          focus: 'series'
        },
        data: warningData,
        itemStyle: {
          color: '#F56C6C'
        }
      }
    ]
  }
  
  // 设置图表选项并渲染
  chartInstances.supplyChain.setOption(option)
}

// 初始化AI预测图表
const initPredictionChart = () => {
  if (!predictionChart.value) return
  
  // 销毁旧图表实例
  if (chartInstances.prediction) {
    chartInstances.prediction.dispose()
  }
  
  // 创建新图表实例
  chartInstances.prediction = echarts.init(predictionChart.value)
  
  // 模拟数据
  const months = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
  const historicalData = [120, 132, 101, 134, 90, 230, 210, 182, 191, 234, 290, 330]
  const predictedData = [null, null, null, null, null, null, null, null, null, 234, 290, 330, 350, 370, 390]
  
  // 图表配置
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['历史数据', 'AI预测']
    },
    xAxis: {
      type: 'category',
      data: [...months, '1月', '2月', '3月']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '历史数据',
        type: 'line',
        data: historicalData,
        smooth: true
      },
      {
        name: 'AI预测',
        type: 'line',
        data: predictedData,
        smooth: true,
        lineStyle: {
          type: 'dashed'
        },
        itemStyle: {
          color: '#F56C6C'
        }
      }
    ]
  }
  
  // 设置图表选项并渲染
  chartInstances.prediction.setOption(option)
}

// 处理图表类型变更
const handleChartTypeChange = (type) => {
  initSalesChart(type)
}

// 组件挂载时初始化图表
onMounted(() => {
  // 初始化所有图表
  setTimeout(() => {
    initCharts()
  }, 100)
  
  // 监听窗口大小变化，调整图表大小
  window.addEventListener('resize', handleResize)
})

// 处理窗口大小变化
const handleResize = () => {
  Object.values(chartInstances).forEach(chart => {
    if (chart) {
      chart.resize()
    }
  })
}
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.dashboard-controls {
  display: flex;
  gap: 10px;
}

.data-overview {
  margin-bottom: 20px;
}

.card-content {
  display: flex;
  align-items: center;
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 15px;
}

.card-icon i {
  font-size: 24px;
  color: white;
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
  margin-bottom: 5px;
}

.card-trend {
  font-size: 12px;
}

.card-trend.up {
  color: #67C23A;
}

.card-trend.down {
  color: #F56C6C;
}

.chart-area {
  margin-bottom: 20px;
}

.chart-card {
  margin-bottom: 20px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  height: 300px;
}

.ai-analysis {
  margin-bottom: 20px;
}

.ai-header {
  display: flex;
  align-items: center;
  gap: 10px;
}

.ai-content {
  display: flex;
  flex-wrap: wrap;
}

.ai-insights {
  flex: 1;
  min-width: 300px;
}

.ai-insights ul {
  padding-left: 20px;
}

.ai-insights li {
  margin-bottom: 10px;
  line-height: 1.5;
}

.ai-predictions {
  flex: 1;
  min-width: 300px;
}

.prediction-chart {
  height: 300px;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .dashboard-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .dashboard-controls {
    margin-top: 10px;
    width: 100%;
  }
  
  .el-select {
    width: 100%;
  }
  
  .card-content {
    flex-direction: column;
    text-align: center;
  }
  
  .card-icon {
    margin: 0 auto 15px;
  }
  
  .ai-content {
    flex-direction: column;
  }
  
  .chart-container,
  .prediction-chart {
    height: 250px;
  }
}
</style>