<template>
  <div class="report-preview-container">
    <div class="preview-header">
      <h2>{{ templateData.templateName }}</h2>
      <p v-if="templateData.description">{{ templateData.description }}</p>
    </div>
    
    <!-- 参数表单 -->
    <div class="params-form" v-if="!previewMode && parsedParamConfig.length > 0">
      <el-form :model="paramValues" label-width="100px" size="small">
        <el-row :gutter="20">
          <el-col :span="8" v-for="(param, index) in parsedParamConfig" :key="index">
            <el-form-item :label="param.label" :prop="param.name">
              <!-- 文本参数 -->
              <el-input 
                v-if="param.type === 'text'" 
                v-model="paramValues[param.name]" 
                :placeholder="`请输入${param.label}`"
              ></el-input>
              
              <!-- 数字参数 -->
              <el-input-number 
                v-else-if="param.type === 'number'" 
                v-model="paramValues[param.name]" 
                :placeholder="`请输入${param.label}`"
                style="width: 100%"
              ></el-input-number>
              
              <!-- 日期参数 -->
              <el-date-picker 
                v-else-if="param.type === 'date'" 
                v-model="paramValues[param.name]" 
                type="date" 
                :placeholder="`请选择${param.label}`"
                style="width: 100%"
              ></el-date-picker>
              
              <!-- 选择器参数 -->
              <el-select 
                v-else-if="param.type === 'select'" 
                v-model="paramValues[param.name]" 
                :placeholder="`请选择${param.label}`"
                style="width: 100%"
              >
                <el-option 
                  v-for="option in param.options" 
                  :key="option.value" 
                  :label="option.label" 
                  :value="option.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item>
          <el-button type="primary" @click="handleGenerateReport">生成报表</el-button>
          <el-button @click="resetParams">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 报表内容 -->
    <div class="report-content" v-loading="loading">
      <!-- 表格报表 -->
      <div v-if="templateData.templateType === 'table' || templateData.templateType === 'mixed'" class="table-report">
        <el-table 
          :data="reportData" 
          border 
          style="width: 100%"
          :header-cell-style="getHeaderStyle()"
        >
          <el-table-column 
            v-for="column in parsedColumnConfig" 
            :key="column.field"
            :prop="column.field"
            :label="column.title"
            :width="column.width"
            :align="column.align"
            v-if="column.visible"
          >
          </el-table-column>
        </el-table>
      </div>
      
      <!-- 图表报表 -->
      <div v-if="templateData.templateType === 'chart' || templateData.templateType === 'mixed'" class="chart-report">
        <div ref="chartContainer" :style="getChartStyle()"></div>
      </div>
      
      <!-- 无数据提示 -->
      <el-empty v-if="!loading && reportData.length === 0" description="暂无数据"></el-empty>
    </div>
    
    <!-- 导出选项 -->
    <div class="export-options" v-if="!previewMode && reportData.length > 0">
      <el-divider>导出选项</el-divider>
      <el-form :inline="true">
        <el-form-item label="导出格式">
          <el-select v-model="exportFormat" placeholder="请选择导出格式">
            <el-option label="Excel" value="xlsx"></el-option>
            <el-option label="PDF" value="pdf"></el-option>
            <el-option label="CSV" value="csv"></el-option>
            <el-option label="HTML" value="html"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="exportReport">导出报表</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import { generateReport, downloadReport } from '@/api/report'

// 定义props
const props = defineProps({
  templateData: {
    type: Object,
    required: true
  },
  previewMode: {
    type: Boolean,
    default: false
  }
})

// 状态变量
const loading = ref(false)
const reportData = ref([])
const exportFormat = ref('xlsx')
const chartInstance = ref(null)
const chartContainer = ref(null)
const paramValues = reactive({})
const generatedReportId = ref(null)

// 解析参数配置
const parsedParamConfig = computed(() => {
  if (!props.templateData.paramConfig) return []
  try {
    return typeof props.templateData.paramConfig === 'string' 
      ? JSON.parse(props.templateData.paramConfig) 
      : props.templateData.paramConfig
  } catch (e) {
    console.error('解析参数配置失败', e)
    return []
  }
})

