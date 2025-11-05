// Vị trí: app/src/main/java/com/example/ezwordmaster/data/repository/DictionaryRepositoryImpl.kt
package com.example.ezwordmaster.data.repository

import com.example.ezwordmaster.data.remote.DictionaryApi
import com.example.ezwordmaster.data.remote.TranslationApi
import com.example.ezwordmaster.domain.repository.DictionaryRepository
import com.example.ezwordmaster.model.Resource
import com.example.ezwordmaster.model.WordDetailsUiState
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.util.Locale

class DictionaryRepositoryImpl(
    private val dictionaryApi: DictionaryApi,
    private val translationApi: TranslationApi
) : DictionaryRepository {

    override suspend fun getWordDetails(word: String): Resource<WordDetailsUiState> {
        try {
            val wordToSearch = word.lowercase(Locale.getDefault())

            return coroutineScope {

                val translationAsync = async { translationApi.translate(q = wordToSearch) }
                val definitionAsync = async { dictionaryApi.getWordDefinition(word = wordToSearch) }

                val translationResponse = translationAsync.await()
                val translationVi = translationResponse.body()?.responseData?.translatedText

                val definitionResponse = try {
                    definitionAsync.await()
                } catch (e: Exception) {
                    null
                }

                var partOfSpeech: String? = null
                var definition: String? = null
                var exampleEn: String? = null
                var phonetic: String? = null
                var audioUrl: String? = null

                // --- BIẾN MỚI ---
                var synonyms: List<String>? = null
                var antonyms: List<String>? = null

                if (definitionResponse != null && definitionResponse.isSuccessful && !definitionResponse.body()
                        .isNullOrEmpty()
                ) {
                    val results = definitionResponse.body()!!

                    val bestPhonetic = results.firstNotNullOfOrNull { it.phonetic }
                    val bestAudio = results.firstNotNullOfOrNull { res ->
                        res.phonetics?.find { !it.audio.isNullOrBlank() && it.audio.endsWith(".mp3") }
                    }

                    val bestMeaning = results
                        .flatMap { it.meanings ?: emptyList() }
                        .sortedBy {
                            when (it.partOfSpeech?.lowercase()) {
                                "verb" -> 1
                                "noun" -> 2
                                else -> 3
                            }
                        }
                        .firstOrNull()

                    val bestDefinition = bestMeaning?.definitions?.firstOrNull()

                    partOfSpeech = bestMeaning?.partOfSpeech
                    definition = bestDefinition?.definition
                    exampleEn = bestDefinition?.example
                    phonetic = bestPhonetic ?: bestAudio?.text
                    audioUrl = bestAudio?.audio
                    synonyms = bestDefinition?.synonyms?.take(3)
                    antonyms = bestDefinition?.antonyms?.take(3)
                }

                val exampleViAsync = if (!exampleEn.isNullOrBlank()) {
                    async { translationApi.translate(q = exampleEn) }
                } else {
                    null
                }

                val exampleViResponse = exampleViAsync?.await()
                val exampleVi = exampleViResponse?.body()?.responseData?.translatedText

                val uiState = WordDetailsUiState(
                    word = wordToSearch,
                    phonetic = phonetic,
                    audioUrl = audioUrl,
                    partOfSpeech = partOfSpeech,
                    definition = definition,
                    exampleEn = exampleEn,
                    exampleVi = exampleVi,
                    translationVi = translationVi,

                    // --- TRUYỀN DATA MỚI VÀO ---
                    synonyms = synonyms,
                    antonyms = antonyms
                )

                if (translationVi == null && definition == null) {
                    return@coroutineScope Resource.Error("Không tìm thấy bất kỳ thông tin nào cho từ '$word'")
                }

                Resource.Success(uiState)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error("Đã xảy ra lỗi: ${e.message}")
        }
    }
}