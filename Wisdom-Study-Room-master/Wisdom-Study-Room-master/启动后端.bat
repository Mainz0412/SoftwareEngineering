@echo off
chcp 65001 >nul
echo ========================================
echo    智慧自习室系统 - 后端启动脚本
echo ========================================
echo.
echo [1/3] 检查环境...
java -version
if %errorlevel% neq 0 (
    echo ❌ 错误: 未检测到 Java 环境，请先安装 JDK 1.8+
    pause
    exit /b 1
)
echo.

echo [2/3] 清理并编译项目...
call mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo ❌ 错误: Maven 编译失败
    pause
    exit /b 1
)
echo.

echo [3/3] 启动 Spring Boot 应用...
echo ✅ 后端服务启动中，请访问: http://localhost:8080
echo.
echo 提示: 按 Ctrl+C 可以停止服务
echo ========================================
echo.
call mvn spring-boot:run
pause
