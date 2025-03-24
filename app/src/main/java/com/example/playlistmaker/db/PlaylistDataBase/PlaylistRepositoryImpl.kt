package com.example.playlistmaker.db.PlaylistDataBase

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.repository.PlaylistRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
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

    override suspend fun refreshPlaylist(playlist: Playlist) {
        dataBase.playlistDao().updatePlaylistFields(
            id = playlist.id,
            trackList = Gson().toJson(playlist.trackList),
            trackCount = playlist.trackList.size,
            date = System.currentTimeMillis(),
        )
    }

    override suspend fun getPlaylist(id: Long): Playlist {
        val playlist = dataBase.playlistDao().getPlaylist(id)
        return playlistConverter.playlistEntityToDomain(playlist)
    }

    override suspend fun removePlaylist(playlistId: Long) {
        dataBase.playlistDao().removePlaylistById(playlistId)
    }

    override suspend fun updatePlaylist(id: Long, uri: String, name: String, description: String) {
        dataBase.playlistDao().updatePlaylist(id, uri, name, description)
    }
}