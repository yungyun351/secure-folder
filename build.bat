@echo off
chcp 65001

set JDK_PATH=C:\Program Files\Java\jdk-21\bin
set APP_NAME=SecureFolder
set JAR_NAME=secure-folder-1.0.jar
set MAIN_CLASS=com.marttapps.securefolder.Main
set VENDOR="Martin T. Studio"

set CURRENT_DIR=%~dp0
set /p APP_VERSION=<%CURRENT_DIR%version.txt

set PATH=%JDK_PATH%;%PATH%

echo 【Build JAR】
call mvnw.cmd clean package

echo 【Remove old runtime】
rmdir /s /q "%CURRENT_DIR%runtime"

echo 【Build runtime】
for /f %%i in ('
  jdeps --ignore-missing-deps --multi-release 21 --print-module-deps target\%JAR_NAME%
') do set MODULES=%%i

jlink ^
 --module-path "%JDK_PATH%\..\jmods" ^
 --add-modules %MODULES% ^
 --strip-debug ^
 --no-header-files ^
 --no-man-pages ^
 --compress=zip-6 ^
 --output runtime

echo 【Remove old dist】
rmdir /s /q "%CURRENT_DIR%dist"

echo 【Build EXE】
jpackage --type app-image ^
 --name %APP_NAME% ^
 --app-version %APP_VERSION% ^
 --input target ^
 --main-jar %JAR_NAME% ^
 --main-class %MAIN_CLASS% ^
 --icon public/icon.ico ^
 --runtime-image runtime ^
 --dest dist ^
 --vendor %VENDOR%

echo 【Finish】

pause