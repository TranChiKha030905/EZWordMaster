package com.example.ezwordmaster.model

enum class FilterSortType {
    ALL,
    Z_TO_A,
    WORD_COUNT,
}

enum class SwipeDirection {
    LEFT, RIGHT
}

enum class MainTab {
    MANAGEMENT, PRACTICE, SETTINGS, DICTIONARY
}

data class HelpItem(
    val id: Int,
    val question: String,
    val answer: String
)

data class HelpUiState(
    val helpItems: List<HelpItem> = emptyList(),
    val selectedItem: HelpItem? = null
)

data class WordDetailsUiState(
    val word: String,
    val phonetic: String?,
    val audioUrl: String?,
    val partOfSpeech: String?,
    val definition: String?,
    val exampleEn: String?,
    val exampleVi: String?,
    val translationVi: String?,
    val synonyms: List<String>?, // Từ đồng nghĩa
    val antonyms: List<String>?  // Từ trái nghĩa
)

// Class sealed để quản lý trạng thái tải dữ liệu (Loading, Success, Error)
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}