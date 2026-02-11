package com.itinerary.feature.details

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.itinerary.core.common.extensions.openMapsWithCoordinates
import com.itinerary.core.common.utils.DateUtils
import com.itinerary.core.designsystem.theme.RatingGold
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    markId: Long,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(markId) {
        viewModel.processIntent(DetailsIntent.LoadDetails(markId))
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Detalhes") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    if (!state.isEditMode) {
                        IconButton(
                            onClick = { viewModel.processIntent(DetailsIntent.ToggleEditMode) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar"
                            )
                        }
                    }
                }
            )
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
                            onClick = { viewModel.processIntent(DetailsIntent.LoadDetails(markId)) }
                        ) {
                            Text("Tentar novamente")
                        }
                    }
                }
            }
            state.interestMark != null -> {
                DetailsContent(
                    state = state,
                    context = context,
                    onIntent = viewModel::processIntent,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun DetailsContent(
    state: DetailsState,
    context: Context,
    onIntent: (DetailsIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val mark = state.interestMark ?: return
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Image
        if (mark.imageUrl != null) {
            AsyncImage(
                model = mark.imageUrl,
                contentDescription = mark.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
        }

        // Name
        if (state.isEditMode) {
            OutlinedTextField(
                value = state.editedName,
                onValueChange = { onIntent(DetailsIntent.UpdateName(it)) },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(
                text = mark.name,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        // Ranking
        Column {
            Text(
                text = "Nível de interesse",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                (1..5).forEach { index ->
                    if (state.isEditMode) {
                        IconButton(
                            onClick = { onIntent(DetailsIntent.UpdateRanking(index)) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Ranking $index",
                                modifier = Modifier.size(32.dp),
                                tint = if (index <= state.editedRanking) {
                                    RatingGold
                                } else {
                                    MaterialTheme.colorScheme.outline
                                }
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = if (index <= mark.ranking) RatingGold else MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }

        // Scheduled Date
        Column {
            Text(
                text = "Data agendada",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (!state.isEditMode && mark.scheduledDate != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    mark.scheduledDate?.let {
                        Text(
                            text = DateUtils.formatDate(it),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                }
            } else if (!state.isEditMode) {
                Text(
                    text = "Sem data agendada",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Coordinates
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                context.openMapsWithCoordinates(mark.latitude, mark.longitude)
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Coordenadas",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "${String.format("%.6f", mark.latitude)}, ${String.format("%.6f", mark.longitude)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Abrir no mapa",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // User Note
        Column {
            Text(
                text = "Notas",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (state.isEditMode) {
                OutlinedTextField(
                    value = state.editedUserNote,
                    onValueChange = { onIntent(DetailsIntent.UpdateUserNote(it)) },
                    label = { Text("Suas anotações") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    maxLines = 5
                )
            } else if (mark.userNote != null) {
                mark.userNote?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                Text(
                    text = "Sem anotações",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Tags
        if (mark.tags.isNotEmpty() && !state.isEditMode) {
            Column {
                Text(
                    text = "Tags",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    mark.tags.forEach { tag ->
                        AssistChip(
                            onClick = { },
                            label = { Text(tag) }
                        )
                    }
                }
            }
        }

        // Edit mode buttons
        if (state.isEditMode) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { onIntent(DetailsIntent.ToggleEditMode) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
                Button(
                    onClick = { onIntent(DetailsIntent.SaveChanges) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Salvar")
                }
            }
        }
    }
}
