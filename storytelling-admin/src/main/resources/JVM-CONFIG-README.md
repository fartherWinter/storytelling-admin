# JVM性能配置指南

本文档介绍如何配置和使用JVM性能监控功能。

## 配置文件说明

### application-performance.yml

该文件包含了完整的JVM性能配置，主要包括以下部分：

#### 1. JVM性能监控配置

```yaml
jvm:
  performance:
    # 启用JVM性能监控
    monitoring:
      enabled: true
      collection-interval: 30000  # 30秒收集一次
```

#### 2. 内存监控配置

```yaml
    # 内存监控配置
    memory:
      # 内存使用率监控
      usage-ratio:
        enabled: true
        warning-threshold: 0.8   # 80%警告阈值
        critical-threshold: 0.9  # 90%严重阈值
        
      # 堆内存配置
      heap:
        initial-size: "2g"       # 初始堆大小
        max-size: "4g"           # 最大堆大小
        
      # 非堆内存配置
      non-heap:
        max-metaspace: "512m"    # 最大元空间
```

#### 3. GC配置

```yaml
    # GC配置
    gc:
      # GC算法选择
      algorithm: "G1"           # G1GC
      
      # G1GC特定配置
      g1:
        max-pause-millis: 200    # 最大暂停时间200ms
        heap-region-size: "16m"  # 堆区域大小
        
      # GC监控
      monitoring:
        enabled: true
        log-gc: true
        gc-time-threshold: 100   # GC时间阈值(ms)
```

#### 4. 线程监控配置

```yaml
    # 线程监控配置
    threads:
      monitoring:
        enabled: true
        max-thread-count: 200    # 最大线程数警告阈值
        deadlock-detection: true # 死锁检测
```

#### 5. CPU监控配置

```yaml
    # CPU监控配置
    cpu:
      monitoring:
        enabled: true
        usage-threshold: 0.8     # CPU使用率阈值
```

#### 6. 诊断配置

```yaml
    # 诊断配置
    diagnostics:
      flight-recorder: true     # 启用JFR
      heap-dump-on-oom: true   # OOM时生成堆转储
      heap-dump-path: "logs/heapdump.hprof"
```

## 使用方法

### 1. 启用性能配置

在启动应用时，激活performance配置文件：

```bash
java -jar storytelling-admin.jar --spring.profiles.active=performance
```

### 2. 使用启动脚本

#### Windows环境

```cmd
cd src/main/resources/scripts
start-with-jvm-config.bat
```

#### Linux/Mac环境

```bash
cd src/main/resources/scripts
chmod +x start-with-jvm-config.sh
./start-with-jvm-config.sh
```

### 3. 自定义JVM参数

如果需要自定义JVM参数，可以通过环境变量设置：

```bash
export JAVA_OPTS="-Xms4g -Xmx8g -XX:+UseG1GC"
java $JAVA_OPTS -jar storytelling-admin.jar
```

## 监控指标

### 1. 内存指标

- `jvm.memory.used.ratio`: 内存使用率
- `jvm.memory.heap.used`: 堆内存使用量
- `jvm.memory.heap.max`: 最大堆内存

### 2. GC指标

- `jvm.gc.time`: GC执行时间
- `jvm.gc.count`: GC执行次数

### 3. 线程指标

- `jvm.threads.count`: 当前线程数
- `jvm.threads.peak`: 峰值线程数

### 4. CPU指标

- `system.cpu.usage`: CPU使用率
- `system.cpu.count`: 可用处理器数

## 性能调优建议

### 1. 内存调优

- 根据应用实际需求调整堆内存大小
- 监控内存使用率，避免频繁GC
- 适当调整元空间大小

### 2. GC调优

- 使用G1GC适合大堆内存应用
- 调整最大暂停时间以平衡吞吐量和延迟
- 启用GC日志进行分析

### 3. 线程调优

- 监控线程数量，避免创建过多线程
- 启用死锁检测，及时发现问题
- 合理配置线程池大小

### 4. 监控和诊断

- 启用JFR进行性能分析
- 配置堆转储以分析内存泄漏
- 使用JMX进行远程监控

## 故障排查

### 1. 内存问题

- 检查堆转储文件分析内存泄漏
- 查看GC日志分析GC行为
- 监控内存使用趋势

### 2. 性能问题

- 使用JFR分析CPU热点
- 检查线程状态和死锁
- 分析GC暂停时间

### 3. 配置问题

- 检查配置文件语法
- 验证JVM参数有效性
- 查看应用启动日志

## 注意事项

1. 生产环境建议降低链路追踪采样率
2. 根据实际硬件资源调整内存配置
3. 定期分析GC日志和性能指标
4. 建议在测试环境先验证配置效果
5. 监控应用启动时间和运行稳定性