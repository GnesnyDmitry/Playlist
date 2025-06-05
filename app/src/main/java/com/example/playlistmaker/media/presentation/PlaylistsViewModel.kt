package com.example.playlistmaker.media.presentation

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.interactor.PlaylistDbInteractor
import com.example.playlistmaker.main.MainActivity
import com.example.playlistmaker.media.ui.PlaylistScreenUiStateMapper
import com.example.playlistmaker.media.ui.model.PlaylistViewState
import com.example.playlistmaker.playlist.ui.model.PlaylistFrag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistDbInteractor: PlaylistDbInteractor,
    val playlistScreenUiStateMapper: PlaylistScreenUiStateMapper
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

    fun goToCreatePlaylist(context: MainActivity) {
        context.findNavController(R.id.container_view).navigate(
            resId = R.id.action_mediaFrag_to_albumFrag
        )
    }

    fun goToPlaylist(context: MainActivity, id: Long) {
        context.findNavController(R.id.container_view).navigate(
            resId = R.id.action_mediaFrag_to_playlistFrag,
            args = bundleOf(PlaylistFrag.PLAYLIST_KEY to id)
        )
    }
}