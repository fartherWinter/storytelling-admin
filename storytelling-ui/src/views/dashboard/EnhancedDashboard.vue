<template>
  <div class="dashboard-container">
    <!-- 顶部控制栏 -->
    <div class="dashboard-header">
      <h2>{{ $t('dashboard.enhancedTitle') }}</h2>
      <div class="dashboard-controls">
        <el-select v-model="timeRange" placeholder="时间范围" size="small">
          <el-option label="今日" value="today"></el-option>
          <el-option label="本周" value="week"></el-option>
          <el-option label="本月" value="month"></el-option>
          <el-option label="本季度" value="quarter"></el-option>
          <el-option label="本年" value="year"></el-option>
        </el-select>
        <el-button type="primary" size="small" @click="refreshData">{{ $t('common.refresh') }}</el-button>
        <el-dropdown trigger="click" @command="handleLanguageChange">
          <el-button size="small">
            {{ currentLanguage === 'zh' ? '中文' : 'English' }}
            <i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="zh">中文</el-dropdown-item>
              <el-dropdown-item command="en">English</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
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
                {{ Math.abs(card.trend) }}% {{ card.trend > 0 ? $t('dashboard.increase') : $t('dashboard.decrease') }}
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-area">
      <!-- 销售趋势图表 -->
      <el-col :xs="24" :md="12">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="chart-header">
              <span>{{ $t('dashboard.salesTrend') }}</span>
              <el-dropdown trigger="click" @command="handleSalesChartTypeChange">
                <span class="el-dropdown-link">
                  {{ $t('dashboard.chartType') }}<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="line">{{ $t('dashboard.lineChart') }}</el-dropdown-item>
                    <el-dropdown-item command="bar">{{ $t('dashboard.barChart') }}</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </template>
          <div class="chart-container" ref="salesChart"></div>
        </el-card>
      </el-col>
      
      <!-- 产品分布图表 -->
      <el-col :xs="24" :md="12">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="chart-header">
              <span>{{ $t('dashboard.productDistribution') }}</span>
            </div>
          </template>
          <div class="chart-container" ref="productChart"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- AI分析预测区域 -->
    <el-card shadow="hover" class="ai-analysis-card">
      <template #header>
        <div class="card-header">
          <span>{{ $t('dashboard.aiAnalysis') }}</span>
          <el-button type="text" @click="refreshAIAnalysis">{{ $t('common.refresh') }}</el-button>
        </div>
      </template>
      <div class="ai-analysis-content">
        <el-row :gutter="20">
          <el-col :xs="24" :md="12">
            <div class="prediction-chart" ref="predictionChart"></div>
          </el-col>
          <el-col :xs="24" :md="12">
            <div class="prediction-insights">
              <h3>{{ $t('dashboard.insights') }}</h3>
              <el-timeline>
                <el-timeline-item
                  v-for="(insight, index) in aiInsights"
                  :key="index"
                  :type="insight.type"
                  :color="insight.color"
                  :timestamp="insight.timestamp"
                >
                  {{ insight.content }}
                </el-timeline-item>
              </el-timeline>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
    
    <!-- 供应链协同平台 -->
    <el-card shadow="hover" class="supply-chain-card">
      <template #header>
        <div class="card-header">
          <span>{{ $t('supplyChain.collaboration') }}</span>
          <el-button type="text" @click="viewSupplyChainDetails">{{ $t('common.more') }}</el-button>
        </div>
      </template>
      <div class="supply-chain-content">
        <el-row :gutter="20">
          <el-col :xs="24" :md="8">
            <div class="partner-status">
              <h3>{{ $t('supplyChain.partnerStatus') }}</h3>
              <div class="status-chart" ref="partnerStatusChart"></div>
            </div>
          </el-col>
          <el-col :xs="24" :md="8">
            <div class="inventory-status">
              <h3>{{ $t('supplyChain.inventoryStatus') }}</h3>
              <div class="status-chart" ref="inventoryStatusChart"></div>
            </div>
          </el-col>
          <el-col :xs="24" :md="8">
            <div class="order-status">
              <h3>{{ $t('supplyChain.orderStatus') }}</h3>
              <div class="status-chart" ref="orderStatusChart"></div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import axios from 'axios'

