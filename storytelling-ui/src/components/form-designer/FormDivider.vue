<template>
  <div class="form-divider-wrapper">
    <el-divider
      :direction="field.direction || 'horizontal'"
      :content-position="field.contentPosition || 'center'"
      :border-style="field.borderStyle || 'solid'"
      :class="field.className"
      :style="field.style"
    >
      {{ field.content || field.label }}
    </el-divider>
  </div>
</template>

<script setup>
import { defineProps } from 'vue'

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
</script>

<style scoped>
.form-divider-wrapper {
  width: 100%;
  margin: 16px 0;
}
</style>