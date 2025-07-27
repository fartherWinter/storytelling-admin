@echo off
echo ========================================
echo 分布式链路追踪系统部署脚本
echo ========================================

:: 检查 Docker 是否运行
echo 检查 Docker 服务状态...
docker version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: Docker 服务未运行，请先启动 Docker Desktop
    pause
    exit /b 1
)

:: 创建必要的目录
echo 创建监控配置目录...
if not exist "..\monitoring\grafana\dashboards" mkdir "..\monitoring\grafana\dashboards"
if not exist "..\monitoring\grafana\provisioning\datasources" mkdir "..\monitoring\grafana\provisioning\datasources"
if not exist "..\monitoring\grafana\provisioning\dashboards" mkdir "..\monitoring\grafana\provisioning\dashboards"

:: 停止现有容器
echo 停止现有的链路追踪容器...
docker-compose -f ../docker-compose-tracing.yml down

:: 清理旧的数据卷（可选）
set /p cleanup="是否清理旧的数据卷？这将删除所有历史数据 (y/N): "
if /i "%cleanup%"=="y" (
    echo 清理数据卷...
    docker volume rm storytelling_elasticsearch-data 2>nul
    docker volume rm storytelling_prometheus-data 2>nul
    docker volume rm storytelling_grafana-data 2>nul
)

:: 拉取最新镜像
echo 拉取最新的 Docker 镜像...
docker-compose -f ../docker-compose-tracing.yml pull

:: 启动服务
echo 启动链路追踪服务...
docker-compose -f ../docker-compose-tracing.yml up -d

:: 等待服务启动
echo 等待服务启动...
timeout /t 30 /nobreak >nul

:: 检查服务状态
echo 检查服务状态...
docker-compose -f ../docker-compose-tracing.yml ps

:: 健康检查
echo.
echo 执行健康检查...
echo 检查 Elasticsearch...
curl -f http://localhost:9200/_cluster/health 2>nul
if %errorlevel% equ 0 (
    echo ✓ Elasticsearch 运行正常
) else (
    echo ✗ Elasticsearch 未就绪
)

echo 检查 Zipkin...
curl -f http://localhost:9411/health 2>nul
if %errorlevel% equ 0 (
    echo ✓ Zipkin 运行正常
) else (
    echo ✗ Zipkin 未就绪
)

echo 检查 Kibana...
curl -f http://localhost:5601/api/status 2>nul
if %errorlevel% equ 0 (
    echo ✓ Kibana 运行正常
) else (
    echo ✗ Kibana 未就绪
)

echo 检查 Prometheus...
curl -f http://localhost:9090/-/healthy 2>nul
if %errorlevel% equ 0 (
    echo ✓ Prometheus 运行正常
) else (
    echo ✗ Prometheus 未就绪
)

echo 检查 Grafana...
curl -f http://localhost:3000/api/health 2>nul
if %errorlevel% equ 0 (
    echo ✓ Grafana 运行正常
) else (
    echo ✗ Grafana 未就绪
)

echo.
echo ========================================
echo 链路追踪系统部署完成！
echo ========================================
echo.
echo 服务访问地址：
echo - Zipkin UI:     http://localhost:9411
echo - Kibana:        http://localhost:5601
echo - Prometheus:    http://localhost:9090
echo - Grafana:       http://localhost:3000 (admin/admin123)
echo - Elasticsearch: http://localhost:9200
echo.
echo 注意事项：
echo 1. 首次启动可能需要几分钟时间
echo 2. 如果服务未就绪，请等待片刻后重新检查
echo 3. Grafana 默认用户名/密码: admin/admin123
echo 4. 确保应用服务配置了正确的 Zipkin 地址
echo.
echo 查看日志命令：
echo docker-compose -f ../docker-compose-tracing.yml logs -f [service_name]
echo.
echo 停止服务命令：
echo docker-compose -f ../docker-compose-tracing.yml down
echo.
pause