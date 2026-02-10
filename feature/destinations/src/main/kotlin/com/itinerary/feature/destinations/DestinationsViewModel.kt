package com.itinerary.feature.destinations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itinerary.core.domain.usecase.interestmark.GetInterestMarksByTripIdUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DestinationsViewModel(
    private val getInterestMarksByTripIdUseCase: GetInterestMarksByTripIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DestinationsState())
    val state: StateFlow<DestinationsState> = _state.asStateFlow()

    fun processIntent(intent: DestinationsIntent) {
        when (intent) {
            is DestinationsIntent.LoadDestinations -> loadDestinations(intent.tripId)
            is DestinationsIntent.SearchDestinations -> searchDestinations(intent.query)
            is DestinationsIntent.NavigateToDetails -> {
                // Navigation handled by UI layer
            }
        }
    }

    private fun loadDestinations(tripId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(tripId = tripId, isLoading = true, error = null) }
            
            getInterestMarksByTripIdUseCase(tripId)
                .catch { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Erro ao carregar destinos"
                        )
                    }
                }
                .collect { destinations ->
                    _state.update {
                        val filtered = filterDestinations(destinations, it.searchQuery)
                        it.copy(
                            destinations = destinations,
                            filteredDestinations = filtered,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    private fun searchDestinations(query: String) {
        _state.update {
            val filtered = filterDestinations(it.destinations, query)
            it.copy(
                searchQuery = query,
                filteredDestinations = filtered
            )
        }
    }

    private fun filterDestinations(
        destinations: List<com.itinerary.core.domain.model.InterestMark>,
        query: String
    ): List<com.itinerary.core.domain.model.InterestMark> {
        if (query.isBlank()) return destinations
        
        return destinations.filter { destination ->
            destination.name.contains(query, ignoreCase = true) ||
            destination.tags.any { it.contains(query, ignoreCase = true) }
        }
    }
}
