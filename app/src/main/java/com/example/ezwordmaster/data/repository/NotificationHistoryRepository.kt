// Vị trí: app/src/main/java/com/example/ezwordmaster/domain/repository/NotificationHistoryRepository.kt
package com.example.ezwordmaster.domain.repository

import com.example.ezwordmaster.data.local.NotificationHistory
import kotlinx.coroutines.flow.Flow

interface NotificationHistoryRepository {

    fun getAllNotifications(): Flow<List<NotificationHistory>>

    suspend fun insertNotification(title: String, body: String)

    suspend fun deleteNotification(notification: NotificationHistory)

    suspend fun deleteAllNotifications()
}