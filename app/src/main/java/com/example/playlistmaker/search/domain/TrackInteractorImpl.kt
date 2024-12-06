package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, action: (ResponseState) -> Unit) {
        executor.execute {
            action.invoke(repository.searchTrack(expression))
        }
    }
}