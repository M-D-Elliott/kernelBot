@echo off && setlocal enableextensions

CMD /c kill.bat %~1

echo starting %~1 ...

CMD /c start "" "%~2" %~3 %~4

exit