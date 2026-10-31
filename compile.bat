@echo off
title Compile Grosir Sembako
cd /d "%~dp0"

echo Mengompilasi proyek Grosir Sembako...
set "JAVAC_EXE=C:\Users\Dimas\jdk-17\bin\javac.exe"
if not exist "%JAVAC_EXE%" set "JAVAC_EXE=javac"

if not exist bin mkdir bin

powershell -Command "Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { '\"' + ($_.FullName -replace '\\', '/') + '\"' } | Set-Content -Encoding ascii sources.txt"
"%JAVAC_EXE%" -encoding UTF-8 -cp "lib/*" -d bin @sources.txt

if exist sources.txt del /f /q sources.txt

if %ERRORLEVEL% equ 0 (
    echo Kompilasi Berhasil! Siap dijalankan dengan run.bat
) else (
    echo Kompilasi Gagal. Periksa pesan error di atas.
)
