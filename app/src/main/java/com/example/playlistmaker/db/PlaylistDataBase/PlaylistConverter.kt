package com.example.playlistmaker.db.PlaylistDataBase

import androidx.room.TypeConverter
import com.example.playlistmaker.db.TracksForPlaylistDataBase.TrackForPlaylistEntity
import com.example.playlistmaker.db.TracksForPlaylistDataBase.TracksPlaylistCrossRef
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
            trackCount = playlistEntity.trackCount
        )
    }

    fun trackToEntity(track: Track): TrackForPlaylistEntity {
        return TrackForPlaylistEntity(
            id = track.trackId.toString(),
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            isFavorite = track.isFavorite
        )
    }

    fun trackToDomain(trackForPlaylistEntity: TrackForPlaylistEntity): Track {
        return Track(
            trackId = trackForPlaylistEntity.id.toInt(),
            trackName = trackForPlaylistEntity.trackName,
            artistName = trackForPlaylistEntity.artistName,
            trackTimeMillis = trackForPlaylistEntity.trackTimeMillis,
            artworkUrl100 = trackForPlaylistEntity.artworkUrl100,
            collectionName = trackForPlaylistEntity.collectionName,
            releaseDate = trackForPlaylistEntity.releaseDate,
            primaryGenreName = trackForPlaylistEntity.primaryGenreName,
            country = trackForPlaylistEntity.country,
            previewUrl = trackForPlaylistEntity.previewUrl,
            isFavorite = trackForPlaylistEntity.isFavorite,
        )
    }

    fun fromEntityList(entities: List<TrackForPlaylistEntity>): List<Track> {
        return entities.map { trackToDomain(it) }
    }

    fun addRelationForTrack(trackId: Int, playlistId: Long): TracksPlaylistCrossRef {
        return TracksPlaylistCrossRef(
            playlistId = playlistId,
            trackId = trackId.toString()
        )
    }

    @TypeConverter
    fun fromTrackIdsList(trackIds: List<String>): String {
        return trackIds.joinToString(",")  //  список в строку
    }

    @TypeConverter
    fun toTrackIdsList(trackIdsString: String): List<String> {
        return trackIdsString.split(",")  //  строку обратно в список
    }
}