# Guia de Solução de Problemas - Itinerary Builder

## Problemas Comuns e Soluções

### ❌ Erro: "Invalid catalog definition: you can only call the 'from' method a single time"

**Erro Completo:**
```
Error resolving plugin [id: 'com.android.application']
Invalid catalog definition:
Problem: In version catalog libs, you can only call the 'from' method a single time.
```

**Causa:**
O Gradle estava tentando carregar o version catalog mais de uma vez devido a uma configuração duplicada no `build-logic/settings.gradle.kts`.

**Solução:**
✅ **JÁ CORRIGIDO** na versão atual do projeto.

Se você encontrar esse erro em uma versão antiga:

1. Abra `build-logic/settings.gradle.kts`
2. Remova o bloco `versionCatalogs`:
```kotlin
// REMOVER:
versionCatalogs {
    create("libs") {
        from(files("../gradle/libs.versions.toml"))
    }
}
```

3. O arquivo deve ficar assim:
```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "build-logic"
include(":convention")
```

4. Sincronize o Gradle novamente

---

### ❌ Erro: "SDK location not found"

**Mensagem:**
```
SDK location not found. Define location with an ANDROID_SDK_ROOT environment variable
or by setting the sdk.dir path in your project's local.properties file.
```

**Causa:**
O arquivo `local.properties` não existe ou o caminho do SDK está incorreto.

**Solução:**

1. Crie o arquivo `local.properties` na raiz do projeto
2. Adicione o caminho do seu Android SDK:

**Windows:**
```properties
sdk.dir=C\:\\Users\\SeuUsuario\\AppData\\Local\\Android\\Sdk
MAPS_API_KEY=SUA_CHAVE_AQUI
```

**Mac:**
```properties
sdk.dir=/Users/seuusuario/Library/Android/sdk
MAPS_API_KEY=SUA_CHAVE_AQUI
```

**Linux:**
```properties
sdk.dir=/home/seuusuario/Android/Sdk
MAPS_API_KEY=SUA_CHAVE_AQUI
```

⚠️ **Importante:** No Windows, use barras invertidas duplas (`\\`)

---

### ❌ Erro: "Unresolved reference: libs"

**Mensagem:**
```
Unresolved reference: libs
e: Compilation failed: unresolved references
```

**Causa:**
O version catalog não está sendo reconhecido.

**Solução:**

1. Verifique se existe o arquivo `gradle/libs.versions.toml`
2. Faça um Gradle sync completo:
   - File → Invalidate Caches / Restart → Invalidate and Restart
3. Ou via terminal:
```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

---

### ❌ Google Maps não carrega / Tela branca

**Causa:**
API Key inválida ou não configurada corretamente.

**Solução:**

1. **Verifique a API Key no `local.properties`:**
```properties
MAPS_API_KEY=AIzaSyC...
```

2. **Confirme que as APIs estão ativas no Google Cloud Console:**
   - Maps SDK for Android ✅
   - Places API (opcional)
   - Geocoding API (opcional)

3. **Restrições da API Key:**
   - Se sua key tem restrições, adicione o package name do app:
   - Package: `com.itinerary.builder`
   - SHA-1: Obtenha com:
```bash
./gradlew signingReport
```

4. **Aguarde 5 minutos** após ativar as APIs (podem levar até 5 min para propagar)

5. **Rebuild o projeto:**
```bash
./gradlew clean assembleDebug
```

---

### ❌ Erro: "Could not find com.android.tools.build:gradle:8.2.2"

**Causa:**
Repositórios não configurados corretamente ou sem conexão com internet.

**Solução:**

1. Verifique sua conexão com internet
2. Limpe o cache do Gradle:
```bash
# Windows
rd /s /q %USERPROFILE%\.gradle\caches

# Mac/Linux
rm -rf ~/.gradle/caches
```

3. Sincronize novamente:
```bash
./gradlew build --refresh-dependencies
```

4. Se usar proxy corporativo, configure em `gradle.properties`:
```properties
systemProp.http.proxyHost=proxy.company.com
systemProp.http.proxyPort=8080
systemProp.https.proxyHost=proxy.company.com
systemProp.https.proxyPort=8080
```

---

### ❌ Build muito lento

**Solução:**

1. **Adicione ao `gradle.properties`:**
```properties
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=true
org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError
```

2. **Use Gradle Daemon:**
```bash
# Já está habilitado por padrão, mas pode verificar:
./gradlew --status
```

3. **Limpe builds antigos:**
```bash
./gradlew clean
```

---

### ❌ Erro: "Execution failed for task ':app:kspDebugKotlin'"

**Causa:**
Problema com KSP (Kotlin Symbol Processing) usado pelo Room.

**Solução:**

1. **Limpe o projeto:**
```bash
./gradlew clean
```

2. **Delete a pasta build:**
```bash
# Windows
rd /s /q build

