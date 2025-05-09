package com.example.playlistmaker.player.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.MediaServiceController
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val playlistDbInteractor: PlaylistDbInteractor
) : ViewModel() {

    private val playerViewState = MutableStateFlow<PlayerViewState>(PlayerViewState.PlayBtn(PlayButtonState.PREPARED))
    private val btnLikeState = SingleLiveEvent<Boolean>()

    val playerViewStateFlow: StateFlow<PlayerViewState> = playerViewState.asStateFlow()
    fun btnLikeLiveData(): LiveData<Boolean> = btnLikeState

    private val bottomSheetState = MutableStateFlow<BottomSheetState>(BottomSheetState.Empty)
    val bottomSheetStateFlow: StateFlow<BottomSheetState> = bottomSheetState.asStateFlow()

    private var mediaServiceController: MediaServiceController? = null

    override fun onCleared() {
        super.onCleared()
        mediaServiceController?.stop()
        mediaServiceController = null
    }

    fun bindService(controller: MediaServiceController?) {
        mediaServiceController = controller

        viewModelScope.launch {
            controller?.playerStateFlow?.collect { state ->
                when (state) {
                    is PlayerViewState.TrackTime -> playerViewState.emit(state)
                    is PlayerViewState.PlayBtn -> playerViewState.emit(state)
                }
            }
        }
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

    fun showNotification(title: String, artist: String) {
        mediaServiceController?.showNotification(title, artist)
    }

    fun hideNotification() {
        mediaServiceController?.hideNotification()
    }

    fun preparePlayer(url: String) {
        mediaServiceController?.prepareMediaPlayer(url)
    }

    fun stopTrack() {
        mediaServiceController?.pause()
    }

    fun onClickedBtnPlay() {
        when (playerInteractor.getState()) {
            MediaPlayerState.PREPARED -> onTrackStart()
            MediaPlayerState.PLAYING -> stopTrack()
            MediaPlayerState.PAUSED -> onTrackStart()
            MediaPlayerState.DEFAULT -> playerViewState.value = PlayerViewState.PlayBtn(PlayButtonState.PREPARED)
        }
    }

    private fun onTrackStart() {
        mediaServiceController?.play()
    }
}