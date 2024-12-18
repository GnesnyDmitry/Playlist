package com.example.playlistmaker.di

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.presentation.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {

    viewModel<PlayerViewModel> {
        PlayerViewModel(
            interactor = get()
        )
    }

    single<PlayerInteractor> {
        PlayerInteractorImpl(
            medialPlayer = get()
        )
    }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(
            mediaPlayer = get()
        )
    }

    factory { MediaPlayer() }
}