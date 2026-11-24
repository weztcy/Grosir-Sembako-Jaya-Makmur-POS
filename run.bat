@echo off
title Grosir Sembako Jaya Makmur - Desktop App
cd /d "%~dp0"

echo ========================================================
echo   MENJALANKAN APLIKASI TRANSAKSI GROSIR SEMBAKO
echo ========================================================

set "JAVA_EXE=C:\Users\Dimas\jdk-17\bin\java.exe"
if not exist "%JAVA_EXE%" (
    set "JAVA_EXE=java"
)

"%JAVA_EXE%" -cp "bin;lib/*" com.grosir.sembako.ui.MainFrame
pause
