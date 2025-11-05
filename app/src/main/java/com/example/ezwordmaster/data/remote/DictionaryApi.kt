// Vị trí: app/src/main/java/com/example/ezwordmaster/data/remote/DictionaryApi.kt
package com.example.ezwordmaster.data.remote

import com.example.ezwordmaster.model.WordResultDto // Dùng DTO cũ
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {

    companion object {
        const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/"
    }

    @GET("{word}")
    suspend fun getWordDefinition(
        @Path("word") word: String
    ): Response<List<WordResultDto>>
}