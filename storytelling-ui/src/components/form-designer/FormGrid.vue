<template>
  <div class="form-grid-wrapper">
    <el-row
      :gutter="field.gutter || 20"
      :type="field.type"
      :justify="field.justify || 'start'"
      :align="field.align || 'top'"
      :tag="field.tag || 'div'"
      :class="field.className"
      :style="field.style"
    >
      <el-col
        v-for="(col, index) in field.columns || []"
        :key="index"
        :span="col.span || 12"
        :offset="col.offset || 0"
        :push="col.push || 0"
        :pull="col.pull || 0"
        :xs="col.xs"
        :sm="col.sm"
        :md="col.md"
        :lg="col.lg"
        :xl="col.xl"
        :tag="col.tag || 'div'"
        :class="col.className"
        :style="col.style"
      >
        <div class="grid-column-content">
          <!-- 在设计模式下显示拖拽区域 -->
          <div
            v-if="!preview"
            class="drop-zone"
            :class="{ 'drag-over': col.dragOver }"
            @dragover.prevent="handleDragOver($event, index)"
            @dragleave="handleDragLeave($event, index)"
            @drop="handleDrop($event, index)"
          >
            <div v-if="!col.fields || col.fields.length === 0" class="empty-zone">
              <el-icon><Plus /></el-icon>
              <span>拖拽组件到此处</span>
            </div>
            <!-- 渲染列中的字段 -->
            <component
              v-for="(childField, childIndex) in col.fields || []"
              :key="childField.id"
              :is="getFieldComponent(childField.type)"
              :field="childField"
              :preview="false"
              @update="(updatedField) => updateChildField(index, childIndex, updatedField)"
              @delete="deleteChildField(index, childIndex)"
            />
          </div>
          <!-- 在预览模式下直接渲染字段 -->
          <template v-else>
            <component
              v-for="childField in col.fields || []"
              :key="childField.id"
              :is="getFieldComponent(childField.type)"
              :field="childField"
              :preview="true"
              :model-value="modelValue[childField.name]"
              @update:model-value="$emit('update:modelValue', { ...modelValue, [childField.name]: $event })"
            />
          </template>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
import { Plus } from '@element-plus/icons-vue'

const props = defineProps({
  field: {
    type: Object,
    required: true
  },
  preview: {
    type: Boolean,
    default: false
  },
  modelValue: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits(['update', 'update:modelValue'])

// 获取字段组件
const getFieldComponent = (type) => {
  const componentMap = {
    'input': 'FormInput',
    'textarea': 'FormTextarea',
    'number': 'FormNumber',
    'select': 'FormSelect',
    'radio': 'FormRadio',
    'checkbox': 'FormCheckbox',
    'switch': 'FormSwitch',
    'slider': 'FormSlider',
    'date-picker': 'FormDatePicker',
    'time-picker': 'FormTimePicker',
    'upload': 'FormUpload',
    'rate': 'FormRate',
    'divider': 'FormDivider',
    'text': 'FormText'
  }
  return componentMap[type] || 'div'
}

// 处理拖拽
const handleDragOver = (event, columnIndex) => {
  event.preventDefault()
  const updatedField = { ...props.field }
  if (!updatedField.columns[columnIndex]) {
    updatedField.columns[columnIndex] = { span: 12, fields: [] }
  }
  updatedField.columns[columnIndex].dragOver = true
  emit('update', updatedField)
}

const handleDragLeave = (event, columnIndex) => {
  const updatedField = { ...props.field }
  if (updatedField.columns[columnIndex]) {
    updatedField.columns[columnIndex].dragOver = false
  }
  emit('update', updatedField)
}

const handleDrop = (event, columnIndex) => {
  event.preventDefault()
  const fieldData = event.dataTransfer.getData('application/json')
  if (fieldData) {
    try {
      const newField = JSON.parse(fieldData)
      const updatedField = { ...props.field }
      if (!updatedField.columns[columnIndex]) {
        updatedField.columns[columnIndex] = { span: 12, fields: [] }
      }
      if (!updatedField.columns[columnIndex].fields) {
        updatedField.columns[columnIndex].fields = []
      }
      updatedField.columns[columnIndex].fields.push(newField)
      updatedField.columns[columnIndex].dragOver = false
      emit('update', updatedField)
    } catch (e) {
      console.error('Failed to parse dropped field data:', e)
    }
  }
}

// 更新子字段
const updateChildField = (columnIndex, fieldIndex, updatedField) => {
  const newField = { ...props.field }
  newField.columns[columnIndex].fields[fieldIndex] = updatedField
  emit('update', newField)
}

// 删除子字段
const deleteChildField = (columnIndex, fieldIndex) => {
  const newField = { ...props.field }
  newField.columns[columnIndex].fields.splice(fieldIndex, 1)
  emit('update', newField)
}
</script>

<style scoped>
.form-grid-wrapper {
  width: 100%;
  margin: 8px 0;
}

.grid-column-content {
  min-height: 60px;
}

.drop-zone {
  min-height: 60px;
  border: 2px dashed #dcdfe6;
  border-radius: 4px;
  padding: 8px;
  transition: all 0.3s;
}

.drop-zone.drag-over {
  border-color: #409eff;
  background-color: #f0f9ff;
}

.empty-zone {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 60px;
  color: #c0c4cc;
  font-size: 14px;
}

.empty-zone .el-icon {
  font-size: 24px;
  margin-bottom: 8px;
}
</style>