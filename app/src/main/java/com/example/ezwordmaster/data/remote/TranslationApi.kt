// Vị trí: app/src/main/java/com/example/ezwordmaster/data/remote/TranslationApi.kt
package com.example.ezwordmaster.data.remote

import com.example.ezwordmaster.model.TranslationDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslationApi {

    companion object {
        const val BASE_URL = "https://api.mymemory.translated.net/"
    }

    @GET("get")
    suspend fun translate(
        @Query("q") q: String, // Từ cần dịch
        @Query("langpair") langpair: String = "en|vi" // Anh -> Việt
    ): Response<TranslationDto>
}