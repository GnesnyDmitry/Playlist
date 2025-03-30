package com.example.playlistmaker.di

import com.example.playlistmaker.edit_playlist.presentation.EditPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val editPlaylistModule = module {
    viewModel { EditPlaylistViewModel(get()) }
}