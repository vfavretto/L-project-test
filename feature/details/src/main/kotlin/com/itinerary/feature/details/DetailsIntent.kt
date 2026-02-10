package com.itinerary.feature.details

sealed interface DetailsIntent {
    data class LoadDetails(val markId: Long) : DetailsIntent
    data object ToggleEditMode : DetailsIntent
    data class UpdateName(val name: String) : DetailsIntent
    data class UpdateRanking(val ranking: Int) : DetailsIntent
    data class UpdateScheduledDate(val date: Long?) : DetailsIntent
    data class UpdateUserNote(val note: String) : DetailsIntent
    data class UpdateTags(val tags: List<String>) : DetailsIntent
    data object SaveChanges : DetailsIntent
    data object OpenExternalMap : DetailsIntent
}
