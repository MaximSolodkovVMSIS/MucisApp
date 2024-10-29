package com.example.musicapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PlayerScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Player")
    }
}
