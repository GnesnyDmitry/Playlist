package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun createPlaylist(playlist: Playlist)
    fun getSavedPlaylists(): Flow<List<Playlist>>
    suspend fun updatePlaylistTracksCount(playlistId: Long)
    suspend fun getPlaylist(id: Long): Playlist
    suspend fun removePlaylist(playlistId: Long)
    suspend fun updatePlaylist(id: Long, uri: String, name: String, description: String)
    suspend fun addTrack(track: Track, playlistId: Long)
    suspend fun removeTrack(playlistId: Long, trackId: Int)
    suspend fun getTracks(playlistId: Long): List<Track>
    suspend fun isTrackAlreadyInPlaylist(playlistId: Long, trackId: String): Boolean
}