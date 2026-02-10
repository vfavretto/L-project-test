# Guia de Início Rápido - Itinerary Builder

## Configuração em 5 Minutos ⚡

### 1️⃣ Pré-requisitos
- Android Studio Hedgehog (2023.1.1) ou superior
- JDK 17
- SDK Android com API 26-34
- Emulador ou dispositivo físico

### 2️⃣ Obter Google Maps API Key

1. Acesse [Google Cloud Console](https://console.cloud.google.com/)
2. Crie um novo projeto ou selecione um existente
3. Ative as seguintes APIs:
   - **Maps SDK for Android**
   - **Places API** (opcional)
   - **Geocoding API** (opcional)

4. Vá em "Credenciais" → "Criar credenciais" → "Chave de API"
5. Copie a chave gerada

### 3️⃣ Configurar o Projeto

1. **Clone ou copie o projeto** para sua máquina

2. **Crie o arquivo `local.properties`** na raiz do projeto:

```properties
# Caminho do SDK Android (ajuste conforme seu sistema)
sdk.dir=C\:\\Users\\SeuUsuario\\AppData\\Local\\Android\\Sdk

# Sua chave do Google Maps
MAPS_API_KEY=AIzaSy...SuaChaveAqui
```

> 💡 **Dica Windows**: Use `\\` duplas no caminho do SDK

> 💡 **Dica Mac/Linux**: 
> ```
> sdk.dir=/Users/seuusuario/Library/Android/sdk
> ```

3. **Abra o projeto no Android Studio**
   - File → Open → Selecione a pasta do projeto
   - Aguarde o Gradle sync completar (pode levar alguns minutos na primeira vez)

### 4️⃣ Verificar Configuração

Execute no terminal (dentro do diretório do projeto):

```bash
# Windows
.\gradlew build

# Mac/Linux
./gradlew build
```

Se tudo estiver OK, você verá:
```
BUILD SUCCESSFUL
```

### 5️⃣ Executar o App

1. **Inicie um emulador** ou conecte um dispositivo físico
2. No Android Studio, clique em **Run** (▶️)
3. Selecione o device e aguarde a instalação

## 🎉 Pronto!

O app deve abrir mostrando a tela Home (vazia inicialmente).

## 📱 Usando o App

### Primeira Viagem
1. Na tela Home, clique no **botão +** (canto inferior direito)
2. Digite o nome da viagem (ex: "Europa 2024")
3. Clique em "Adicionar"

### Adicionar Pontos de Interesse
1. Clique na viagem criada
2. Você verá o mapa (tab "Mapa")
3. **Clique em qualquer lugar do mapa**
4. Digite o nome do local
5. Selecione o ranking (1-5 estrelas)
6. Clique em "Adicionar"

### Explorar Funcionalidades
- **Tab Destinos**: Veja lista de todos os pontos
- **Tab Agenda**: Pontos com data agendada
- **Modo Distância**: Botão no mapa mostra linhas entre pontos
- **Recentralizar**: Botão no mapa ajusta zoom para ver todos os pontos
- **Detalhes**: Clique em um card para ver/editar detalhes completos

## 🐛 Solução de Problemas

### Gradle sync falhou
```bash
# Limpar e rebuild
./gradlew clean
./gradlew build --refresh-dependencies
```

### Erro "SDK location not found"
- Verifique se o caminho em `local.properties` está correto
- Use barras invertidas duplas no Windows: `C:\\Users\\...`

### Mapa não carrega
- Verifique se a API Key está correta em `local.properties`
- Confirme que Maps SDK for Android está ativo no Google Cloud
- Aguarde alguns minutos após ativar a API (pode levar até 5 min)

### Build muito lento
```properties
# Adicione ao gradle.properties
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.jvmargs=-Xmx4g
```

## 📂 Estrutura de Pastas

```
ItineraryBuilder/
├── app/                    # Módulo principal
├── core/                   # Módulos compartilhados
│   ├── common/
│   ├── design-system/
│   ├── domain/
│   ├── database/
│   ├── network/
│   └── data/
├── feature/                # Módulos de features
│   ├── home/
│   ├── trip/
│   ├── map/
│   ├── destinations/
│   ├── schedule/
│   └── details/
├── build-logic/            # Convention plugins
├── gradle/                 # Gradle wrapper e libs
├── README.md              # Documentação completa
├── ARCHITECTURE.md        # Detalhes de arquitetura
└── local.properties       # Configurações locais (criar)
```

## 🔧 Comandos Úteis

```bash
# Build debug
./gradlew assembleDebug

# Build release
./gradlew assembleRelease

# Instalar debug no device
./gradlew installDebug

# Rodar testes
./gradlew test

# Limpar projeto
./gradlew clean

# Listar tasks
./gradlew tasks
```

## 📖 Documentação Completa

- **[README.md](README.md)** - Visão geral e funcionalidades
- **[ARCHITECTURE.md](ARCHITECTURE.md)** - Arquitetura detalhada
- **[PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)** - Resumo do projeto

## 💡 Dicas de Desenvolvimento

### Hot Reload
- Compose suporta hot reload automático
- Edite UI e veja mudanças instantaneamente
- Não funciona para mudanças em ViewModels (precisa rebuild)

### Debug
- Use breakpoints em ViewModels para inspecionar state
- Compose Layout Inspector para debug de UI
- Database Inspector para visualizar Room database

### Logs
```kotlin
// Use Timber ou Android Log
Log.d("HomeViewModel", "State: $state")
```

## 🎓 Recursos de Aprendizado

### Jetpack Compose
- [Compose Basics](https://developer.android.com/jetpack/compose/tutorial)
- [Compose Samples](https://github.com/android/compose-samples)

### Clean Architecture
- [Uncle Bob's Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

### MVI Pattern
- [MVI on Android](https://www.kodeco.com/817602-mvi-architecture-for-android-tutorial-getting-started)

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/NovaFeature`)
3. Commit suas mudanças (`git commit -m 'Add: nova feature'`)
4. Push para a branch (`git push origin feature/NovaFeature`)
5. Abra um Pull Request

## 📞 Suporte

- Problemas técnicos: Abra uma issue no GitHub
- Dúvidas sobre arquitetura: Veja ARCHITECTURE.md
- Documentação geral: Veja README.md

---

**Bom desenvolvimento! 🚀**

Se tiver dúvidas, consulte a documentação completa ou abra uma issue.
