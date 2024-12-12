package com.example.playlistmaker.search.data

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.domain.ResponseState

class DtoTrackMapper {

    fun map(response: TrackResponse): ResponseState {

        val data = response.results.map {
            Track(
                trackId = it.trackId,
                trackName = it.trackName,
                artistName = it.artistName,
                trackTimeMillis = it.trackTimeMillis,
                artworkUrl100 = it.artworkUrl100,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                previewUrl = it.previewUrl
            )
        }.filter { it.previewUrl != null }
        return if (data.isNotEmpty()) {
            ResponseState.Content(data)
        } else ResponseState.NoData()
    }
}