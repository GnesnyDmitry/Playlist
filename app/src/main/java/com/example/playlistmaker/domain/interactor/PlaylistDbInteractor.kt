package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistDbInteractor {

    suspend fun createPlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addToPlaylist(track: Track, playlist: Playlist)
    suspend fun getPlaylist(id: Long): Playlist
    suspend fun removeTrack(playlistId: Long, trackId: Int)
    suspend fun removePlaylist(playlistId: Long)
    suspend fun updateAlbum(id: Long, uri: String, name: String, description: String)
}