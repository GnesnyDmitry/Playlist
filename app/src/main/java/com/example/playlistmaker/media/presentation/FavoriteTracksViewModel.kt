package com.example.playlistmaker.media.presentation

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.media.domain.interactor.FavoriteTracksInteractor
import com.example.playlistmaker.media.ui.FavoriteTracksUiStateMapper
import com.example.playlistmaker.media.ui.model.FavoriteFragState
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.tools.TRACK_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val interactor: FavoriteTracksInteractor,
    val favoriteTracksUiStateMapper: FavoriteTracksUiStateMapper
) : ViewModel() {

    private val uiState = MutableStateFlow<FavoriteFragState>(FavoriteFragState.Empty)
    val uiStateFlow: StateFlow<FavoriteFragState> = uiState

    init {
        observeTracks()
    }

    private fun observeTracks() {
        viewModelScope.launch {
            interactor.trackFlow.collect { trackList ->
                if(trackList.isEmpty()) {
                    uiState.value = FavoriteFragState.Empty
                } else {
                    uiState.value = FavoriteFragState.Success(list = trackList)
                }
            }
        }
    }

    fun openPlayerActivity(context: Context, track: Track) {
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra(TRACK_KEY, track)
        context.startActivity(intent)
    }
}