<template>
  <div class="form-select-wrapper">
    <el-form-item
      :label="field.label"
      :prop="field.name"
      :required="field.required"
      :label-width="field.labelWidth || '100px'"
      :class="field.className"
      :style="field.style"
    >
      <el-select
        v-if="!preview"
        :model-value="field.defaultValue"
        :placeholder="field.placeholder"
        :disabled="field.disabled"
        :multiple="field.multiple"
        :clearable="field.clearable !== false"
        :filterable="field.filterable"
        :allow-create="field.allowCreate"
        style="width: 100%"
        @change="handleChange"
      >
        <el-option
          v-for="option in field.options"
          :key="option.value"
          :label="option.label"
          :value="option.value"
          :disabled="option.disabled"
        />
      </el-select>
      <el-select
        v-else
        :model-value="modelValue"
        :placeholder="field.placeholder"
        :disabled="field.disabled"
        :multiple="field.multiple"
        :clearable="field.clearable !== false"
        :filterable="field.filterable"
        :allow-create="field.allowCreate"
        style="width: 100%"
        @change="$emit('update:modelValue', $event)"
      >
        <el-option
          v-for="option in field.options"
          :key="option.value"
          :label="option.label"
          :value="option.value"
          :disabled="option.disabled"
        />
      </el-select>
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
    type: [String, Number, Array],
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
.form-select-wrapper {
  width: 100%;
}

.help-text {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
  line-height: 1.4;
}
</style>