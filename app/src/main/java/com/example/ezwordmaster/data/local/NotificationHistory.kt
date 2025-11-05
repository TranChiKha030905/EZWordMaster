// Vị trí: app/src/main/java/com/example/ezwordmaster/data/local/NotificationHistory.kt
package com.example.ezwordmaster.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_history")
data class NotificationHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val body: String,
    val timestamp: Long // Lưu thời gian nhận
)