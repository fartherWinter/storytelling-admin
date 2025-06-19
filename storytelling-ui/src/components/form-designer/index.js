// 表单设计器组件库入口文件

// 导入所有表单组件
import FormInput from './FormInput.vue'
import FormTextarea from './FormTextarea.vue'
import FormNumber from './FormNumber.vue'
import FormSelect from './FormSelect.vue'
import FormRadio from './FormRadio.vue'
import FormCheckbox from './FormCheckbox.vue'
import FormSwitch from './FormSwitch.vue'
import FormSlider from './FormSlider.vue'
import FormDatePicker from './FormDatePicker.vue'
import FormTimePicker from './FormTimePicker.vue'
import FormUpload from './FormUpload.vue'
import FormRate from './FormRate.vue'
import FormDivider from './FormDivider.vue'
import FormText from './FormText.vue'
import FormGrid from './FormGrid.vue'

// 组件映射表
export const componentMap = {
  'input': FormInput,
  'textarea': FormTextarea,
  'number': FormNumber,
  'select': FormSelect,
  'radio': FormRadio,
  'checkbox': FormCheckbox,
  'switch': FormSwitch,
  'slider': FormSlider,
  'date-picker': FormDatePicker,
  'time-picker': FormTimePicker,
  'upload': FormUpload,
  'rate': FormRate,
  'divider': FormDivider,
  'text': FormText,
  'grid': FormGrid
}

