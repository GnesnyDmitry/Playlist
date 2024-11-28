package com.example.playlistmaker.player.presentation

import com.example.playlistmaker.domain.models.Track

interface PlayerView {
    fun changeImageForPlayButton(image: Int)
    fun updateTrackTimer(time: Int)
    fun getTrack(): Track
}