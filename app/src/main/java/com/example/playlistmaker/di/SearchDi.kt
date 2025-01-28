package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.App
import com.example.playlistmaker.search.data.DtoTrackMapper
import com.example.playlistmaker.search.data.GsonConverter
import com.example.playlistmaker.search.data.LocalTrackStorage
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.data.network.ITunesSearchApi
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.TrackInteractorImpl
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.presentation.SearchViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchModule = module {

    viewModel<SearchViewModel> {
        SearchViewModel(
            searchInteractor = get()
        )
    }

    single<TrackInteractor> {
        TrackInteractorImpl(
            repository = get()
        )
    }

    single<TrackRepository> {
        TrackRepositoryImpl(
            networkClient = get(),
            dtoTrackMapper = get(),
            localTrackStorage = get()
        )
    }

    single<NetworkClient> {
        RetrofitNetworkClient(
            imdbService = get(),
            context = get(),
        )
    }

    factory<DtoTrackMapper> {
        DtoTrackMapper()
    }

    single<LocalTrackStorage> {
        LocalTrackStorage(
            gsonConverter = get(),
            sharedPreferences = get(),
        )
    }

    single {
        androidContext()
            .getSharedPreferences("app_preference", Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<GsonConverter> {
        GsonConverter(
            gson = get()
        )
    }

    single<ITunesSearchApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesSearchApi::class.java)
    }

    single<App> {
        App.instance
    }

}