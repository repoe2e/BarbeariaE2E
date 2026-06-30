@echo off
echo ========================================
echo   Barbearia E2E - Iniciando Frontend
echo ========================================
cd frontend
if not exist node_modules (
    echo Instalando dependencias...
    call npm install
)
call npm run dev
