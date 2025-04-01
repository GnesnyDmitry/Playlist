package com.example.playlistmaker.db.PlaylistDataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.playlistmaker.db.TracksForPlaylistDataBase.TrackForPlaylistEntity
import com.example.playlistmaker.db.TracksForPlaylistDataBase.TracksPlaylistCrossRef

@Database(
    version = 3,
    entities = [PlaylistEntity::class, TrackForPlaylistEntity::class, TracksPlaylistCrossRef::class]
)
@TypeConverters(PlaylistConverter::class)
abstract class PlaylistDataBase: RoomDatabase() {

    abstract fun playlistDao(): PlaylistDao
}