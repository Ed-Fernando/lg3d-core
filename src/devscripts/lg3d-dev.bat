@echo off
:: 
:: lg3d-dev.bat 
::  
:: Run the LG desktop in "development mode" as a normal
:: application (in a window) under the control of another
:: window system. This mode does not support native window
:: applications; only 3D applications are supported.
:: 
:: lg3d-dev.bat [<options>] [<config-file>]
::               -f     : Run in the no-border-full-screen mode
:: 

set cmd=%0

if "%1" == "-f" goto endif01
  set dispconfig=j3d1x1
  set config=%1
  goto endif02
:endif01
  set dispconfig=j3d1x1-nbfs
  set config=%2
:endif02

set scriptdir=%cmd:.bat=%
set scriptdir=%scriptdir:\lg3d-dev=%
if "%scriptdir%" == "lg3d-dev" set scriptdir=.
set scriptdirjava=%scriptdir:\=/%

call %scriptdir%\setup

if "%config%" == "" goto endif11
    if exist "%scriptdir%\..\etc\lg3d\%config%" goto endif12
:endif11
    set config=lgconfig_1p_nox.xml
:endif12

set LGCONFIG=file:%scriptdirjava%/../etc/lg3d/%config%
set DISP_CONFIG=-Dlg.displayconfigurl=file:%scriptdirjava%/../etc/lg3d/displayconfig/%dispconfig%

:: comment out the following until we start uring RMI again
:: start "rmiregistry" /min "%JAVA_HOME%/bin/rmiregistry" %RMI_PORT%

:: setup basic configurations
set lgsettings=-Dj3d.sortShape3DBounds="true" %app_codebase% -Dlg.configurl=%LGCONFIG% -Dlg.configurl=%LGCONFIG% %DISP_CONFIG% -Dlg.etcdir=%scriptdirjava%/../etc/ -Dlg.resourcedir=%scriptdirjava%/../resources/ -Dlg.dir=%lgdir% 

:: enable LG3D/AWT toolkit implementation
set lgsettings=%lgsettings% -Dlg.use3dtoolkit=true %TOOLKIT%

set javacmd="%JAVA_HOME%\bin\java" -Xbootclasspath/a:%BOOTCLASSPATH% %jvmargs% %lgsettings% -cp %CLASSPATH% org.jdesktop.lg3d.displayserver.Main

echo LG3D Dir         : %lgdir%              > %logfile%
echo LGCONFIG         : %LGCONFIG%          >> %logfile%
echo JAVA_HOME        : %JAVA_HOME%         >> %logfile%
echo CLASSPATH        : %CLASSPATH%         >> %logfile%
echo PATH             : %PATH%              >> %logfile%
echo javacmd          : %javacmd%           >> %logfile%
echo lgsettings       : %lgsessings%        >> %logfile%

%javacmd% >> lgserver.log 2<&1

rem Many users reported it as an issue
rem | findstr "SEVERE WARNING ClassVersionError"
:exit
