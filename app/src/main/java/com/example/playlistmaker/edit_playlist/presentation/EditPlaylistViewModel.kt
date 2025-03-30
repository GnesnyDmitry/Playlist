package com.example.playlistmaker.edit_playlist.presentation

import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.creat_album.presentation.CreatePlaylistViewModel
import com.example.playlistmaker.creat_album.ui.model.CreatePlaylistViewState
import com.example.playlistmaker.domain.interactor.PlaylistDbInteractor
import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val interactor: PlaylistDbInteractor
): CreatePlaylistViewModel(interactor) {

    override fun onGoBack() {
        viewModelScope.launch {
            if (playlist.name.isNotEmpty()) {
                uiState.emit(CreatePlaylistViewState.GoBack)
            }
        }
    }

    override fun createPlaylist(data: Playlist?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (data!!.name.isNotEmpty()) {
                interactor.updateAlbum(
                    id = data.id,
                    uri = if (playlist.uri == "") data.uri else playlist.uri,
                    name = playlist.name,
                    description = playlist.description
                )
                uiState.emit(CreatePlaylistViewState.SavePlaylist(playlist.name))
            }
        }
    }
}