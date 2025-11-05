// Vị trí: app/src/main/java/com/example/ezwordmaster/data/local/NotificationDao.kt
package com.example.ezwordmaster.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationHistory)

    @Delete
    suspend fun deleteNotification(notification: NotificationHistory)

    @Query("SELECT * FROM notification_history ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationHistory>>

    @Query("DELETE FROM notification_history")
    suspend fun deleteAllNotifications()
}