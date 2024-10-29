package com.example.musicapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyMusicScreen(favoriteSongs: List<MusicFile>) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (favoriteSongs.isNotEmpty()) {
            Column {
                Text(text = "Избранная музыка:", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(8.dp))
                favoriteSongs.forEach { song ->
                    Text(
                        text = "${song.title} - ${song.artist ?: "Неизвестно"}",
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        } else {
            Text(text = "Нет добавленных песен", modifier = Modifier.fillMaxSize())
        }
    }
}

