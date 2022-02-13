```
%%%%%%%%%%%%%%%%%%%%%%%%%
% Welcome to KernelBot! %
%%%%%%%%%%%%%%%%%%%%%%%%%

-------TABLE OF CONTENTS---------
1. What is Kernelbot?
2. Who wants Kernelbot?
3. Installation guide
4. Configuration guide
5. User's guide
6. Developer's guide
---------------------------------
```


*********************
1. What is Kernelbot?
*********************
```
Kernelbot is a discord bot a bit different than others.
Instead of running on a server like Heroku, Kernelbot
runs on your local machine, the host computer.

Kernelbot allows predefined tasks (defined by you) to be
executed by white listed users via discord.

Kernelbot is capable of three main functionalities:
Press -- presses hotkey(s)
Script -- runs scripts (only .bat in v1.0.0)
Mix -- runs presses or scripts (or mixes?) with waits in between.
```


***********************
2. Who wants Kernelbot?
***********************
```
Kernelbot is for people who host servers. Say you have a server
for the popular video game "Project Zomboid". Say that you have
to run a program to start the server, and/or end a program
to stop the server. This can be done with .bat (batch) files and
windows scheduled tasks.

However, Kernelbot can execute batch files on demand. The name of the
.bat file becomes the command name. So if you have a .bat file
in the myscripts folder called "stopserver.bat" then it will appear
in your Kernelbot's menu as stopserver. So a discord friend simply needs
to type ?stopserver to run the batch file that stop your server.

%%% NOTE: in myscripts I have example .bat files for starting and stopping
an application. You simply need to change the path in startvlc and change
the exe name in stopvlc. Also probably change the script names to accomodate
your users. %%%
```


*********************
3. Installation guide
*********************
```
CHECK IF JAVA IS INSTALLED
-Open the command prompt or other command-line shell.
-Type 'java -version' without quotes and press enter.
-Mine reads:
java version "11.0.8" 2020-07-14 LTS
-For reference make sure to have a java version above 11.0.8, but this may
run in SE 8.

WITH JAVA INSTALLED LOCALLY
-Simply place the jar into its own folder and double click.
install.bat and installWizard.bat will be generated.
-For first time install double-click the installWizard.

NO JAVA INSTALLED --OR-- WRONG JAVA VERSION
-Place the jar into it's own folder along with nojava.7z
-Extract nojava.7z using 7zip or other program.
-For first time install double-click the installWizard.

TO START APPLICATION:
-This application is fully autonomous and self-installs.
-To start you must use startbot.bat even if you have java installed.
-The install or installWizard will provide startbot.bat based on
your install method. The only difference is the java path referenced.
```


