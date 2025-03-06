package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.db.TracksDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataBaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), TracksDataBase::class.java, "tracks_database.db")
            .build()
    }
}