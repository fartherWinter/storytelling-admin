<template>
  <div class="form-designer-container">
    <div class="page-header">
      <h2>表单设计器</h2>
      <p>可视化设计工作流表单</p>
    </div>
    
    <!-- 工具栏 -->
    <div class="toolbar">
      <el-card>
        <div class="toolbar-content">
          <div class="toolbar-left">
            <el-button type="primary" @click="handleSave">
              <el-icon><DocumentAdd /></el-icon>
              保存表单
            </el-button>
            <el-button @click="handlePreview">
              <el-icon><View /></el-icon>
              预览
            </el-button>
            <el-button @click="handleClear">
              <el-icon><Delete /></el-icon>
              清空
            </el-button>
            <el-button @click="handleImport">
              <el-icon><Upload /></el-icon>
              导入JSON
            </el-button>
            <el-button @click="handleExport">
              <el-icon><Download /></el-icon>
              导出JSON
            </el-button>
          </div>
          
          <div class="toolbar-right">
            <el-input
              v-model="formConfig.formName"
              placeholder="请输入表单名称"
              style="width: 200px; margin-right: 10px;"
            />
            <el-select
              v-model="formConfig.formType"
              placeholder="表单类型"
              style="width: 120px;"
            >
              <el-option label="启动表单" value="start" />
              <el-option label="任务表单" value="task" />
              <el-option label="通用表单" value="common" />
            </el-select>
          </div>
        </div>
      </el-card>
    </div>
    
    <!-- 主要内容区域 -->
    <div class="main-content">
      <el-row :gutter="20">
        <!-- 组件面板 -->
        <el-col :span="4">
          <el-card class="component-panel">
            <template #header>
              <span>组件库</span>
            </template>
            
            <el-collapse v-model="activeComponentGroups" accordion>
              <!-- 基础组件 -->
              <el-collapse-item title="基础组件" name="basic">
                <div class="component-list">
                  <div
                    v-for="component in basicComponents"
                    :key="component.type"
                    class="component-item"
                    draggable="true"
                    @dragstart="handleDragStart(component)"
                  >
                    <el-icon><component :is="component.icon" /></el-icon>
                    <span>{{ component.label }}</span>
                  </div>
                </div>
              </el-collapse-item>
              
              <!-- 高级组件 -->
              <el-collapse-item title="高级组件" name="advanced">
                <div class="component-list">
                  <div
                    v-for="component in advancedComponents"
                    :key="component.type"
                    class="component-item"
                    draggable="true"
                    @dragstart="handleDragStart(component)"
                  >
                    <el-icon><component :is="component.icon" /></el-icon>
                    <span>{{ component.label }}</span>
                  </div>
                </div>
              </el-collapse-item>
              
              <!-- 布局组件 -->
              <el-collapse-item title="布局组件" name="layout">
                <div class="component-list">
                  <div
                    v-for="component in layoutComponents"
                    :key="component.type"
                    class="component-item"
                    draggable="true"
                    @dragstart="handleDragStart(component)"
                  >
                    <el-icon><component :is="component.icon" /></el-icon>
                    <span>{{ component.label }}</span>
                  </div>
                </div>
              </el-collapse-item>
            </el-collapse>
          </el-card>
        </el-col>
        
        <!-- 设计区域 -->
        <el-col :span="12">
          <el-card class="design-area">
            <template #header>
              <div class="design-header">
                <span>设计区域</span>
                <div class="design-actions">
                  <el-button size="small" @click="handleUndo" :disabled="!canUndo">
                    <el-icon><RefreshLeft /></el-icon>
                    撤销
                  </el-button>
                  <el-button size="small" @click="handleRedo" :disabled="!canRedo">
                    <el-icon><RefreshRight /></el-icon>
                    重做
                  </el-button>
                </div>
              </div>
            </template>
            
            <div
              class="form-canvas"
              @drop="handleDrop"
              @dragover="handleDragOver"
              @dragenter="handleDragEnter"
            >
              <div v-if="formFields.length === 0" class="empty-canvas">
                <el-icon class="empty-icon"><Plus /></el-icon>
                <p>拖拽左侧组件到此处开始设计表单</p>
              </div>
              
              <draggable
                v-model="formFields"
                group="form-fields"
                item-key="id"
                class="form-fields-container"
                @change="handleFieldsChange"
              >
                <template #item="{ element, index }">
                  <div
                    class="form-field-wrapper"
                    :class="{ active: selectedFieldIndex === index }"
                    @click="selectField(index)"
                  >
                    <div class="field-actions">
                      <el-button size="small" type="primary" text @click="copyField(index)">
                        <el-icon><CopyDocument /></el-icon>
                      </el-button>
                      <el-button size="small" type="danger" text @click="deleteField(index)">
                        <el-icon><Delete /></el-icon>
                      </el-button>
                    </div>
                    
                    <component
                      :is="getFieldComponent(element.type)"
                      :field="element"
                      :preview="false"
                      @update="updateField(index, $event)"
                    />
                  </div>
                </template>
              </draggable>
            </div>
          </el-card>
        </el-col>
        
        <!-- 属性面板 -->
        <el-col :span="8">
          <el-card class="property-panel">
            <template #header>
              <span>属性配置</span>
            </template>
            
            <div v-if="selectedField" class="property-content">
              <el-tabs v-model="activePropertyTab">
                <el-tab-pane label="基础属性" name="basic">
                  <el-form :model="selectedField" label-width="80px" size="small">
                    <el-form-item label="字段标签">
                      <el-input v-model="selectedField.label" @input="updateSelectedField" />
                    </el-form-item>
                    
                    <el-form-item label="字段名称">
                      <el-input v-model="selectedField.name" @input="updateSelectedField" />
                    </el-form-item>
                    
                    <el-form-item label="占位符">
                      <el-input v-model="selectedField.placeholder" @input="updateSelectedField" />
                    </el-form-item>
                    
                    <el-form-item label="默认值">
                      <el-input v-model="selectedField.defaultValue" @input="updateSelectedField" />
                    </el-form-item>
                    
                    <el-form-item label="帮助文本">
                      <el-input
                        v-model="selectedField.helpText"
                        type="textarea"
                        :rows="2"
                        @input="updateSelectedField"
                      />
                    </el-form-item>
                  </el-form>
                </el-tab-pane>
                
                <el-tab-pane label="验证规则" name="validation">
                  <el-form :model="selectedField" label-width="80px" size="small">
                    <el-form-item label="必填">
                      <el-switch v-model="selectedField.required" @change="updateSelectedField" />
                    </el-form-item>
                    
                    <el-form-item label="只读">
                      <el-switch v-model="selectedField.readonly" @change="updateSelectedField" />
                    </el-form-item>
                    
                    <el-form-item label="禁用">
                      <el-switch v-model="selectedField.disabled" @change="updateSelectedField" />
                    </el-form-item>
                    
                    <el-form-item v-if="selectedField.type === 'input'" label="最小长度">
                      <el-input-number
                        v-model="selectedField.minLength"
                        :min="0"
                        @change="updateSelectedField"
                      />
                    </el-form-item>
                    
                    <el-form-item v-if="selectedField.type === 'input'" label="最大长度">
                      <el-input-number
                        v-model="selectedField.maxLength"
                        :min="0"
                        @change="updateSelectedField"
                      />
                    </el-form-item>
                    
                    <el-form-item v-if="selectedField.type === 'input'" label="正则表达式">
                      <el-input v-model="selectedField.pattern" @input="updateSelectedField" />
                    </el-form-item>
                    
                    <el-form-item label="验证消息">
                      <el-input v-model="selectedField.validationMessage" @input="updateSelectedField" />
                    </el-form-item>
                  </el-form>
                </el-tab-pane>
                
                <el-tab-pane label="样式设置" name="style">
                  <el-form :model="selectedField" label-width="80px" size="small">
                    <el-form-item label="宽度">
                      <el-select v-model="selectedField.width" @change="updateSelectedField">
                        <el-option label="25%" value="25%" />
                        <el-option label="50%" value="50%" />
                        <el-option label="75%" value="75%" />
                        <el-option label="100%" value="100%" />
                      </el-select>
                    </el-form-item>
                    
                    <el-form-item label="标签宽度">
                      <el-input v-model="selectedField.labelWidth" @input="updateSelectedField" />
                    </el-form-item>
                    
                    <el-form-item label="CSS类名">
                      <el-input v-model="selectedField.className" @input="updateSelectedField" />
                    </el-form-item>
                    
                    <el-form-item label="内联样式">
                      <el-input
                        v-model="selectedField.style"
                        type="textarea"
                        :rows="3"
                        @input="updateSelectedField"
                      />
                    </el-form-item>
                  </el-form>
                </el-tab-pane>
                
                <el-tab-pane v-if="hasOptions(selectedField.type)" label="选项配置" name="options">
                  <div class="options-config">
                    <div class="options-header">
                      <span>选项列表</span>
                      <el-button size="small" type="primary" @click="addOption">
                        <el-icon><Plus /></el-icon>
                        添加选项
                      </el-button>
                    </div>
                    
                    <div class="options-list">
                      <div
                        v-for="(option, index) in selectedField.options"
                        :key="index"
                        class="option-item"
                      >
                        <el-input
                          v-model="option.label"
                          placeholder="选项标签"
                          size="small"
                          @input="updateSelectedField"
                        />
                        <el-input
                          v-model="option.value"
                          placeholder="选项值"
                          size="small"
                          @input="updateSelectedField"
                        />
                        <el-button
                          size="small"
                          type="danger"
                          text
                          @click="removeOption(index)"
                        >
                          <el-icon><Delete /></el-icon>
                        </el-button>
                      </div>
                    </div>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </div>
            
            <div v-else class="no-selection">
              <el-icon class="no-selection-icon"><InfoFilled /></el-icon>
              <p>请选择一个字段来配置属性</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <!-- 预览对话框 -->
    <el-dialog
      v-model="previewVisible"
      title="表单预览"
      width="60%"
      :before-close="handlePreviewClose"
    >
      <div class="form-preview">
        <el-form
          :model="previewData"
          :label-width="formConfig.labelWidth || '100px'"
          size="default"
        >
          <component
            v-for="field in formFields"
            :key="field.id"
            :is="getFieldComponent(field.type)"
            :field="field"
            :preview="true"
            :model-value="previewData[field.name]"
            @update:model-value="previewData[field.name] = $event"
          />
        </el-form>
      </div>
      
      <template #footer>
        <el-button @click="previewVisible = false">关闭</el-button>
        <el-button type="primary" @click="handlePreviewSubmit">提交测试</el-button>
      </template>
    </el-dialog>
    
    <!-- 导入JSON对话框 -->
    <el-dialog
      v-model="importVisible"
      title="导入JSON"
      width="50%"
    >
      <el-input
        v-model="importJson"
        type="textarea"
        :rows="10"
        placeholder="请粘贴表单JSON配置"
      />
      
      <template #footer>
        <el-button @click="importVisible = false">取消</el-button>
        <el-button type="primary" @click="handleImportConfirm">确认导入</el-button>
      </template>
    </el-dialog>
    
    <!-- 保存表单对话框 -->
    <el-dialog
      v-model="saveVisible"
      title="保存表单"
      width="40%"
    >
      <el-form :model="saveForm" label-width="100px">
        <el-form-item label="表单名称" required>
          <el-input v-model="saveForm.formName" placeholder="请输入表单名称" />
        </el-form-item>
        
        <el-form-item label="表单描述">
          <el-input
            v-model="saveForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入表单描述"
          />
        </el-form-item>
        
        <el-form-item label="表单类型">
          <el-select v-model="saveForm.formType" placeholder="请选择表单类型">
            <el-option label="启动表单" value="start" />
            <el-option label="任务表单" value="task" />
            <el-option label="通用表单" value="common" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="关联流程">
          <el-select
            v-model="saveForm.processDefinitionKey"
            placeholder="请选择关联流程"
            clearable
          >
            <el-option
              v-for="process in processDefinitions"
              :key="process.key"
              :label="process.name"
              :value="process.key"
            />
          </el-select>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="saveVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveConfirm">确认保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  DocumentAdd,
  View,
  Delete,
  Upload,
  Download,
  RefreshLeft,
  RefreshRight,
  Plus,
  CopyDocument,
  InfoFilled
} from '@element-plus/icons-vue'
import draggable from 'vuedraggable'
import { workflowConfig } from '@/api/workflow'

