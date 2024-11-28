package com.example.playlistmaker.search.domain

import com.example.playlistmaker.domain.models.Track

sealed interface ResponseState {
    data class Content(val data: List<Track>) : ResponseState
    data class NoData(val data: List<Track> = emptyList()) : ResponseState
    data object NoConnect : ResponseState
    data object Error : ResponseState
}