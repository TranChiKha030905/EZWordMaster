// Vị trí: app/src/main/java/com/example/ezwordmaster/model/ApiDtoModels.kt
package com.example.ezwordmaster.model

import com.google.gson.annotations.SerializedName

// --- DTOs CHO 'dictionaryapi.dev' (API 1) ---

data class WordResultDto(
    @SerializedName("word") val word: String?,
    @SerializedName("phonetic") val phonetic: String?,
    @SerializedName("phonetics") val phonetics: List<PhoneticDto>?,
    @SerializedName("meanings") val meanings: List<MeaningDto>?
)

data class PhoneticDto(
    @SerializedName("text") val text: String?,
    @SerializedName("audio") val audio: String?
)

data class MeaningDto(
    @SerializedName("partOfSpeech") val partOfSpeech: String?, // Loại từ
    @SerializedName("definitions") val definitions: List<DefinitionDto>?
)

// *** SỬA LỖI Ở ĐÂY: THÊM 2 DÒNG "XỊN" VÀO ***
data class DefinitionDto(
    @SerializedName("definition") val definition: String?, // Định nghĩa
    @SerializedName("example") val example: String?,      // Ví dụ
    @SerializedName("synonyms") val synonyms: List<String>?, // <-- THÊM DÒNG NÀY
    @SerializedName("antonyms") val antonyms: List<String>?  // <-- THÊM DÒNG NÀY
)


// --- DTOs CHO MYMEMORY API (API 2) ---

data class TranslationDto(
    @SerializedName("responseData") val responseData: ResponseDataDto?,
    @SerializedName("responseStatus") val responseStatus: Int?
)

data class ResponseDataDto(
    @SerializedName("translatedText") val translatedText: String?
)