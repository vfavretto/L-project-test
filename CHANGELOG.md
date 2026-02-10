# Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto adere ao [Semantic Versioning](https://semver.org/lang/pt-BR/).

## [1.0.1] - 2026-02-10

### Corrigido
- **[CRITICAL FIX]** Erro de build "Invalid catalog definition: you can only call the 'from' method a single time"
  - Removido `versionCatalogs` duplicado em `build-logic/settings.gradle.kts`
  - Atualizado `build-logic/convention/build.gradle.kts` para usar dependências diretas
  - O projeto agora compila corretamente em máquinas diferentes

### Adicionado
- `TROUBLESHOOTING.md` - Guia completo de solução de problemas
- `CHANGELOG.md` - Este arquivo para documentar mudanças
- Seção de troubleshooting no README.md

### Detalhes Técnicos
O problema ocorria porque o Gradle tentava carregar o version catalog duas vezes:
1. Uma vez automaticamente do projeto root
2. Outra vez manualmente via `from(files(...))` no build-logic

**Arquivos alterados:**
- `build-logic/settings.gradle.kts` - Removido bloco `versionCatalogs`
- `build-logic/convention/build.gradle.kts` - Dependências especificadas diretamente
- `build-logic/gradle/libs.versions.toml` - Removido (não era necessário)

## [1.0.0] - 2026-02-10

### Adicionado
- ✨ **Projeto inicial completo** - Itinerary Builder Android App
- 🏗️ **Arquitetura** - Clean Architecture multi-módulo (13 módulos)
- 🎨 **UI** - Jetpack Compose com Material Design 3
- 🗺️ **Google Maps** - Integração completa com marcadores e distâncias
- 💾 **Database** - Room com estratégia TAO edge graph
- 🔧 **DI** - Koin configurado em todas as camadas
- 🧭 **Navigation** - Navigation 3 com type-safe routing

### Features Implementadas

#### Telas
- **Home Screen** - Lista de viagens com busca, filtros e favoritos
- **Trip Screen** - Container com bottom navigation (Mapa, Destinos, Agenda)
- **Map Screen** - Google Maps interativo com marcadores e linhas de distância
- **Destinations Screen** - Lista de pontos de interesse com busca
- **Schedule Screen** - Agenda de itens ordenados por data
- **Details Screen** - Detalhes completos com modo edição

#### Core Modules
- `core:common` - Utilitários (DateUtils, DistanceCalculator, Extensions)
- `core:design-system` - Tema verde Material3 e componentes reutilizáveis
- `core:domain` - 14 use cases, 3 entidades, 2 repository interfaces
- `core:database` - Room com TAO edge graph (3 entities, 3 DAOs)
- `core:network` - Google Maps/Places API integration (Retrofit)
- `core:data` - Implementação de repositórios

#### Feature Modules
- `feature:home` - MVI pattern com ViewModel e Compose
- `feature:trip` - Container com bottom navigation
- `feature:map` - Google Maps com controles avançados
- `feature:destinations` - Lista com busca e filtros
- `feature:schedule` - Agenda ordenada por data
- `feature:details` - Detalhes com edição inline

#### Funcionalidades do Mapa
- ➕ Adicionar marcadores clicando no mapa
- 📍 Card de detalhes ao selecionar marcador
- 📏 Modo distância com polylines conectando pontos
- 🎯 Botão recentralizar para ver todos os marcadores
- ✏️ Editar e excluir marcadores
- 🗺️ Abrir coordenadas em app de mapas externo

#### Padrões e Princípios
- ✅ **SOLID** - Todos os 5 princípios aplicados
- ✅ **DRY** - Código reutilizável e componentes compartilhados
- ✅ **Clean Architecture** - Camadas bem definidas
- ✅ **MVI Pattern** - Estado unidirecional em todas as features
- ✅ **Type-Safe Navigation** - Kotlin Serialization

#### Tecnologias
- Kotlin 1.9.22
- Jetpack Compose (BOM 2024.02.00)
- Material Design 3
- Navigation Compose 2.8.0
- Room 2.6.1
- Koin 3.5.3
- Google Maps Compose 4.3.3
- Retrofit 2.9.0
- Coroutines 1.8.0
- Coil 2.6.0

### Documentação
- 📖 `README.md` - Documentação completa (500+ linhas)
- 🏗️ `ARCHITECTURE.md` - Arquitetura detalhada (350+ linhas)
- 📊 `PROJECT_SUMMARY.md` - Resumo executivo
- ⚡ `QUICK_START.md` - Guia de início rápido
- 🔧 `TROUBLESHOOTING.md` - Solução de problemas

### Estatísticas v1.0.0
- **Módulos Gradle**: 13
- **Arquivos Kotlin**: 120+
- **Linhas de código**: ~8,000+
- **Use Cases**: 14
- **ViewModels**: 6
- **Screens Compose**: 6
- **Componentes reutilizáveis**: 4+

---

## Tipos de Mudanças
- `Added` - Novas funcionalidades
- `Changed` - Mudanças em funcionalidades existentes
- `Deprecated` - Funcionalidades que serão removidas
- `Removed` - Funcionalidades removidas
- `Fixed` - Correções de bugs
- `Security` - Correções de segurança

---

## Versionamento

Este projeto segue [Semantic Versioning](https://semver.org/):
- **MAJOR** - Mudanças incompatíveis na API
- **MINOR** - Nova funcionalidade mantendo compatibilidade
- **PATCH** - Correções de bugs mantendo compatibilidade

---

**Nota:** Para detalhes de implementação e arquitetura, consulte [ARCHITECTURE.md](ARCHITECTURE.md)
