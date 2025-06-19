<template>
  <div class="form-rate-wrapper">
    <el-form-item
      :label="field.label"
      :prop="field.name"
      :required="field.required"
      :label-width="field.labelWidth || '100px'"
      :class="field.className"
      :style="field.style"
    >
      <el-rate
        v-if="!preview"
        :model-value="field.defaultValue"
        :disabled="field.disabled"
        :max="field.max || 5"
        :allow-half="field.allowHalf"
        :low-threshold="field.lowThreshold || 2"
        :high-threshold="field.highThreshold || 4"
        :colors="field.colors"
        :void-color="field.voidColor"
        :disabled-void-color="field.disabledVoidColor"
        :icon-classes="field.iconClasses"
        :void-icon-class="field.voidIconClass"
        :disabled-void-icon-class="field.disabledVoidIconClass"
        :show-text="field.showText"
        :show-score="field.showScore"
        :text-color="field.textColor"
        :texts="field.texts"
        :score-template="field.scoreTemplate"
        @change="handleChange"
      />
      <el-rate
        v-else
        :model-value="modelValue"
        :disabled="field.disabled"
        :max="field.max || 5"
        :allow-half="field.allowHalf"
        :low-threshold="field.lowThreshold || 2"
        :high-threshold="field.highThreshold || 4"
        :colors="field.colors"
        :void-color="field.voidColor"
        :disabled-void-color="field.disabledVoidColor"
        :icon-classes="field.iconClasses"
        :void-icon-class="field.voidIconClass"
        :disabled-void-icon-class="field.disabledVoidIconClass"
        :show-text="field.showText"
        :show-score="field.showScore"
        :text-color="field.textColor"
        :texts="field.texts"
        :score-template="field.scoreTemplate"
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
.form-rate-wrapper {
  width: 100%;
}

.help-text {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
  line-height: 1.4;
}
</style>