@echo off
REM JVM性能优化启动脚本
REM 基于application-performance.yml中的JVM配置

REM 设置应用程序路径
set APP_HOME=%~dp0..
set APP_JAR=%APP_HOME%\storytelling-admin.jar
set LOG_DIR=%APP_HOME%\logs

REM 创建日志目录
if not exist "%LOG_DIR%" mkdir "%LOG_DIR%"

REM JVM内存配置
set HEAP_OPTS=-Xms2g -Xmx4g
set METASPACE_OPTS=-XX:MaxMetaspaceSize=512m
set STACK_OPTS=-Xss256k

REM GC配置
set GC_OPTS=-XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:G1HeapRegionSize=16m

REM 编译器配置
set COMPILER_OPTS=-XX:+TieredCompilation

REM 诊断配置
set DIAGNOSTIC_OPTS=-XX:+FlightRecorder -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=%LOG_DIR%\heapdump.hprof

REM GC日志配置
set GC_LOG_OPTS=-XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:%LOG_DIR%\gc.log

REM JFR配置
set JFR_OPTS=-XX:+FlightRecorder -XX:StartFlightRecording=duration=60s,filename=%LOG_DIR%\app-profile.jfr

REM 性能监控配置
set MONITOR_OPTS=-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9999 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false

REM Spring配置
set SPRING_OPTS=-Dspring.profiles.active=performance

REM 组合所有JVM参数
set JAVA_OPTS=%HEAP_OPTS% %METASPACE_OPTS% %STACK_OPTS% %GC_OPTS% %COMPILER_OPTS% %DIAGNOSTIC_OPTS% %GC_LOG_OPTS% %JFR_OPTS% %MONITOR_OPTS% %SPRING_OPTS%

echo Starting application with JVM configuration...
echo JAVA_OPTS: %JAVA_OPTS%
echo.

REM 启动应用程序
java %JAVA_OPTS% -jar "%APP_JAR%"

if %ERRORLEVEL% neq 0 (
    echo Application failed to start with error code %ERRORLEVEL%
    pause
) else (
    echo Application started successfully
)