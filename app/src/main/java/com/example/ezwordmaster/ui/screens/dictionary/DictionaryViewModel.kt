// Vị trí: app/src/main/java/com/example/ezwordmaster/ui/screens/dictionary/DictionaryViewModel.kt
package com.example.ezwordmaster.ui.screens.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ezwordmaster.domain.repository.DictionaryRepository
import com.example.ezwordmaster.model.Resource
import com.example.ezwordmaster.model.WordDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
// THÊM DÒNG IMPORT NÀY
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// State cho màn hình tra từ
data class DictionaryUiState(
    val isLoading: Boolean = false,
    val searchResult: WordDetailsUiState? = null,
    val errorMessage: String? = null
)

class DictionaryViewModel(
    private val repository: DictionaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DictionaryUiState())
    val uiState = _uiState.asStateFlow()

    fun searchWord(word: String) {
        if (word.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // SỬA LẠI 3 DÒNG "is Resource..."
            when (val result = repository.getWordDetails(word.trim())) {
                is Resource.Success<*> -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            searchResult = result.data
                        )
                    }
                }

                is Resource.Error<*> -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message,
                            searchResult = null
                        )
                    }
                }

                is Resource.Loading<*> -> {
                    // Đã xử lý ở trên
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}