package com.itinerary.feature.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.itinerary.core.common.Constants
import com.itinerary.core.domain.usecase.interestmark.CreateInterestMarkUseCase
import com.itinerary.core.domain.usecase.interestmark.DeleteInterestMarkUseCase
import com.itinerary.core.domain.usecase.interestmark.GetInterestMarksByTripIdUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MapViewModel(
    private val getInterestMarksByTripIdUseCase: GetInterestMarksByTripIdUseCase,
    private val createInterestMarkUseCase: CreateInterestMarkUseCase,
    private val deleteInterestMarkUseCase: DeleteInterestMarkUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MapState())
    val state: StateFlow<MapState> = _state.asStateFlow()

    fun processIntent(intent: MapIntent) {
        when (intent) {
            is MapIntent.LoadMarkers -> loadMarkers(intent.tripId)
            is MapIntent.AddMarkerAtPosition -> showAddMarkerDialog(intent.position)
            is MapIntent.SaveMarker -> saveMarker(intent.name, intent.ranking)
            is MapIntent.CancelAddMarker -> cancelAddMarker()
            is MapIntent.SelectMarker -> selectMarker(intent.markerId)
            is MapIntent.ClearSelection -> clearSelection()
            is MapIntent.ToggleDistanceMode -> toggleDistanceMode()
            is MapIntent.RecenterMap -> recenterMap()
            is MapIntent.DeleteMarker -> deleteMarker(intent.markerId)
            is MapIntent.UpdateMarker -> {
                // Handle update - navigation to details screen
            }
        }
    }

    private fun loadMarkers(tripId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(tripId = tripId, isLoading = true, error = null) }
            
            getInterestMarksByTripIdUseCase(tripId)
                .catch { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Erro ao carregar marcadores"
                        )
                    }
                }
                .collect { markers ->
                    _state.update {
                        it.copy(
                            markers = markers,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    private fun showAddMarkerDialog(position: LatLng) {
        _state.update {
            it.copy(
                showAddMarkerDialog = true,
                pendingMarkerPosition = position
            )
        }
    }

    private fun saveMarker(name: String, ranking: Int) {
        val position = _state.value.pendingMarkerPosition ?: return
        val tripId = _state.value.tripId
        
        viewModelScope.launch {
            try {
                createInterestMarkUseCase(
                    tripId = tripId,
                    name = name,
                    latitude = position.latitude,
                    longitude = position.longitude,
                    ranking = ranking
                )
                
                _state.update {
                    it.copy(
                        showAddMarkerDialog = false,
                        pendingMarkerPosition = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = "Erro ao salvar marcador: ${e.message}",
                        showAddMarkerDialog = false,
                        pendingMarkerPosition = null
                    )
                }
            }
        }
    }

    private fun cancelAddMarker() {
        _state.update {
            it.copy(
                showAddMarkerDialog = false,
                pendingMarkerPosition = null
            )
        }
    }

    private fun selectMarker(markerId: Long) {
        val marker = _state.value.markers.find { it.id == markerId }
        _state.update { it.copy(selectedMarker = marker) }
    }

    private fun clearSelection() {
        _state.update { it.copy(selectedMarker = null) }
    }

    private fun toggleDistanceMode() {
        _state.update { it.copy(isDistanceMode = !it.isDistanceMode) }
    }

    private fun recenterMap() {
        val markers = _state.value.markers
        if (markers.isEmpty()) return
        
        // Calculate center point
        val avgLat = markers.map { it.latitude }.average()
        val avgLng = markers.map { it.longitude }.average()
        
        _state.update {
            it.copy(cameraPosition = LatLng(avgLat, avgLng))
        }
    }

    private fun deleteMarker(markerId: Long) {
        viewModelScope.launch {
            try {
                deleteInterestMarkUseCase(markerId)
                _state.update { it.copy(selectedMarker = null) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = "Erro ao deletar marcador: ${e.message}")
                }
            }
        }
    }
}
