package com.itinerary.feature.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itinerary.core.domain.usecase.interestmark.GetScheduledInterestMarksUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val getScheduledInterestMarksUseCase: GetScheduledInterestMarksUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ScheduleState())
    val state: StateFlow<ScheduleState> = _state.asStateFlow()

    fun processIntent(intent: ScheduleIntent) {
        when (intent) {
            is ScheduleIntent.LoadScheduled -> loadScheduled(intent.tripId)
            is ScheduleIntent.SearchScheduled -> searchScheduled(intent.query)
            is ScheduleIntent.NavigateToDetails -> {
                // Navigation handled by UI layer
            }
        }
    }

    private fun loadScheduled(tripId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(tripId = tripId, isLoading = true, error = null) }
            
            getScheduledInterestMarksUseCase(tripId)
                .catch { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Erro ao carregar agenda"
                        )
                    }
                }
                .collect { marks ->
                    // Sort by scheduled date
                    val sorted = marks.sortedBy { it.scheduledDate }
                    _state.update {
                        val filtered = filterScheduled(sorted, it.searchQuery)
                        it.copy(
                            scheduledMarks = sorted,
                            filteredMarks = filtered,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    private fun searchScheduled(query: String) {
        _state.update {
            val filtered = filterScheduled(it.scheduledMarks, query)
            it.copy(
                searchQuery = query,
                filteredMarks = filtered
            )
        }
    }

    private fun filterScheduled(
        marks: List<com.itinerary.core.domain.model.InterestMark>,
        query: String
    ): List<com.itinerary.core.domain.model.InterestMark> {
        if (query.isBlank()) return marks
        
        return marks.filter { mark ->
            mark.name.contains(query, ignoreCase = true) ||
            mark.tags.any { it.contains(query, ignoreCase = true) }
        }
    }
}
