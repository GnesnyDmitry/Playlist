package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.player.ui.model.MediaPlayerState
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    var state: MediaPlayerState

    suspend fun addToFavorites(track: Track)
    suspend fun removeFromFavorites(track: Track)
    fun isFavorite(id: Int): Flow<Boolean>
    fun prepareMediaPlayer(url: String)
    fun startTrack()
    fun getTime(): Int
    fun setStopListener(action: () -> Unit)
    fun stopTrack()
    fun stopMediaPlayer()
}