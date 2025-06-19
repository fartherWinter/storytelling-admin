<template>
  <div class="form-checkbox-wrapper">
    <el-form-item
      :label="field.label"
      :prop="field.name"
      :required="field.required"
      :label-width="field.labelWidth || '100px'"
      :class="field.className"
      :style="field.style"
    >
      <el-checkbox-group
        v-if="!preview"
        :model-value="field.defaultValue || []"
        :disabled="field.disabled"
        :size="field.size || 'default'"
        :min="field.min"
        :max="field.max"
        @change="handleChange"
      >
        <el-checkbox
          v-for="option in field.options"
          :key="option.value"
          :label="option.value"
          :disabled="option.disabled"
          :border="field.border"
        >
          {{ option.label }}
        </el-checkbox>
      </el-checkbox-group>
      <el-checkbox-group
        v-else
        :model-value="modelValue || []"
        :disabled="field.disabled"
        :size="field.size || 'default'"
        :min="field.min"
        :max="field.max"
        @change="$emit('update:modelValue', $event)"
      >
        <el-checkbox
          v-for="option in field.options"
          :key="option.value"
          :label="option.value"
          :disabled="option.disabled"
          :border="field.border"
        >
          {{ option.label }}
        </el-checkbox>
      </el-checkbox-group>
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
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['update', 'update:modelValue'])

const handleChange = (value) => {
  const updatedField = { ...props.field, defaultValue: value }
  emit('update', updatedField)
}
</script>

<style scoped>
.form-checkbox-wrapper {
  width: 100%;
}

.help-text {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
  line-height: 1.4;
}

.el-checkbox-group {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}
</style>