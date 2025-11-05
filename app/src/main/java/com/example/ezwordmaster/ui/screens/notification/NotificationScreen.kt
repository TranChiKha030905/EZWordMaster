// Vị trí: app/src/main/java/com/example/ezwordmaster/ui/screens/notification/NotificationScreen.kt
package com.example.ezwordmaster.ui.screens.notification

// --- KHỐI IMPORT ĐẦY ĐỦ VÀ CHÍNH XÁC ---
// *** SỬA TÊN HÀM IMPORT ***
// ------------------------------------
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ezwordmaster.EzWordMasterApplication
import com.example.ezwordmaster.R
import com.example.ezwordmaster.ViewModelFactory
import com.example.ezwordmaster.data.local.NotificationHistory
import com.example.ezwordmaster.ui.common.CommonTopAppBar
import com.example.ezwordmaster.ui.common.GradientBackground
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavHostController) {

    val context = LocalContext.current
    val application = context.applicationContext as EzWordMasterApplication
    val appContainer = application.appContainer
    val viewModel: NotificationViewModel = viewModel(
        factory = ViewModelFactory(appContainer)
    )

    val notifications by viewModel.notifications.collectAsState()

    GradientBackground {
        Scaffold(
            // Giữ nguyên topBar
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(WindowInsets.statusBars.asPaddingValues())
                        .height(56.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        CommonTopAppBar(
                            title = "Thông Báo",
                            canNavigateBack = true,
                            onNavigateUp = { navController.popBackStack() },
                            onLogoClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                    IconButton(onClick = {
                        viewModel.deleteAllNotifications()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Xóa tất cả",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            if (notifications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.NotificationsOff,
                            contentDescription = "Không có thông báo",
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Không có thông báo nào",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(notifications, key = { it.id }) { notification ->

                        val density = LocalDensity.current
                        val thresholdPx = with(density) { 150.dp.toPx() }

                        // *** SỬA TÊN HÀM TỪ 'rememberDismissState' ***
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { newValue ->
                                if (newValue == SwipeToDismissBoxValue.EndToStart || newValue == SwipeToDismissBoxValue.StartToEnd) {
                                    viewModel.deleteNotification(notification)
                                    true
                                } else {
                                    false
                                }
                            },
                            positionalThreshold = { thresholdPx }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            backgroundContent = {
                                val color = when (dismissState.targetValue) {
                                    SwipeToDismissBoxValue.EndToStart -> Color.Red.copy(alpha = 0.8f)
                                    SwipeToDismissBoxValue.StartToEnd -> Color.Red.copy(alpha = 0.8f)
                                    else -> Color.Transparent
                                }
                                val alignment = when (dismissState.targetValue) {
                                    SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                                    else -> Alignment.CenterStart
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color, shape = CardDefaults.shape)
                                        .padding(12.dp),
                                    contentAlignment = alignment
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            },
                        ) {
                            NotificationItem(notification)
                        }
                    }
                }
            }
        }
    }
}

// Giữ nguyên NotificationItem
@Composable
fun NotificationItem(notification: NotificationHistory) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.small
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    notification.title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    notification.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = formatTimestamp(notification.timestamp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End
            )
        }
    }
}

// Giữ nguyên hàm formatTimestamp
private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    val days = TimeUnit.MILLISECONDS.toDays(diff)

    return when {
        days == 0L -> {
            val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
            sdf.format(Date(timestamp))
        }

        days == 1L -> "Yesterday"
        days < 7 -> "$days days ago"
        else -> {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.format(Date(timestamp))
        }
    }
}