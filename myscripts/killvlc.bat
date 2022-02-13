@echo off

echo stopping vlc...
set progName=vlc.exe
tasklist /fi "imagename eq %progName%" |find ":" > nul
if errorlevel 1 taskkill /f /im "%progName%"

exit