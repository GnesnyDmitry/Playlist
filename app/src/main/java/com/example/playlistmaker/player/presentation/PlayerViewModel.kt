package com.example.playlistmaker.player.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.interactor.PlaylistDbInteractor
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.player.ui.model.MediaPlayerState
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.ui.model.BottomSheetState
import com.example.playlistmaker.player.ui.model.PlayButtonState
import com.example.playlistmaker.player.ui.model.PlayerViewState
import com.example.playlistmaker.tools.DELAY300L
import com.example.playlistmaker.tools.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val playlistDbInteractor: PlaylistDbInteractor
) : ViewModel() {

    private val playerViewState = MutableLiveData<PlayerViewState>()
    private val btnLikeState = SingleLiveEvent<Boolean>()

    fun playBtnStateLiveData(): LiveData<PlayerViewState> = playerViewState
    fun btnLikeLiveData(): LiveData<Boolean> = btnLikeState

    private val bottomSheetState = MutableStateFlow<BottomSheetState>(BottomSheetState.Empty)
    val bottomSheetStateFlow: StateFlow<BottomSheetState> = bottomSheetState.asStateFlow()

    private var timerJob: Job? = null

    override fun onCleared() {
        super.onCleared()
        playerInteractor.stopMediaPlayer()
    }

    fun getFavoriteState(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playerInteractor.isFavorite(id)
                .take(1)
                .collect { btnLikeState.postValue(it) }
        }
    }

    fun onClickAddTrackInPlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistDbInteractor.getPlaylists().collect { list ->
                if (list.isEmpty()) bottomSheetState.emit(BottomSheetState.Empty)
                else bottomSheetState.emit(BottomSheetState.Playlists(list))
            }
        }
    }

    fun onClickedPlaylist(track: Track, playlist: Playlist) {
        viewModelScope.launch {
            println("qqqq ${playlist.trackList}\n${track.trackId}")
            if (playlistDbInteractor.isTrackAlreadyInPlaylist(playlist.id, track.trackId)) bottomSheetState.emit(BottomSheetState.TrackExistInPlaylist(playlist.name))
            else {
                withContext(Dispatchers.IO) {
                    playlistDbInteractor.addToPlaylist(track, playlist)
                    bottomSheetState.emit(BottomSheetState.AddNewTrackInPlaylist(playlist.name))
                }
            }
        }
    }

    fun onClickBtnLike(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            playerInteractor.isFavorite(track.trackId).collect { isFavorite ->
                if (isFavorite)
                    playerInteractor.removeFromFavorites(track)
                else
                    playerInteractor.handleFavoritesTrack(track)
                btnLikeState.postValue(!isFavorite)
            }
        }
    }

    fun preparePlayer(url: String) {
        playerViewState.postValue(PlayerViewState.PlayBtn(PlayButtonState.PREPARED))
        playerInteractor.prepareMediaPlayer(url)
        playerInteractor.onTrackEnd {
            playerViewState.postValue(PlayerViewState.PlayBtn(PlayButtonState.PREPARED))
        }
    }

    fun stopTrack() {
        playerViewState.postValue(PlayerViewState.PlayBtn(PlayButtonState.PAUSE))
        playerInteractor.stopTrack()
        timerJob?.cancel()
    }

    fun onClickedBtnPlay() {
        when (playerInteractor.getState()) {
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
        playerInteractor.startTrack()
        timerJob = viewModelScope.launch {
            while (playerInteractor.getState() == MediaPlayerState.PLAYING) {
                playerViewState.postValue(PlayerViewState.TrackTime(playerInteractor.getTime()))
                delay(DELAY300L)
            }
        }
    }
}