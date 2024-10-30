package com.example.musicapp

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyMusicScreen(favoriteSongs: List<MusicFile>, playSong: (Uri) -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Избранная музыка:", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))

            if (favoriteSongs.isNotEmpty()) {
                favoriteSongs.forEach { song ->
                    SongItem(song = song, playSong = playSong)
                }
            } else {
                Text(text = "Нет добавленных песен", modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun SongItem(song: MusicFile, playSong: (Uri) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { playSong(song.uri) }
            .padding(vertical = 8.dp) // Отделяем песни друг от друга
    ) {
        Text(
            text = song.title,
            style = MaterialTheme.typography.h6.copy(fontSize = 18.sp), // Увеличиваем размер текста
            modifier = Modifier.padding(bottom = 4.dp) // Отделяем название от исполнителя
        )
        Text(
            text = song.artist ?: "Неизвестно",
            style = MaterialTheme.typography.body1.copy(fontSize = 14.sp) // Меньший размер для исполнителя
        )
    }
}
