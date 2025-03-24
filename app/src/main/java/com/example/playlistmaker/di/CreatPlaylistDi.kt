package com.example.playlistmaker.di

import com.example.playlistmaker.creat_album.presentation.CreatePlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val createPlaylistModule = module {

    viewModel { CreatePlaylistViewModel(
        interactor = get()
    ) }
}