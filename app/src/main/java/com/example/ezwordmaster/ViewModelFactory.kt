// Vị trí: app/src/main/java/com/example/ezwordmaster/ViewModelFactory.kt
package com.example.ezwordmaster

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ezwordmaster.ui.screens.dictionary.DictionaryViewModel
import com.example.ezwordmaster.ui.screens.notification.NotificationViewModel
import com.example.ezwordmaster.ui.screens.regime.PracticeViewModel
import com.example.ezwordmaster.ui.screens.settings.SettingsViewModel
import com.example.ezwordmaster.ui.screens.topic_managment.TopicViewModel

class ViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return when {
            // --- SỬA TÊN THAM SỐ (parameter) THEO ĐÚNG LOG LỖI ---

            modelClass.isAssignableFrom(TopicViewModel::class.java) -> {
                // Sửa thành 'TOPICREPOSITORY' (VIẾT HOA)
                TopicViewModel(TOPICREPOSITORY = container.topicRepository) as T
            }

            modelClass.isAssignableFrom(PracticeViewModel::class.java) -> {
                // Sửa thành 'topicRepository' (viết thường)
                PracticeViewModel(topicRepository = container.topicRepository) as T
            }

            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                // Tên này đã đúng từ lần trước
                SettingsViewModel(settingsRepository = container.settingsRepository) as T
            }

            // --- CÁC VM MỚI (Giữ nguyên) ---
            modelClass.isAssignableFrom(DictionaryViewModel::class.java) -> {
                DictionaryViewModel(repository = container.dictionaryRepository) as T
            }

            modelClass.isAssignableFrom(NotificationViewModel::class.java) -> {
                NotificationViewModel(repository = container.notificationRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}