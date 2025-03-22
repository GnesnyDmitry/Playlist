package com.example.playlistmaker.player.domain

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.media.domain.FavoriteTracksRepository
import com.example.playlistmaker.player.ui.model.MediaPlayerState
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.api.PlayerRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.coroutineContext

class PlayerInteractorImpl(
    private val playerRepository: PlayerRepository,
) : PlayerInteractor {

    override fun isFavorite(id: Int): Flow<Boolean> {
        return playerRepository.isFavorite(id)
    }

    override suspend fun handleFavoritesTrack(track: Track) {
          playerRepository.addToFavorites(track)
    }

    override suspend fun removeFromFavorites(track: Track) {
            playerRepository.removeFromFavorites(track)
    }

    override fun prepareMediaPlayer(url: String) {
        playerRepository.prepareMediaPlayer(url)
    }

    override fun startTrack() {
        playerRepository.startTrack()
    }

    override fun getTime(): Int {
        return playerRepository.getTime()
    }

    override fun onTrackEnd(action: () -> Unit) {
        playerRepository.setStopListener(action)
    }

    override fun stopTrack() {
        playerRepository.stopTrack()
    }

    override fun stopMediaPlayer() {
        playerRepository.stopMediaPlayer()
    }

    override fun getState(): MediaPlayerState {
        return playerRepository.state
    }
}