package com.example.ezwordmaster.ui.screens.dictionary

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ezwordmaster.EzWordMasterApplication
import com.example.ezwordmaster.R
import com.example.ezwordmaster.ViewModelFactory
import com.example.ezwordmaster.model.WordDetailsUiState
import com.example.ezwordmaster.ui.common.CommonTopAppBar
import com.example.ezwordmaster.ui.common.GradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen(navController: NavController) {
    val context = LocalContext.current
    val application = context.applicationContext as EzWordMasterApplication
    val appContainer = application.appContainer
    val viewModel: DictionaryViewModel = viewModel(factory = ViewModelFactory(appContainer))
    val uiState by viewModel.uiState.collectAsState()
    var query by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    GradientBackground {
        Scaffold(
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
                            title = "Tra từ",
                            canNavigateBack = false,
                            onNavigateUp = {},
                            onLogoClick = {}
                        )
                    }
                    IconButton(onClick = { navController.navigate("notificationscreen") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bell),
                            contentDescription = "Thông báo",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Nhập từ cần tra...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        viewModel.searchWord(query)
                        keyboardController?.hide()
                    }),
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AnimatedVisibility(
                                visible = query.isNotBlank(),
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                IconButton(onClick = { query = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Xóa")
                                }
                            }
                            IconButton(onClick = {
                                viewModel.searchWord(query)
                                keyboardController?.hide()
                            }) {
                                Icon(Icons.Default.Search, contentDescription = "Tìm kiếm")
                            }
                        }
                    }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    when {
                        uiState.isLoading -> CircularProgressIndicator()
                        uiState.errorMessage != null -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = "Lỗi",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    text = uiState.errorMessage!!,
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        uiState.searchResult != null -> {
                            SearchResultContent(
                                result = uiState.searchResult!!,
                                context = context,
                                onWordClick = { newWord ->
                                    query = newWord
                                    viewModel.searchWord(newWord)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultContent(
    result: WordDetailsUiState,
    context: Context,
    onWordClick: (String) -> Unit
) {
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }

    DisposableEffect(Unit) { onDispose { mediaPlayer?.release() } }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Column {
                    Text(
                        text = result.word,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    result.phonetic?.let {
                        Text(text = it, fontSize = 18.sp, color = Color.Gray)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                result.audioUrl?.let { url ->
                    IconButton(onClick = {
                        try {
                            mediaPlayer?.release()
                            mediaPlayer = MediaPlayer().apply {
                                setAudioAttributes(
                                    AudioAttributes.Builder()
                                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                        .setUsage(AudioAttributes.USAGE_MEDIA)
                                        .build()
                                )
                                setDataSource(url)
                                prepareAsync()
                                setOnPreparedListener { start() }
                                setOnCompletionListener { release() }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Phát âm",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        result.translationVi?.let {
            InfoCard(Icons.Default.Translate, "Nghĩa Tiếng Việt", it)
        }
        result.partOfSpeech?.let {
            InfoCard(Icons.Default.Category, "Loại từ", it)
        }
        result.definition?.let {
            InfoCard(Icons.Default.Book, "Định nghĩa (Tiếng Anh)", it)
        }
        result.exampleEn?.let { example ->
            InfoCard(
                Icons.Default.FormatQuote,
                "Ví dụ",
                example,
                exampleVietnamese = result.exampleVi,
                isItalic = true
            )
        }
        if (!result.synonyms.isNullOrEmpty()) {
            InfoCardWithChips(
                Icons.Default.SyncAlt,
                "Từ đồng nghĩa",
                result.synonyms!!,
                onWordClick
            )
        }
        if (!result.antonyms.isNullOrEmpty()) {
            InfoCardWithChips(
                Icons.Default.CompareArrows,
                "Từ trái nghĩa",
                result.antonyms!!,
                onWordClick
            )
        }
    }
}

@Composable
fun InfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    content: String,
    exampleVietnamese: String? = null,
    isItalic: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, title, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal
            )
            exampleVietnamese?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = it, color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InfoCardWithChips(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    words: List<String>,
    onWordClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, title, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                words.forEach { word ->
                    Box(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { onWordClick(word) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = word, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                }
            }
        }
    }
}
