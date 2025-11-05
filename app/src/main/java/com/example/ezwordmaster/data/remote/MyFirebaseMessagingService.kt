// Vị trí: app/src/main/java/com/example/ezwordmaster/data/remote/MyFirebaseMessagingService.kt
package com.example.ezwordmaster.data.remote

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.ezwordmaster.EzWordMasterApplication
import com.example.ezwordmaster.MainActivity
import com.example.ezwordmaster.R
import com.example.ezwordmaster.domain.repository.NotificationHistoryRepository
import com.example.ezwordmaster.worker.NotificationWorker
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val GROUP_KEY_FCM = "com.example.ezwordmaster.FCM_NOTIFICATION_GROUP"

    // THÊM: Scope để chạy coroutine (lưu vào DB)
    private val jobScope = CoroutineScope(Dispatchers.IO)

    // THÊM: Lấy Repository từ AppContainer
    private val notificationRepository: NotificationHistoryRepository by lazy {
        (application as EzWordMasterApplication).appContainer.notificationRepository
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_TOKEN", "Refreshed token: $token")
        // Bạn có thể gửi token này lên server của mình tại đây
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.notification?.let { notification ->
            val title = notification.title
            val body = notification.body

            if (title != null && body != null) {
                // 1. Hiển thị thông báo (như cũ)
                sendNotification(title, body)

                // 2. THÊM: Lưu thông báo vào Database
                jobScope.launch {
                    notificationRepository.insertNotification(title, body)
                }
            }
        }
    }

    private fun sendNotification(title: String, messageBody: String) {
        // ... (Giữ nguyên toàn bộ code của hàm sendNotification đã build lại)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, NotificationWorker.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_playstore)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
            .setGroup(GROUP_KEY_FCM)
            .build()
        val notificationId = (System.currentTimeMillis() % 10000).toInt()
        notificationManager.notify(notificationId, notification)
    }
}


