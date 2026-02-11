package com.itinerary.feature.trip

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripScreen(
    tripId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToDetails: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TripViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(tripId) {
        viewModel.processIntent(TripIntent.LoadTrip(tripId))
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(state.trip?.name ?: "Viagem") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Email, contentDescription = null) },
                    label = { Text(TripDestination.Map.label) },
                    selected = state.currentDestination == TripDestination.Map,
                    onClick = {
                        viewModel.processIntent(
                            TripIntent.NavigateToDestination(TripDestination.Map)
                        )
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                    label = { Text(TripDestination.Destinations.label) },
                    selected = state.currentDestination == TripDestination.Destinations,
                    onClick = {
                        viewModel.processIntent(
                            TripIntent.NavigateToDestination(TripDestination.Destinations)
                        )
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    label = { Text(TripDestination.Schedule.label) },
                    selected = state.currentDestination == TripDestination.Schedule,
                    onClick = {
                        viewModel.processIntent(
                            TripIntent.NavigateToDestination(TripDestination.Schedule)
                        )
                    }
                )
            }
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
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
                            onClick = { viewModel.processIntent(TripIntent.LoadTrip(tripId)) }
                        ) {
                            Text("Tentar novamente")
                        }
                    }
                }
            }
            state.trip != null -> {
                // Content based on selected destination
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    when (state.currentDestination) {
                        TripDestination.Map -> {
                            com.itinerary.feature.map.MapScreen(
                                tripId = tripId,
                                onNavigateToDetails = onNavigateToDetails
                            )
                        }
                        TripDestination.Destinations -> {
                            com.itinerary.feature.destinations.DestinationsScreen(
                                tripId = tripId,
                                onNavigateToDetails = onNavigateToDetails
                            )
                        }
                        TripDestination.Schedule -> {
                            com.itinerary.feature.schedule.ScheduleScreen(
                                tripId = tripId,
                                onNavigateToDetails = onNavigateToDetails
                            )
                        }
                    }
                }
            }
        }
    }
}
