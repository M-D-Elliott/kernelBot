@ECHO OFF
SETLOCAL enableextensions enabledelayedexpansion

SET endFileName=all.min.js
DEL !endFileName!

SET i=0
for /R %%F in (*.js) do (
    SET "filePath=%%F"

    SET "curr=!filePath:.js=.min.js!"

    SET "allFiles=!allFiles!+!curr!"
    SET list[!i!]=!curr!
    SET /A i+=1

    CALL uglifyjs %%F -o !curr!
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


