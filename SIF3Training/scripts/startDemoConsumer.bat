@echo off
set LIB_PATH=..\lib
set CONFIG_PATH=..\config
set WAR_LIB_PATH=..\war\WEB-INF\lib

set EXE_JAR_PATH=..\build\dist

set JVM_SETTINGS=

rem ####################################
rem # JVM settings for proxy tunneling
rem ####################################
rem set JVM_SETTINGS=%JVM_SETTINGS% -Dhttp.proxyHost=<IP ADDRESS> -Dhttp.proxyPort=<PORT> -Dhttps.proxyHost=<IP ADDRESS> -Dhttps.proxyPort=<PORT>

rem ####################################
rem # JVM settings for ignore proxy IPs
rem ####################################
rem set NO_PROXY=-Dhttp.nonProxyHosts=<MACHINE NAME>|<MACHINE NAME>|...

set NO_PROXY=
set JVM_SETTINGS=%JVM_SETTINGS% %NO_PROXY%

rem ##############################
rem # JVM Memory settings
rem ##############################

set JVM_SETTINGS=%JVM_SETTINGS% -Xms128m -Xmx512m -Xss256k -XX:MaxPermSize=64m

rem #######################################
rem # Class Path including all libraries
rem #######################################

set SERVICE_CLASS_PATH=

SETLOCAL ENABLEDELAYEDEXPANSION
for /f %%a IN ('dir /b /S %LIB_PATH%\jaxb\*.jar') do set SERVICE_CLASS_PATH=!SERVICE_CLASS_PATH!;%%a
for /f %%a IN ('dir /b /S %LIB_PATH%\jersey\JBoss6\*.jar') do set SERVICE_CLASS_PATH=!SERVICE_CLASS_PATH!;%%a
for /f %%a IN ('dir /b /S %WAR_LIB_PATH%\*.jar') do set SERVICE_CLASS_PATH=!SERVICE_CLASS_PATH!;%%a

for /f %%a IN ('dir /b /S %EXE_JAR_PATH%\sif3training-v1.0.jar') do set SERVICE_CLASS_PATH=!SERVICE_CLASS_PATH!;%%a

rem ######################################################################
rem # set the config dir and the main executable jar in the classpath
rem #######################################################################

set CONFIG_PATH=%CONFIG_PATH%;%CONFIG_PATH%\hibernate;%CONFIG_PATH%\consumers

set SERVICE_CLASS_PATH=%SERVICE_CLASS_PATH%;%CONFIG_PATH%;

set SERVICE_ID=%1%
set PROP_FILE_NAME=%2%

echo ======================================================================================================
echo Start Service with JVM Settings:
echo %JVM_SETTINGS%
echo ======================================================================================================
echo Start Service with Classpath:
echo %SERVICE_CLASS_PATH%
echo ======================================================================================================

%JAVA_HOME%\bin\java %JVM_SETTINGS% -cp %SERVICE_CLASS_PATH% sif3demo.service.DemoConsumer