// 导入新的表单组件库
import {
  componentMap,
  componentConfig,
  getComponentConfig,
  getComponent,
  FormInput,
  FormTextarea,
  FormNumber,
  FormSelect,
  FormRadio,
  FormCheckbox,
  FormSwitch,
  FormSlider,
  FormDatePicker,
  FormTimePicker,
  FormUpload,
  FormRate,
  FormDivider,
  FormText,
  FormGrid
} from '@/components/form-designer'

// 表单配置
const formConfig = reactive({
  formName: '',
  formType: 'common',
  labelWidth: '100px'
})

// 组件面板状态
const activeComponentGroups = ref(['basic'])

// 使用新的组件配置
const basicComponents = componentConfig.basic
const advancedComponents = componentConfig.advanced
const layoutComponents = componentConfig.layout

// 表单字段
const formFields = ref([])

// 选中的字段
const selectedFieldIndex = ref(-1)
const selectedField = computed(() => {
  return selectedFieldIndex.value >= 0 ? formFields.value[selectedFieldIndex.value] : null
})

// 属性面板状态
const activePropertyTab = ref('basic')

// 历史记录
const history = ref([])
const historyIndex = ref(-1)

const canUndo = computed(() => historyIndex.value > 0)
const canRedo = computed(() => historyIndex.value < history.value.length - 1)

