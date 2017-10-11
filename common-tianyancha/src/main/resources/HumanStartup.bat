@echo on

taskkill /F /IM geckodriver.exe

set EXTRACT_HOME=.


set CLASSPATH=.;

FOR %%F IN (%EXTRACT_HOME%\lib\*.jar) DO call :addcp %%F


goto extlibe

:addcp
set CLASSPATH=%CLASSPATH%;%1
goto :eof

:extlibe
java -Xms512M -Xmx1024M com.kf.data.tianyancha.HumansApp
pause
