// Vị trí: app/src/main/java/com/example/ezwordmaster/domain/repository/DictionaryRepository.kt
package com.example.ezwordmaster.domain.repository

// SỬA DÒNG IMPORT DƯỚI ĐÂY
import com.example.ezwordmaster.model.Resource
import com.example.ezwordmaster.model.WordDetailsUiState

interface DictionaryRepository {
    suspend fun getWordDetails(word: String): Resource<WordDetailsUiState>
}