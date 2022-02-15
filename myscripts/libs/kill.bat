@echo off
echo stopping %~1 ...
tasklist /fi "imagename eq %~1" |find ":" > nul
if errorlevel 1 taskkill /f /im "%~1"