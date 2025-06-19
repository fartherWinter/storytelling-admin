<template>
  <div class="form-switch-wrapper">
    <el-form-item
      :label="field.label"
      :prop="field.name"
      :required="field.required"
      :label-width="field.labelWidth || '100px'"
      :class="field.className"
      :style="field.style"
    >
      <el-switch
        v-if="!preview"
        :model-value="field.defaultValue"
        :disabled="field.disabled"
        :active-text="field.activeText"
        :inactive-text="field.inactiveText"
        :active-value="field.activeValue || true"
        :inactive-value="field.inactiveValue || false"
        :active-color="field.activeColor"
        :inactive-color="field.inactiveColor"
        :width="field.width"
        :inline-prompt="field.inlinePrompt"
        @change="handleChange"
      />
      <el-switch
        v-else
        :model-value="modelValue"
        :disabled="field.disabled"
        :active-text="field.activeText"
        :inactive-text="field.inactiveText"
        :active-value="field.activeValue || true"
        :inactive-value="field.inactiveValue || false"
        :active-color="field.activeColor"
        :inactive-color="field.inactiveColor"
        :width="field.width"
        :inline-prompt="field.inlinePrompt"
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
    type: [Boolean, String, Number],
    default: false
  }
})

const emit = defineEmits(['update', 'update:modelValue'])

const handleChange = (value) => {
  const updatedField = { ...props.field, defaultValue: value }
  emit('update', updatedField)
}
</script>

<style scoped>
.form-switch-wrapper {
  width: 100%;
}

.help-text {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
  line-height: 1.4;
}
</style>