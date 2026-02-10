# Arquitetura do Itinerary Builder

## Visão Geral

O Itinerary Builder segue **Clean Architecture** com camadas bem definidas e separação de responsabilidades através de módulos Gradle independentes.

## Camadas da Arquitetura

```
┌─────────────────────────────────────────┐
│          Presentation Layer             │
│  (Feature Modules - Jetpack Compose)    │
├─────────────────────────────────────────┤
│           Domain Layer                  │
│  (Entities, Use Cases, Repositories)    │
├─────────────────────────────────────────┤
│            Data Layer                   │
│   (Repository Implementations)          │
├─────────────────────────────────────────┤
│         Framework Layer                 │
│  (Room, Retrofit, Google Maps)          │
└─────────────────────────────────────────┘
```

## Fluxo de Dados

```
User Action → Intent → ViewModel → Use Case → Repository → Data Source
                ↓
            State Update
                ↓
            UI Recomposition
```

## Módulos Core

### core:common
**Responsabilidade:** Utilitários compartilhados

**Conteúdo:**
- `DateUtils` - Formatação e manipulação de datas
- `DistanceCalculator` - Cálculo de distâncias usando fórmula Haversine
- Extensions (Context, Flow)
- Constantes globais

**Dependências:** Nenhuma (módulo base)

### core:design-system
**Responsabilidade:** Sistema de design e componentes visuais

**Conteúdo:**
- Tema Material3 com paleta verde
- Tipografia e cores
- Componentes reutilizáveis (Cards, SearchBar, etc.)

**Dependências:** Compose, Material3

### core:domain
**Responsabilidade:** Lógica de negócio e regras

**Conteúdo:**
- **Entidades:** Trip, InterestMark, Edge
- **Use Cases:** 14 use cases para operações de Trip e InterestMark
- **Interfaces:** TripRepository, InterestMarkRepository

**Dependências:** core:common

**Princípios:**
- Zero dependências de frameworks Android
- Apenas Kotlin puro e Coroutines
- Testável sem contexto Android

### core:database
**Responsabilidade:** Persistência local com Room

**Conteúdo:**
- **Entities:** TripEntity, InterestMarkEntity, EdgeEntity
- **DAOs:** TripDao, InterestMarkDao, EdgeDao
- **Database:** ItineraryDatabase
- **Mappers:** Entity ↔ Domain

**Estratégia TAO:**
```kotlin
EdgeEntity(
    id1: Long,    // Source (e.g., Trip ID)
    id2: Long,    // Target (e.g., Mark ID)
    type: String, // Relationship type
    timestamp: Long,
    data: String? // JSON metadata
)
```

**Dependências:** core:domain, Room

### core:network
**Responsabilidade:** Comunicação com APIs externas

**Conteúdo:**
- Google Places API (Retrofit)
- DTOs e responses
- NetworkModule (Koin)

**Dependências:** Retrofit, OkHttp, Moshi

### core:data
**Responsabilidade:** Implementação de repositórios

**Conteúdo:**
- TripRepositoryImpl
- InterestMarkRepositoryImpl
- Lógica de mapeamento e cache

**Dependências:** core:domain, core:database, core:network

## Módulos Feature

Cada feature segue o padrão MVI e tem estrutura similar:

```
feature/
├── FeatureState.kt       # Estado imutável
├── FeatureIntent.kt      # Ações do usuário
├── FeatureViewModel.kt   # Lógica e processamento
├── FeatureScreen.kt      # UI Compose
└── di/
    └── FeatureModule.kt  # Injeção de dependências
```

### feature:home
**Tela:** Lista de viagens

**Responsabilidades:**
- Exibir todas as viagens
- Buscar e filtrar
- Toggle de favoritos
- Navegação para Trip

**State:**
```kotlin
data class HomeState(
    val trips: List<Trip>,
    val filteredTrips: List<Trip>,
    val searchQuery: String,
    val isLoading: Boolean,
    val error: String?,
    val showFavoritesOnly: Boolean
)
```

### feature:trip
**Tela:** Container com bottom navigation

**Responsabilidades:**
- Gerenciar navegação entre Map, Destinations, Schedule
- Carregar informações da viagem
- Coordenar sub-features

**Destinos:**
- Map (Mapa principal)
- Destinations (Lista)
- Schedule (Agenda)

### feature:map
**Tela:** Mapa interativo com Google Maps

**Responsabilidades:**
- Renderizar Google Map
- Adicionar/editar/remover marcadores
- Modo distância (polylines)
- Recentralizar câmera
- Integrar com Places API

**Componentes:**
- `MapScreen` - Tela principal
- `MarkerDetailsCard` - Card de detalhes
- `AddMarkerDialog` - Dialog de criação
- `DistancePolylines` - Overlay de linhas

**Funcionalidades Avançadas:**
- CameraPositionState gerenciado
- Click listeners no mapa
- Markers customizados
- Polyline rendering

### feature:destinations
**Tela:** Lista de pontos de interesse

**Responsabilidades:**
- Listar todos os interest marks
- Buscar por nome/tags
- Navegação para detalhes

### feature:schedule
**Tela:** Agenda de itens agendados

**Responsabilidades:**
- Listar marks com scheduledDate
- Ordenar por data
- Destacar visualmente datas
- Buscar