# Mac/Linux
rm -rf build
```

3. **Rebuild:**
```bash
./gradlew build
```

4. Se persistir, verifique se as versões são compatíveis em `gradle/libs.versions.toml`:
```toml
kotlin = "1.9.22"
ksp = "1.9.22-1.0.17"  # Deve corresponder à versão do Kotlin
```

---

### ❌ Erro: "Manifest merger failed"

**Causa:**
Conflito no AndroidManifest.xml entre módulos.

**Solução:**

1. **Veja o erro detalhado:**
```bash
./gradlew processDebugManifest --stacktrace
```

2. **Conflitos comuns:**
   - `MAPS_API_KEY` não definida
   - Permissões duplicadas
   - Theme/Activity duplicados

3. **Verifique o `local.properties`:**
```properties
MAPS_API_KEY=SUA_CHAVE_AQUI
```

---

### ❌ App crasha ao abrir

**Possíveis causas:**

1. **Koin não inicializado:**
   - Verifique se `ItineraryApplication` está no manifest
   - Confirme que todos os módulos estão em `startKoin`

2. **Maps API Key inválida:**
   - Verifique logs com:
```bash
adb logcat | grep -i "maps"
```

3. **Permissões não concedidas:**
   - Vá em Settings → Apps → Itinerary Builder → Permissions
   - Ative Location

**Verificar logs:**
```bash
# Ver todos os crashes
adb logcat *:E

# Ver apenas do app
adb logcat | grep com.itinerary
```

---

### ❌ Erro: "More than one file was found with OS independent path"

**Mensagem completa:**
```
More than one file was found with OS independent path 'META-INF/...'
```

**Solução:**

Adicione ao `app/build.gradle.kts`:
```kotlin
android {
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }
}
```

---

### ❌ Compose Preview não funciona

**Solução:**

1. **Rebuild o módulo:**
   - Right-click no módulo → Rebuild Module

2. **Invalide caches:**
   - File → Invalidate Caches / Restart

3. **Verifique a versão do Android Studio:**
   - Requer Hedgehog (2023.1.1) ou superior

4. **Force refresh do preview:**
   - Click no ícone de refresh no preview pane

---

### ❌ Erro ao fazer checkout em outro computador

**Solução:**

1. **Depois de clonar, crie `local.properties`:**
```properties
sdk.dir=CAMINHO_DO_SEU_SDK
MAPS_API_KEY=SUA_CHAVE
```

2. **Sincronize o Gradle:**
```bash
./gradlew build
```

3. **Se der erro de permissão no gradlew (Mac/Linux):**
```bash
chmod +x gradlew
```

---

## Comandos Úteis para Debug

### Limpar projeto completo
```bash
./gradlew clean
rm -rf build
rm -rf app/build
rm -rf ~/.gradle/caches  # Use com cuidado
```

### Ver dependências
```bash
./gradlew :app:dependencies
```

### Ver tasks disponíveis
```bash
./gradlew tasks --all
```

### Build com logs detalhados
```bash
./gradlew assembleDebug --stacktrace --info
```

### Verificar configuração do Android
```bash
./gradlew signingReport
```

### Atualizar Gradle wrapper
```bash
./gradlew wrapper --gradle-version=8.5
```

---

## Versões Testadas

| Ferramenta | Versão Mínima | Versão Recomendada |
|------------|---------------|-------------------|
| Android Studio | Hedgehog (2023.1.1) | Iguana (2023.2.1) ou superior |
| JDK | 17 | 17 |
| Gradle | 8.2 | 8.5 |
| Android SDK | API 26 | API 34 |
| Kotlin | 1.9.22 | 1.9.22 |

---

## Suporte

Se você encontrou um problema não listado aqui:

1. **Verifique os logs completos:**
```bash
./gradlew assembleDebug --stacktrace > build-log.txt
```

2. **Procure no Google** pelo erro específico

3. **Stack Overflow** - Tag: `android`, `jetpack-compose`, `gradle`

4. **GitHub Issues** - Procure problemas similares

5. **Abra uma issue** com:
   - Mensagem de erro completa
   - Stack trace
   - Versões (AS, Gradle, Kotlin)
   - Sistema operacional
   - Arquivo `build-log.txt`

---

**Última atualização:** Fevereiro 2026
