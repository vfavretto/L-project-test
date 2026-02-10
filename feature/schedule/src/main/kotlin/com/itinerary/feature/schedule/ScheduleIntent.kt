package com.itinerary.feature.schedule

sealed interface ScheduleIntent {
    data class LoadScheduled(val tripId: Long) : ScheduleIntent
    data class SearchScheduled(val query: String) : ScheduleIntent
    data class NavigateToDetails(val markId: Long) : ScheduleIntent
}
