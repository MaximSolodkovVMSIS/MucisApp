package com.example.musicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.musicapp.ui.theme.MusicAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicAppTheme {
                MyApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyApp() {
    var selectedTab by remember { mutableStateOf(0) }
    val favoriteSongs = remember { mutableStateListOf<MusicFile>() } // Список избранных песен
    val addToFavorites: (MusicFile) -> Unit = { file ->
        if (file !in favoriteSongs) favoriteSongs.add(file)
    }
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
                0 -> SearchScreen(addToFavorites = { favoriteSongs.add(it) }) // Передаём функцию добавления
                1 -> MyMusicScreen(favoriteSongs) // Передаём список в MyMusicScreen
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
    MusicAppTheme {
        MyApp()
    }
}