// 对话框状态
const previewVisible = ref(false)
const importVisible = ref(false)
const saveVisible = ref(false)

// 预览数据
const previewData = ref({})

// 导入JSON
const importJson = ref('')

// 保存表单
const saveForm = reactive({
  formName: '',
  description: '',
  formType: 'common',
  processDefinitionKey: ''
})

// 流程定义列表
const processDefinitions = ref([])

// 字段ID计数器
let fieldIdCounter = 0

// 生成唯一ID
const generateId = () => {
  return `field_${++fieldIdCounter}_${Date.now()}`
}

// 获取字段组件
const getFieldComponent = (type) => {
  return getComponent(type) || FormInput
}

// 判断是否有选项配置
const hasOptions = (type) => {
  return ['select', 'radio', 'checkbox'].includes(type)
}

// 创建默认字段配置
const createDefaultField = (type) => {
  const config = getComponentConfig(type)
  if (!config) {
    return {
      id: generateId(),
      type,
      name: `field_${fieldIdCounter}`,
      label: '字段标签',
      required: false,
      disabled: false
    }
  }

  return {
    id: generateId(),
    name: `field_${fieldIdCounter}`,
    type,
    ...config.defaultConfig
  }
}

// 拖拽开始
const handleDragStart = (component) => {
  // 存储拖拽的组件信息
  window.dragComponent = component
}

