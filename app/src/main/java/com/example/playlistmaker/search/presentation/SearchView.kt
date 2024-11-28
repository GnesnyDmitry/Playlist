package com.example.playlistmaker.search.presentation

import com.example.playlistmaker.domain.models.Track

interface SearchView {
    fun showPlaceholderNoConnection()
    fun showPlaceholderNothingFound()
    fun hideAllPlaceholders()
    fun hideHeaderAndFooter()
    fun showLoadingTrackList()
    fun setTrackList(bodyResponse: List<Track>)
    fun showTrackList()
}