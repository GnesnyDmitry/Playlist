package com.example.playlistmaker.db.PlaylistDataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.db.TracksForPlaylistDataBase.TrackForPlaylistEntity
import com.example.playlistmaker.db.TracksForPlaylistDataBase.TracksPlaylistCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createPlaylist(playlist: PlaylistEntity)

    @Transaction
    @Query("SELECT * FROM tracks_for_playlist WHERE id IN (SELECT trackId FROM tracks_playlist_cross_ref WHERE playlistId = :playlistId)")
    fun getTracksForPlaylist(playlistId: Long): List<TrackForPlaylistEntity>

    @Query("UPDATE playlists_tracks SET trackCount = :count WHERE id = :playlistId")
    suspend fun updateTrackCount(playlistId: Long, count: Int)

    @Query("SELECT COUNT(trackId) FROM tracks_playlist_cross_ref WHERE playlistId = :playlistId")
    fun getTrackCount(playlistId: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackForPlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPlaylist(tracksPlaylistCrossRef: TracksPlaylistCrossRef)

    @Query("DELETE FROM tracks_playlist_cross_ref WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: String)

    @Query("SELECT COUNT(*) FROM tracks_playlist_cross_ref WHERE trackId = :trackId")
    suspend fun countPlaylistRelations(trackId: String): Int

    @Delete
    suspend fun deleteTrack(track: TrackForPlaylistEntity)

    suspend fun removeTrack(playlistId: Long, trackId: String) {
        removeTrackFromPlaylist(playlistId, trackId)

        val relationCount = countPlaylistRelations(trackId)
        if (relationCount == 0) {
            val track = getTrackById(trackId)
            track?.let {
                deleteTrack(it)
            }
        }
    }

    @Query("SELECT * FROM tracks_for_playlist WHERE id = :trackId LIMIT 1")
    suspend fun getTrackById(trackId: String): TrackForPlaylistEntity?

    @Query("UPDATE playlists_tracks SET uri = :uri, name = :name, description = :description WHERE id = :id;")
    suspend fun updatePlaylist(id: Long, uri: String, name: String, description: String)

    @Query("SELECT * FROM playlists_tracks ORDER BY date DESC;")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists_tracks WHERE id = :id LIMIT 1;")
    suspend fun getPlaylist(id: Long): PlaylistEntity

    @Query("DELETE FROM playlists_tracks WHERE id = :id;")
    suspend fun removePlaylistById(id: Long)

    @Query("SELECT COUNT(*) FROM tracks_playlist_cross_ref WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun isTrackInPlaylist(playlistId: Long, trackId: String): Int
}