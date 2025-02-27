package com.example.playlistmaker.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.search.domain.ResponseState.Content
import com.example.playlistmaker.search.domain.ResponseState.Error
import com.example.playlistmaker.search.domain.ResponseState.NoConnect
import com.example.playlistmaker.search.domain.ResponseState.NoData
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.ui.model.ClearBtnState
import com.example.playlistmaker.search.ui.model.SearchViewState
import com.example.playlistmaker.tools.DELAY1000L
import com.example.playlistmaker.tools.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel(private val searchInteractor: TrackInteractor) : ViewModel() {

    private val trackList = mutableListOf<Track>()
    private var latestText: String = ""

    private val searchViewState = MutableLiveData<SearchViewState>()

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

    fun searchViewStateLiveData(): LiveData<SearchViewState> = searchViewState

    init {
        trackList.addAll(searchInteractor.getTracksFromLocalStorage(HISTORY_KEY))
    }

    fun onClickedTrack(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            searchInteractor.saveTrackToLocalStorage(HISTORY_KEY, trackList, track)
        }
    }

    fun onClickEditText(query: String) {
        if (query.isEmpty()) showHistoryContent()
    }

    fun onClickClearEditText() {
        searchViewState.value = SearchViewState.ClearBtn(ClearBtnState.DEFAULT)
        latestText = ""
        showHistoryContent()
    }

    fun onClickedBtnClearHistory() {
        searchViewState.postValue(SearchViewState.Default)
        searchInteractor.clearTrackLocalStorage(HISTORY_KEY)
    }

    fun onTextChange(text: String) {
        defineState(text)
        if (text.length < 2) {
            debounceSearchTracks(null)
            showHistoryContent()
        } else searchDebounce(text = text)
    }

    fun onClickedRefresh(text: String) {
        searchViewState.value = SearchViewState.Loading
        searchTracks(text)
    }

    private fun showHistoryContent() {
        searchViewState.value = SearchViewState.Loading
        viewModelScope.launch {
            searchViewState.postValue(
                SearchViewState.HistoryContent(
                    searchInteractor.getTracksFromLocalStorage(HISTORY_KEY)
                )
            )
        }
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
                    is NoConnect -> searchViewState.postValue(SearchViewState.Error)
                    is Error -> searchViewState.postValue(SearchViewState.Error)
                    is NoData -> searchViewState.postValue(SearchViewState.NoData(responseState.data))
                    is Content -> searchViewState.postValue(
                        SearchViewState.SearchContent(
                            responseState.data
                        )
                    )
                }
            }
        }
    }

    private fun defineState(text: String) {
        if (text.isNotEmpty()) {
            searchViewState.value = SearchViewState.ClearBtn(ClearBtnState.TEXT)
        }
    }

    companion object {
        private const val HISTORY_KEY = "history_tracks"
    }
}