#!/bin/bash
echo "Atualizando Gradle Wrapper para versão 8.11.1..."
./gradlew wrapper --gradle-version=8.11.1 --distribution-type=bin
echo ""
echo "Gradle Wrapper atualizado com sucesso!"
echo "Execute './gradlew build' para testar."
