package com.example.musicapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.media.MediaPlayer
import androidx.compose.foundation.clickable


@Composable
fun SearchScreen() {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }
    var musicFiles by remember { mutableStateOf(emptyList<Pair<String, Uri>>()) }
    var currentPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // Запуск активности для запроса разрешений
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
    }

    // Лаунчер для выбора файлов
    val filePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            val fileName = getFileNameFromUri(context, it)
            if (fileName.endsWith(".mp3")) {
                musicFiles = musicFiles + Pair(fileName, it)
            }
        }
    }

    // Проверяем и запрашиваем разрешение при необходимости
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
            } else {
                permissionGranted = true
            }
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                permissionGranted = true
            }
        }
    }

    // Получаем список файлов, если разрешение получено
    if (permissionGranted) {
        LaunchedEffect(Unit) {
            musicFiles = getMusicFiles(context)
        }
    }

    // Отображение списка файлов и кнопки для открытия файловой системы
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (permissionGranted) {
            if (musicFiles.isNotEmpty()) {
                Text(text = "Music files found:", modifier = Modifier.fillMaxWidth())
                musicFiles.forEach { (file, uri) ->
                    Text(
                        text = file,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Остановка предыдущего трека, если он воспроизводится
                                currentPlayer?.stop()

                                // Инициализация нового MediaPlayer для выбранного файла
                                currentPlayer = MediaPlayer.create(context, uri).apply {
                                    start()
                                }
                            }
                            .padding(8.dp)
                    )
                }
            } else {
                Text(text = "No MP3 files found", modifier = Modifier.fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                // Открываем файловую систему и фильтруем только mp3
                filePickerLauncher.launch(arrayOf("audio/mpeg"))
            }) {
                Text("Open File System")
            }

        } else {
            Text(text = "Permission not granted", modifier = Modifier.fillMaxWidth())
        }
    }
}

// Функция для получения списка mp3-файлов
suspend fun getMusicFiles(context: Context): List<Pair<String, Uri>> {
    return withContext(Dispatchers.IO) {
        val musicFiles = mutableListOf<Pair<String, Uri>>()
        val projection = arrayOf(MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media._ID)
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val query = context.contentResolver.query(uri, projection, selection, null, sortOrder)

        query?.use { cursor ->
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)

            while (cursor.moveToNext()) {
                val title = cursor.getString(titleColumn)
                val id = cursor.getLong(idColumn)
                val contentUri = Uri.withAppendedPath(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id.toString()
                )
                musicFiles.add(Pair(title, contentUri))
            }
        }

        musicFiles
    }
}

// Функция для получения имени файла из Uri
fun getFileNameFromUri(context: Context, uri: Uri): String {
    var name = ""
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            name = it.getString(it.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
        }
    }
    return name
}


