package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun createPlaylist(playlist: Playlist)
    fun getSavedPlaylists(): Flow<List<Playlist>>
    suspend fun refreshPlaylist(playlist: Playlist)
    suspend fun getPlaylist(id: Long): Playlist
    suspend fun removePlaylist(playlistId: Long)
    suspend fun updatePlaylist(id: Long, uri: String, name: String, description: String)
}