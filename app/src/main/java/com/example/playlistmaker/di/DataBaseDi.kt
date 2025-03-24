package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.db.PlaylistDataBase.PlaylistConverter
import com.example.playlistmaker.db.PlaylistDataBase.PlaylistDataBase
import com.example.playlistmaker.db.PlaylistDataBase.PlaylistRepositoryImpl
import com.example.playlistmaker.db.TracksDataBase.TracksDataBase
import com.example.playlistmaker.domain.interactor.PlaylistDbInteractor
import com.example.playlistmaker.domain.interactor.PlaylistDbDbInteractorImpl
import com.example.playlistmaker.domain.repository.PlaylistRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataBaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), TracksDataBase::class.java, "tracks_database.db")
            .build()
    }

    single {
        Room.databaseBuilder(androidContext(), PlaylistDataBase::class.java, "playlists_tracks_database.db")
            .build()
    }

    factory<PlaylistRepository> {
        PlaylistRepositoryImpl(
            playlistConverter = PlaylistConverter(),
            dataBase = get()
        )
    }

    single<PlaylistDbInteractor> {
        PlaylistDbDbInteractorImpl(
            playlistRepository = get()
        )
    }
}