// 拖拽进入
const handleDragEnter = (e) => {
  e.preventDefault()
}

// 拖拽悬停
const handleDragOver = (e) => {
  e.preventDefault()
}

// 拖拽放置
const handleDrop = (e) => {
  e.preventDefault()
  
  if (window.dragComponent) {
    const field = createDefaultField(window.dragComponent.type)
    addToHistory()
    formFields.value.push(field)
    selectField(formFields.value.length - 1)
    window.dragComponent = null
  }
}

// 选择字段
const selectField = (index) => {
  selectedFieldIndex.value = index
  activePropertyTab.value = 'basic'
}

// 更新字段
const updateField = (index, field) => {
  formFields.value[index] = { ...field }
}

// 更新选中字段
const updateSelectedField = () => {
  if (selectedFieldIndex.value >= 0) {
    // 触发响应式更新
    formFields.value[selectedFieldIndex.value] = { ...selectedField.value }
  }
}

// 复制字段
const copyField = (index) => {
  const field = { ...formFields.value[index] }
  field.id = generateId()
  field.name = field.name + '_copy'
  addToHistory()
  formFields.value.splice(index + 1, 0, field)
}

// 删除字段
const deleteField = (index) => {
  addToHistory()
  formFields.value.splice(index, 1)
  if (selectedFieldIndex.value === index) {
    selectedFieldIndex.value = -1
  } else if (selectedFieldIndex.value > index) {
    selectedFieldIndex.value--
  }
}

