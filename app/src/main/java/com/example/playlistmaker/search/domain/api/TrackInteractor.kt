package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.search.domain.ResponseState
import com.example.playlistmaker.search.ui.model.SearchViewState
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    suspend fun searchTracks(expression: String): ResponseState
    fun getTracksFromLocalStorage(key: String): List<Track>
    fun clearTrackLocalStorage(key: String)
    fun saveTrackToLocalStorage(key: String, tracList: MutableList<Track>, track: Track): MutableList<Track>
}