// 组件配置
export const componentConfig = {
  // 基础组件
  basic: [
    {
      type: 'input',
      label: '单行文本',
      icon: 'Edit',
      component: FormInput,
      defaultConfig: {
        label: '单行文本',
        placeholder: '请输入',
        required: false,
        disabled: false,
        readonly: false,
        clearable: true,
        showWordLimit: false,
        maxlength: null,
        minlength: null
      }
    },
    {
      type: 'textarea',
      label: '多行文本',
      icon: 'Document',
      component: FormTextarea,
      defaultConfig: {
        label: '多行文本',
        placeholder: '请输入',
        required: false,
        disabled: false,
        readonly: false,
        clearable: true,
        showWordLimit: false,
        maxlength: null,
        minlength: null,
        rows: 4,
        autosize: false,
        resize: 'vertical'
      }
    },
    {
      type: 'number',
      label: '数字输入',
      icon: 'Sort',
      component: FormNumber,
      defaultConfig: {
        label: '数字输入',
        placeholder: '请输入数字',
        required: false,
        disabled: false,
        min: null,
        max: null,
        step: 1,
        precision: null,
        controls: true,
        controlsPosition: 'right'
      }
    },
    {
      type: 'select',
      label: '下拉选择',
      icon: 'ArrowDown',
      component: FormSelect,
      defaultConfig: {
        label: '下拉选择',
        placeholder: '请选择',
        required: false,
        disabled: false,
        multiple: false,
        clearable: true,
        filterable: false,
        allowCreate: false,
        options: [
          { label: '选项1', value: 'option1' },
          { label: '选项2', value: 'option2' }
        ]
      }
    }
  ],
  
  // 高级组件
  advanced: [
    {
      type: 'radio',
      label: '单选框',
      icon: 'CircleCheck',
      component: FormRadio,
      defaultConfig: {
        label: '单选框',
        required: false,
        disabled: false,
        size: 'default',
        border: false,
        options: [
          { label: '选项1', value: 'option1' },
          { label: '选项2', value: 'option2' }
        ]
      }
    },
    {
      type: 'checkbox',
      label: '多选框',
      icon: 'Check',
      component: FormCheckbox,
      defaultConfig: {
        label: '多选框',
        required: false,
        disabled: false,
        size: 'default',
        min: null,
        max: null,
        border: false,
        options: [
          { label: '选项1', value: 'option1' },
          { label: '选项2', value: 'option2' }
        ]
      }
    },
    {
      type: 'switch',
      label: '开关',
      icon: 'SwitchButton',
      component: FormSwitch,
      defaultConfig: {
        label: '开关',
        required: false,
        disabled: false,
        activeText: '',
        inactiveText: '',
        activeValue: true,
        inactiveValue: false,
        activeColor: '#409eff',
        inactiveColor: '#dcdfe6',
        width: 40,
        inlinePrompt: false
      }
    },
    {
      type: 'slider',
      label: '滑块',
      icon: 'minus',
      component: FormSlider,
      defaultConfig: {
        label: '滑块',
        required: false,
        disabled: false,
        min: 0,
        max: 100,
        step: 1,
        showStops: false,
        showTooltip: true,
        range: false,
        vertical: false,
        height: null,
        marks: null
      }
    },
    {
      type: 'date-picker',
      label: '日期选择',
      icon: 'Calendar',
      component: FormDatePicker,
      defaultConfig: {
        label: '日期选择',
        placeholder: '请选择日期',
        required: false,
        disabled: false,
        readonly: false,
        clearable: true,
        format: 'YYYY-MM-DD',
        valueFormat: 'YYYY-MM-DD',
        type: 'date'
      }
    },
    {
      type: 'time-picker',
      label: '时间选择',
      icon: 'Clock',
      component: FormTimePicker,
      defaultConfig: {
        label: '时间选择',
        placeholder: '请选择时间',
        required: false,
        disabled: false,
        readonly: false,
        clearable: true,
        format: 'HH:mm:ss',
        valueFormat: 'HH:mm:ss',
        isRange: false
      }
    },
    {
      type: 'upload',
      label: '文件上传',
      icon: 'upload',
      component: FormUpload,
      defaultConfig: {
        label: '文件上传',
        action: '/api/upload',
        required: false,
        disabled: false,
        multiple: false,
        limit: 1,
        accept: '',
        listType: 'text',
        autoUpload: true,
        showFileList: true,
        drag: false
      }
    },
    {
      type: 'rate',
      label: '评分',
      icon: 'StarFilled',
      component: FormRate,
      defaultConfig: {
        label: '评分',
        required: false,
        disabled: false,
        max: 5,
        allowHalf: false,
        lowThreshold: 2,
        highThreshold: 4,
        showText: false,
        showScore: false,
        textColor: '#1f2d3d',
        texts: ['极差', '失望', '一般', '满意', '惊喜']
      }
    }
  ],
  
  // 布局组件
  layout: [
    {
      type: 'divider',
      label: '分割线',
      icon: 'Minus',
      component: FormDivider,
      defaultConfig: {
        label: '分割线',
        content: '',
        direction: 'horizontal',
        contentPosition: 'center',
        borderStyle: 'solid'
      }
    },
    {
      type: 'text',
      label: '文本',
      icon: 'Document',
      component: FormText,
      defaultConfig: {
        label: '文本内容',
        content: '这是一段文本内容',
        tag: 'p'
      }
    },
    {
      type: 'grid',
      label: '栅格布局',
      icon: 'Grid',
      component: FormGrid,
      defaultConfig: {
        label: '栅格布局',
        gutter: 20,
        type: 'flex',
        justify: 'start',
        align: 'top',
        columns: [
          { span: 12, fields: [] },
          { span: 12, fields: [] }
        ]
      }
    }
  ]
}

// 获取组件配置
export const getComponentConfig = (type) => {
  const allComponents = [...componentConfig.basic, ...componentConfig.advanced, ...componentConfig.layout]
  return allComponents.find(comp => comp.type === type)
}

// 获取组件实例
export const getComponent = (type) => {
  return componentMap[type]
}

// 导出所有组件
export {
  FormInput,
  FormTextarea,
  FormNumber,
  FormSelect,
  FormRadio,
  FormCheckbox,
  FormSwitch,
  FormSlider,
  FormDatePicker,
  FormTimePicker,
  FormUpload,
  FormRate,
  FormDivider,
  FormText,
  FormGrid
}

// 默认导出
export default {
  componentMap,
  componentConfig,
  getComponentConfig,
  getComponent
}