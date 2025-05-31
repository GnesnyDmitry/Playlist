package com.example.playlistmaker.search.presentation

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.ResponseState.Content
import com.example.playlistmaker.search.domain.ResponseState.Error
import com.example.playlistmaker.search.domain.ResponseState.NoConnect
import com.example.playlistmaker.search.domain.ResponseState.NoData
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.ui.SearchScreenUiStateMapper
import com.example.playlistmaker.search.ui.model.SearchViewState
import com.example.playlistmaker.tools.DELAY1000L
import com.example.playlistmaker.tools.TRACK_KEY
import com.example.playlistmaker.tools.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: TrackInteractor,
    val searchScreenUiStateMapper: SearchScreenUiStateMapper
) : ViewModel() {

    private val trackListHistory = mutableListOf<Track>()
    private var latestText: String = ""

    private val searchViewState = MutableStateFlow<SearchViewState>(SearchViewState.Default)
    val searchViewStateFlow: StateFlow<SearchViewState> = searchViewState

    private var debounceSearchTracks = debounce(
        delayMillis = DELAY1000L,
        coroutineScope = viewModelScope,
        deferredUsing = true,
        action = ::searchIfNeed
    )

    private fun searchIfNeed(text: String?) {
        if (text != null) {
            searchTracks(text)
        }
    }

    init {
        trackListHistory.addAll(searchInteractor.getTracksFromLocalStorage(HISTORY_KEY))
    }

    private fun openPlayerActivity(context: Context, track: Track) {
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra(TRACK_KEY, track)
        context.startActivity(intent)
    }


    fun onClickedTrack(track: Track, context: Context) {
        openPlayerActivity(context, track)
        viewModelScope.launch(Dispatchers.IO) {
            searchInteractor.saveTrackToLocalStorage(HISTORY_KEY, trackListHistory, track)
        }
    }

    fun onClickedBtnClearHistory() {
        searchViewState.value = SearchViewState.Default
        searchInteractor.clearTrackLocalStorage(HISTORY_KEY)
    }

    fun onTextChange(text: String) {
        if (text.length < 2) {
            debounceSearchTracks(null)
            showHistoryContent()
        } else searchDebounce(text = text)
    }

     fun showHistoryContent() {
        searchViewState.value = SearchViewState.Loading
        viewModelScope.launch {
            searchViewState.value =
                SearchViewState.HistoryContent(
                    searchInteractor.getTracksFromLocalStorage(HISTORY_KEY)
                )
        }
    }

    fun hideHistory() {
        searchViewState.value = SearchViewState.Default
    }

    private fun searchDebounce(text: String) {
        if (text != latestText) {
            latestText = text
            debounceSearchTracks(text)
        }
    }

    private fun searchTracks(query: String) {

        searchViewState.value = SearchViewState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            searchInteractor.searchTracks(query).collect { responseState ->
                when (responseState) {
                    is NoConnect -> searchViewState.value = SearchViewState.Error
                    is Error -> searchViewState.value = SearchViewState.Error
                    is NoData -> searchViewState.value = SearchViewState.NoData(responseState.data)
                    is Content -> searchViewState.value = SearchViewState.SearchContent(responseState.data)
                }
            }
        }
    }

    companion object {
        private const val HISTORY_KEY = "history_tracks"
    }
}