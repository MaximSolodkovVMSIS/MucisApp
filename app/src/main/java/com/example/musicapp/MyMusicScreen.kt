package com.example.musicapp

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.items
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@Composable
fun MyMusicScreen(
    favoriteSongs: List<MusicFile>,
    playSong: (Uri) -> Unit,
    onRemoveSong: (MusicFile) -> Unit
) {
    var selectedSongUri by remember { mutableStateOf<Uri?>(null) }
    var songs by remember { mutableStateOf(favoriteSongs.toMutableList()) }

    Surface(modifier = Modifier.fillMaxSize()) {
        if (songs.isNotEmpty()) {
            LazyColumn {
                items(songs, key = { it.uri }) { song ->
                    SongItem(
                        song = song,
                        isSelected = song.uri == selectedSongUri,
                        onClick = {
                            selectedSongUri = song.uri
                            playSong(song.uri)
                        },
                        onSwipeToDelete = {
                            songs = songs.filter { it.uri != song.uri }.toMutableList()
                            onRemoveSong(song)
                        }
                    )
                    Divider(
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
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
fun SongItem(
    song: MusicFile,
    isSelected: Boolean,
    onClick: () -> Unit,
    onSwipeToDelete: () -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val maxSwipeDistance = -250f
    val coroutineScope = rememberCoroutineScope()

    val borderModifier = if (isSelected) {
        Modifier.border(2.dp, Color.Blue, RoundedCornerShape(4.dp))
    } else {
        Modifier
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .then(borderModifier)
            .background(Color.Red)
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete Icon",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
        )
        Column(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                if (offsetX.value <= maxSwipeDistance) {
                                    onSwipeToDelete()
                                } else {
                                    offsetX.animateTo(0f, animationSpec = tween(300))
                                }
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        coroutineScope.launch {
                            offsetX.snapTo((offsetX.value + dragAmount).coerceIn(maxSwipeDistance, 0f))
                        }
                    }
                }
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface)
                .clickable { onClick() }
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Text(
                text = song.title,
                style = MaterialTheme.typography.h6.copy(fontSize = 16.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth().padding(bottom = 2.dp)
            )
            Text(
                text = song.artist,
                style = MaterialTheme.typography.body2.copy(fontSize = 12.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
