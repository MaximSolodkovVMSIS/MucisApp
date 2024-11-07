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
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.launch
import kotlin.math.abs

@SuppressLint("MutableCollectionMutableState")
@Composable
fun MyMusicScreen(
    favoriteSongs: List<MusicFile>,
    currentSong: MusicFile?,
    playSong: (Uri) -> Unit,
    onRemoveSong: (MusicFile) -> Unit,
    onAddToQueue: (MusicFile) -> Unit
) {
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarCoroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (favoriteSongs.isNotEmpty()) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(favoriteSongs, key = { it.uri }) { song ->
                        SongItem(
                            song = song,
                            isSelected = song == currentSong,
                            onClick = {
                                playSong(song.uri)
                            },
                            onSwipeToDelete = {
                                onRemoveSong(song)
                                snackbarMessage = "Песня удалена"
                            },
                            onSwipeToAddToQueue = {
                                onAddToQueue(song)
                                snackbarMessage = "Песня добавлена в очередь"
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

            LaunchedEffect(snackbarMessage) {
                snackbarMessage?.let {
                    snackbarCoroutineScope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(it)
                    }
                    snackbarMessage = null
                }
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun SongItem(
    song: MusicFile,
    isSelected: Boolean,
    onClick: () -> Unit,
    onSwipeToDelete: () -> Unit,
    onSwipeToAddToQueue: () -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val maxSwipeDistance = 250f
    val coroutineScope = rememberCoroutineScope()

    var showAddIcon by remember { mutableStateOf(false) }
    var showDeleteIcon by remember { mutableStateOf(false) }

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
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(with(LocalDensity.current) { abs(offsetX.value).toDp().coerceAtMost(maxSwipeDistance.dp) })
                .background(
                    when {
                        offsetX.value >= maxSwipeDistance -> Color.Green
                        offsetX.value <= -maxSwipeDistance -> Color.Red
                        else -> Color.Transparent
                    }
                )
                .align(if (offsetX.value >= 0) Alignment.CenterStart else Alignment.CenterEnd)
        )

        if (showAddIcon) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add to Queue Icon",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
            )
        }

        if (showDeleteIcon) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Icon",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
            )
        }

        Column(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                if (offsetX.value >= maxSwipeDistance) {
                                    onSwipeToAddToQueue()
                                } else if (offsetX.value <= -maxSwipeDistance) {
                                    onSwipeToDelete()
                                }
                                showAddIcon = false
                                showDeleteIcon = false
                                offsetX.animateTo(0f, animationSpec = tween(300))
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        coroutineScope.launch {
                            offsetX.snapTo((offsetX.value + dragAmount).coerceIn(-maxSwipeDistance, maxSwipeDistance))

                            when {
                                offsetX.value >= maxSwipeDistance -> {
                                    showAddIcon = true
                                    showDeleteIcon = false
                                }
                                offsetX.value <= -maxSwipeDistance -> {
                                    showAddIcon = false
                                    showDeleteIcon = true
                                }
                                else -> {
                                    showAddIcon = false
                                    showDeleteIcon = false
                                }
                            }
                        }
                    }
                }
                .fillMaxWidth()
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