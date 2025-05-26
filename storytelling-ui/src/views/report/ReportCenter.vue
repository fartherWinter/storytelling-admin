<template>
  <div class="report-center-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>报表中心</span>
        </div>
      </template>
      
      <!-- 报表模板选择区域 -->
      <el-form :model="reportForm" label-width="120px">
        <el-form-item label="报表模板">
          <el-select v-model="reportForm.templateId" placeholder="请选择报表模板" @change="handleTemplateChange">
            <el-option
              v-for="item in templateOptions"
              :key="item.id"
              :label="item.templateName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="报表名称">
          <el-input v-model="reportForm.reportName" placeholder="请输入报表名称"></el-input>
        </el-form-item>
        
        <el-form-item label="导出格式">
          <el-select v-model="reportForm.reportFormat" placeholder="请选择导出格式">
            <el-option
              v-for="item in formatOptions"
              :key="item"
              :label="item.toUpperCase()"
              :value="item">
            </el-option>
          </el-select>
        </el-form-item>
        
        <!-- 动态参数表单 -->
        <template v-if="currentTemplate && currentTemplate.paramConfig">
          <el-divider content-position="left">报表参数</el-divider>
          <div v-for="(param, key) in templateParams" :key="key">
            <el-form-item :label="param.label">
              <!-- 日期范围选择器 -->
              <el-date-picker 
                v-if="param.type === 'date_range'"
                v-model="reportForm.params[key]"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
              
              <!-- 普通输入框 -->
              <el-input 
                v-else-if="param.type === 'text'"
                v-model="reportForm.params[key]"
                :placeholder="`请输入${param.label}`">
              </el-input>
              
              <!-- 数字输入框 -->
              <el-input-number 
                v-else-if="param.type === 'number'"
                v-model="reportForm.params[key]"
                :placeholder="`请输入${param.label}`">
              </el-input-number>
              
              <!-- 下拉选择框 -->
              <el-select 
                v-else-if="param.type === 'select'"
                v-model="reportForm.params[key]"
                :placeholder="`请选择${param.label}`">
                <el-option
                  v-for="option in param.options"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </template>
        
        <el-form-item>
          <el-button type="primary" @click="generateReport">生成报表</el-button>
          <el-button type="success" @click="previewReport" :disabled="!currentRecord">预览报表</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 报表记录列表 -->
      <el-divider content-position="left">报表记录</el-divider>
      <el-table :data="reportRecords" style="width: 100%" v-loading="tableLoading">
        <el-table-column prop="reportName" label="报表名称" width="180"></el-table-column>
        <el-table-column prop="templateName" label="模板名称" width="180"></el-table-column>
        <el-table-column prop="reportFormat" label="格式" width="80">
          <template #default="scope">
            <el-tag>{{ scope.row.reportFormat.toUpperCase() }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="generateTime" label="生成时间" width="180"></el-table-column>
        <el-table-column prop="downloadCount" label="下载次数" width="100"></el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button 
              size="small" 
              type="primary" 
              @click="downloadReport(scope.row)"
              :disabled="scope.row.status !== '1'">下载</el-button>
            <el-button 
              size="small" 
              type="danger" 
              @click="deleteReport(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 数据可视化区域 -->
      <el-divider content-position="left">数据可视化</el-divider>
      <div class="chart-container" v-if="currentTemplate">
        <el-form :inline="true">
          <el-form-item label="图表类型">
            <el-select v-model="chartForm.chartType" placeholder="请选择图表类型" @change="generateChart">
              <el-option
                v-for="item in chartOptions"
                :key="item"
                :label="item"
                :value="item">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="generateChart">生成图表</el-button>
            <el-button type="success" @click="exportChart">导出图表</el-button>
          </el-form-item>
        </el-form>
        
        <div id="chartContainer" style="width: 100%; height: 400px;"></div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

export default {
  name: 'ReportCenter',
  setup() {
    // 报表表单数据
    const reportForm = reactive({
      templateId: '',
      reportName: '',
      reportFormat: 'xlsx',
      params: {}
    })
    
    // 图表表单数据
    const chartForm = reactive({
      chartType: 'bar',
      templateId: '',
      params: {}
    })
    
    // 模板选项
    const templateOptions = ref([])
    // 格式选项
    const formatOptions = ref(['xlsx', 'pdf', 'csv', 'html'])
    // 图表选项
    const chartOptions = ref(['bar', 'line', 'pie', 'scatter', 'radar'])
    // 当前选中的模板
    const currentTemplate = ref(null)
    // 当前生成的报表记录
    const currentRecord = ref(null)
    // 报表记录列表
    const reportRecords = ref([])
    // 表格加载状态
    const tableLoading = ref(false)
    
    // 计算属性：模板参数
    const templateParams = computed(() => {
      if (currentTemplate.value && currentTemplate.value.paramConfig) {
        try {
          return JSON.parse(currentTemplate.value.paramConfig)
        } catch (e) {
          console.error('解析模板参数配置失败', e)
          return {}
        }
      }
      return {}
    })
    
    // 初始化
    onMounted(() => {
      fetchTemplates()
      fetchReportRecords()
    })
    
    // 获取报表模板列表
    const fetchTemplates = async () => {
      try {
        const response = await axios.get('/report/api/templates')
        if (response.data.code === 200) {
          templateOptions.value = response.data.data
        } else {
          ElMessage.error(response.data.msg || '获取报表模板失败')
        }
      } catch (error) {
        console.error('获取报表模板失败', error)
        ElMessage.error('获取报表模板失败')
      }
    }
    
    // 获取报表记录列表
    const fetchReportRecords = async () => {
      tableLoading.value = true
      try {
        const response = await axios.get('/report/api/reports')
        if (response.data.code === 200) {
          reportRecords.value = response.data.data
        } else {
          ElMessage.error(response.data.msg || '获取报表记录失败')
        }
      } catch (error) {
        console.error('获取报表记录失败', error)
        ElMessage.error('获取报表记录失败')
      } finally {
        tableLoading.value = false
      }
    }
    
    // 处理模板变更
    const handleTemplateChange = async () => {
      if (!reportForm.templateId) {
        currentTemplate.value = null
        return
      }
      
      try {
        const response = await axios.get(`/report/api/templates/${reportForm.templateId}`)
        if (response.data.code === 200) {
          currentTemplate.value = response.data.data
          // 设置默认报表名称
          reportForm.reportName = `${currentTemplate.value.templateName}_${new Date().toISOString().split('T')[0]}`
          // 重置参数
          reportForm.params = {}
          // 设置图表表单的模板ID
          chartForm.templateId = reportForm.templateId
        } else {
          ElMessage.error(response.data.msg || '获取报表模板详情失败')
        }
      } catch (error) {
        console.error('获取报表模板详情失败', error)
        ElMessage.error('获取报表模板详情失败')
      }
    }
    
    // 生成报表
    const generateReport = async () => {
      if (!reportForm.templateId) {
        ElMessage.warning('请选择报表模板')
        return
      }
      
      if (!reportForm.reportName) {
        ElMessage.warning('请输入报表名称')
        return
      }
      
      try {
        const response = await axios.post('/report/api/reports/generate', {
          templateId: reportForm.templateId,
          reportName: reportForm.reportName,
          reportFormat: reportForm.reportFormat,
          params: reportForm.params
        })
        
        if (response.data.code === 200) {
          ElMessage.success('报表生成成功')
          currentRecord.value = response.data.data
          fetchReportRecords()
        } else {
          ElMessage.error(response.data.msg || '报表生成失败')
        }
      } catch (error) {
        console.error('报表生成失败', error)
        ElMessage.error('报表生成失败')
      }
    }
    
    // 预览报表
    const previewReport = () => {
      if (!currentRecord.value) {
        ElMessage.warning('请先生成报表')
        return
      }
      
      window.open(`/report/api/reports/${currentRecord.value.id}/download`, '_blank')
    }
    
    // 下载报表
    const downloadReport = (record) => {
      window.open(`/report/api/reports/${record.id}/download`, '_blank')
    }
    
    // 删除报表
    const deleteReport = (record) => {
      ElMessageBox.confirm('确定要删除该报表记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await axios.delete(`/report/api/reports/${record.id}`)
          if (response.data.code === 200) {
            ElMessage.success('删除成功')
            fetchReportRecords()
          } else {
            ElMessage.error(response.data.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除报表记录失败', error)
          ElMessage.error('删除报表记录失败')
        }
      }).catch(() => {})
    }
    
    // 重置表单
    const resetForm = () => {
      reportForm.templateId = ''
      reportForm.reportName = ''
      reportForm.reportFormat = 'xlsx'
      reportForm.params = {}
      currentTemplate.value = null
      currentRecord.value = null
    }
    
    // 生成图表
    const generateChart = async () => {
      if (!chartForm.templateId) {
        ElMessage.warning('请选择报表模板')
        return
      }
      
      try {
        const response = await axios.post('/report/api/visualization/chart', {
          chartType: chartForm.chartType,
          templateId: chartForm.templateId,
          params: reportForm.params
        })
        
        if (response.data.code === 200) {
          renderChart(response.data.data)
        } else {
          ElMessage.error(response.data.msg || '生成图表失败')
        }
      } catch (error) {
        console.error('生成图表失败', error)
        ElMessage.error('生成图表失败')
      }
    }
    
    // 导出图表
    const exportChart = () => {
      if (!chartForm.templateId) {
        ElMessage.warning('请选择报表模板')
        return
      }
      
      const url = `/report/api/visualization/export?chartType=${chartForm.chartType}&exportFormat=png&templateId=${chartForm.templateId}`
      window.open(url, '_blank')
    }
    
    // 渲染图表
    const renderChart = (chartData) => {
      // 这里简化处理，实际项目中应使用ECharts等图表库
      const container = document.getElementById('chartContainer')
      container.innerHTML = `<div style="text-align: center; padding: 20px;">
        <h3>${chartData.title || '数据可视化图表'}</h3>
        <p>图表类型: ${chartData.type}</p>
        <p>数据已生成，请点击"导出图表"查看完整图表</p>
      </div>`
    }
    
    // 获取状态类型
    const getStatusType = (status) => {
      switch (status) {
        case '0': return 'warning'  // 生成中
        case '1': return 'success'  // 已完成
        case '2': return 'danger'   // 失败
        default: return 'info'
      }
    }
    
    // 获取状态文本
    const getStatusText = (status) => {
      switch (status) {
        case '0': return '生成中'
        case '1': return '已完成'
        case '2': return '失败'
        default: return '未知'
      }
    }
    
    return {
      reportForm,
      chartForm,
      templateOptions,
      formatOptions,
      chartOptions,
      currentTemplate,
      currentRecord,
      reportRecords,
      tableLoading,
      templateParams,
      handleTemplateChange,
      generateReport,
      previewReport,
      downloadReport,
      deleteReport,
      resetForm,
      generateChart,
      exportChart,
      getStatusType,
      getStatusText
    }
  }
}
</script>

<style scoped>
.report-center-container {
  padding: 20px;
}

.box-card {
  width: 100%;
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  margin-top: 20px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 20px;
  background-color: #fff;
}

.el-divider {
  margin: 24px 0;
}
</style>