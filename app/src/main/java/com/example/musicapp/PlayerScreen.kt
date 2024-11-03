package com.example.musicapp

import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

enum class RepeatMode {
    NONE, ONE, ALL
}

@Composable
fun PlayerScreen(
    isPlaying: Boolean,
    currentSong: MusicFile?,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onShuffleToggle: () -> Unit,
    getCurrentPosition: () -> Int,
    getDuration: () -> Int,
    onSeekTo: (Int) -> Unit,
    onRepeatToggle: () -> Unit,
    repeatMode: RepeatMode
) {
    var position by remember { mutableIntStateOf(0) }
    val duration = getDuration()
    var isSeeking by remember { mutableStateOf(false) }
    var isShuffleEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(isPlaying, isSeeking) {
        while (isPlaying && !isSeeking) {
            position = getCurrentPosition()
            delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (currentSong == null) {
            Text(
                text = "Музыка не воспроизводится",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = currentSong.title, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Исполнитель: ${currentSong.artist}")
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Slider(
                    value = position.toFloat(),
                    onValueChange = { newPosition ->
                        isSeeking = true
                        position = newPosition.toInt()
                    },
                    onValueChangeFinished = {
                        onSeekTo(position)
                        isSeeking = false
                    },
                    valueRange = 0f..duration.toFloat(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = formatTime(position))
                    Text(text = formatTime(duration))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    onRepeatToggle()
                }) {
                    Icon(
                        imageVector = when (repeatMode) {
                            RepeatMode.NONE -> Icons.Filled.Repeat
                            RepeatMode.ALL -> Icons.Filled.Repeat
                            RepeatMode.ONE -> Icons.Filled.RepeatOne
                        },
                        contentDescription = "Repeat Mode",
                        tint = if (repeatMode != RepeatMode.NONE) MaterialTheme.colors.primary else Color.Gray
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = onPrevious) {
                    Icon(Icons.Filled.SkipPrevious, contentDescription = "Previous")
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onPlayPause) {
                    Icon(
                        if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onNext) {
                    Icon(Icons.Filled.SkipNext, contentDescription = "Next")
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        onShuffleToggle()
                        isShuffleEnabled = !isShuffleEnabled
                    }
                ) {
                    Icon(
                        Icons.Filled.Shuffle,
                        contentDescription = "Shuffle",
                        tint = if (isShuffleEnabled) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}

private fun formatTime(milliseconds: Int): String {
    val minutes = (milliseconds / 1000) / 60
    val seconds = (milliseconds / 1000) % 60
    return "%02d:%02d".format(minutes, seconds)
}
