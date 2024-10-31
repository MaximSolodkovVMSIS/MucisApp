package com.example.musicapp

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Surface
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun MyMusicScreen(favoriteSongs: List<MusicFile>, playSong: (Uri) -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        if (favoriteSongs.isNotEmpty()) {
            LazyColumn {
                itemsIndexed(favoriteSongs) { index, song ->
                    SongItem(song = song, playSong = playSong)

                    // Divider на всю ширину между элементами
                    if (index < favoriteSongs.size - 1) {
                        Divider(
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                            thickness = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        } else {
            Text(
                text = "Нет добавленных песен",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun SongItem(song: MusicFile, playSong: (Uri) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { playSong(song.uri) }
            .padding(vertical = 8.dp, horizontal = 16.dp) // Можно увеличить отступы для более четкого отображения
            .height(56.dp) // Задаем фиксированную высоту для каждого элемента
    ) {
        Text(
            text = song.title,
            style = MaterialTheme.typography.h6.copy(fontSize = 16.sp),
            maxLines = 1, // Ограничение на одну строку
            overflow = TextOverflow.Ellipsis, // Добавление многоточия, если текст слишком длинный
            modifier = Modifier.fillMaxWidth().padding(bottom = 2.dp)
        )
        Text(
            text = song.artist ?: "Неизвестно",
            style = MaterialTheme.typography.body2.copy(fontSize = 12.sp),
            maxLines = 1, // Ограничение на одну строку
            overflow = TextOverflow.Ellipsis, // Добавление многоточия, если текст слишком длинный
            modifier = Modifier.fillMaxWidth()
        )
    }
}

