@ECHO OFF
SETLOCAL enableextensions enabledelayedexpansion

SET endFileName=all.min.css
DEL !endFileName!

SET i=0
for /R %%F in (*.css) do (
    SET "filePath=%%F"

    SET "curr=!filePath:.css=.min.css!"

    SET "allFiles=!allFiles!+!curr!"
    SET list[!i!]=!curr!
    SET /A i+=1

    CALL uglifycss %%F --output !curr!
)

COPY /B !allFiles:~1! !endFileName!

SET "x=0" 
:SymLoop 
if defined list[%x%] ( 
   call DEL %%list[%x%]%% 
   SET /a "x+=1"
   GOTO :SymLoop 
)

EXIT