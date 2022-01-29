@echo off

title kernelbot
java -jar kernelbot.jar W

timeout /t 5

del install.bat
del installWizard.bat
exit