// 字段变化
const handleFieldsChange = () => {
  addToHistory()
}

// 添加选项
const addOption = () => {
  if (selectedField.value && selectedField.value.options) {
    selectedField.value.options.push({
      label: `选项${selectedField.value.options.length + 1}`,
      value: `option${selectedField.value.options.length + 1}`
    })
    updateSelectedField()
  }
}

// 删除选项
const removeOption = (index) => {
  if (selectedField.value && selectedField.value.options) {
    selectedField.value.options.splice(index, 1)
    updateSelectedField()
  }
}

// 添加到历史记录
const addToHistory = () => {
  const snapshot = JSON.parse(JSON.stringify(formFields.value))
  history.value = history.value.slice(0, historyIndex.value + 1)
  history.value.push(snapshot)
  historyIndex.value = history.value.length - 1
  
  // 限制历史记录数量
  if (history.value.length > 50) {
    history.value.shift()
    historyIndex.value--
  }
}

// 撤销
const handleUndo = () => {
  if (canUndo.value) {
    historyIndex.value--
    formFields.value = JSON.parse(JSON.stringify(history.value[historyIndex.value]))
    selectedFieldIndex.value = -1
  }
}

// 重做
const handleRedo = () => {
  if (canRedo.value) {
    historyIndex.value++
    formFields.value = JSON.parse(JSON.stringify(history.value[historyIndex.value]))
    selectedFieldIndex.value = -1
  }
}

// 清空表单
const handleClear = () => {
  ElMessageBox.confirm('确定要清空所有字段吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    addToHistory()
    formFields.value = []
    selectedFieldIndex.value = -1
    ElMessage.success('已清空表单')
  })
}

// 预览表单
const handlePreview = () => {
  if (formFields.value.length === 0) {
    ElMessage.warning('请先添加表单字段')
    return
  }
  
  // 初始化预览数据
  previewData.value = {}
  formFields.value.forEach(field => {
    if (field.name) {
      previewData.value[field.name] = field.defaultValue || ''
    }
  })
  
  previewVisible.value = true
}

// 关闭预览
const handlePreviewClose = () => {
  previewVisible.value = false
  previewData.value = {}
}

// 预览提交测试
const handlePreviewSubmit = () => {
  console.log('预览数据:', previewData.value)
  ElMessage.success('提交测试成功，请查看控制台输出')
}

// 导入JSON
const handleImport = () => {
  importJson.value = ''
  importVisible.value = true
}

// 确认导入
const handleImportConfirm = () => {
  try {
    const data = JSON.parse(importJson.value)
    if (Array.isArray(data)) {
      addToHistory()
      formFields.value = data
      selectedFieldIndex.value = -1
      importVisible.value = false
      ElMessage.success('导入成功')
    } else {
      ElMessage.error('JSON格式错误，请确保是字段数组格式')
    }
  } catch (error) {
    ElMessage.error('JSON格式错误：' + error.message)
  }
}

