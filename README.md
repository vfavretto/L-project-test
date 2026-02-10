# Itinerary Builder - Android App

Aplicativo Android nativo para planejamento de viagens com marcadores de pontos de interesse em mapa interativo.

## Arquitetura

O projeto segue **Clean Architecture** com separação em múltiplos módulos Gradle, implementando:

- **Padrão MVI** (Model-View-Intent) para gerenciamento de estado
- **Jetpack Compose** para UI declarativa
- **Room Database** com estratégia edge graph inspirada no TAO da Meta
- **Koin** para injeção de dependências
- **Navigation 3** com type-safe routing
- **Material Design 3** com tema verde Adventure & Freedom

### Estrutura de Módulos

```
ItineraryBuilder/
├── app/                          # Módulo principal da aplicação
├── build-logic/                  # Convention plugins do Gradle
│   └── convention/
├── core/                         # Módulos core compartilhados
│   ├── common/                   # Utilitários e extensões
│   ├── design-system/            # Tema Material3 e componentes
│   ├── domain/                   # Entidades, use cases, interfaces
│   ├── database/                 # Room + TAO edge graph
│   ├── network/                  # Google Maps/Places API
│   └── data/                     # Implementação dos repositórios
└── feature/                      # Módulos de features
    ├── home/                     # Lista de viagens
    ├── trip/                     # Container com bottom navigation
    ├── map/                      # Mapa interativo com Google Maps
    ├── destinations/             # Lista de pontos de interesse
    ├── schedule/                 # Agenda de itens agendados
    └── details/                  # Detalhes do ponto de interesse
```

## Tecnologias Utilizadas

### Android & Kotlin
- **Kotlin** 1.9.22
- **Android Gradle Plugin** 8.2.2
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (Android 14)

### Jetpack
- **Compose BOM** 2024.02.00
- **Material3** - Design system
- **Navigation Compose** 2.8.0 - Type-safe navigation
- **Room** 2.6.1 - Banco de dados local
- **Lifecycle** 2.7.0 - ViewModels e flows

### Networking
- **Retrofit** 2.9.0
- **OkHttp** 4.12.0
- **Moshi** 1.15.1

### Maps
- **Google Maps Compose** 4.3.3
- **Play Services Maps** 18.2.0
- **Play Services Location** 21.1.0

### Dependency Injection
- **Koin** 3.5.3

### Image Loading
- **Coil** 2.6.0

## Configuração do Projeto

### Pré-requisitos

1. **Android Studio** Hedgehog ou superior
2. **JDK** 17
3. **Google Maps API Key**

### Configurando a API Key do Google Maps