**********************
4. Configuration guide
**********************
```
NOTE : FILES WILL BE PLACED IN PROPER POSITIONS BY EITHER INSTALLATION.

%%% config.txt %%%
-This file is used for general settings for the Kernelbot.
-This file must be at the same level as Kernelbot, as generated
by the installWizard.

--USABLE config.txt CONTENTS---

{
  "token" : "my token here.",
  "commandIndicator" : "?",
  "formats" : [ "bat" ],
  "border" : "+",
  "securityLevel" : "PRIVATE",
  "userSessionDuration" : "00:48:00:00",
  "rejectUserMessage" : "You aren't Chris...",
  "onlySecureChannelWarning" : "I won't do that for just anybody...",
  "publicChannelWarning" : "This is a public channel... "
}

--END USABLE config.txt CONTENTS---

ANNOTATED
{
  "token" : "my token here.", #paste your discord bot token here. If you use the wizard it will do this for you.
  "commandIndicator" : "?", #this is used to determine when your bot will respond in public channels. Must be a character, default is ?.
  "formats" : [ "bat" ], #these are the script files the bot will search for on startup.
                          All files must be downstream of myscripts and the bot will search recursively (subfolders).
  "border" : "+", #this determines the border for all menus except for the main help menu.
  "securityLevel" : "PRIVATE", #PUBLIC, PROTECTED, and PRIVATE. PUBLIC means the bot will respond to anyone,
                     PROTECTED means user-based white-list with some password commands, and PRIVATE means
					 PROTECTED and users must sign in every userSessionDuration using their personal password.
  "userSessionDuration" : "00:48:00:00", #this setting is only used in PRIVATE securityLevel. DD:HH:MM:SS. This example is set to 48 hours.
  "rejectUserMessage" : "You aren't Chris...", #This is the response when the bot was listening to the message, but found no matching commands.
  "onlySecureChannelWarning" : "I won't do that for just anybody...", #This is the response when the bot function must be done in a private message to the bot.
  "publicChannelWarning" : "This is a public channel... " #This is what the bot says when you us sensitive commands like change password in a public channel.
}


---REPOS---
%%% repos/hotkeys.txt %%%
-Hotkeys are simply represented by String--String pairs.
-The left string, the hotkey name will be the name that appears
in your bot's press menu.
eg. ab, test, ctrla.
-The right string represents the actual keys to be pressed and in order.
e.g. a+b pushes a down then b down then releases b then releases a.
-Finally notice the hello command in this example. This will cause
the bot to literally type hello on your host computer!
-once configured these are activated with ?stream, etc.

--USABLE hotkeys.txt CONTENTS---

{
  "stream" : "ctrl+f1",
  "ab" : "a+b",
  "test" : "ctrl+a+b+c+d+e+f+g+h",
  "ctrla" : "ctrl+a",
  "hello" : "h+e+l+l+o",
  "startmyserver": "ctrl+f1"
}

--END USABLE hotkeys.txt CONTENTS---


%%% myscripts folder %%%
-Based on what extensions you have specified in config.txt
 your bot will search this folder for script commands.
-Check out my github's example scripts for inspiration!
-The installation gives you a starting script called
 resetbot.


%%% repos/mixes.txt %%%
-Extremely similar to hotkeys.
-instead of specifying hotkeys you must specify
 hotkey or script names
-use w(1000) to create a 1000 ms (1s) wait between commands.
e.g. I create a 5s wait with w(5000) for my live commands.
-once these have been configured you can activate with ?live, etc.
-You can also use these to alias script or hotkey command names!

--USABLE mixes.txt CONTENTS---
{
  "live" : "startvlc+w(5000)+stream",
  "cut" : "stream+stopvlc"
}


%%% repos/secrets.txt %%%
-The bot will respond in discord with the contents
 of repos/secrets.txt when the ?spill command is activated.


%%% repos/users.txt %%%

--USABLE users.txt CONTENTS---
{
  "myDiscordUsername" : {
    "username" : "myDiscordUsername",
    "password" : "myunrelatedpassword",
    "welcome" : "my welcome message!",
    "session" : {
      "endTime" : 0
    }
  },
  "myDiscordUsername" : {
    "username" : "myDiscordUsername",
    "password" : "myfriendsunrelatedpassword",
    "welcome" : "my friends welcome message",
    "session" : {
      "endTime" : 0
    }
  }
}
--END USABLE users.txt CONTENTS---
```


***************
5. User's guide
***************
```
-Once the bot is running simply type ?help
 to see the help menu. If on protected or private
 if you are whitelisted this should work in anybody
 text channel.
-type ?script, ?press, or ?mix to see respective menus.
-type ?command name to run any command such as ?myscripts
 or ?myhotkey or ?mymix
-?press brings up the press help menu but ?press ctrl+f1
 will press ctrl+f1! For easier usability for your users
 though you should use press presets using hotkeys.txt.
-?mix works the same as ?press, and you can even do
 mixes on the fly! Still, presets are better.
-I could certainly allow scripts to work the same as press and mix,
 but allowing even whitelisted users to run unvalidated
 scripts on your computer seems to be a security risk to me!
-?user menu shows various user functions such as whitelisting
 a new user or changing your password! The bot moderator
 of course can do this for you by modifying various files.
 NOTE any modifcation to a txt file requires a bot restart.
```


********************
6. Developer's guide
********************
```
-Most simply Kernelbot is comprised of three main components:

LISTENER
A discord bot listener that sends interacts with the bot code
via a wrapper called APIWrapper. This is so the discord IO could
be swapped with perhaps a Spring API or some command-line interface.

PARSER
A string parser which separates the indicator from the command and following args.

COMMAND MAP
A function map with Strings as keys and functions as values.
The signature is: Map<String,Receivable<APIWrapper>>.
The Receivable interface is like a Java Runnable that has parameters.

-For simplicity each of the major menu sections were separated into controllers;
the larger one BotUserController was also broken up into an explicit repository.
-Each of the major sections have a data object representing them,
though hotkeys and press are just Map<String, String>

-Serialization/Deserialization is done using Jackson. Reflection is used
by passing Jackson a class.


-KernelBot was also broken up into a Kernel (script, mix, press, system)
 and AuthKernel (user) layers. This is so the non-essential user layer
 could easily be removed in future projects.

-Kernelbot heavily relies on jPlus, my general purpose Java library.
 Some of these functions may be improved by other libraries,
 but this allows the bot to have so few dependencies.
```
