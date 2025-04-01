package com.example.playlistmaker.db.PlaylistDataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_tracks")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val uri: String,
    val name: String,
    val description: String,
    val trackCount: Int,
    val date: Long,
)