package com.example.playlistmaker.search.domain

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    override fun searchTracks(expression: String): Flow<ResponseState> {
        return (repository.searchTrack(expression))
    }

    override fun getTracksFromLocalStorage(key: String): MutableList<Track> {
        return repository.getTracksFromLocalStorage(key)
    }

    override fun clearTrackLocalStorage(key: String) {
        repository.clearTrackLocalStorage(key)
    }

    override fun saveTrackToLocalStorage(key: String, list: MutableList<Track>, track: Track): MutableList<Track> {
        list.remove(track)
        list.add(0, track)
        if (list.size > maxSizeOfHistoryList) list.removeAt(9)
        repository.saveTrackToLocalStorage(key, list)
        return list
    }
    private companion object { const val maxSizeOfHistoryList = 10 }
}