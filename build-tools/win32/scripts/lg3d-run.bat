@echo off

rem runs lg3d-dev mode, does not work
rem work around for special chars in
rem path is made through changing dir

set original=%CD%
set cmd=%0
set config=%1

set scriptdir=###%cmd%###

set scriptdir=%scriptdir:"###=%
set scriptdir=%scriptdir:###"=%
set scriptdir=%scriptdir:###=%

set scriptdir=%scriptdir:.bat=%
set scriptdir=%scriptdir:\lg3d-run=%
if "%scriptdir%" == "lg3d-run" set scriptdir=.

rem change directory
cd "%scriptdir%"
set scriptdir=.

set scriptdirjava=%scriptdir:\=/%

rem set JAVA_HOME to cobundled jre
set JAVA_HOME=%scriptdir%\..\jre

call %scriptdir%\lg3d-dev %config%


cd "%original%"