1. Obtenha uma chave de API do Google Maps:
   - Acesse [Google Cloud Console](https://console.cloud.google.com/)
   - Crie ou selecione um projeto
   - Ative as APIs:
     - Maps SDK for Android
     - Places API
     - Geocoding API
   - Crie credenciais (API Key)

2. Crie o arquivo `local.properties` na raiz do projeto:

```properties
sdk.dir=C\:\\Users\\SeuUsuario\\AppData\\Local\\Android\\Sdk
MAPS_API_KEY=SUA_CHAVE_DE_API_AQUI
```

### Build e Execução

1. Clone o repositório
2. Configure a API Key conforme descrito acima
3. Abra o projeto no Android Studio
4. Sincronize o projeto com os arquivos Gradle
5. Execute o app no emulador ou dispositivo físico

```bash
# Via linha de comando
./gradlew :app:assembleDebug
./gradlew :app:installDebug
```

## Funcionalidades

### Tela Home
- ✅ Lista de todas as viagens
- ✅ Busca de viagens
- ✅ Filtro de favoritos
- ✅ Marcar/desmarcar como favorito
- ✅ Adicionar nova viagem (placeholder)
- ✅ Navegação para detalhes da viagem

### Tela Trip (Container)
- ✅ Bottom navigation com 3 destinos
- ✅ Navegação entre Mapa, Destinos e Agenda
- ✅ Exibição do nome da viagem
- ✅ Botão voltar

### Mapa Interativo
- ✅ Google Maps integrado
- ✅ Adicionar marcadores ao clicar no mapa
- ✅ Dialog para nomear e ranquear marcador
- ✅ Card com detalhes ao selecionar marcador
- ✅ Modo distância (exibe linhas entre marcadores)
- ✅ Botão recentralizar (zoom para mostrar todos os marcadores)
- ✅ Editar e excluir marcadores
- ✅ Integração com Room database + TAO

### Lista de Destinos
- ✅ Lista de todos os pontos de interesse da viagem
- ✅ Busca e filtro
- ✅ Cards com imagem, nome, ranking e data agendada
- ✅ Navegação para detalhes

### Agenda
- ✅ Lista de pontos com data agendada
- ✅ Ordenação por data
- ✅ Destaque visual para datas
- ✅ Busca e filtro

### Detalhes do Ponto de Interesse
- ✅ Visualização completa de informações
- ✅ Imagem, nome, ranking, coordenadas
- ✅ Data agendada, notas, tags
- ✅ Modo edição inline
- ✅ Abrir no Google Maps (intent)
- ✅ Salvar alterações

## Padrão MVI

Cada feature segue o padrão MVI:

```kotlin
// State - Estado imutável
data class FeatureState(
    val data: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

// Intent - Intenções do usuário
sealed interface FeatureIntent {
    data object Load : FeatureIntent
    data class Action(val param: String) : FeatureIntent
}

// ViewModel - Processa intents e emite states
class FeatureViewModel : ViewModel() {
    private val _state = MutableStateFlow(FeatureState())
    val state: StateFlow<FeatureState> = _state.asStateFlow()
    
    fun processIntent(intent: FeatureIntent) { /* ... */ }
}
```

## Estratégia TAO (Edge Graph)

O banco de dados implementa uma estratégia de edge graph inspirada no TAO do Facebook para gerenciar relacionamentos:

```kotlin
@Entity(tableName = "edges")
data class EdgeEntity(
    val id1: Long,      // Trip ID
    val id2: Long,      // InterestMark ID
    val type: String,   // Tipo de relacionamento
    val timestamp: Long,
    val data: String?   // Metadados opcionais
)
```

**Benefícios:**
- Queries eficientes de relacionamentos
- Suporte a múltiplos tipos de relações
- Fácil adição de novos tipos de relacionamentos
- Auditoria temporal com timestamps

## Princípios SOLID

### Single Responsibility
- Cada módulo tem uma responsabilidade única
- ViewModels gerenciam apenas lógica de UI
- Use cases encapsulam regras de negócio

### Open/Closed
- Use cases extensíveis via interfaces
- Novos tipos de edges sem modificar código existente

### Liskov Substitution
- Interfaces de repositório são substituíveis
- Implementações podem ser trocadas

### Interface Segregation
- Interfaces segregadas por funcionalidade
- DAOs específicos para cada entidade

### Dependency Inversion
- Features dependem de abstrações (domain)
- Core domain não depende de frameworks

## Tema e Design

### Paleta de Cores (Adventure Green)

**Light Mode:**
- Primary: `#2E7D32` (Verde floresta)
- Secondary: `#66BB6A` (Verde vibrante)
- Tertiary: `#81C784` (Verde suave)
- Accent: `#FFA726` (Laranja aventura)

**Dark Mode:**
- Cores ajustadas para alto contraste
- Suporte completo a tema escuro

### Componentes Reutilizáveis
- `ItineraryCard` - Card de viagem
- `InterestMarkCard` - Card de ponto de interesse
- `SearchBar` - Barra de busca
- `FavoriteButton` - Botão de favorito

## Testes

```bash
# Testes unitários
./gradlew test

# Testes instrumentados
./gradlew connectedAndroidTest
```

## Build de Produção

```bash
./gradlew :app:assembleRelease
```

O ProGuard está configurado para otimizar e ofuscar o código em builds de release.

## Estrutura de Pastas

```
src/main/
├── kotlin/com/itinerary/
│   ├── builder/              # App module
│   ├── core/
│   │   ├── common/
│   │   ├── designsystem/
│   │   ├── domain/
│   │   ├── database/
│   │   ├── network/
│   │   └── data/
│   └── feature/
│       ├── home/
│       ├── trip/
│       ├── map/
│       ├── destinations/
│       ├── schedule/
│       └── details/
└── res/
    ├── values/
    ├── drawable/
    └── mipmap/
```

## Contribuindo

1. Faça fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## Licença

Este projeto está sob a licença MIT.

## Contato

Para dúvidas ou sugestões, entre em contato.

---

**Desenvolvido com ❤️ usando Kotlin e Jetpack Compose**
