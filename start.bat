@echo off
echo ========================================
echo   Barbearia E2E - Build + Start
echo ========================================

cd /d "%~dp0frontend"
if not exist node_modules (
    echo [1/3] Instalando dependencias do frontend...
    call npm install --registry=https://registry.npmmirror.com
    if errorlevel 1 (
        echo ERRO: npm install falhou. Verifique Node.js instalado.
        pause
        exit /b 1
    )
)

echo [2/3] Build do frontend...
call npm run build
if errorlevel 1 (
    echo ERRO: build falhou.
    pause
    exit /b 1
)

echo [3/3] Copiando para backend e iniciando...
cd /d "%~dp0"
if exist backend\src\main\resources\static rmdir /s /q backend\src\main\resources\static
xcopy /E /I /Y frontend\dist backend\src\main\resources\static

cd backend
echo.
echo Aplicacao disponivel em: http://localhost:8080
echo Admin: admin / admin123
echo.
call mvn spring-boot:run
