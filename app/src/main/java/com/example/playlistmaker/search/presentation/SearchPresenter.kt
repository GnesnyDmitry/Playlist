package com.example.playlistmaker.search.presentation

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.search.domain.ResponseState
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.ui.SearchRouter

class SearchPresenter(
    private val view: SearchView,
    private val router: SearchRouter,
    private val searchInteractor: TrackInteractor
) {

    private fun onLoadingTracks(response: ResponseState) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            when(response) {
                is ResponseState.Content -> {
                    view.showLoadingTrackList()
                    view.setTrackList(response.data)
                    view.showTrackList()
                    view.hideAllPlaceholders()
                }
                is ResponseState.NoData -> {
                    view.showTrackList()
                    view.showPlaceholderNothingFound()
                }
                is ResponseState.Error -> {
                    view.showTrackList()
                    view.showPlaceholderNothingFound()
                }
                is ResponseState.NoConnect -> {
                    view.showTrackList()
                    view.showPlaceholderNoConnection()
                }
            }
        }
    }

    fun onClickedTrack(track: Track) {
        router.openPlayerActivity(track = track)
    }
    fun searchTrack(expression: String) {
        searchInteractor.searchTracks(expression, this::onLoadingTracks)
    }
}