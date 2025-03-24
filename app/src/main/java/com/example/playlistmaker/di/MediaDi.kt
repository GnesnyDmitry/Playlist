package com.example.playlistmaker.di

import com.example.playlistmaker.db.TracksDataBase.TrackConverter
import com.example.playlistmaker.media.data.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.media.domain.FavoriteTracksRepository
import com.example.playlistmaker.media.domain.interactor.FavoriteTracksInteractor
import com.example.playlistmaker.media.domain.interactor.FavoriteTracksInteractorIpml
import com.example.playlistmaker.media.presentation.FavoriteTracksViewModel
import com.example.playlistmaker.media.presentation.MediaViewModel
import com.example.playlistmaker.media.presentation.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule = module {

    factory<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(
            tracksDataBase = get(),
            trackConverter = TrackConverter()
        )
    }

    single<FavoriteTracksInteractor> {
        FavoriteTracksInteractorIpml(
            favoriteTracksRepository = get()
        )
    }

    viewModel { MediaViewModel() }

    viewModel {
        FavoriteTracksViewModel(
            interactor = get()
        )
    }

    viewModel {
        PlaylistViewModel(
            playlistDbInteractor = get()
        )
    }
}

