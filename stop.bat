@echo off
setlocal enabledelayedexpansion

cd /d "%~dp0"

set "ARG=%~1"
if /i "%ARG%"=="backend"  goto :stop_backend
if /i "%ARG%"=="back"     goto :stop_backend
if /i "%ARG%"=="b"        goto :stop_backend
if /i "%ARG%"=="frontend" goto :stop_frontend
if /i "%ARG%"=="front"    goto :stop_frontend
if /i "%ARG%"=="f"        goto :stop_frontend
if /i "%ARG%"=="all"      goto :stop_all
if /i "%ARG%"=="both"     goto :stop_all
if /i "%ARG%"=="a"        goto :stop_all
if not "%ARG%"=="" (
    echo Opcao invalida: %ARG%
    echo.
    goto :menu
)
if "%ARG%"=="" goto :menu

:menu
echo ========================================
echo   Barbearia E2E - Parar aplicacao
echo ========================================
echo.
echo   1 - Backend  (porta 8080)
echo   2 - Frontend (porta 5173)
echo   3 - Ambos
echo   0 - Sair
echo.
set /p OPCAO="Escolha uma opcao: "

if "%OPCAO%"=="1" goto :stop_backend
if "%OPCAO%"=="2" goto :stop_frontend
if "%OPCAO%"=="3" goto :stop_all
if "%OPCAO%"=="0" exit /b 0
echo Opcao invalida.
goto :menu

:stop_backend
echo.
echo [Backend] Encerrando processo na porta 8080...
call :kill_port 8080
goto :done

:stop_frontend
echo.
echo [Frontend] Encerrando processo na porta 5173...
call :kill_port 5173
goto :done

:stop_all
echo.
echo [Backend] Encerrando processo na porta 8080...
call :kill_port 8080
echo [Frontend] Encerrando processo na porta 5173...
call :kill_port 5173
goto :done

:kill_port
set "PORT=%~1"
set "FOUND=0"
set "KILLED="
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":%PORT% " ^| findstr LISTENING') do (
    echo !KILLED! | findstr /C:" %%a " >nul || (
        set "KILLED=!KILLED! %%a "
        set "FOUND=1"
        echo   PID %%a encerrado.
        taskkill /PID %%a /F >nul 2>&1
    )
)
if "!FOUND!"=="0" (
    echo   Nenhum processo encontrado na porta %PORT%.
)
exit /b 0

:done
echo.
echo Concluido.
pause
exit /b 0
