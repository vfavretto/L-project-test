package com.itinerary.feature.map

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.itinerary.core.common.Constants
import com.itinerary.feature.map.components.AddMarkerDialog
import com.itinerary.feature.map.components.DistancePolylines
import com.itinerary.feature.map.components.MarkerDetailsCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreen(
    tripId: Long,
    onNavigateToDetails: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    val defaultPosition = remember {
        LatLng(Constants.DEFAULT_LATITUDE, Constants.DEFAULT_LONGITUDE)
    }
    
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            state.cameraPosition ?: defaultPosition,
            Constants.DEFAULT_ZOOM
        )
    }

    LaunchedEffect(tripId) {
        viewModel.processIntent(MapIntent.LoadMarkers(tripId))
    }

    LaunchedEffect(state.cameraPosition) {
        state.cameraPosition?.let { position ->
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                position,
                cameraPositionState.position.zoom
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Google Map
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                viewModel.processIntent(MapIntent.AddMarkerAtPosition(latLng))
            }
        ) {
            // Render markers
            state.markers.forEach { marker ->
                Marker(
                    state = MarkerState(position = LatLng(marker.latitude, marker.longitude)),
                    title = marker.name,
                    snippet = "Ranking: ${marker.ranking}/5",
                    onClick = {
                        viewModel.processIntent(MapIntent.SelectMarker(marker.id))
                        false
                    }
                )
            }

            // Render distance lines
            if (state.isDistanceMode && state.markers.size >= 2) {
                DistancePolylines(markers = state.markers)
            }
        }

        // Map controls
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Distance mode toggle
            FloatingActionButton(
                onClick = { viewModel.processIntent(MapIntent.ToggleDistanceMode) },
                containerColor = if (state.isDistanceMode) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surface
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Modo distância",
                    tint = if (state.isDistanceMode) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }

            // Recenter button
            FloatingActionButton(
                onClick = { viewModel.processIntent(MapIntent.RecenterMap) },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Recentralizar"
                )
            }
        }

        // Selected marker details card
        if (state.selectedMarker != null) {
            MarkerDetailsCard(
                marker = state.selectedMarker!!,
                onEdit = {
                    viewModel.processIntent(MapIntent.UpdateMarker(state.selectedMarker!!.id))
                    onNavigateToDetails(state.selectedMarker!!.id)
                },
                onDelete = {
                    viewModel.processIntent(MapIntent.DeleteMarker(state.selectedMarker!!.id))
                },
                onDismiss = {
                    viewModel.processIntent(MapIntent.ClearSelection)
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        // Add marker dialog
        if (state.showAddMarkerDialog) {
            AddMarkerDialog(
                onConfirm = { name, ranking ->
                    viewModel.processIntent(MapIntent.SaveMarker(name, ranking))
                },
                onDismiss = {
                    viewModel.processIntent(MapIntent.CancelAddMarker)
                }
            )
        }

        // Loading indicator
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Error snackbar
        state.error?.let { error ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text(error)
            }
        }
    }
}
