package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistDbDbInteractorImpl(
    private val playlistRepository: PlaylistRepository,
): PlaylistDbInteractor {

    override suspend fun createPlaylist(playlist: Playlist) {
        playlistRepository.createPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getSavedPlaylists()
    }

    override suspend fun getPlaylist(id: Long): Playlist {
        return playlistRepository.getPlaylist(id)
    }

    override suspend fun addToPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.refreshPlaylist(playlist.copy(trackList = listOf(track) + playlist.trackList))
    }

    override suspend fun removeTrack(playlistId: Long, trackId: Int) {
        var playlist = playlistRepository.getPlaylist(playlistId)
        playlist = playlist.copy(trackList = playlist.trackList.filter { it.trackId != trackId })
        playlistRepository.refreshPlaylist(playlist)
    }

    override suspend fun removePlaylist(playlistId: Long) {
        playlistRepository.removePlaylist(playlistId)
    }

    override suspend fun updateAlbum(id: Long, uri: String, name: String, description: String) {
        playlistRepository.updatePlaylist(id, uri, name, description)
    }
}