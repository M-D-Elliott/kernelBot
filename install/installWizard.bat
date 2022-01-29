@echo off

title kernelbot
jre\bin\java11.exe -jar kernelbot.jar W J

timeout /t 5

del install.bat
del installWizard.bat
exit