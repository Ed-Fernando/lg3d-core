@echo off
set debug=1
set cmd=%0
set fullpath=%~pd0
set config=%1

set scriptdir=%cmd:.bat=%
set scriptdir=%scriptdir:\setup=%
if "%scriptdir%" == "setup" set scriptdir=.
set scriptdirjava=%scriptdir:\=/%

rem if "%lgdir%" neq "" goto printenv

set logfile=lgserver.log

if exist "%scriptdir%\..\bin" goto else
    set lgdir=%scriptdir%\..\..
    set lgcoreextdir=%lgdir%\ext
    set fullpath_extdir=%fullpath%..\..\ext
    set CLASSPATH="%lgdir%\build\current\debug\lib\lg3d-core.jar;%lgdir%\build\current\debug\lib\lg3d-demo-apps.jar;%lgcoreextdir%\j3d-contrib-utils.jar;%lgcoreextdir%\escher-0.2.2.lg.jar;%lgcoreextdir%\satin-v2.3.jar;%lgcoreextdir%\app\bgmanager.jar"
    set BOOTCLASSPATH="%lgdir%\build\current\debug\lib\lg3d-awt-toolkit.jar"
    goto endif
:else
    set lgdir=%scriptdir%\..
    set lgcoreextdir=%lgdir%\ext
    set fullpath_extdir=%fullpath%..\ext
    set CLASSPATH="%lgdir%\lib\lg3d-core.jar;%lgdir%\lib\lg3d-demo-apps.jar;%lgcoreextdir%\j3d-contrib-utils.jar;%lgcoreextdir%\escher-0.2.2.lg.jar;%lgcoreextdir%\satin-v2.3.jar;%lgcoreextdir%\app\bgmanager.jar"
    set BOOTCLASSPATH="%lgdir%\lib\lg3d-awt-toolkit.jar"
:endif

set lgdirjava=%lgdir:\=/%

set TOOLKIT=-Dawt.toolkit=org.jdesktop.lg3d.awt.wlg3dtoolkit

if not "%JAVA_HOME%" == "" goto endif1
    echo Error: JAVA_HOME is not set.
    echo Set the JAVA_HOME environment variable to the JDK 1.5.0 
    echo installation directory (e.g., c:\progra~1\java\jdk1.5.0)
    goto exit:
:endif1

if exist "%JAVA_HOME%" goto endif2
    echo Error: JAVA_HOME (%JAVA_HOME%) does not exist. 
    echo Install JDK 1.5.0 and set the JAVA_HOME environment variable to the 
    echo JDK installation directory (e.g., c:\progra~1\java\jdk1.5.0)
    goto exit:
:endif2

set CLASSPATH=%CLASSPATH:\=/%


if not "%LGCORESRC%" == "" goto endif6
    set LGCORESRC=%lgdir%\..\src
:endif6

if not "%LGCONFIG%" == "" goto endif7
    if not exist "%lgdir%\src\etc\lg3d\lgconfig_1p_x.xml" goto else8
        set LGCONFIG=%lgdir%\src\etc\lg3d\lgconfig_1p_x.xml
        goto endif8
:else8
        set LGCONFIG=%lgdir%\etc\lg3d\lgconfig_1p_x.xml
:endif8
:endif7

set PATH=%JAVA_HOME%\bin;%PATH%

set DISPLAY=:0.0

set RMI_PORT=44817

:: JVM arguments for GC configuration
set compiler=-client
:: set compiler=-server
set heap=-Xms128m -Xmx256m
:: set heap=-XX:PermSize=16m -XX:MaxPermSize=24m -XX:NewSize=128m -XX:MaxNewSize=128m -Xms256m -Xmx256m
:: set heap=-Xms32m -Xmx384m
:: set heap=-Xmx512m
set collector=-XX:+UseConcMarkSweepGC -XX:+DisableExplicitGC
:: set collector=-Xincgc
set other=
:: set other=-XX:MaxGCPauseMillis=20 -XX:CompileThreshold=500
set app_codebase=-Dlg.appcodebase=file://%lgcoreextdir%/app
set jvmargs=%compiler% %heap% %collector% %app_codebase% %other%
set app_codebase=-Dlg.appcodebase="file:///%fullpath_extdir%/app"

echo.
echo Starting up Project Looking Glass...
echo.


:printenv
if not "%debug%" == "1" goto exit
    echo Log file         : %logfile%
    echo LG3D Dir         : %lgdir%
    echo JAVA_HOME        : %JAVA_HOME%
    echo CLASSPATH        : %CLASSPATH%
    echo App Code Base    : %app_codebase%
    echo.
:exit