export default {
  name: 'EnhancedDashboard',
  setup() {
    const { t, locale } = useI18n()
    
    // 引用DOM元素
    const salesChart = ref(null)
    const productChart = ref(null)
    const predictionChart = ref(null)
    const partnerStatusChart = ref(null)
    const inventoryStatusChart = ref(null)
    const orderStatusChart = ref(null)
    
    // 图表实例
    let salesChartInstance = null
    let productChartInstance = null
    let predictionChartInstance = null
    let partnerStatusChartInstance = null
    let inventoryStatusChartInstance = null
    let orderStatusChartInstance = null
    
    // 状态数据
    const timeRange = ref('month')
    const salesChartType = ref('line')
    const currentLanguage = computed(() => locale.value)
    
    // 概览卡片数据
    const overviewCards = reactive([
      {
        title: t('dashboard.totalSales'),
        value: '¥1,234,567',
        trend: 12.5,
        icon: 'el-icon-s-data',
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
        trend: 4.7,
        icon: 'el-icon-user',
        color: '#E6A23C'
      },
      {
        title: t('dashboard.inventoryValue'),
        value: '¥2,345,678',
        trend: -2.1,
        icon: 'el-icon-s-goods',
        color: '#F56C6C'
      }
    ])
    
    // AI分析洞察
    const aiInsights = reactive([
      {
        content: t('dashboard.aiInsight1'),
        timestamp: '2023-06-15',
        type: 'success',
        color: '#67C23A'
      },
      {
        content: t('dashboard.aiInsight2'),
        timestamp: '2023-06-14',
        type: 'warning',
        color: '#E6A23C'
      },
      {
        content: t('dashboard.aiInsight3'),
        timestamp: '2023-06-13',
        type: 'info',
        color: '#909399'
      }
    ])
    
    // 初始化图表
    const initCharts = () => {
      // 销售趋势图表
      salesChartInstance = echarts.init(salesChart.value)
      const salesOption = {
        title: {
          text: t('dashboard.salesTrend')
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: [t('dashboard.sales'), t('dashboard.orders')]
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['1月', '2月', '3月', '4月', '5月', '6月']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: t('dashboard.sales'),
            type: salesChartType.value,
            data: [150, 230, 224, 218, 135, 147]
          },
          {
            name: t('dashboard.orders'),
            type: salesChartType.value,
            data: [80, 122, 119, 123, 90, 92]
          }
        ]
      }
      salesChartInstance.setOption(salesOption)
      
      // 产品分布图表
      productChartInstance = echarts.init(productChart.value)
      const productOption = {
        title: {
          text: t('dashboard.productDistribution'),
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: ['电子产品', '服装', '食品', '家具', '其他']
        },
        series: [
          {
            name: t('dashboard.productCategory'),
            type: 'pie',
            radius: '55%',
            center: ['50%', '60%'],
            data: currentLanguage.value === 'zh' ? [
              {value: 335, name: '电子产品'},
              {value: 310, name: '服装'},
              {value: 234, name: '食品'},
              {value: 135, name: '家具'},
              {value: 1548, name: '其他'}
            ] : [
              {value: 335, name: 'Electronics'},
              {value: 310, name: 'Clothing'},
              {value: 234, name: 'Food'},
              {value: 135, name: 'Furniture'},
              {value: 1548, name: 'Others'}
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
      productChartInstance.setOption(productOption)
      
      // AI预测图表
      predictionChartInstance = echarts.init(predictionChart.value)
      const predictionOption = {
        title: {
          text: t('dashboard.salesPrediction')
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            label: {
              backgroundColor: '#6a7985'
            }
          }
        },
        legend: {
          data: [t('dashboard.actualSales'), t('dashboard.predictedSales')]
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
            boundaryGap: false,
            data: currentLanguage.value === 'zh' ? 
              ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月'] : 
              ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug']
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: [
          {
            name: t('dashboard.actualSales'),
            type: 'line',
            stack: 'Total',
            areaStyle: {},
            emphasis: {
              focus: 'series'
            },
            data: [120, 132, 101, 134, 90, 230, 210]
          },
          {
            name: t('dashboard.predictedSales'),
            type: 'line',
            stack: 'Total',
            areaStyle: {},
            emphasis: {
              focus: 'series'
            },
            data: [220, 182, 191, 234, 290, 330, 310, 350],
            lineStyle: {
              type: 'dashed'
            }
          }
        ]
      }
      predictionChartInstance.setOption(predictionOption)
      
      // 供应链合作伙伴状态图表
      partnerStatusChartInstance = echarts.init(partnerStatusChart.value)
      const partnerStatusOption = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '5%',
          left: 'center'
        },
        series: [
          {
            name: t('supplyChain.partnerStatus'),
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              {value: 35, name: t('supplyChain.active')},
              {value: 20, name: t('supplyChain.inactive')},
              {value: 15, name: t('supplyChain.pending')}
            ]
          }
        ]
      }
      partnerStatusChartInstance.setOption(partnerStatusOption)
      
      // 库存状态图表
      inventoryStatusChartInstance = echarts.init(inventoryStatusChart.value)
      const inventoryStatusOption = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
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
            data: ['原材料', '在产品', '成品'],
            axisTick: {
              alignWithLabel: true
            }
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: [
          {
            name: t('supplyChain.inventoryValue'),
            type: 'bar',
            barWidth: '60%',
            data: [
              {value: 10, itemStyle: {color: '#91cc75'}},
              {value: 52, itemStyle: {color: '#fac858'}},
              {value: 200, itemStyle: {color: '#5470c6'}}
            ]
          }
        ]
      }
      inventoryStatusChartInstance.setOption(inventoryStatusOption)
      
      // 订单状态图表
      orderStatusChartInstance = echarts.init(orderStatusChart.value)
      const orderStatusOption = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
        },
        series: [
          {
            name: t('supplyChain.orderStatus'),
            type: 'pie',
            radius: '50%',
            data: [
              {value: 1048, name: t('supplyChain.completed')},
              {value: 735, name: t('supplyChain.processing')},
              {value: 580, name: t('supplyChain.pending')},
              {value: 484, name: t('supplyChain.cancelled')}
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
      orderStatusChartInstance.setOption(orderStatusOption)
    }
    
    // 窗口大小变化时重新调整图表大小
    const handleResize = () => {
      salesChartInstance && salesChartInstance.resize()
      productChartInstance && productChartInstance.resize()
      predictionChartInstance && predictionChartInstance.resize()
      partnerStatusChartInstance && partnerStatusChartInstance.resize()
      inventoryStatusChartInstance && inventoryStatusChartInstance.resize()
      orderStatusChartInstance && orderStatusChartInstance.resize()
    }
    
    // 刷新数据
    const refreshData = async () => {
      try {
        ElMessage.success(t('dashboard.dataRefreshed'))
        // 这里可以添加实际的API调用来刷新数据
        // 重新渲染图表
        initCharts()
      } catch (error) {
        console.error('刷新数据失败:', error)
        ElMessage.error(t('dashboard.refreshFailed'))
      }
    }
    
    // 刷新AI分析
    const refreshAIAnalysis = async () => {
      try {
        ElMessage.success(t('dashboard.aiAnalysisRefreshed'))
        // 这里可以添加实际的API调用来获取AI分析数据
      } catch (error) {
        console.error('刷新AI分析失败:', error)
        ElMessage.error(t('dashboard.refreshFailed'))
      }
    }
    
    // 查看供应链详情
    const viewSupplyChainDetails = () => {
      // 跳转到供应链详情页面
      window.location.href = '#/supplyChain'
    }
    
    // 处理销售图表类型变更
    const handleSalesChartTypeChange = (type) => {
      salesChartType.value = type
      if (salesChartInstance) {
        const option = salesChartInstance.getOption()
        option.series[0].type = type
        option.series[1].type = type
        salesChartInstance.setOption(option)
      }
    }
    
    // 处理语言变更
    const handleLanguageChange = (lang) => {
      locale.value = lang
      // 重新初始化图表以应用新语言
      setTimeout(() => {
        initCharts()
      }, 100)
    }
    
    // 监听语言变化
    watch(locale, () => {
      // 更新卡片数据
      overviewCards[0].title = t('dashboard.totalSales')
      overviewCards[1].title = t('dashboard.totalOrders')
      overviewCards[2].title = t('dashboard.totalCustomers')
      overviewCards[3].title = t('dashboard.inventoryValue')
      
      // 更新AI洞察
      aiInsights[0].content = t('dashboard.aiInsight1')
      aiInsights[1].content = t('dashboard.aiInsight2')
      aiInsights[2].content = t('dashboard.aiInsight3')
      
      // 重新初始化图表
      initCharts()
    })
    
    // 组件挂载时初始化
    onMounted(() => {
      initCharts()
      // 添加窗口大小变化监听
      window.addEventListener('resize', handleResize)
    })
    
    return {
      timeRange,
      salesChart,
      productChart,
      predictionChart,
      partnerStatusChart,
      inventoryStatusChart,
      orderStatusChart,
      overviewCards,
      aiInsights,
      currentLanguage,
      refreshData,
      refreshAIAnalysis,
      viewSupplyChainDetails,
      handleSalesChartTypeChange,
      handleLanguageChange
    }
  }
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
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
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
  display: flex;
  align-items: center;
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

.ai-analysis-card,
.supply-chain-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.prediction-chart,
.status-chart {
  height: 250px;
}

.prediction-insights {
  padding: 0 15px;
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
    flex-wrap: wrap;
  }
  
  .chart-container,
  .prediction-chart,
  .status-chart {
    height: 200px;
  }
  
  .prediction-insights {
    margin-top: 20px;
  }
}
</style>