// Vị trí: app/src/main/java/com/example/ezwordmaster/data/repository/NotificationHistoryRepositoryImpl.kt
package com.example.ezwordmaster.data.repository

import com.example.ezwordmaster.data.local.NotificationDao
import com.example.ezwordmaster.data.local.NotificationHistory
import com.example.ezwordmaster.domain.repository.NotificationHistoryRepository
import kotlinx.coroutines.flow.Flow

class NotificationHistoryRepositoryImpl(
    private val dao: NotificationDao
) : NotificationHistoryRepository {

    override fun getAllNotifications(): Flow<List<NotificationHistory>> {
        return dao.getAllNotifications()
    }

    override suspend fun insertNotification(title: String, body: String) {
        val notification = NotificationHistory(
            title = title,
            body = body,
            timestamp = System.currentTimeMillis()
        )
        dao.insertNotification(notification)
    }

    override suspend fun deleteNotification(notification: NotificationHistory) {
        dao.deleteNotification(notification)
    }

    override suspend fun deleteAllNotifications() {
        dao.deleteAllNotifications()
    }
}