package com.example.playlistmaker.db.TracksForPlaylistDataBase

import androidx.room.Entity

@Entity(
    tableName = "tracks_playlist_cross_ref",
    primaryKeys = ["playlistId", "trackId"]
)
data class TracksPlaylistCrossRef(
    val playlistId: Long,
    val trackId: String
)
