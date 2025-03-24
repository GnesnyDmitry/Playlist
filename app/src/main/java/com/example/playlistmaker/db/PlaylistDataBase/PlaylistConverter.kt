package com.example.playlistmaker.db.PlaylistDataBase

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistConverter {

    fun playlistToEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            uri = playlist.uri,
            name = playlist.name,
            description = playlist.description,
            trackList = Gson().toJson(playlist.trackList),
            trackCount = playlist.trackCount,
            date = System.currentTimeMillis(),
        )
    }
    fun playlistEntityToDomain(playlistEntity: PlaylistEntity): Playlist {
        val type = object : TypeToken<List<Track>>() {}.type
        return Playlist(
            id = playlistEntity.id,
            uri = playlistEntity.uri,
            name = playlistEntity.name,
            description = playlistEntity.description,
            trackList = Gson().fromJson(playlistEntity.trackList, type),
            trackCount = playlistEntity.trackCount
        )
    }
}