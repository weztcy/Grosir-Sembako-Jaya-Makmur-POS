# PowerShell Launcher for Wholesale Grocery Desktop App
$ErrorActionPreference = "Stop"

$jdkPath = "C:\Users\Dimas\jdk-17\bin\java.exe"
if (Test-Path $jdkPath) {
    $java = $jdkPath
} else {
    $java = "java"
}

Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "   MEMULAI APLIKASI TRANSAKSI GROSIR SEMBAKO            " -ForegroundColor Green
Write-Host "========================================================" -ForegroundColor Cyan

& $java -cp "bin;lib/*" com.grosir.sembako.ui.MainFrame
