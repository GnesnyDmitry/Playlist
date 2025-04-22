package com.example.playlistmaker.di

import com.example.playlistmaker.playlist.presentation.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistModule = module {
    viewModel { (id: Long) -> PlaylistViewModel(
        playlistId = id,
        playlistDbInteractor = get()
    ) }
}