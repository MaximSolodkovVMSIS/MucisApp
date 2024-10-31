package com.example.musicapp

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.example.musicapp.ui.theme.MusicAppTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : ComponentActivity() {

    private val favoriteSongs = mutableStateListOf<MusicFile>()
    private val gson = Gson()
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadFavorites()

        setContent {
            MusicAppTheme {
                MyApp(favoriteSongs, ::addToFavorites, ::isFavorite, ::playSong)
            }
        }
    }

    private fun addToFavorites(musicFile: MusicFile) {
        if (!favoriteSongs.contains(musicFile)) {
            favoriteSongs.add(musicFile)
            saveFavorites()
        }
    }

    private fun isFavorite(musicFile: MusicFile): Boolean {
        return favoriteSongs.contains(musicFile)
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

            // Преобразуем SerializableMusicFile обратно в MusicFile
            val musicFiles = serializableList.map { MusicFile(it.title, it.artist, Uri.parse(it.uriString)) }

            favoriteSongs.clear()
            favoriteSongs.addAll(musicFiles)
        }
    }

    private fun playSong(uri: Uri) {
        mediaPlayer?.release() // Освобождаем предыдущий MediaPlayer, если он есть
        mediaPlayer = MediaPlayer().apply {
            setDataSource(this@MainActivity, uri)
            prepare()
            start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // Освобождаем ресурсы MediaPlayer
    }
}

@Composable
fun MyApp(
    favoriteSongs: List<MusicFile>,
    addToFavorites: (MusicFile) -> Unit,
    isFavorite: (MusicFile) -> Boolean,
    playSong: (Uri) -> Unit // Передаем функцию воспроизведения
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Music App") },
                actions = {
                    IconButton(onClick = { /* Логика для модального окна */ }) {
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
                1 -> MyMusicScreen(favoriteSongs, playSong) // Передаем функцию воспроизведения
                2 -> PlayerScreen()
            }
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val dummyFavorites = listOf(
        MusicFile("Song 1", "Artist 1", Uri.parse("uri1")),
        MusicFile("Song 2", "Artist 2", Uri.parse("uri2"))
    )
    MusicAppTheme {
        MyApp(
            favoriteSongs = dummyFavorites,
            addToFavorites = {},
            isFavorite = { false },
            playSong = {}
        )
    }
}