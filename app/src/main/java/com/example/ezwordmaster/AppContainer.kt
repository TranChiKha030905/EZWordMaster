// Vị trí: app/src/main/java/com/example/ezwordmaster/AppContainer.kt
package com.example.ezwordmaster

// import okhttp3.OkHttpClient // <-- KHÔNG CẦN NỮA
// import okhttp3.logging.HttpLoggingInterceptor // <-- KHÔNG CẦN NỮA
import android.content.Context
import com.example.ezwordmaster.data.local.NotificationDatabase
import com.example.ezwordmaster.data.remote.DictionaryApi
import com.example.ezwordmaster.data.remote.TranslationApi
import com.example.ezwordmaster.data.repository.DictionaryRepositoryImpl
import com.example.ezwordmaster.data.repository.NotificationHistoryRepositoryImpl
import com.example.ezwordmaster.data.repository.SettingsRepositoryImpl
import com.example.ezwordmaster.data.repository.StudyResultRepositoryImpl
import com.example.ezwordmaster.data.repository.TopicRepositoryImpl
import com.example.ezwordmaster.domain.repository.DictionaryRepository
import com.example.ezwordmaster.domain.repository.ISettingsRepository
import com.example.ezwordmaster.domain.repository.IStudyResultRepository
import com.example.ezwordmaster.domain.repository.ITopicRepository
import com.example.ezwordmaster.domain.repository.NotificationHistoryRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(private val context: Context) {

    // --- API 1 (Từ điển) ---
    private val dictionaryRetrofit = Retrofit.Builder()
        .baseUrl(DictionaryApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val dictionaryApi: DictionaryApi = dictionaryRetrofit.create(DictionaryApi::class.java)

    // --- API 2 (Dịch) ---
    private val translationRetrofit = Retrofit.Builder()
        .baseUrl(TranslationApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val translationApi: TranslationApi =
        translationRetrofit.create(TranslationApi::class.java)

    // --- PHẦN REPOSITORY ---
    val dictionaryRepository: DictionaryRepository = DictionaryRepositoryImpl(
        dictionaryApi = dictionaryApi,
        translationApi = translationApi // <-- THÊM LẠI
    )

    // --- PHẦN CÒN LẠI (Giữ nguyên) ---
    private val notificationDb: NotificationDatabase = NotificationDatabase.getDatabase(context)
    private val notificationDao = notificationDb.notificationDao()
    val notificationRepository: NotificationHistoryRepository = NotificationHistoryRepositoryImpl(
        dao = notificationDao
    )
    val settingsRepository: ISettingsRepository = SettingsRepositoryImpl(context)
    val topicRepository: ITopicRepository = TopicRepositoryImpl(context)
    val studyResultRepository: IStudyResultRepository = StudyResultRepositoryImpl(context)
}