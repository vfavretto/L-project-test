package com.itinerary.feature.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itinerary.core.domain.model.InterestMark
import com.itinerary.core.domain.usecase.interestmark.GetInterestMarkByIdUseCase
import com.itinerary.core.domain.usecase.interestmark.UpdateInterestMarkUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val getInterestMarkByIdUseCase: GetInterestMarkByIdUseCase,
    private val updateInterestMarkUseCase: UpdateInterestMarkUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsState())
    val state: StateFlow<DetailsState> = _state.asStateFlow()

    fun processIntent(intent: DetailsIntent) {
        when (intent) {
            is DetailsIntent.LoadDetails -> loadDetails(intent.markId)
            is DetailsIntent.ToggleEditMode -> toggleEditMode()
            is DetailsIntent.UpdateName -> _state.update { it.copy(editedName = intent.name) }
            is DetailsIntent.UpdateRanking -> _state.update { it.copy(editedRanking = intent.ranking) }
            is DetailsIntent.UpdateScheduledDate -> _state.update { it.copy(editedScheduledDate = intent.date) }
            is DetailsIntent.UpdateUserNote -> _state.update { it.copy(editedUserNote = intent.note) }
            is DetailsIntent.UpdateTags -> _state.update { it.copy(editedTags = intent.tags) }
            is DetailsIntent.SaveChanges -> saveChanges()
            is DetailsIntent.OpenExternalMap -> {
                // Navigation handled by UI layer
            }
        }
    }

    private fun loadDetails(markId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            getInterestMarkByIdUseCase(markId)
                .catch { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Erro ao carregar detalhes"
                        )
                    }
                }
                .collect { mark ->
                    if (mark != null) {
                        _state.update {
                            it.copy(
                                interestMark = mark,
                                editedName = mark.name,
                                editedRanking = mark.ranking,
                                editedScheduledDate = mark.scheduledDate,
                                editedUserNote = mark.userNote ?: "",
                                editedTags = mark.tags,
                                isLoading = false,
                                error = null
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = "Ponto de interesse não encontrado"
                            )
                        }
                    }
                }
        }
    }

    private fun toggleEditMode() {
        val mark = _state.value.interestMark ?: return
        
        if (_state.value.isEditMode) {
            // Cancel edit - reset to original values
            _state.update {
                it.copy(
                    isEditMode = false,
                    editedName = mark.name,
                    editedRanking = mark.ranking,
                    editedScheduledDate = mark.scheduledDate,
                    editedUserNote = mark.userNote ?: "",
                    editedTags = mark.tags
                )
            }
        } else {
            // Enter edit mode
            _state.update { it.copy(isEditMode = true) }
        }
    }

    private fun saveChanges() {
        val mark = _state.value.interestMark ?: return
        
        viewModelScope.launch {
            try {
                val updatedMark = mark.copy(
                    name = _state.value.editedName,
                    ranking = _state.value.editedRanking,
                    scheduledDate = _state.value.editedScheduledDate,
                    userNote = _state.value.editedUserNote.ifBlank { null },
                    tags = _state.value.editedTags
                )
                
                updateInterestMarkUseCase(updatedMark)
                
                _state.update {
                    it.copy(
                        interestMark = updatedMark,
                        isEditMode = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = "Erro ao salvar alterações: ${e.message}")
                }
            }
        }
    }
}
