<template>
  <div class="report-designer-container">
    <el-card class="designer-card">
      <template #header>
        <div class="card-header">
          <span>报表设计器</span>
          <div class="header-actions">
            <el-button type="primary" @click="saveTemplate">保存模板</el-button>
            <el-button @click="previewReport">预览报表</el-button>
          </div>
        </div>
      </template>
      
      <el-form :model="templateForm" :rules="rules" ref="templateFormRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模板名称" prop="templateName">
              <el-input v-model="templateForm.templateName" placeholder="请输入模板名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模板编码" prop="templateCode">
              <el-input v-model="templateForm.templateCode" placeholder="请输入模板编码"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模板类型" prop="templateType">
              <el-select v-model="templateForm.templateType" placeholder="请选择模板类型" style="width: 100%">
                <el-option label="表格报表" value="table"></el-option>
                <el-option label="图表报表" value="chart"></el-option>
                <el-option label="混合报表" value="mixed"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据源类型" prop="dataSourceType">
              <el-select v-model="templateForm.dataSourceType" placeholder="请选择数据源类型" style="width: 100%">
                <el-option label="SQL查询" value="sql"></el-option>
                <el-option label="API接口" value="api"></el-option>
                <el-option label="静态数据" value="static"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="描述" prop="description">
          <el-input v-model="templateForm.description" type="textarea" :rows="2" placeholder="请输入模板描述"></el-input>
        </el-form-item>
        
        <el-tabs v-model="activeTab" type="border-card">
          <el-tab-pane label="数据源配置" name="dataSource">
            <div class="tab-content">
              <template v-if="templateForm.dataSourceType === 'sql'">
                <el-form-item label="SQL语句" prop="dataSourceConfig.sql">
                  <el-input v-model="dataSourceConfig.sql" type="textarea" :rows="5" placeholder="请输入SQL查询语句"></el-input>
                </el-form-item>
                <el-form-item label="数据库连接" prop="dataSourceConfig.connection">
                  <el-select v-model="dataSourceConfig.connection" placeholder="请选择数据库连接" style="width: 100%">
                    <el-option label="默认数据源" value="default"></el-option>
                    <el-option label="业务数据源" value="business"></el-option>
                  </el-select>
                </el-form-item>
              </template>
              
              <template v-if="templateForm.dataSourceType === 'api'">
                <el-form-item label="API地址" prop="dataSourceConfig.url">
                  <el-input v-model="dataSourceConfig.url" placeholder="请输入API地址"></el-input>
                </el-form-item>
                <el-form-item label="请求方法" prop="dataSourceConfig.method">
                  <el-radio-group v-model="dataSourceConfig.method">
                    <el-radio label="GET">GET</el-radio>
                    <el-radio label="POST">POST</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="请求头" prop="dataSourceConfig.headers">
                  <el-input v-model="dataSourceConfig.headers" type="textarea" :rows="2" placeholder="请输入请求头（JSON格式）"></el-input>
                </el-form-item>
              </template>
              
              <template v-if="templateForm.dataSourceType === 'static'">
                <el-form-item label="静态数据" prop="dataSourceConfig.data">
                  <el-input v-model="dataSourceConfig.data" type="textarea" :rows="5" placeholder="请输入静态数据（JSON格式）"></el-input>
                </el-form-item>
              </template>
            </div>
          </el-tab-pane>
          
          <el-tab-pane label="参数配置" name="params">
            <div class="tab-content">
              <el-button type="primary" size="small" @click="addParam">添加参数</el-button>
              <el-table :data="paramConfig" style="width: 100%; margin-top: 15px;">
                <el-table-column prop="name" label="参数名称" width="180">
                  <template #default="scope">
                    <el-input v-model="scope.row.name" placeholder="参数名称"></el-input>
                  </template>
                </el-table-column>
                <el-table-column prop="label" label="参数标签" width="180">
                  <template #default="scope">
                    <el-input v-model="scope.row.label" placeholder="参数标签"></el-input>
                  </template>
                </el-table-column>
                <el-table-column prop="type" label="参数类型" width="180">
                  <template #default="scope">
                    <el-select v-model="scope.row.type" placeholder="参数类型">
                      <el-option label="文本" value="text"></el-option>
                      <el-option label="数字" value="number"></el-option>
                      <el-option label="日期" value="date"></el-option>
                      <el-option label="选择器" value="select"></el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column prop="required" label="是否必填" width="100">
                  <template #default="scope">
                    <el-switch v-model="scope.row.required"></el-switch>
                  </template>
                </el-table-column>
                <el-table-column prop="defaultValue" label="默认值">
                  <template #default="scope">
                    <el-input v-model="scope.row.defaultValue" placeholder="默认值"></el-input>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="100">
                  <template #default="scope">
                    <el-button type="danger" size="small" @click="removeParam(scope.$index)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
          
          <el-tab-pane label="列配置" name="columns">
            <div class="tab-content">
              <el-button type="primary" size="small" @click="addColumn">添加列</el-button>
              <el-table :data="columnConfig" style="width: 100%; margin-top: 15px;">
                <el-table-column prop="field" label="字段名" width="180">
                  <template #default="scope">
                    <el-input v-model="scope.row.field" placeholder="字段名"></el-input>
                  </template>
                </el-table-column>
                <el-table-column prop="title" label="列标题" width="180">
                  <template #default="scope">
                    <el-input v-model="scope.row.title" placeholder="列标题"></el-input>
                  </template>
                </el-table-column>
                <el-table-column prop="width" label="列宽" width="100">
                  <template #default="scope">
                    <el-input-number v-model="scope.row.width" :min="50" :max="500"></el-input-number>
                  </template>
                </el-table-column>
                <el-table-column prop="align" label="对齐方式" width="120">
                  <template #default="scope">
                    <el-select v-model="scope.row.align" placeholder="对齐方式">
                      <el-option label="左对齐" value="left"></el-option>
                      <el-option label="居中" value="center"></el-option>
                      <el-option label="右对齐" value="right"></el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column prop="visible" label="是否显示" width="100">
                  <template #default="scope">
                    <el-switch v-model="scope.row.visible"></el-switch>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150">
                  <template #default="scope">
                    <el-button type="primary" size="small" @click="moveColumn(scope.$index, -1)" :disabled="scope.$index === 0">上移</el-button>
                    <el-button type="primary" size="small" @click="moveColumn(scope.$index, 1)" :disabled="scope.$index === columnConfig.length - 1">下移</el-button>
                    <el-button type="danger" size="small" @click="removeColumn(scope.$index)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
          
          <el-tab-pane label="样式配置" name="style">
            <div class="tab-content">
              <el-form-item label="标题" prop="styleConfig.title">
                <el-input v-model="styleConfig.title" placeholder="报表标题"></el-input>
              </el-form-item>
              <el-form-item label="副标题" prop="styleConfig.subtitle">
                <el-input v-model="styleConfig.subtitle" placeholder="报表副标题"></el-input>
              </el-form-item>
              <el-form-item label="页眉" prop="styleConfig.header">
                <el-input v-model="styleConfig.header" placeholder="页眉内容"></el-input>
              </el-form-item>
              <el-form-item label="页脚" prop="styleConfig.footer">
                <el-input v-model="styleConfig.footer" placeholder="页脚内容"></el-input>
              </el-form-item>
              <el-form-item label="主题" prop="styleConfig.theme">
                <el-select v-model="styleConfig.theme" placeholder="选择主题">
                  <el-option label="默认主题" value="default"></el-option>
                  <el-option label="暗色主题" value="dark"></el-option>
                  <el-option label="蓝色主题" value="blue"></el-option>
                </el-select>
              </el-form-item>
            </div>
          </el-tab-pane>
          
          <el-tab-pane label="可视化配置" name="visual" v-if="templateForm.templateType === 'chart' || templateForm.templateType === 'mixed'">
            <div class="tab-content">
              <el-form-item label="图表类型" prop="visualConfig.chartType">
                <el-select v-model="visualConfig.chartType" placeholder="选择图表类型">
                  <el-option label="折线图" value="line"></el-option>
                  <el-option label="柱状图" value="bar"></el-option>
                  <el-option label="饼图" value="pie"></el-option>
                  <el-option label="散点图" value="scatter"></el-option>
                  <el-option label="雷达图" value="radar"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="X轴字段" prop="visualConfig.xField">
                <el-input v-model="visualConfig.xField" placeholder="X轴对应的字段名"></el-input>
              </el-form-item>
              <el-form-item label="Y轴字段" prop="visualConfig.yField">
                <el-input v-model="visualConfig.yField" placeholder="Y轴对应的字段名"></el-input>
              </el-form-item>
              <el-form-item label="图表标题" prop="visualConfig.title">
                <el-input v-model="visualConfig.title" placeholder="图表标题"></el-input>
              </el-form-item>
              <el-form-item label="图表宽度" prop="visualConfig.width">
                <el-input-number v-model="visualConfig.width" :min="300" :max="1200"></el-input-number>
              </el-form-item>
              <el-form-item label="图表高度" prop="visualConfig.height">
                <el-input-number v-model="visualConfig.height" :min="200" :max="800"></el-input-number>
              </el-form-item>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-form>
    </el-card>
    
    <!-- 预览对话框 -->
    <el-dialog v-model="previewDialogVisible" title="报表预览" width="80%">
      <report-preview :template-data="templateForm" :preview-mode="true"></report-preview>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="previewDialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="exportPreview">导出</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRoute, useRouter } from 'vue-router'
