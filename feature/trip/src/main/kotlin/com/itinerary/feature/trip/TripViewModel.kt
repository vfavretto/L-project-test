package com.itinerary.feature.trip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itinerary.core.domain.usecase.trip.GetTripByIdUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TripViewModel(
    private val getTripByIdUseCase: GetTripByIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TripState())
    val state: StateFlow<TripState> = _state.asStateFlow()

    fun processIntent(intent: TripIntent) {
        when (intent) {
            is TripIntent.LoadTrip -> loadTrip(intent.tripId)
            is TripIntent.NavigateToDestination -> navigateToDestination(intent.destination)
        }
    }

    private fun loadTrip(tripId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            getTripByIdUseCase(tripId)
                .catch { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Erro ao carregar viagem"
                        )
                    }
                }
                .collect { trip ->
                    _state.update {
                        it.copy(
                            trip = trip,
                            isLoading = false,
                            error = if (trip == null) "Viagem não encontrada" else null
                        )
                    }
                }
        }
    }

    private fun navigateToDestination(destination: TripDestination) {
        _state.update { it.copy(currentDestination = destination) }
    }
}
