package com.example.playlistmaker.creat_album.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.creat_album.ui.model.CreatePlaylistViewState
import com.example.playlistmaker.domain.interactor.PlaylistDbInteractor
import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class CreatePlaylistViewModel(
    private val interactor: PlaylistDbInteractor
) : ViewModel() {

    protected val uiState = MutableStateFlow<CreatePlaylistViewState>(CreatePlaylistViewState.Default)
    val uiStateFlow: StateFlow<CreatePlaylistViewState> = uiState.asStateFlow()

    protected var playlist = Playlist(
        id = 0,
        name = "",
        description = "",
        uri = "",
        trackList = emptyList(),
        trackCount = 0
    )

    open fun createPlaylist(data: Playlist?) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.createPlaylist(playlist)
            uiState.emit(CreatePlaylistViewState.SavePlaylist(name = playlist.name))
        }
    }

    fun doOnDescriptionTextChange(text: String) {
        playlist = playlist.copy(description = text)
    }

    fun doOnNameTextChange(text: String) {
        playlist = playlist.copy(name = text)
    }

    fun doOnPhotoChange(uri: String) {
        playlist = playlist.copy(uri = uri)
    }

    fun hideDialogGoBack() {
        viewModelScope.launch {
            uiState.emit(CreatePlaylistViewState.Default)
        }
    }

    open fun onGoBack() {
        viewModelScope.launch {
            if (playlist.uri.isNotEmpty() || playlist.name.isNotEmpty() || playlist.description.isNotEmpty()) {
                uiState.emit(CreatePlaylistViewState.ShowDialog)
            } else {
                uiState.emit(CreatePlaylistViewState.GoBack)
            }
        }
    }
}