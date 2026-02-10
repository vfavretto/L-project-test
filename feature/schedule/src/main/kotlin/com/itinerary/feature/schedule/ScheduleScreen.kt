package com.itinerary.feature.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itinerary.core.common.utils.DateUtils
import com.itinerary.core.designsystem.components.InterestMarkCard
import com.itinerary.core.designsystem.components.SearchBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScheduleScreen(
    tripId: Long,
    onNavigateToDetails: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(tripId) {
        viewModel.processIntent(ScheduleIntent.LoadScheduled(tripId))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        
        // Search bar
        SearchBar(
            query = state.searchQuery,
            onQueryChange = { viewModel.processIntent(ScheduleIntent.SearchScheduled(it)) },
            placeholder = "Buscar na agenda..."
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
                            onClick = { 
                                viewModel.processIntent(ScheduleIntent.LoadScheduled(tripId))
                            }
                        ) {
                            Text("Tentar novamente")
                        }
                    }
                }
            }
            state.filteredMarks.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (state.scheduledMarks.isEmpty()) {
                            "Nenhum item agendado.\nAdicione datas aos seus destinos!"
                        } else {
                            "Nenhum item encontrado"
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(
                        items = state.filteredMarks,
                        key = { it.id }
                    ) { mark ->
                        InterestMarkCard(
                            name = mark.name,
                            imageUrl = mark.imageUrl,
                            ranking = mark.ranking,
                            scheduledDate = mark.scheduledDate?.let { 
                                DateUtils.formatDate(it) 
                            },
                            onClick = {
                                viewModel.processIntent(ScheduleIntent.NavigateToDetails(mark.id))
                                onNavigateToDetails(mark.id)
                            },
                            showScheduleHighlight = true
                        )
                    }
                }
            }
        }
    }
}
