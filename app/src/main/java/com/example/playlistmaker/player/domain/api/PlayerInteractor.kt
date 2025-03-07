package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.player.ui.model.MediaPlayerState
import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {
    fun isFavorite(id: Int): Flow<Boolean>
    suspend fun handleFavoritesTrack(track: Track)
    suspend fun removeFromFavorites(track: Track)
    fun prepareMediaPlayer(url: String)
    fun startTrack()
    fun getTime(): Int
    fun onTrackEnd(action: () -> Unit)
    fun stopTrack()
    fun stopMediaPlayer()
    fun getState(): MediaPlayerState
}