package com.example.playlistmaker.db.PlaylistDataBase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [PlaylistEntity::class]
)
abstract class PlaylistDataBase: RoomDatabase() {

    abstract fun playlistDao(): PlaylistDao
}