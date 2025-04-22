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
        playlistRepository.addTrack(track, playlist.id)
    }

    override suspend fun removeTrack(playlistId: Long, trackId: Int) {
        playlistRepository.removeTrack(playlistId, trackId)
    }

    override suspend fun removePlaylist(playlistId: Long) {
        playlistRepository.removePlaylist(playlistId)
    }

    override suspend fun updatePlaylist(id: Long, uri: String, name: String, description: String) {
        playlistRepository.updatePlaylist(id, uri, name, description)
    }

    override suspend fun getTracks(playlistId: Long): List<Track> {
        return playlistRepository.getTracks(playlistId)
    }

    override suspend fun isTrackAlreadyInPlaylist(playlistId: Long, trackId: Int): Boolean {
        return playlistRepository.isTrackAlreadyInPlaylist(playlistId, trackId.toString())
    }


}