package com.example.playlistmaker.db.PlaylistDataBase

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.PlaylistRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistConverter: PlaylistConverter,
    private val dataBase: PlaylistDataBase
): PlaylistRepository  {

    override suspend fun createPlaylist(playlist: Playlist) {
        dataBase.playlistDao().createPlaylist(playlistConverter.playlistToEntity(playlist))
    }

    override fun getSavedPlaylists(): Flow<List<Playlist>> {
        return dataBase.playlistDao()
            .getPlaylists()
            .map { playlist -> playlist.map { playlistConverter.playlistEntityToDomain(it) } }
    }

    override suspend fun updatePlaylistTracksCount(playlistId: Long) {
        val count = dataBase.playlistDao().getTrackCount(playlistId).first()
        dataBase.playlistDao().updateTrackCount(playlistId, count)
    }

    override suspend fun getPlaylist(id: Long): Playlist {
        val playlist = dataBase.playlistDao().getPlaylist(id)
        return playlistConverter.playlistEntityToDomain(playlist)
    }

    override suspend fun removePlaylist(playlistId: Long) {
        dataBase.playlistDao().removePlaylistWithTracks(playlistId)

    }

    override suspend fun updatePlaylist(id: Long, uri: String, name: String, description: String) {
        dataBase.playlistDao().updatePlaylist(id, uri, name, description)
    }

    override suspend fun addTrack(track: Track, playlistId: Long) {
        dataBase.playlistDao().insertTrack(playlistConverter.trackToEntity(track))
        dataBase.playlistDao().addTrackToPlaylist(playlistConverter.addRelationForTrack(track.trackId, playlistId))
        updatePlaylistTracksCount(playlistId)
    }

    override suspend fun removeTrack(playlistId: Long, trackId: Int) {
        dataBase.playlistDao().removeTrack(playlistId, trackId.toString())
        updatePlaylistTracksCount(playlistId)
    }

    override suspend fun getTracks(playlistId: Long): List<Track> {
        val tracksEntity = dataBase.playlistDao().getTracksForPlaylist(playlistId)
        return playlistConverter.fromEntityList(tracksEntity)
    }

    override suspend fun isTrackAlreadyInPlaylist(playlistId: Long, trackId: String): Boolean {
        return dataBase.playlistDao().isTrackInPlaylist(playlistId, trackId) > 0
    }


}