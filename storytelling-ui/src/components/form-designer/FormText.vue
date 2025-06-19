<template>
  <div class="form-text-wrapper">
    <component
      :is="field.tag || 'p'"
      :class="['form-text', field.className]"
      :style="getTextStyle"
      v-html="field.content || field.label"
    />
  </div>
</template>

<script setup>
import { defineProps, computed } from 'vue'

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

const getTextStyle = computed(() => {
  const baseStyle = {
    margin: '8px 0',
    lineHeight: '1.6',
    color: '#606266'
  }
  
  // 根据标签类型设置默认样式
  switch (props.field.tag) {
    case 'h1':
      baseStyle.fontSize = '28px'
      baseStyle.fontWeight = 'bold'
      baseStyle.color = '#303133'
      break
    case 'h2':
      baseStyle.fontSize = '24px'
      baseStyle.fontWeight = 'bold'
      baseStyle.color = '#303133'
      break
    case 'h3':
      baseStyle.fontSize = '20px'
      baseStyle.fontWeight = 'bold'
      baseStyle.color = '#303133'
      break
    case 'h4':
      baseStyle.fontSize = '18px'
      baseStyle.fontWeight = 'bold'
      baseStyle.color = '#303133'
      break
    case 'h5':
      baseStyle.fontSize = '16px'
      baseStyle.fontWeight = 'bold'
      baseStyle.color = '#303133'
      break
    case 'h6':
      baseStyle.fontSize = '14px'
      baseStyle.fontWeight = 'bold'
      baseStyle.color = '#303133'
      break
    case 'small':
      baseStyle.fontSize = '12px'
      baseStyle.color = '#909399'
      break
    case 'strong':
      baseStyle.fontWeight = 'bold'
      baseStyle.color = '#303133'
      break
    case 'em':
      baseStyle.fontStyle = 'italic'
      break
    default:
      baseStyle.fontSize = '14px'
  }
  
  // 合并自定义样式
  if (props.field.style) {
    try {
      const customStyle = typeof props.field.style === 'string' 
        ? JSON.parse(`{${props.field.style.replace(/;/g, ',').replace(/:/g, '":"').replace(/,/g, '","').replace(/"/g, '"')}}`) 
        : props.field.style
      Object.assign(baseStyle, customStyle)
    } catch (e) {
      // 如果解析失败，直接使用字符串样式
      return props.field.style
    }
  }
  
  return baseStyle
})
</script>

<style scoped>
.form-text-wrapper {
  width: 100%;
}

.form-text {
  word-wrap: break-word;
  word-break: break-all;
}

.form-text :deep(a) {
  color: #409eff;
  text-decoration: none;
}

.form-text :deep(a:hover) {
  text-decoration: underline;
}
</style>