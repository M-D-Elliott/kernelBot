@echo off && setlocal enableextensions && cd /d "%~dp0"

CMD /c kill.bat %~1

echo starting %~1 ...

CMD /c start "" "%~2" %~3 %~4

exit