// 导出JSON
const handleExport = () => {
  if (formFields.value.length === 0) {
    ElMessage.warning('没有可导出的字段')
    return
  }
  
  const json = JSON.stringify(formFields.value, null, 2)
  const blob = new Blob([json], { type: 'application/json' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `form_${Date.now()}.json`
  a.click()
  URL.revokeObjectURL(url)
  
  ElMessage.success('导出成功')
}

// 保存表单
const handleSave = () => {
  if (formFields.value.length === 0) {
    ElMessage.warning('请先添加表单字段')
    return
  }
  
  saveForm.formName = formConfig.formName
  saveForm.formType = formConfig.formType
  saveVisible.value = true
}

// 确认保存
const handleSaveConfirm = async () => {
  if (!saveForm.formName) {
    ElMessage.error('请输入表单名称')
    return
  }
  
  try {
    const formData = {
      formName: saveForm.formName,
      description: saveForm.description,
      formType: saveForm.formType,
      processDefinitionKey: saveForm.processDefinitionKey,
      formConfig: {
        labelWidth: formConfig.labelWidth,
        fields: formFields.value
      }
    }
    
    await workflowConfig.saveFormTemplate(formData)
    saveVisible.value = false
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error('保存失败：' + error.message)
  }
}

// 获取流程定义列表
const getProcessDefinitions = async () => {
  try {
    const response = await workflowConfig.getProcessDefinitionList({ size: 1000 })
    processDefinitions.value = response.data.records
  } catch (error) {
    ElMessage.error('获取流程定义列表失败')
  }
}

// 初始化
onMounted(() => {
  // 添加初始历史记录
  addToHistory()
  
  // 获取流程定义列表
  getProcessDefinitions()
})
</script>

<style scoped>
.form-designer-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.toolbar {
  margin-bottom: 20px;
}

.toolbar-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.toolbar-left {
  display: flex;
  gap: 10px;
}

.toolbar-right {
  display: flex;
  align-items: center;
}

.main-content {
  min-height: 600px;
}

.component-panel {
  height: 600px;
  overflow-y: auto;
}

.component-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.component-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background: #fff;
  cursor: grab;
  transition: all 0.3s;
}

.component-item:hover {
  border-color: #409eff;
  background: #ecf5ff;
}

.component-item:active {
  cursor: grabbing;
}

.design-area {
  height: 600px;
}

.design-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.design-actions {
  display: flex;
  gap: 8px;
}

.form-canvas {
  min-height: 500px;
  border: 2px dashed #e4e7ed;
  border-radius: 4px;
  position: relative;
  background: #fafafa;
}

.empty-canvas {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 500px;
  color: #909399;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.form-fields-container {
  padding: 20px;
  min-height: 460px;
}

.form-field-wrapper {
  position: relative;
  margin-bottom: 16px;
  padding: 12px;
  border: 1px solid transparent;
  border-radius: 4px;
  background: #fff;
  transition: all 0.3s;
}

.form-field-wrapper:hover {
  border-color: #c0c4cc;
}

.form-field-wrapper.active {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.field-actions {
  position: absolute;
  top: -8px;
  right: 8px;
  display: none;
  gap: 4px;
  z-index: 10;
}

.form-field-wrapper:hover .field-actions,
.form-field-wrapper.active .field-actions {
  display: flex;
}

.property-panel {
  height: 600px;
  overflow-y: auto;
}

.property-content {
  padding: 10px 0;
}

.no-selection {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 300px;
  color: #909399;
}

.no-selection-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.options-config {
  padding: 10px 0;
}

.options-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.options-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.option-item {
  display: flex;
  gap: 8px;
  align-items: center;
}

.form-preview {
  padding: 20px;
  background: #fff;
  border-radius: 4px;
  max-height: 500px;
  overflow-y: auto;
}

.el-card {
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.el-collapse {
  border: none;
}

.el-collapse :deep(.el-collapse-item__header) {
  background: #f5f7fa;
  border: none;
  padding-left: 12px;
  font-weight: 500;
}

.el-collapse :deep(.el-collapse-item__content) {
  padding: 12px;
  background: #fff;
}

.el-form-item {
  margin-bottom: 16px;
}

.el-tabs :deep(.el-tabs__header) {
  margin: 0 0 16px 0;
}

.el-tabs :deep(.el-tabs__nav-wrap) {
  padding: 0;
}
</style>