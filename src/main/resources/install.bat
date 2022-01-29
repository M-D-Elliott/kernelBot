@echo off

title kernelbot
java -jar kernelbot.jar I

timeout /t 5

del install.bat
del installWizard.bat
exit