package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.search.domain.ResponseState
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTracks(expression: String): Flow<ResponseState>
    fun getTracksFromLocalStorage(key: String): List<Track>
    fun clearTrackLocalStorage(key: String)
    fun saveTrackToLocalStorage(key: String, tracList: MutableList<Track>, track: Track): MutableList<Track>
}