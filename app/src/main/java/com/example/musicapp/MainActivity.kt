package com.example.musicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
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

@Composable
fun MyApp() {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Music App") },
                actions = {
                    IconButton(onClick = { /* Ничего не делает */ }) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Account")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

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
