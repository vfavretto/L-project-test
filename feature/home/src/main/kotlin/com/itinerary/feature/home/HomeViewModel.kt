package com.itinerary.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itinerary.core.domain.usecase.trip.CreateTripUseCase
import com.itinerary.core.domain.usecase.trip.DeleteTripUseCase
import com.itinerary.core.domain.usecase.trip.GetAllTripsUseCase
import com.itinerary.core.domain.usecase.trip.SearchTripsUseCase
import com.itinerary.core.domain.usecase.trip.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getAllTripsUseCase: GetAllTripsUseCase,
    private val searchTripsUseCase: SearchTripsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val deleteTripUseCase: DeleteTripUseCase,
    private val createTripUseCase: CreateTripUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        processIntent(HomeIntent.LoadTrips)
    }

    fun processIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadTrips -> loadTrips()
            is HomeIntent.SearchTrips -> searchTrips(intent.query)
            is HomeIntent.ToggleFavoritesFilter -> toggleFavoritesFilter()
            is HomeIntent.ToggleFavorite -> toggleFavorite(intent.tripId)
            is HomeIntent.DeleteTrip -> deleteTrip(intent.tripId)
            is HomeIntent.ShowAddTripDialog -> showAddTripDialog()
            is HomeIntent.DismissAddTripDialog -> dismissAddTripDialog()
            is HomeIntent.CreateTrip -> createTrip(intent.name, intent.imageUrl)
            is HomeIntent.NavigateToTrip -> {
                // Navigation handled by UI layer
            }
        }
    }
    
    private fun showAddTripDialog() {
        _state.update { it.copy(showAddTripDialog = true) }
    }
    
    private fun dismissAddTripDialog() {
        _state.update { it.copy(showAddTripDialog = false) }
    }
    
    private fun createTrip(name: String, imageUrl: String?) {
        viewModelScope.launch {
            try {
                createTripUseCase(name, imageUrl)
                _state.update { it.copy(showAddTripDialog = false) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = "Erro ao criar viagem: ${e.message}")
                }
            }
        }
    }

    private fun loadTrips() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            getAllTripsUseCase()
                .catch { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Erro ao carregar viagens"
                        )
                    }
                }
                .collect { trips ->
                    _state.update {
                        val filtered = filterTrips(trips, it.searchQuery, it.showFavoritesOnly)
                        it.copy(
                            trips = trips,
                            filteredTrips = filtered,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    private fun searchTrips(query: String) {
        _state.update {
            val filtered = filterTrips(it.trips, query, it.showFavoritesOnly)
            it.copy(
                searchQuery = query,
                filteredTrips = filtered
            )
        }
    }

    private fun toggleFavoritesFilter() {
        _state.update {
            val newShowFavorites = !it.showFavoritesOnly
            val filtered = filterTrips(it.trips, it.searchQuery, newShowFavorites)
            it.copy(
                showFavoritesOnly = newShowFavorites,
                filteredTrips = filtered
            )
        }
    }

    private fun toggleFavorite(tripId: Long) {
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(tripId)
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = "Erro ao atualizar favorito: ${e.message}")
                }
            }
        }
    }

    private fun deleteTrip(tripId: Long) {
        viewModelScope.launch {
            try {
                deleteTripUseCase(tripId)
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = "Erro ao deletar viagem: ${e.message}")
                }
            }
        }
    }

    private fun filterTrips(trips: List<com.itinerary.core.domain.model.Trip>, query: String, favoritesOnly: Boolean): List<com.itinerary.core.domain.model.Trip> {
        var filtered = trips
        
        if (favoritesOnly) {
            filtered = filtered.filter { it.isFavorite }
        }
        
        if (query.isNotBlank()) {
            filtered = filtered.filter { 
                it.name.contains(query, ignoreCase = true)
            }
        }
        
        return filtered
    }
}
