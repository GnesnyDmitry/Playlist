package com.example.playlistmaker.media.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.interactor.PlaylistDbInteractor
import com.example.playlistmaker.media.ui.model.PlaylistViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistDbInteractor: PlaylistDbInteractor
) : ViewModel() {

    private val uiState = MutableStateFlow<PlaylistViewState>(PlaylistViewState.Empty)
    val uiStateFlow: StateFlow<PlaylistViewState> = uiState.asStateFlow()

    init { getPlaylists() }

    private fun getPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistDbInteractor.getPlaylists().collect { list ->
                if (list.isEmpty()) uiState.emit(PlaylistViewState.Empty)
                else uiState.emit(PlaylistViewState.PlaylistSuccess(list))
            }

        }
    }
}