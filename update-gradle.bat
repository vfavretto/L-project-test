@echo off
echo Atualizando Gradle Wrapper para versao 8.11.1...
gradlew.bat wrapper --gradle-version=8.11.1 --distribution-type=bin
echo.
echo Gradle Wrapper atualizado com sucesso!
echo Execute 'gradlew.bat build' para testar.
pause