// 解析列配置
const parsedColumnConfig = computed(() => {
  if (!props.templateData.columnConfig) return []
  try {
    return typeof props.templateData.columnConfig === 'string' 
      ? JSON.parse(props.templateData.columnConfig) 
      : props.templateData.columnConfig
  } catch (e) {
    console.error('解析列配置失败', e)
    return []
  }
})

// 解析样式配置
const parsedStyleConfig = computed(() => {
  if (!props.templateData.styleConfig) return {}
  try {
    return typeof props.templateData.styleConfig === 'string' 
      ? JSON.parse(props.templateData.styleConfig) 
      : props.templateData.styleConfig
  } catch (e) {
    console.error('解析样式配置失败', e)
    return {}
  }
})

// 解析可视化配置
const parsedVisualConfig = computed(() => {
  if (!props.templateData.visualConfig) return {}
  try {
    return typeof props.templateData.visualConfig === 'string' 
      ? JSON.parse(props.templateData.visualConfig) 
      : props.templateData.visualConfig
  } catch (e) {
    console.error('解析可视化配置失败', e)
    return {}
  }
})

// 监听模板数据变化
watch(() => props.templateData, () => {
  initParamValues()
  if (props.previewMode) {
    // 预览模式下自动生成示例数据
    generateSampleData()
  }
}, { immediate: true })

// 初始化参数值
const initParamValues = () => {
  parsedParamConfig.value.forEach(param => {
    paramValues[param.name] = param.defaultValue || ''
  })
}

// 重置参数
const resetParams = () => {
  initParamValues()
}

// 生成报表
const handleGenerateReport = async () => {
  if (props.previewMode) {
    generateSampleData()
    return
  }
  
  loading.value = true
  try {
    const res = await generateReport({ // This still calls the imported API function
      templateId: props.templateData.id,
      reportName: props.templateData.templateName,
      reportFormat: exportFormat.value,
      params: paramValues
    })
    
    if (res.data.code === 200) {
      const data = res.data.data
      generatedReportId.value = data.id
      
      // 获取报表数据
      reportData.value = data.reportData ? JSON.parse(data.reportData) : []
      
      // 如果是图表类型，渲染图表
      if (props.templateData.templateType === 'chart' || props.templateData.templateType === 'mixed') {
        renderChart()
      }
      
      ElMessage.success('报表生成成功')
    } else {
      ElMessage.error(res.data.msg || '生成报表失败')
    }
  } catch (error) {
    console.error('生成报表出错', error)
    ElMessage.error('生成报表出错')
  } finally {
    loading.value = false
  }
}

// 生成示例数据（预览模式使用）
const generateSampleData = () => {
  loading.value = true
  
  // 生成模拟数据
  const sampleData = []
  for (let i = 1; i <= 10; i++) {
    const item = {}
    parsedColumnConfig.value.forEach(column => {
      if (column.field.includes('id')) {
        item[column.field] = i
      } else if (column.field.includes('name') || column.field.includes('title')) {
        item[column.field] = `示例${column.field}${i}`
      } else if (column.field.includes('date') || column.field.includes('time')) {
        item[column.field] = new Date().toISOString().split('T')[0]
      } else if (column.field.includes('price') || column.field.includes('amount') || column.field.includes('value')) {
        item[column.field] = Math.round(Math.random() * 1000)
      } else {
        item[column.field] = `数据${i}`
      }
    })
    sampleData.push(item)
  }
  
  reportData.value = sampleData
  
  // 如果是图表类型，渲染图表
  if (props.templateData.templateType === 'chart' || props.templateData.templateType === 'mixed') {
    setTimeout(() => {
      renderChart()
    }, 100)
  }
  
  loading.value = false
}