### feature:details
**Tela:** Detalhes completos do ponto

**Responsabilidades:**
- Visualizar todas as informações
- Modo edição inline
- Abrir no Google Maps
- Salvar alterações

**Campos Editáveis:**
- Nome
- Ranking (1-5 estrelas)
- Data agendada
- Notas do usuário
- Tags

## Padrão MVI Detalhado

### Model (State)
Estado imutável que representa o estado atual da UI:

```kotlin
data class FeatureState(
    val data: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
```

### View (Screen)
Componente Compose que observa o State e dispara Intents:

```kotlin
@Composable
fun FeatureScreen(viewModel: FeatureViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    // UI reage ao state
    when {
        state.isLoading -> LoadingIndicator()
        state.error != null -> ErrorMessage(state.error)
        else -> Content(state.data)
    }
    
    // Disparar intents
    Button(onClick = { 
        viewModel.processIntent(FeatureIntent.Action) 
    })
}
```

### Intent
Ações do usuário representadas como sealed interface:

```kotlin
sealed interface FeatureIntent {
    data object Load : FeatureIntent
    data class Search(val query: String) : FeatureIntent
    data class Delete(val id: Long) : FeatureIntent
}
```

### ViewModel
Processa intents e atualiza o state:

```kotlin
class FeatureViewModel(
    private val useCase: UseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(FeatureState())
    val state: StateFlow<FeatureState> = _state.asStateFlow()
    
    fun processIntent(intent: FeatureIntent) {
        when (intent) {
            is FeatureIntent.Load -> loadData()
            is FeatureIntent.Search -> search(intent.query)
            is FeatureIntent.Delete -> delete(intent.id)
        }
    }
    
    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            useCase()
                .catch { e -> _state.update { it.copy(error = e.message) } }
                .collect { data -> _state.update { it.copy(data = data, isLoading = false) } }
        }
    }
}
```

## Injeção de Dependências (Koin)

### Estrutura de Módulos DI

```kotlin
// app/di/
- appModule          # App-level
- databaseModule     # Room database e DAOs
- domainModule       # Use cases

// core/network/
- networkModule      # Retrofit, OkHttp

// core/data/
- dataModule         # Repositories

// feature/*/di/
- featureModule      # ViewModels
```

### Inicialização

```kotlin
class ItineraryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ItineraryApplication)
            modules(
                appModule, databaseModule, networkModule,
                dataModule, domainModule,
                homeModule, tripModule, mapModule,
                destinationsModule, scheduleModule, detailsModule
            )
        }
    }
}
```

### Exemplo de Módulo

```kotlin
val homeModule = module {
    viewModel {
        HomeViewModel(
            getAllTripsUseCase = get(),
            searchTripsUseCase = get(),
            toggleFavoriteUseCase = get(),
            deleteTripUseCase = get()
        )
    }
}
```

## Navegação Type-Safe

### Definição de Rotas

```kotlin
@Serializable
object HomeRoute

@Serializable
data class TripRoute(val tripId: Long)

@Serializable
data class InterestMarkDetailsRoute(val markId: Long)
```

### NavHost

```kotlin
NavHost(
    navController = navController,
    startDestination = HomeRoute
) {
    composable<HomeRoute> {
        HomeScreen(
            onNavigateToTrip = { tripId ->
                navController.navigate(TripRoute(tripId))
            }
        )
    }
    
    composable<TripRoute> { backStackEntry ->
        val args = backStackEntry.toRoute<TripRoute>()
        TripScreen(tripId = args.tripId)
    }
}
```

## Estratégia de Testes

### Testes Unitários
- Use cases (domain layer)
- ViewModels (presentation layer)
- Mappers (data layer)

### Testes de Integração
- Repositórios com Room in-memory
- DAOs com queries complexas

### Testes de UI
- Compose UI tests
- Screenshots tests

## Convenções de Código

### Nomenclatura
- **Entidades Domain:** `Trip`, `InterestMark`
- **Entities Room:** `TripEntity`, `InterestMarkEntity`
- **Use Cases:** `GetAllTripsUseCase`, `CreateTripUseCase`
- **ViewModels:** `HomeViewModel`, `TripViewModel`
- **Screens:** `HomeScreen`, `MapScreen`

### Package Structure
```
com.itinerary.[module]/
├── [feature]/
│   ├── FeatureState.kt
│   ├── FeatureIntent.kt
│   ├── FeatureViewModel.kt
│   ├── FeatureScreen.kt
│   ├── components/
│   │   └── FeatureComponent.kt
│   └── di/
│       └── FeatureModule.kt
```

## Performance

### Otimizações Implementadas
- StateFlow para gerenciamento de estado reativo
- Lazy loading com Room Flows
- Coil para cache de imagens
- ProGuard para minificação de release
- R8 full mode habilitado

### Boas Práticas
- Evitar recomposições desnecessárias
- Keys em LazyColumn/LazyRow
- remember e derivedStateOf
- Compose metrics para análise

## Segurança

### API Keys
- Nunca commitadas no controle de versão
- Armazenadas em `local.properties`
- Injetadas via BuildConfig

### ProGuard
- Obfuscação de código
- Remoção de código não utilizado
- Otimização de bytecode

---

**Documentação mantida atualizada:** Fevereiro 2026
