@echo off
chcp 65001 >nul
echo ========================================
echo    智慧自习室系统 - 前端启动脚本
echo ========================================
echo.
echo [1/3] 检查环境...
node -v
if %errorlevel% neq 0 (
    echo ❌ 错误: 未检测到 Node.js 环境，请先安装 Node.js 14+
    pause
    exit /b 1
)
npm -v
if %errorlevel% neq 0 (
    echo ❌ 错误: 未检测到 npm，请检查 Node.js 安装
    pause
    exit /b 1
)
echo.

echo [2/3] 检查依赖是否已安装...
if not exist "node_modules\" (
    echo ⚠️  首次运行，正在安装依赖，可能需要几分钟...
    call npm install --registry=https://registry.npmmirror.com
    if %errorlevel% neq 0 (
        echo ❌ 错误: npm 安装依赖失败
        pause
        exit /b 1
    )
) else (
    echo ✅ 依赖已安装
)
echo.

echo [3/3] 启动 Vue 开发服务器...
echo ✅ 前端服务启动中，请稍候...
echo ✅ 启动成功后请访问: http://localhost:8081
echo.
echo 提示: 按 Ctrl+C 可以停止服务
echo ========================================
echo.
call npm run serve
pause