// 渲染图表
const renderChart = () => {
  if (!chartContainer.value) return
  
  // 销毁旧图表实例
  if (chartInstance.value) {
    chartInstance.value.dispose()
  }
  
  // 创建新图表实例
  chartInstance.value = echarts.init(chartContainer.value)
  
  // 获取可视化配置
  const visualConfig = parsedVisualConfig.value
  const chartType = visualConfig.chartType || 'line'
  const xField = visualConfig.xField || ''
  const yField = visualConfig.yField || ''
  
  // 准备图表数据
  const xData = reportData.value.map(item => item[xField])
  const yData = reportData.value.map(item => item[yField])
  
  // 图表配置
  let option = {}
  
  switch (chartType) {
    case 'line':
      option = {
        title: {
          text: visualConfig.title || '折线图'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: xData
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: yData,
          type: 'line'
        }]
      }
      break
      
    case 'bar':
      option = {
        title: {
          text: visualConfig.title || '柱状图'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: xData
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: yData,
          type: 'bar'
        }]
      }
      break
      
    case 'pie':
      const pieData = reportData.value.map(item => ({
        name: item[xField],
        value: item[yField]
      }))
      
      option = {
        title: {
          text: visualConfig.title || '饼图'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        series: [{
          name: visualConfig.title || '数据',
          type: 'pie',
          radius: '50%',
          data: pieData
        }]
      }
      break
      
    case 'scatter':
      const scatterData = reportData.value.map(item => [item[xField], item[yField]])
      
      option = {
        title: {
          text: visualConfig.title || '散点图'
        },
        xAxis: {},
        yAxis: {},
        series: [{
          symbolSize: 10,
          data: scatterData,
          type: 'scatter'
        }]
      }
      break
      
    case 'radar':
      // 雷达图需要特殊处理
      const indicators = reportData.value.map(item => ({
        name: item[xField],
        max: Math.max(...reportData.value.map(d => d[yField])) * 1.2
      }))
      
      const radarData = [{
        value: reportData.value.map(item => item[yField]),
        name: visualConfig.title || '数据'
      }]
      
      option = {
        title: {
          text: visualConfig.title || '雷达图'
        },
        tooltip: {},
        radar: {
          indicator: indicators
        },
        series: [{
          type: 'radar',
          data: radarData
        }]
      }
      break
  }
  
  // 设置图表选项并渲染
  chartInstance.value.setOption(option)
}

// 导出报表
const exportReport = async () => {
  if (!generatedReportId.value) {
    ElMessage.warning('请先生成报表')
    return
  }
  
  try {
    await downloadReport(generatedReportId.value)
    ElMessage.success('报表导出成功')
  } catch (error) {
    console.error('导出报表出错', error)
    ElMessage.error('导出报表出错')
  }
}

// 获取表头样式
const getHeaderStyle = () => {
  const theme = parsedStyleConfig.value.theme || 'default'
  
  switch (theme) {
    case 'dark':
      return { backgroundColor: '#303133', color: '#fff' }
    case 'blue':
      return { backgroundColor: '#409EFF', color: '#fff' }
    default:
      return { backgroundColor: '#f5f7fa' }
  }
}

// 获取图表样式
const getChartStyle = () => {
  const width = parsedVisualConfig.value.width || 800
  const height = parsedVisualConfig.value.height || 400
  
  return {
    width: `${width}px`,
    height: `${height}px`,
    margin: '0 auto'
  }
}

// 组件卸载时销毁图表实例
onMounted(() => {
  window.addEventListener('resize', handleResize)
})

const handleResize = () => {
  if (chartInstance.value) {
    chartInstance.value.resize()
  }
}
</script>

<style scoped>
.report-preview-container {
  padding: 20px;
}

.preview-header {
  text-align: center;
  margin-bottom: 20px;
}

.params-form {
  margin-bottom: 20px;
  padding: 20px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  background-color: #f5f7fa;
}

.report-content {
  margin-top: 20px;
  min-height: 300px;
}

.table-report {
  margin-bottom: 20px;
}

.chart-report {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.export-options {
  margin-top: 30px;
}
</style>