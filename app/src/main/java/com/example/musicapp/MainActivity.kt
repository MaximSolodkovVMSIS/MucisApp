package com.example.musicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.musicapp.ui.theme.MusicAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicAppTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { /* TODO: Handle Search Music click */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Search Music", color = MaterialTheme.colorScheme.onPrimary)
                    }
                    TextButton(
                        onClick = { /* TODO: Handle My Music click */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "My Music", color = MaterialTheme.colorScheme.onPrimary)
                    }
                    TextButton(
                        onClick = { /* TODO: Handle The Player click */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "The Player", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        // Основной контент экрана (можно оставить пустым)
        // Используйте innerPadding для отступов, если требуется
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MusicAppTheme {
        MainScreen()
    }
}
