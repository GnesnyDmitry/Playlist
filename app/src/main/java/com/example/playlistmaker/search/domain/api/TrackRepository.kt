package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.search.domain.ResponseState
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    suspend fun searchTrack(expression: String): ResponseState
    fun getTracksFromLocalStorage(key: String): MutableList<Track>
    fun clearTrackLocalStorage(key: String)
    fun saveTrackToLocalStorage(key: String, list: MutableList<Track>)
}