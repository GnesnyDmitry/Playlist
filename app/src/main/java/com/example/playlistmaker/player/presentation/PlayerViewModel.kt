package com.example.playlistmaker.player.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.player.ui.model.MediaPlayerState
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.ui.model.PlayButtonState
import com.example.playlistmaker.player.ui.model.PlayerViewState
import com.example.playlistmaker.tools.DELAY300L
import com.example.playlistmaker.tools.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class PlayerViewModel(private val interactor: PlayerInteractor) : ViewModel() {

    private val playerViewState = MutableLiveData<PlayerViewState>()
    private val btnLikeState = SingleLiveEvent<Boolean>()

    fun playBtnStateLiveData(): LiveData<PlayerViewState> = playerViewState
    fun btnLikeLiveData(): LiveData<Boolean> = btnLikeState

    private var timerJob: Job? = null

    override fun onCleared() {
        super.onCleared()
        interactor.stopMediaPlayer()
    }

    fun getFavoriteState(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.isFavorite(id)
                .take(1)
                .collect { btnLikeState.postValue(it) }
        }
    }

    fun onClickBtnLike(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.isFavorite(track.trackId).collect { isFavorite ->
                if (isFavorite)
                    interactor.removeFromFavorites(track)
                else
                    interactor.handleFavoritesTrack(track)
                btnLikeState.postValue(!isFavorite)
            }
        }
    }

    fun preparePlayer(url: String) {
        playerViewState.postValue(PlayerViewState.PlayBtn(PlayButtonState.PREPARED))
        interactor.prepareMediaPlayer(url)
        interactor.onTrackEnd {
            playerViewState.postValue(PlayerViewState.PlayBtn(PlayButtonState.PREPARED))
        }
    }

    fun stopTrack() {
        playerViewState.postValue(PlayerViewState.PlayBtn(PlayButtonState.PAUSE))
        interactor.stopTrack()
        timerJob?.cancel()
    }

    fun onClickedBtnPlay() {
        when (interactor.getState()) {
            MediaPlayerState.PREPARED -> onTrackStart()
            MediaPlayerState.PLAYING -> stopTrack()
            MediaPlayerState.PAUSED -> onTrackStart()
            MediaPlayerState.DEFAULT -> playerViewState.postValue(
                PlayerViewState.PlayBtn(
                    PlayButtonState.PREPARED
                )
            )
        }
    }

    private fun onTrackStart() {
        playerViewState.value = PlayerViewState.PlayBtn(PlayButtonState.PLAY)
        interactor.startTrack()
        timerJob = viewModelScope.launch {
            while (interactor.getState() == MediaPlayerState.PLAYING) {
                playerViewState.postValue(PlayerViewState.TrackTime(interactor.getTime()))
                delay(DELAY300L)
            }
        }
    }
}