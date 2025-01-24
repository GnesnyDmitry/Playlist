package com.example.playlistmaker.di

import com.example.playlistmaker.media.presentation.MediaViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule = module {
    viewModel { MediaViewModel() }
}
