<template>
  <div class="form-radio-wrapper">
    <el-form-item
      :label="field.label"
      :prop="field.name"
      :required="field.required"
      :label-width="field.labelWidth || '100px'"
      :class="field.className"
      :style="field.style"
    >
      <el-radio-group
        v-if="!preview"
        :model-value="field.defaultValue"
        :disabled="field.disabled"
        :size="field.size || 'default'"
        @change="handleChange"
      >
        <el-radio
          v-for="option in field.options"
          :key="option.value"
          :label="option.value"
          :disabled="option.disabled"
          :border="field.border"
        >
          {{ option.label }}
        </el-radio>
      </el-radio-group>
      <el-radio-group
        v-else
        :model-value="modelValue"
        :disabled="field.disabled"
        :size="field.size || 'default'"
        @change="$emit('update:modelValue', $event)"
      >
        <el-radio
          v-for="option in field.options"
          :key="option.value"
          :label="option.value"
          :disabled="option.disabled"
          :border="field.border"
        >
          {{ option.label }}
        </el-radio>
      </el-radio-group>
      <div v-if="field.helpText" class="help-text">{{ field.helpText }}</div>
    </el-form-item>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

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
    type: [String, Number, Boolean],
    default: ''
  }
})

const emit = defineEmits(['update', 'update:modelValue'])

const handleChange = (value) => {
  const updatedField = { ...props.field, defaultValue: value }
  emit('update', updatedField)
}
</script>

<style scoped>
.form-radio-wrapper {
  width: 100%;
}

.help-text {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
  line-height: 1.4;
}

.el-radio-group {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}
</style>