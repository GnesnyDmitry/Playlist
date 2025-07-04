package com.example.playlistmaker.playlist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.interactor.PlaylistDbInteractor
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.playlist.ui.model.PlaylistState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistId: Long,
    private val playlistDbInteractor: PlaylistDbInteractor,
) : ViewModel() {

    private val uiState = MutableStateFlow<PlaylistState>(PlaylistState.Default)
    val uiStateflow: StateFlow<PlaylistState> = uiState.asStateFlow()

    private var playlist: Playlist? = null
    private var tracksPlaylist: List<Track>? = null

    init {
        getPlaylist()
    }

    fun getTimeAllTracks(tracks: List<Track>): Int {
           return tracks.sumOf { it.trackTimeMillis }
    }

    fun getPlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            playlist = playlistDbInteractor.getPlaylist(playlistId)
            tracksPlaylist = playlistDbInteractor.getTracks(playlistId)
            val totalTime = getTimeAllTracks(tracksPlaylist!!)
            if (playlist!!.trackCount == 0) {
                uiState.emit(PlaylistState.EmptyBottomSheet(playlist!!))
            } else {
                uiState.emit(PlaylistState.SuccessBottomSheet(playlist!!, tracksPlaylist!!, totalTime))
            }
        }
    }

    fun onClickDots() {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.emit(PlaylistState.Default)
            val playlist = playlistDbInteractor.getPlaylist(playlistId)
            uiState.emit(PlaylistState.Dots(playlist))
        }
    }

    fun onClickShare() {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.emit(PlaylistState.Default)
            playlist = playlistDbInteractor.getPlaylist(playlistId)
            tracksPlaylist = playlistDbInteractor.getTracks(playlistId)
            if (playlist!!.trackCount == 0) {
                uiState.emit(PlaylistState.EmptyShare)
            } else {
                uiState.emit(PlaylistState.Share(playlist!!, tracksPlaylist!!))
            }
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistDbInteractor.removePlaylist(playlistId)
        }
    }

    fun deleteTrack(trackId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistDbInteractor.removeTrack(playlistId, trackId)
            getPlaylist()
        }
    }
}