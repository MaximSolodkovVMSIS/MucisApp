package com.example.musicapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.media.MediaPlayer
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalConfiguration
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(addToFavorites: (MusicFile) -> Unit) {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }
    var musicFiles by remember { mutableStateOf(emptyList<MusicFile>()) }
    var selectedFile by remember { mutableStateOf<MusicFile?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
    }

    LaunchedEffect(Unit) {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_AUDIO
        else
            Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(permission)
        } else {
            permissionGranted = true
        }
    }

    if (permissionGranted) {
        LaunchedEffect(Unit) {
            musicFiles = getMusicFiles(context)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        if (permissionGranted) {
            if (musicFiles.isNotEmpty()) {
                Text(
                    text = "Найденные файлы:",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(8.dp)
                )
                musicFiles.forEach { file ->
                    var lastClickTime by remember { mutableLongStateOf(0L) }
                    val doubleClickThreshold = 200L

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        val currentTime = System.currentTimeMillis()
                                        if (currentTime - lastClickTime < doubleClickThreshold) {
                                            addToFavorites(file)
                                        } else {
                                            coroutineScope.launch {
                                                delay(doubleClickThreshold)
                                                if (System.currentTimeMillis() - lastClickTime >= doubleClickThreshold) {
                                                    selectedFile = file
                                                }
                                            }
                                        }
                                        lastClickTime = currentTime
                                    }
                                )
                            },
                        elevation = 4.dp,
                        backgroundColor = MaterialTheme.colors.surface
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = file.title,
                                style = MaterialTheme.typography.subtitle1
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = file.artist,
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            } else {
                Text("Музыкальные файлы не найдены.")
            }
        } else {
            Text("Разрешение не предоставлено.")
        }
    }

    selectedFile?.let { file ->
        FileInfoDialog(
            file = file,
            onAdd = { addToFavorites(it); selectedFile = null },
            onDismiss = { selectedFile = null }
        )
    }
}

private fun formatTime(milliseconds: Int): String {
    val minutes = (milliseconds / 1000) / 60
    val seconds = (milliseconds / 1000) % 60
    return "%d:%02d".format(minutes, seconds)
}

@Composable
fun FileInfoDialog(
    file: MusicFile,
    onAdd: (MusicFile) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf(file.title) }
    var artist by remember { mutableStateOf(file.artist) }
    var isPlaying by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, file.uri) }
    var currentProgress by remember { mutableIntStateOf(0) }
    val duration = mediaPlayer.duration
    val coroutineScope = rememberCoroutineScope()

    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            coroutineScope.launch {
                while (isPlaying && mediaPlayer.isPlaying) {
                    currentProgress = mediaPlayer.currentPosition
                    delay(1000)
                }
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.size(
                width = if (isTablet) 500.dp else 300.dp,
                height = if (isTablet) 450.dp else 350.dp
            ),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.background,
            border = BorderStroke(2.dp, MaterialTheme.colors.primary)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Редактировать файл", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Название") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = artist,
                    onValueChange = { artist = it },
                    label = { Text("Исполнитель") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = {
                        if (isPlaying) {
                            mediaPlayer.pause()
                        } else {
                            mediaPlayer.start()
                        }
                        isPlaying = !isPlaying
                    }) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play"
                        )
                    }

                    Slider(
                        value = currentProgress.toFloat(),
                        onValueChange = {
                            currentProgress = it.toInt()
                            mediaPlayer.seekTo(currentProgress)
                        },
                        valueRange = 0f..duration.toFloat(),
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "${formatTime(currentProgress)}/${formatTime(duration)}",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        mediaPlayer.release()
                        onDismiss()
                    }) {
                        Text("Закрыть")
                    }
                    Button(onClick = {
                        mediaPlayer.release()
                        onAdd(file.copy(title = title, artist = artist))
                        onDismiss()
                    }) {
                        Text("Сохранить")
                    }
                }
            }
        }
    }
}


suspend fun getMusicFiles(context: Context): List<MusicFile> {
    return withContext(Dispatchers.IO) {
        val musicFiles = mutableListOf<MusicFile>()
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST
        )
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val query = context.contentResolver.query(uri, projection, selection, null, sortOrder)

        query?.use { cursor ->
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)

            while (cursor.moveToNext()) {
                val title = cursor.getString(titleColumn)
                val id = cursor.getLong(idColumn)
                val artist = cursor.getString(artistColumn)
                val contentUri = Uri.withAppendedPath(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id.toString()
                )
                musicFiles.add(MusicFile(title, artist, contentUri))
            }
        }
        musicFiles
    }
}
