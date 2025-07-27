@echo off
echo ========================================
echo 分布式链路追踪系统验证脚本
echo ========================================

:: 设置变量
set ZIPKIN_URL=http://localhost:9411
set GATEWAY_URL=http://localhost:8080
set API_URL=http://localhost:8081
set ADMIN_URL=http://localhost:8082

:: 检查基础服务
echo 1. 检查基础服务状态...
echo.

echo 检查 Zipkin 服务...
curl -s -o nul -w "HTTP Status: %%{http_code}\n" %ZIPKIN_URL%/health
if %errorlevel% neq 0 (
    echo ✗ Zipkin 服务不可用
    goto :error
) else (
    echo ✓ Zipkin 服务正常
)

echo 检查 Elasticsearch...
curl -s -o nul -w "HTTP Status: %%{http_code}\n" http://localhost:9200/_cluster/health
if %errorlevel% neq 0 (
    echo ✗ Elasticsearch 服务不可用
    goto :error
) else (
    echo ✓ Elasticsearch 服务正常
)

echo 检查 Prometheus...
curl -s -o nul -w "HTTP Status: %%{http_code}\n" http://localhost:9090/-/healthy
if %errorlevel% neq 0 (
    echo ✗ Prometheus 服务不可用
    goto :error
) else (
    echo ✓ Prometheus 服务正常
)

echo 检查 Grafana...
curl -s -o nul -w "HTTP Status: %%{http_code}\n" http://localhost:3000/api/health
if %errorlevel% neq 0 (
    echo ✗ Grafana 服务不可用
    goto :error
) else (
    echo ✓ Grafana 服务正常
)

echo.
echo 2. 检查应用服务状态...
echo.

echo 检查 Gateway 服务...
curl -s -o nul -w "HTTP Status: %%{http_code}\n" %GATEWAY_URL%/actuator/health 2>nul
if %errorlevel% neq 0 (
    echo ⚠ Gateway 服务不可用（可能未启动）
) else (
    echo ✓ Gateway 服务正常
)

echo 检查 API 服务...
curl -s -o nul -w "HTTP Status: %%{http_code}\n" %API_URL%/actuator/health 2>nul
if %errorlevel% neq 0 (
    echo ⚠ API 服务不可用（可能未启动）
) else (
    echo ✓ API 服务正常
)

echo 检查 Admin 服务...
curl -s -o nul -w "HTTP Status: %%{http_code}\n" %ADMIN_URL%/actuator/health 2>nul
if %errorlevel% neq 0 (
    echo ⚠ Admin 服务不可用（可能未启动）
) else (
    echo ✓ Admin 服务正常
)

echo.
echo 3. 生成测试链路数据...
echo.

:: 生成一些测试请求
echo 发送测试请求到 Gateway...
for /l %%i in (1,1,5) do (
    curl -s -o nul %GATEWAY_URL%/actuator/health 2>nul
    echo 请求 %%i 已发送
    timeout /t 1 /nobreak >nul
)

echo 发送测试请求到 API 服务...
for /l %%i in (1,1,3) do (
    curl -s -o nul %API_URL%/actuator/health 2>nul
    echo API 请求 %%i 已发送
    timeout /t 1 /nobreak >nul
)

echo 发送测试请求到 Admin 服务...
for /l %%i in (1,1,3) do (
    curl -s -o nul %ADMIN_URL%/actuator/health 2>nul
    echo Admin 请求 %%i 已发送
    timeout /t 1 /nobreak >nul
)

echo.
echo 等待链路数据传输到 Zipkin...
timeout /t 10 /nobreak >nul

echo.
echo 4. 验证链路数据...
echo.

:: 检查 Zipkin 中的链路数据
echo 查询 Zipkin 中的服务列表...
curl -s %ZIPKIN_URL%/api/v2/services > services.json 2>nul
if %errorlevel% neq 0 (
    echo ✗ 无法获取服务列表
    goto :error
)

:: 检查文件内容
findstr /c:"gateway" services.json >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ 发现 Gateway 服务链路数据
) else (
    echo ⚠ 未发现 Gateway 服务链路数据
)

findstr /c:"storytelling-api" services.json >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ 发现 API 服务链路数据
) else (
    echo ⚠ 未发现 API 服务链路数据
)

findstr /c:"storytelling-admin" services.json >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ 发现 Admin 服务链路数据
) else (
    echo ⚠ 未发现 Admin 服务链路数据
)

:: 清理临时文件
del services.json 2>nul

echo.
echo 5. 检查监控指标...
echo.

echo 检查 Prometheus 指标...
curl -s "http://localhost:9090/api/v1/query?query=up" > metrics.json 2>nul
if %errorlevel% neq 0 (
    echo ✗ 无法获取 Prometheus 指标
) else (
    echo ✓ Prometheus 指标正常
)

:: 清理临时文件
del metrics.json 2>nul

echo.
echo ========================================
echo 验证完成！
echo ========================================
echo.
echo 访问链路追踪系统：
echo - Zipkin UI:     %ZIPKIN_URL%
echo - Kibana:        http://localhost:5601
echo - Prometheus:    http://localhost:9090
echo - Grafana:       http://localhost:3000
echo.
echo 使用说明：
echo 1. 在 Zipkin UI 中可以查看完整的链路追踪信息
echo 2. 在 Grafana 中可以查看监控仪表板
echo 3. 在 Prometheus 中可以查看原始指标数据
echo 4. 在 Kibana 中可以分析日志数据
echo.
echo 如果某些服务显示未发现链路数据，请：
echo 1. 确保应用服务已启动
echo 2. 确保应用配置了正确的 Zipkin 地址
echo 3. 发送一些业务请求以生成链路数据
echo 4. 等待几分钟后重新验证
echo.
goto :end

:error
echo.
echo ========================================
echo 验证失败！
echo ========================================
echo.
echo 请检查：
echo 1. Docker 服务是否正常运行
echo 2. 链路追踪服务是否已启动
echo 3. 网络连接是否正常
echo 4. 端口是否被占用
echo.
echo 重新部署命令：
echo scripts\deploy-tracing.bat
echo.

:end
pause