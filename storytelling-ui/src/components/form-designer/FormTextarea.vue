<template>
  <div class="form-textarea-wrapper">
    <el-form-item
      :label="field.label"
      :prop="field.name"
      :required="field.required"
      :label-width="field.labelWidth || '100px'"
      :class="field.className"
      :style="field.style"
    >
      <el-input
        v-if="!preview"
        type="textarea"
        :model-value="field.defaultValue"
        :placeholder="field.placeholder"
        :disabled="field.disabled"
        :readonly="field.readonly"
        :rows="field.rows || 3"
        :maxlength="field.maxLength"
        :minlength="field.minLength"
        :show-word-limit="field.maxLength > 0"
        :resize="field.resize || 'vertical'"
        @input="handleInput"
      />
      <el-input
        v-else
        type="textarea"
        :model-value="modelValue"
        :placeholder="field.placeholder"
        :disabled="field.disabled"
        :readonly="field.readonly"
        :rows="field.rows || 3"
        :maxlength="field.maxLength"
        :minlength="field.minLength"
        :show-word-limit="field.maxLength > 0"
        :resize="field.resize || 'vertical'"
        @input="$emit('update:modelValue', $event)"
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
    type: [String, Number],
    default: ''
  }
})

const emit = defineEmits(['update', 'update:modelValue'])

const handleInput = (value) => {
  const updatedField = { ...props.field, defaultValue: value }
  emit('update', updatedField)
}
</script>

<style scoped>
.form-textarea-wrapper {
  width: 100%;
}

.help-text {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
  line-height: 1.4;
}
</style>