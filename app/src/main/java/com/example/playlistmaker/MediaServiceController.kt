package com.example.playlistmaker

import android.net.Uri
import com.example.playlistmaker.player.ui.model.PlayerViewState
import kotlinx.coroutines.flow.StateFlow

interface MediaServiceController {
    val playerStateFlow: StateFlow<PlayerViewState>
    fun play()
    fun pause()
    fun stop()
    fun prepareMediaPlayer(uri: String)
    fun showNotification(title: String, artist: String)
    fun hideNotification()
}