@echo off && setlocal enableextensions && cd /d "%~dp0" && cd ..

cmd /c libs\kill.bat kernelbot
timeout /t 5
cmd /c libs\startExe.bat "startbot.bat"

exit