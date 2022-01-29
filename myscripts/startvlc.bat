:: turn off auto-logging of all cmd function calls.
@echo off

:: allow the batch file to use extensions, which includes calling other batch files.
@setlocal enableextensions
:: in case the batch was called as a super user, or from an unexpected folder,
:: set the cmd working directory to be the same as the batch file's location.
@cd /d "%~dp0"
:: call stopVLC.bat first to make sure only 1 vlc is running at a time.
CMD /C stopVLC.bat

:: remind the code user that vlc is starting.
echo starting vlc...
:: invoke cmd.exe to /c start vlc with an empty "" window string.
:: This method of starting any application works from any
:: working dur and works when another language, like Java,
:: calls the batch file! Very important for the bot.
CMD /c start "" "C:\Program Files (x86)\VideoLAN\VLC\vlc.exe"

exit