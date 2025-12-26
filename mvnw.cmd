@echo off
rem -----------------------------------------------------------------------------
rem Maven Wrapper for Windows
rem -----------------------------------------------------------------------------
setlocal
set SCRIPT_DIR=%~dp0
java -jar "%SCRIPT_DIR%\.mvn\wrapper\maven-wrapper.jar" %*
endlocal
