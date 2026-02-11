package com.itinerary.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itinerary.core.designsystem.components.ItineraryCard
import com.itinerary.core.designsystem.components.SearchBar
import com.itinerary.feature.home.components.AddTripDialog
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToTrip: (Long) -> Unit,
    onAddTrip: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Minhas Viagens") },
                actions = {
                    IconButton(
                        onClick = { viewModel.processIntent(HomeIntent.ToggleFavoritesFilter) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtrar favoritos",
                            tint = if (state.showFavoritesOnly) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.processIntent(HomeIntent.ShowAddTripDialog) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar viagem"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            // Search bar
            SearchBar(
                query = state.searchQuery,
                onQueryChange = { viewModel.processIntent(HomeIntent.SearchTrips(it)) },
                placeholder = "Buscar viagens..."
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = state.error ?: "Erro desconhecido",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(
                                onClick = { viewModel.processIntent(HomeIntent.LoadTrips) }
                            ) {
                                Text("Tentar novamente")
                            }
                        }
                    }
                }
                state.filteredTrips.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (state.trips.isEmpty()) {
                                "Nenhuma viagem cadastrada.\nClique no + para adicionar!"
                            } else {
                                "Nenhuma viagem encontrada"
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(
                            items = state.filteredTrips,
                            key = { it.id }
                        ) { trip ->
                            ItineraryCard(
                                name = trip.name,
                                imageUrl = trip.imageUrl,
                                isFavorite = trip.isFavorite,
                                onClick = {
                                    viewModel.processIntent(HomeIntent.NavigateToTrip(trip.id))
                                    onNavigateToTrip(trip.id)
                                },
                                onFavoriteClick = {
                                    viewModel.processIntent(HomeIntent.ToggleFavorite(trip.id))
                                }
                            )
                        }
                    }
                }
            }
        }
        
        // Add Trip Dialog
        if (state.showAddTripDialog) {
            AddTripDialog(
                onConfirm = { name, imageUrl ->
                    viewModel.processIntent(HomeIntent.CreateTrip(name, imageUrl))
                },
                onDismiss = {
                    viewModel.processIntent(HomeIntent.DismissAddTripDialog)
                }
            )
        }
    }
}
