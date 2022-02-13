@echo off

Taskkill /FI "WINDOWTITLE eq mktvbot"

timeout /t 5

@setlocal enableextensions
@cd /d "%~dp0"
@cd ..

CMD /C startbot.bat

exit