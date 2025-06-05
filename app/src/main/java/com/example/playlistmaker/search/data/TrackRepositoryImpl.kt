package com.example.playlistmaker.search.data

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.domain.ResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val dtoTrackMapper: DtoTrackMapper,
    private val localTrackStorage: LocalTrackStorage,
) : TrackRepository {
    override suspend fun searchTrack(expression: String): ResponseState {

        val response = networkClient.doRequest(TrackSearchRequest(expression))

        return when(response.resultCode) {
            in 200..300 -> dtoTrackMapper.map(response as TrackResponse)
            in 400..500-> ResponseState.Error
            in 500..600 -> ResponseState.NoConnect
            -1 -> ResponseState.NoConnect
            else -> ResponseState.NoData()
        }
    }

    override fun getTracksFromLocalStorage(key: String): MutableList<Track> {
        return localTrackStorage.readData(key)
    }

    override fun clearTrackLocalStorage(key: String) {
        localTrackStorage.clearSharedPreference(key)
    }

    override fun saveTrackToLocalStorage(key: String, list: MutableList<Track>) {
        localTrackStorage.writeData(key, list)
    }
}
