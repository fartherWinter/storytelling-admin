<template>
  <div class="form-slider-wrapper">
    <el-form-item
      :label="field.label"
      :prop="field.name"
      :required="field.required"
      :label-width="field.labelWidth || '100px'"
      :class="field.className"
      :style="field.style"
    >
      <el-slider
        v-if="!preview"
        :model-value="field.defaultValue"
        :disabled="field.disabled"
        :min="field.min || 0"
        :max="field.max || 100"
        :step="field.step || 1"
        :show-stops="field.showStops"
        :show-tooltip="field.showTooltip !== false"
        :format-tooltip="field.formatTooltip"
        :range="field.range"
        :vertical="field.vertical"
        :height="field.height"
        :marks="field.marks"
        style="width: 100%"
        @change="handleChange"
      />
      <el-slider
        v-else
        :model-value="modelValue"
        :disabled="field.disabled"
        :min="field.min || 0"
        :max="field.max || 100"
        :step="field.step || 1"
        :show-stops="field.showStops"
        :show-tooltip="field.showTooltip !== false"
        :format-tooltip="field.formatTooltip"
        :range="field.range"
        :vertical="field.vertical"
        :height="field.height"
        :marks="field.marks"
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
    type: [Number, Array],
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
.form-slider-wrapper {
  width: 100%;
}

.help-text {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
  line-height: 1.4;
}
</style>