import ReportPreview from './ReportPreview.vue'
import { getTemplateById, createTemplate, updateTemplate } from '@/api/report'

const route = useRoute()
const router = useRouter()
const templateFormRef = ref(null)
const previewDialogVisible = ref(false)
const activeTab = ref('dataSource')
const isEdit = computed(() => route.query.id !== undefined)

// 表单数据
const templateForm = reactive({
  id: null,
  templateName: '',
  templateCode: '',
  description: '',
  templateType: 'table',
  dataSourceType: 'sql',
  dataSourceConfig: '',
  paramConfig: '',
  columnConfig: '',
  styleConfig: '',
  visualConfig: '',
  status: '0'
})

// 数据源配置
const dataSourceConfig = reactive({
  sql: '',
  connection: 'default',
  url: '',
  method: 'GET',
  headers: '',
  data: ''
})

// 参数配置
const paramConfig = ref([])

// 列配置
const columnConfig = ref([])

// 样式配置
const styleConfig = reactive({
  title: '',
  subtitle: '',
  header: '',
  footer: '',
  theme: 'default'
})

// 可视化配置
const visualConfig = reactive({
  chartType: 'line',
  xField: '',
  yField: '',
  title: '',
  width: 800,
  height: 400
})

// 表单验证规则
const rules = {
  templateName: [
    { required: true, message: '请输入模板名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  templateCode: [
    { required: true, message: '请输入模板编码', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9_]+$/, message: '只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  templateType: [
    { required: true, message: '请选择模板类型', trigger: 'change' }
  ],
  dataSourceType: [
    { required: true, message: '请选择数据源类型', trigger: 'change' }
  ]
}

// 初始化
onMounted(async () => {
  if (isEdit.value) {
    const id = route.query.id
    await loadTemplateData(id)
  }
})

// 加载模板数据
const loadTemplateData = async (id) => {
  try {
    const res = await getTemplateById(id)
    if (res.data.code === 200) {
      const data = res.data.data
      Object.assign(templateForm, data)
      
      // 解析各配置项
      if (data.dataSourceConfig) {
        try {
          Object.assign(dataSourceConfig, JSON.parse(data.dataSourceConfig))
        } catch (e) {
          console.error('解析数据源配置失败', e)
        }
      }
      
      if (data.paramConfig) {
        try {
          paramConfig.value = JSON.parse(data.paramConfig)
        } catch (e) {
          console.error('解析参数配置失败', e)
          paramConfig.value = []
        }
      }
      
      if (data.columnConfig) {
        try {
          columnConfig.value = JSON.parse(data.columnConfig)
        } catch (e) {
          console.error('解析列配置失败', e)
          columnConfig.value = []
        }
      }
      
      if (data.styleConfig) {
        try {
          Object.assign(styleConfig, JSON.parse(data.styleConfig))
        } catch (e) {
          console.error('解析样式配置失败', e)
        }
      }
      
      if (data.visualConfig) {
        try {
          Object.assign(visualConfig, JSON.parse(data.visualConfig))
        } catch (e) {
          console.error('解析可视化配置失败', e)
        }
      }
    } else {
      ElMessage.error('获取模板数据失败')
    }
  } catch (error) {
    console.error('加载模板数据出错', error)
    ElMessage.error('加载模板数据出错')
  }
}

// 添加参数
const addParam = () => {
  paramConfig.value.push({
    name: '',
    label: '',
    type: 'text',
    required: false,
    defaultValue: ''
  })
}

// 移除参数
const removeParam = (index) => {
  paramConfig.value.splice(index, 1)
}

// 添加列
const addColumn = () => {
  columnConfig.value.push({
    field: '',
    title: '',
    width: 100,
    align: 'left',
    visible: true
  })
}

// 移除列
const removeColumn = (index) => {
  columnConfig.value.splice(index, 1)
}

// 移动列
const moveColumn = (index, direction) => {
  const newIndex = index + direction
  if (newIndex < 0 || newIndex >= columnConfig.value.length) return
  
  const temp = columnConfig.value[index]
  columnConfig.value[index] = columnConfig.value[newIndex]
  columnConfig.value[newIndex] = temp
}

// 保存模板
const saveTemplate = async () => {
  if (!templateFormRef.value) return
  
  await templateFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 准备保存的数据
        const saveData = { ...templateForm }
        
        // 转换各配置为JSON字符串
        saveData.dataSourceConfig = JSON.stringify(dataSourceConfig)
        saveData.paramConfig = JSON.stringify(paramConfig.value)
        saveData.columnConfig = JSON.stringify(columnConfig.value)
        saveData.styleConfig = JSON.stringify(styleConfig)
        saveData.visualConfig = JSON.stringify(visualConfig)
        
        let res
        if (isEdit.value) {
          res = await updateTemplate(saveData.id, saveData)
        } else {
          res = await createTemplate(saveData)
        }
        
        if (res.data.code === 200) {
          ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
          router.push('/report/templates')
        } else {
          ElMessage.error(res.data.msg || '保存失败')
        }
      } catch (error) {
        console.error('保存模板出错', error)
        ElMessage.error('保存模板出错')
      }
    } else {
      ElMessage.warning('请完善表单信息')
      return false
    }
  })
}

// 预览报表
const previewReport = () => {
  previewDialogVisible.value = true
}

// 导出预览
const exportPreview = () => {
  ElMessage.info('导出功能开发中...')
}
</script>

<style scoped>
.report-designer-container {
  padding: 20px;
}

.designer-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.tab-content {
  padding: 20px;
  min-height: 300px;
}
</style>