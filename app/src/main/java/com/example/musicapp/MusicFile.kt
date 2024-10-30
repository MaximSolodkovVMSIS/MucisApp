package com.example.musicapp

import android.net.Uri

data class SerializableMusicFile(
    val title: String,
    val artist: String,
    val uriString: String
)

data class MusicFile(
    val title: String,
    val artist: String,
    val uri: Uri
)
