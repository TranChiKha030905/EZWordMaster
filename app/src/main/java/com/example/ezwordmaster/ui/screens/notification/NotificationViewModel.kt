// Vị trí: app/src/main/java/com/example/ezwordmaster/ui/screens/notification/NotificationViewModel.kt
package com.example.ezwordmaster.ui.screens.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ezwordmaster.data.local.NotificationHistory
import com.example.ezwordmaster.domain.repository.NotificationHistoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val repository: NotificationHistoryRepository
) : ViewModel() {

    // Tự động cập nhật khi data trong Room thay đổi
    val notifications: StateFlow<List<NotificationHistory>> = repository.getAllNotifications()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun deleteNotification(notification: NotificationHistory) {
        viewModelScope.launch {
            repository.deleteNotification(notification)
        }
    }

    fun deleteAllNotifications() {
        viewModelScope.launch {
            repository.deleteAllNotifications()
        }
    }
}