The Java launcher for Windows v1.22
by Jacob Marner
sep 9th 2003

-----------------------------------------------------

Purpose of this program:
---------------------------------------
Java is a great programming language, but it is
generally somewhat complex for novice end-users to
get the program up running. Traditionally they have 
to install a JRE, install any optional packages like
Java3D, set the path environment variable and then
start a batch file. 

This doesn't exactly seem professional to the Windows
end-user who generally just expect to be able to hit 
an icon or .exe file to run. This launcher adresses this
problem.

This program is open-source under no license at all.
Use it for what ever you want.


Bundle your own Java VM
--------------------------------------------
A common problem with Java on the Windows platform
is that various versions may be installed, including
Microsofts own incompatible one and some of these
may even be installed into the system path. To be 
absolutely sure that your program runs no matter what
I highly recommend that you bundle a Java virtual
machine with your application. Simply take the JRE
directory of your preferred version and ship it with
your program (in a zip file or whatever). I usually
make a JRE subdirectory in my main application folder.


Instructions
-------------------------------------------
Examples are shown in the next section.

In this directory is a file called Launch.exe. This
is the file that end-user will need to execute to
run your Java program.

You can rename it to whatever you want to fit your
purposes. 

When the user starts the exe the following things
will happen.

It will try to open a text file in the current dir with
the same name as the .exe file but with the .cfg extension.
If that was not found it will look for a file named
launcher.cfg. The found file will contain the 
information needed to start the application. You will
need to edit that file.

The first line of the launcher.cfg file is the
current directory you want to use when your program
runs. I usually find it convenient to change the
directory to the root of my classes. If you do not
want to change directory, just set this to the
current directory (.). You can use both relative and
absolute paths.

The second line of the launcher.cfg file is the
executable you want to run. This will usually be
either java.exe or javaw.exe. To ensure that the
correct VM is called you must give the full path
(possibly relative) to the file. If you use a
relative path it must be relative to the *new*
workign directory as set in the first line.

The third line of the launcher.cfg file is the
parameters to the executeable. Say, I have 
packaged my program into a .jar file called
MyCoolGame.jar then I would write: 
-jar MyCoolGame.jar
Note that you can end this line with any
parameters you wish to pass to your program.

And thats that. You are running.

Example 1:
-----------------------------------------

I have a game tetris that I want to distribute on the
net. I have a lot of graphics and sound in it.

Make a directory structure like this:

tetris.exe (renamed version of launcher.exe)
JRE/ (a copy of of JRE 1.3.1 files - you can use any JRE)
launcher.cfg
classes/tetris.jar (the java program)
classes/data/ (the graphics and sound needed for the game)

The set launcher.cfg to this:

classes
..\Jre\1.3.1\Bin\javaw.exe 
-jar tetris.jar

RUN!

Advanced issues:
---------------------------------------------

Desktop icon:
Also you will likely want to make a shortcut
to it from the desktop or start menu. If you use
programs such as InstallShield then this can be done 
for you automatically. If you package your program
with say WinZip (possibly self-extracting) then
users will have to click the .exe file to start.

Changing icon embedded in exe:
To change the icon embedded in launch.exe you need
to recompile the program. The source and project
files for Microsoft Visual C++ can be found in the
source directory. In Visual C++ just replace the
icon with your own one and then recompile.

An issue with native DLLs:
To be sure that your own native DLLs can be 
found you must place them in one of the 
following directories:
* The current directory
* The directory with the executable you started.
   (usually JRE\1.3.1\bin)
If you change the current directory during the 
execution of your program I recommend putting
them in the JRE\1.3.1\bin directory.

Having multiple launchers in the same directory:
When just using launcher.cfg it is impossible to start
several Java applications from within the same directory
since they would all use the same launcher.cfg file. 
To allow this, the launcher will first look for a
file that has the same name as the .exe file but has the
.cfg extension instead. If such a file is found that is
used instead of launcher.cfg. Note that this only works
if the .exe file has no spaces in its name.
   
Error messages:
--------------------------------------------------------
First, let me note that the launcher error messages
are written with the end-user in mind - not you, so
to get the explanations you must read here.

Error message: "Could not find file %s or launcher.cfg. 
Please reinstall the application."
Reason: The file launcher.cfg could not be found
in the working directory. Put it in the same
directory as the exe itself. If you start the
exe via a shortcut make sure that the working
directory set in the shortcut is current.

Error message: "Could not find the directory given 
in %s. Please reinstall the application."
Reason: The directory given in the first line of
the launcher.cfg could not be found. Make sure it
exists.

Error message: "Could not start the application.
Please reinstall the application"
Reason: The executable file given in line two of
launcher.cfg (typically javaw.exe) could not be found 
in the directory given in line 1 of launcher.cfg. Since 
this error is produced after the one stating that the 
directory could not be found (see above) it is most like 
that it is line two in launcher.cfg that is incorrect.


Trouble shooting:
--------------------------------------------------------

Problem: Nothing happens when I start the exe.

Reason 1: You made a console application but started 
it with javaw.exe in line 2 of launcher.cfg. Change it
to java.exe

Reason 2: The java class or jar file you gave to the
launcher in line 3 of launcher.cfg could not be
found or did not contain a main file (class files) or
a manifest file (jar). Line 3 of launcher.cfg should
be the same command line arguments that you use with
java or javaw on the command line to start the application
manually. I.e. if you are used to writing 
java -hotspot myapp
on the command line then line 3 should be
-hotspot myapp

Problem: The custom named configuration file I made could
not be found.

Reason: The .exe file name may not contain any spaces. be
sure to remove them.


Tips:
------------------------------------------------------

Passing arguments to the JVM:

If you need special JVM options simply pass them
by writing them in the beginning of launcher.cfg
line 3 before the jar or class file name.

Passing arguments to java:

If you need to pass arguments to your java program
you can do this in two ways:
1. Write them in the end of the line 3 in launcher.cfg
2. Pass them to the exe file (e.g. launch.exe).
If you pass arguments both places then they are 
concatenated and all passed to your Java application.
The ordering of the arguments in Java is such that
those in launcher.cfg comes first.