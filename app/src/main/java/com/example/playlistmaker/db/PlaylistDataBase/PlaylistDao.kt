package com.example.playlistmaker.db.PlaylistDataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createPlaylist(playlist: PlaylistEntity)

    @Query("UPDATE playlists_tracks SET trackList = :trackList, trackCount = :trackCount, date = :date WHERE id = :id;")
    suspend fun updatePlaylistFields(id: Long, trackList: String, trackCount: Int, date: Long)

    @Query("UPDATE playlists_tracks SET uri = :uri, name = :name, description = :description WHERE id = :id;")
    suspend fun updatePlaylist(id: Long, uri: String, name: String, description: String)

    @Query("SELECT * FROM playlists_tracks ORDER BY date DESC;")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists_tracks WHERE id = :id LIMIT 1;")
    suspend fun getPlaylist(id: Long): PlaylistEntity

    @Query("DELETE FROM playlists_tracks WHERE id = :id;")
    suspend fun removePlaylistById(id: Long)
}