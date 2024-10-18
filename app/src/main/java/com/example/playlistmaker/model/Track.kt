package com.example.playlistmaker.model

data class Track(
    val trackId : Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String
)
