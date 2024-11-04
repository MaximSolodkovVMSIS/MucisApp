package com.example.musicapp

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.window.Dialog
import com.example.musicapp.ui.theme.MusicAppTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    private val favoriteSongs = mutableStateListOf<MusicFile>()
    private val gson = Gson()
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var auth: FirebaseAuth
    private var currentSong: MusicFile? by mutableStateOf(null)
    private var currentSongIndex by mutableIntStateOf(-1)
    private var isPlaying by mutableStateOf(false)
    private var isShuffleEnabled by mutableStateOf(false)
    private var repeatMode by mutableStateOf(RepeatMode.NONE)
    private var shuffleStartIndex = -1 // Индекс песни, с которой началось случайное воспроизведение

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        Log.d("MusicApp", "Firebase initialized")

        loadFavorites()
        setContent {
            MusicAppTheme {
                MyApp(
                    favoriteSongs = favoriteSongs,
                    currentSong = currentSong,
                    isPlaying,
                    ::addToFavorites,
                    ::playSong,
                    ::pauseSong,
                    seekTo = ::seekTo,
                    ::removeFromFavorites,
                    getCurrentPosition = { getCurrentPosition() },
                    getDuration = { getDuration() },
                    playNext = { playNextSong() },
                    playPrevious = { playPreviousSong() },
                    onShuffleToggle = {
                        isShuffleEnabled = !isShuffleEnabled
                        if (isShuffleEnabled) shuffleStartIndex = currentSongIndex
                    },
                    onRepeatToggle = {
                        repeatMode = when (repeatMode) {
                            RepeatMode.NONE -> RepeatMode.ALL
                            RepeatMode.ALL -> RepeatMode.ONE
                            RepeatMode.ONE -> RepeatMode.NONE
                        }
                    },
                    repeatMode = repeatMode,
                    onAddToQueue = { musicFile -> onAddToQueue(musicFile) }
                )
            }
        }
    }

    private fun addToFavorites(musicFile: MusicFile) {
        val existingFile = favoriteSongs.find { it.uri == musicFile.uri }
        if (existingFile != null) {
            val updatedFile = existingFile.copy(title = musicFile.title, artist = musicFile.artist)
            favoriteSongs[favoriteSongs.indexOf(existingFile)] = updatedFile
        } else {
            favoriteSongs.add(musicFile)
        }
        saveFavorites()
    }

    private fun onAddToQueue(musicFile: MusicFile) {
        // Логика добавления в очередь
    }

    private fun saveFavorites() {
        val sharedPreferences = getSharedPreferences("music_app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val serializableList = favoriteSongs.map { SerializableMusicFile(it.title, it.artist, it.uri.toString()) }
        val json = gson.toJson(serializableList)
        editor.putString("favorite_songs", json)
        editor.apply()
    }

    private fun loadFavorites() {
        val sharedPreferences = getSharedPreferences("music_app_prefs", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("favorite_songs", null)

        if (json != null) {
            val type = object : TypeToken<List<SerializableMusicFile>>() {}.type
            val serializableList: List<SerializableMusicFile> = gson.fromJson(json, type)

            val musicFiles = serializableList.map { MusicFile(it.title, it.artist, Uri.parse(it.uriString)) }

            favoriteSongs.clear()
            favoriteSongs.addAll(musicFiles)
        }
    }

    private fun getCurrentPosition(): Int = mediaPlayer?.currentPosition ?: 0

    private fun getDuration(): Int = mediaPlayer?.duration ?: 0

    private fun playSong(uri: Uri) {
        if (mediaPlayer == null || currentSong?.uri != uri) {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(this@MainActivity, uri)
                prepare()
                start()

                setOnCompletionListener {
                    if (repeatMode == RepeatMode.ONE) {
                        playSong(favoriteSongs[currentSongIndex].uri)
                    } else {
                        playNextSong()
                    }
                }
            }
            currentSong = favoriteSongs.find { it.uri == uri }
            currentSongIndex = favoriteSongs.indexOf(currentSong)

        } else {
            mediaPlayer?.start()
        }
        isPlaying = true
    }

    private fun playNextSong() {
        if (favoriteSongs.isNotEmpty()) {
            when {
                isShuffleEnabled -> {
                    var nextIndex: Int
                    do {
                        nextIndex = Random.nextInt(favoriteSongs.size)
                    } while (nextIndex == currentSongIndex)
                    currentSongIndex = nextIndex
                    playSong(favoriteSongs[currentSongIndex].uri)
                }
                currentSongIndex < favoriteSongs.size - 1 -> {
                    currentSongIndex += 1
                    playSong(favoriteSongs[currentSongIndex].uri)
                }
                repeatMode == RepeatMode.ALL -> {
                    currentSongIndex = 0
                    playSong(favoriteSongs[currentSongIndex].uri)
                }
            }
        }
    }

    private fun playPreviousSong() {
        if (favoriteSongs.isNotEmpty()) {
            when {
                isShuffleEnabled -> {
                    if (shuffleStartIndex != -1) {
                        currentSongIndex = shuffleStartIndex
                        playSong(favoriteSongs[currentSongIndex].uri)
                    }
                }
                currentSongIndex > 0 -> {
                    currentSongIndex -= 1
                    playSong(favoriteSongs[currentSongIndex].uri)
                }
                repeatMode == RepeatMode.ALL -> {
                    currentSongIndex = favoriteSongs.size - 1
                    playSong(favoriteSongs[currentSongIndex].uri)
                }
            }
        }
    }

    private fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    private fun pauseSong() {
        mediaPlayer?.pause()
        isPlaying = false
    }

    private fun removeFromFavorites(musicFile: MusicFile) {
        favoriteSongs.remove(musicFile)
        saveFavorites()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}


@Composable
fun MyApp(
    favoriteSongs: List<MusicFile>,
    currentSong: MusicFile?,
    isPlaying: Boolean,
    addToFavorites: (MusicFile) -> Unit,
    playSong: (Uri) -> Unit,
    pauseSong: () -> Unit,
    seekTo: (Int) -> Unit,
    removeFromFavorites: (MusicFile) -> Unit,
    onAddToQueue: (MusicFile) -> Unit,
    getCurrentPosition: () -> Int,
    getDuration: () -> Int,
    playNext: () -> Unit,
    playPrevious: () -> Unit,
    onShuffleToggle: () -> Unit,
    onRepeatToggle: () -> Unit,
    repeatMode: RepeatMode
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showRegistration by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Music App") },
                actions = {
                    IconButton(onClick = { showRegistration = true }) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Account")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(selectedTab = selectedTab, onTabSelected = { selectedTab = it })
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> SearchScreen(addToFavorites = addToFavorites)
                1 -> MyMusicScreen(
                    favoriteSongs,
                    currentSong = currentSong,
                    playSong = playSong,
                    onRemoveSong = removeFromFavorites,
                    onAddToQueue = onAddToQueue

                )
                2 -> PlayerScreen(
                    isPlaying = isPlaying,
                    currentSong = currentSong,
                    onPlayPause = {
                        if (isPlaying) {
                            pauseSong()
                        } else {
                            currentSong?.uri?.let { playSong(it) }
                        }
                    },
                    onNext = playNext,
                    onPrevious = playPrevious,
                    onShuffleToggle = onShuffleToggle,
                    getCurrentPosition = { getCurrentPosition() },
                    getDuration = { getDuration() },
                    onSeekTo = seekTo,
                    onRepeatToggle = onRepeatToggle,
                    repeatMode = repeatMode
                )
            }
        }
    }
    if (showRegistration) {
        Dialog(onDismissRequest = { showRegistration = false }) {
            AccountScreen(onDismiss = { showRegistration = false })
        }
    }
}

@Composable
fun BottomNavigationBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    BottomNavigation {
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            label = { Text("Search") }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.MusicNote, contentDescription = "My Music") },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            label = { Text("My Music") }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.PlayArrow, contentDescription = "Player") },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            label = { Text("Player") }
        )
    }
}