package com.example.playlistmaker.creator

import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.presentation.PlayerPresenter
import com.example.playlistmaker.player.ui.PlayerRouter
import com.example.playlistmaker.player.presentation.PlayerView
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.search.data.DtoTrackMapper
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.TrackInteractorImpl
import com.example.playlistmaker.search.presentation.SearchPresenter
import com.example.playlistmaker.search.ui.SearchRouter
import com.example.playlistmaker.search.presentation.SearchView

object Creator {

    private fun getTracksRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(), DtoTrackMapper())
    }

    private fun provideTracksInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTracksRepository())
    }

    fun createSearchPresenter(searchView: SearchView, router: SearchRouter): SearchPresenter {
        return SearchPresenter(
            view = searchView,
            router = router,
            searchInteractor = provideTracksInteractor()
        )
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    private fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    fun createPlayerPresenter(view: PlayerView, router: PlayerRouter): PlayerPresenter {
        return PlayerPresenter(
            view = view,
            router = router,
            interactor = providePlayerInteractor()
        )
    }
}