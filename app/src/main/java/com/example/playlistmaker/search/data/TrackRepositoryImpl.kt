package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.domain.ResponseState

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val dtoTrackMapper: DtoTrackMapper,
) : TrackRepository {
    override fun searchTrack(expression: String): ResponseState {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when(response.resultCode) {
            in 200..300 -> dtoTrackMapper.map(response as TrackResponse)
            in 400..500-> ResponseState.Error
            in 500..600 -> ResponseState.NoConnect
            else -> ResponseState.NoData()
        }
    }
}
