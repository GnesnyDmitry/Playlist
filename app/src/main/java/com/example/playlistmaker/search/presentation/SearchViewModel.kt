package com.example.playlistmaker.search.presentation

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.search.domain.ResponseState.Content
import com.example.playlistmaker.search.domain.ResponseState.Error
import com.example.playlistmaker.search.domain.ResponseState.NoConnect
import com.example.playlistmaker.search.domain.ResponseState.NoData
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.ui.model.ClearBtnState
import com.example.playlistmaker.search.ui.model.SearchViewState
import com.example.playlistmaker.tools.DELAY800L
import java.util.concurrent.Executors

class SearchViewModel(private val searchInteractor: TrackInteractor) : ViewModel() {

    private val trackList = mutableListOf<Track>()
    private val handler = Handler(Looper.getMainLooper())
    private var latestText: String = ""
    private val executor = Executors.newFixedThreadPool(4)

    private val searchViewState = MutableLiveData<SearchViewState>()

    fun searchViewStateLiveData(): LiveData<SearchViewState> = searchViewState

    init {
        trackList.addAll(searchInteractor.getTracksFromLocalStorage(HISTORY_KEY))
    }

    fun onClickedTrack(track: Track) {
        searchInteractor.saveTrackToLocalStorage(HISTORY_KEY, trackList, track)
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
        if (text.isEmpty()) {
            handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

            showHistoryContent()
        } else searchDebounce(text)
    }

    fun onClickedRefresh(text: String) {
        searchViewState.value = SearchViewState.Loading
        getSearch(text)
    }

    fun onStop(text: String) {
        if (text.isEmpty()) showHistoryContent()
    }

    private fun showHistoryContent() {
        searchViewState.value = SearchViewState.Loading
        executor.execute {
            searchViewState.postValue(
                SearchViewState.HistoryContent(
                    searchInteractor.getTracksFromLocalStorage(HISTORY_KEY)
                )
            )
        }
    }

    private fun searchDebounce(text: String) {
        if (latestText == text) return

        latestText = text
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val runnable = Runnable { getSearch(query = text) }
        val postTime = SystemClock.uptimeMillis() + DELAY800L
        handler.postAtTime(runnable, SEARCH_REQUEST_TOKEN, postTime)
    }

    private fun getSearch(query: String) {
        executor.execute {
            searchInteractor.searchTracks(query) { response ->
                println("qqq ${response}")
                when (response) {
                    is NoConnect -> searchViewState.postValue(SearchViewState.Error)
                    is Error -> searchViewState.postValue(SearchViewState.Error)
                    is NoData -> searchViewState.postValue(SearchViewState.NoData(response.data))
                    is Content -> searchViewState.postValue(SearchViewState.SearchContent(response.data))
                }
            }
        }
        searchViewState.value = SearchViewState.Loading
    }

    private fun defineState(text: String) {
        if (text.isNotEmpty()) {
            searchViewState.value = SearchViewState.ClearBtn(ClearBtnState.TEXT)
        }
    }

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
        private const val HISTORY_KEY = "history_tracks"
    }
}