package com.example.playlistmaker.db.TracksForPlaylistDataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_for_playlist")
data class TrackForPlaylistEntity(
    @PrimaryKey val id: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String?,
)