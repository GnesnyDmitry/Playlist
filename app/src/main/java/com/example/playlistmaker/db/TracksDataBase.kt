package com.example.playlistmaker.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [TrackEntity::class])
abstract class TracksDataBase: RoomDatabase() {

    abstract fun trackDao(): TrackDao
}