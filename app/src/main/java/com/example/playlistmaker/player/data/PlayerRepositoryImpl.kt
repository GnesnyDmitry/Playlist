package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.db.Converter
import com.example.playlistmaker.db.TrackEntity
import com.example.playlistmaker.db.TracksDataBase
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.ui.model.MediaPlayerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
    private val tracksDataBase: TracksDataBase,
    private val converter: Converter
) : PlayerRepository {

    override var state = MediaPlayerState.PREPARED

    override suspend fun addToFavorites(track: Track) {
        val entity = converter.trackToEntity(track).copy(isFavorite = true)
        tracksDataBase.trackDao().insertTrack(entity)
    }

    override suspend fun removeFromFavorites(track: Track) {
        tracksDataBase.trackDao().deleteTrackEntity(converter.trackToEntity(track).copy(isFavorite = false))
    }

    override fun isFavorite(id: Int): Flow<Boolean> = flow {
        emit(tracksDataBase.trackDao().isFavorite(id))
    }

    override fun prepareMediaPlayer(url: String) {
        mediaPlayer.apply {
            reset()
            setOnPreparedListener { state = MediaPlayerState.PREPARED }
            setDataSource(url)
            prepare()
        }
    }

    override fun startTrack() {
        state = MediaPlayerState.PLAYING
        mediaPlayer.start()
    }

    override fun getTime(): Int {
        return mediaPlayer.currentPosition
    }

    override fun setStopListener(action: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            state = MediaPlayerState.PREPARED
            action.invoke()
        }
    }

    override fun stopTrack() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            state = MediaPlayerState.PAUSED
        }
    }

    override fun stopMediaPlayer() {
        if (state == MediaPlayerState.PAUSED) {
            mediaPlayer.stop()
        }
        mediaPlayer.reset()
    }
}