@echo off
echo ========================================
echo Verificando Versoes do Projeto
echo ========================================
echo.
echo Lendo versoes do libs.versions.toml...
echo.
findstr "kotlin\|ksp\|compose-bom\|agp" gradle\libs.versions.toml
echo.
echo ========================================
echo Testando resolucao de dependencias...
echo ========================================
echo.
gradlew.bat dependencies --configuration compileClasspath > nul 2>&1
if %ERRORLEVEL% == 0 (
    echo ✓ Dependencias resolvidas com sucesso!
) else (
    echo ✗ Erro ao resolver dependencias!
    echo Execute: gradlew.bat dependencies --stacktrace
)
echo.
echo ========================================
echo Verificando plugins...
echo ========================================
echo.
gradlew.bat buildEnvironment > nul 2>&1
if %ERRORLEVEL% == 0 (
    echo ✓ Plugins encontrados!
) else (
    echo ✗ Erro nos plugins!
    echo Execute: gradlew.bat buildEnvironment --stacktrace
)
echo.
echo ========================================
echo Para mais detalhes, consulte:
echo VERSOES_REAIS.txt
echo ========================================
pause
