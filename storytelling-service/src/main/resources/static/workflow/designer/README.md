# Flowable 设计器静态资源目录

此目录用于存放Flowable流程设计器所需的静态资源文件，包括：

- CSS样式文件
- JavaScript脚本
- 图标和图片资源
- 字体文件

请确保在部署应用时，将Flowable UI Modeler的静态资源复制到此目录下，以便设计器能够正常加载和显示。

## 资源结构参考

```
workflow/designer/
├── css/
├── fonts/
├── images/
├── js/
└── index.html
```

## 注意事项

1. 资源路径已在`WorkflowDesignerConfig`类中配置
2. 静态资源映射路径为：`/workflow/designer/**`
3. 确保资源文件的版本与Flowable依赖版本一致