<template>
  <div class="form-number-wrapper">
    <el-form-item
      :label="field.label"
      :prop="field.name"
      :required="field.required"
      :label-width="field.labelWidth || '100px'"
      :class="field.className"
      :style="field.style"
    >
      <el-input-number
        v-if="!preview"
        :model-value="field.defaultValue"
        :placeholder="field.placeholder"
        :disabled="field.disabled"
        :min="field.min"
        :max="field.max"
        :step="field.step || 1"
        :precision="field.precision"
        :controls="field.controls !== false"
        :controls-position="field.controlsPosition"
        style="width: 100%"
        @change="handleChange"
      />
      <el-input-number
        v-else
        :model-value="modelValue"
        :placeholder="field.placeholder"
        :disabled="field.disabled"
        :min="field.min"
        :max="field.max"
        :step="field.step || 1"
        :precision="field.precision"
        :controls="field.controls !== false"
        :controls-position="field.controlsPosition"
        style="width: 100%"
        @change="$emit('update:modelValue', $event)"
      />
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
    type: Number,
    default: 0
  }
})

const emit = defineEmits(['update', 'update:modelValue'])

const handleChange = (value) => {
  const updatedField = { ...props.field, defaultValue: value }
  emit('update', updatedField)
}
</script>

<style scoped>
.form-number-wrapper {
  width: 100%;
}

.help-text {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
  line-height: 1.4;
}
</style>