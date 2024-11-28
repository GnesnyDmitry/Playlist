package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.search.domain.ResponseState

interface TrackRepository {
    fun searchTrack(expression: String): ResponseState
}