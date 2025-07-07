#!/bin/bash
# JVM性能优化启动脚本
# 基于application-performance.yml中的JVM配置

# 设置应用程序路径
APP_HOME="$(cd "$(dirname "$0")/.." && pwd)"
APP_JAR="$APP_HOME/storytelling-admin.jar"
LOG_DIR="$APP_HOME/logs"

# 创建日志目录
mkdir -p "$LOG_DIR"

# JVM内存配置
HEAP_OPTS="-Xms2g -Xmx4g"
METASPACE_OPTS="-XX:MaxMetaspaceSize=512m"
STACK_OPTS="-Xss256k"

# GC配置
GC_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:G1HeapRegionSize=16m"

# 编译器配置
COMPILER_OPTS="-XX:+TieredCompilation"

# 诊断配置
DIAGNOSTIC_OPTS="-XX:+FlightRecorder -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$LOG_DIR/heapdump.hprof"

# GC日志配置
GC_LOG_OPTS="-XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:$LOG_DIR/gc.log"

# JFR配置
JFR_OPTS="-XX:+FlightRecorder -XX:StartFlightRecording=duration=60s,filename=$LOG_DIR/app-profile.jfr"

# 性能监控配置
MONITOR_OPTS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9999 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"

# Spring配置
SPRING_OPTS="-Dspring.profiles.active=performance"

# 组合所有JVM参数
JAVA_OPTS="$HEAP_OPTS $METASPACE_OPTS $STACK_OPTS $GC_OPTS $COMPILER_OPTS $DIAGNOSTIC_OPTS $GC_LOG_OPTS $JFR_OPTS $MONITOR_OPTS $SPRING_OPTS"

echo "Starting application with JVM configuration..."
echo "JAVA_OPTS: $JAVA_OPTS"
echo

# 检查Java是否可用
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    exit 1
fi

# 检查JAR文件是否存在
if [ ! -f "$APP_JAR" ]; then
    echo "Error: Application JAR not found at $APP_JAR"
    exit 1
fi

# 启动应用程序
echo "Starting application..."
java $JAVA_OPTS -jar "$APP_JAR"

# 检查启动结果
if [ $? -eq 0 ]; then
    echo "Application started successfully"
else
    echo "Application failed to start with error code $?"
    exit 1
fi