<template>
  <div class="form-upload-wrapper">
    <el-form-item
      :label="field.label"
      :prop="field.name"
      :required="field.required"
      :label-width="field.labelWidth || '100px'"
      :class="field.className"
      :style="field.style"
    >
      <el-upload
        v-if="!preview"
        :action="field.action || '/api/upload'"
        :multiple="field.multiple"
        :limit="field.limit || 1"
        :accept="field.accept"
        :list-type="field.listType || 'text'"
        :auto-upload="field.autoUpload !== false"
        :disabled="field.disabled"
        :show-file-list="field.showFileList !== false"
        :drag="field.drag"
        :file-list="field.defaultValue || []"
        @change="handleChange"
        @exceed="handleExceed"
        @error="handleError"
      >
        <template v-if="field.listType === 'picture-card'">
          <el-icon class="avatar-uploader-icon"><Plus /></el-icon>
        </template>
        <template v-else-if="field.drag">
          <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
          <div class="el-upload__text">
            将文件拖到此处，或<em>点击上传</em>
          </div>
        </template>
        <template v-else>
          <el-button type="primary">
            <el-icon><Upload /></el-icon>
            {{ field.buttonText || '选择文件' }}
          </el-button>
        </template>
      </el-upload>
      <el-upload
        v-else
        :action="field.action || '/api/upload'"
        :multiple="field.multiple"
        :limit="field.limit || 1"
        :accept="field.accept"
        :list-type="field.listType || 'text'"
        :auto-upload="field.autoUpload !== false"
        :disabled="field.disabled"
        :show-file-list="field.showFileList !== false"
        :drag="field.drag"
        :file-list="modelValue || []"
        @change="$emit('update:modelValue', $event)"
        @exceed="handleExceed"
        @error="handleError"
      >
        <template v-if="field.listType === 'picture-card'">
          <el-icon class="avatar-uploader-icon"><Plus /></el-icon>
        </template>
        <template v-else-if="field.drag">
          <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
          <div class="el-upload__text">
            将文件拖到此处，或<em>点击上传</em>
          </div>
        </template>
        <template v-else>
          <el-button type="primary">
            <el-icon><Upload /></el-icon>
            {{ field.buttonText || '选择文件' }}
          </el-button>
        </template>
      </el-upload>
      <div v-if="field.helpText" class="help-text">{{ field.helpText }}</div>
    </el-form-item>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Upload, UploadFilled } from '@element-plus/icons-vue'

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
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['update', 'update:modelValue'])

const handleChange = (uploadFile, uploadFiles) => {
  const updatedField = { ...props.field, defaultValue: uploadFiles }
  emit('update', updatedField)
}

const handleExceed = (files, uploadFiles) => {
  ElMessage.warning(`当前限制选择 ${props.field.limit || 1} 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + uploadFiles.length} 个文件`)
}

const handleError = (error, uploadFile, uploadFiles) => {
  ElMessage.error('文件上传失败')
  console.error('Upload error:', error)
}
</script>

<style scoped>
.form-upload-wrapper {
  width: 100%;
}

.help-text {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
  line-height: 1.4;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>