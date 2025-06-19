<template>
  <div class="form-time-picker-wrapper">
    <el-form-item
      :label="field.label"
      :prop="field.name"
      :required="field.required"
      :label-width="field.labelWidth || '100px'"
      :class="field.className"
      :style="field.style"
    >
      <el-time-picker
        v-if="!preview"
        :model-value="field.defaultValue"
        :placeholder="field.placeholder"
        :disabled="field.disabled"
        :readonly="field.readonly"
        :clearable="field.clearable !== false"
        :format="field.format || 'HH:mm:ss'"
        :value-format="field.valueFormat || 'HH:mm:ss'"
        :start-placeholder="field.startPlaceholder"
        :end-placeholder="field.endPlaceholder"
        :range-separator="field.rangeSeparator || '至'"
        :is-range="field.isRange"
        :arrow-control="field.arrowControl"
        style="width: 100%"
        @change="handleChange"
      />
      <el-time-picker
        v-else
        :model-value="modelValue"
        :placeholder="field.placeholder"
        :disabled="field.disabled"
        :readonly="field.readonly"
        :clearable="field.clearable !== false"
        :format="field.format || 'HH:mm:ss'"
        :value-format="field.valueFormat || 'HH:mm:ss'"
        :start-placeholder="field.startPlaceholder"
        :end-placeholder="field.endPlaceholder"
        :range-separator="field.rangeSeparator || '至'"
        :is-range="field.isRange"
        :arrow-control="field.arrowControl"
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
    type: [String, Date, Array],
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
.form-time-picker-wrapper {
  width: 100%;
}

.help-text {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
  line-height: 1.4;
}
</style>