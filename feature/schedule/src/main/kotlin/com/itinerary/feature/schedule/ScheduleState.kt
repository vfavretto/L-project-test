package com.itinerary.feature.schedule

import com.itinerary.core.domain.model.InterestMark

data class ScheduleState(
    val tripId: Long = 0,
    val scheduledMarks: List<InterestMark> = emptyList(),
    val filteredMarks: List<InterestMark> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
