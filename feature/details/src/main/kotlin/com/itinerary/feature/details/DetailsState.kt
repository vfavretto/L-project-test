package com.itinerary.feature.details

import com.itinerary.core.domain.model.InterestMark

data class DetailsState(
    val interestMark: InterestMark? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditMode: Boolean = false,
    val editedName: String = "",
    val editedRanking: Int = 3,
    val editedScheduledDate: Long? = null,
    val editedUserNote: String = "",
    val editedTags: List<String> = emptyList()
)
