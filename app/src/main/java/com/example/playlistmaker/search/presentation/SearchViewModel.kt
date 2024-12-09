package com.example.playlistmaker.search.presentation

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.search.domain.ResponseState.Content
import com.example.playlistmaker.search.domain.ResponseState.Error
import com.example.playlistmaker.search.domain.ResponseState.NoConnect
import com.example.playlistmaker.search.domain.ResponseState.NoData
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.ui.model.ClearBtnState
import com.example.playlistmaker.search.ui.model.TrackListState
import com.example.playlistmaker.tools.DELAY300L
import com.example.playlistmaker.tools.SingleLiveEvent
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SearchViewModel(private val searchInteractor: TrackInteractor) : ViewModel() {

    private val trackList = mutableListOf<Track>()
    private val handler = Handler(Looper.getMainLooper())
    private var latestText: String = ""
    private val executor = Executors.newFixedThreadPool(4)

    private val trackListState = MutableLiveData<TrackListState>()
    private val clearBtnState = SingleLiveEvent<ClearBtnState>()

    fun trackListStateLiveData(): LiveData<TrackListState> = trackListState
    fun clearButtonStateLiveData(): LiveData<ClearBtnState> = clearBtnState


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
        clearBtnState.value = ClearBtnState.DEFAULT
        latestText = ""
        showHistoryContent()
    }

    fun onClickedBtnClearHistory() {
        trackListState.postValue(TrackListState.Default)
        searchInteractor.clearTrackLocalStorage(HISTORY_KEY)
    }

    fun onTextChange(text: String) {
        defineState(text)
        if (text.isEmpty())
            showHistoryContent()
        else
            searchDebounce(text)
    }

    fun onClickedRefresh(text: String) {
        trackListState.value = TrackListState.Loading
        getSearch(text)
    }

    fun onStop(text: String) {
        if (text.isEmpty()) showHistoryContent()
    }

    private fun showHistoryContent() {
        trackListState.value = TrackListState.Loading
        executor.execute {
            trackListState.postValue(
                TrackListState.HistoryContent(
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
        val postTime = SystemClock.uptimeMillis() + DELAY300L
        handler.postAtTime(runnable, SEARCH_REQUEST_TOKEN, postTime)
    }

    private fun getSearch(query: String) {
        executor.execute {
            searchInteractor.searchTracks(query) { response ->
                when (response) {
                    is NoConnect -> trackListState.postValue(TrackListState.Error)
                    is Error -> trackListState.postValue(TrackListState.Error)
                    is NoData -> trackListState.postValue(TrackListState.NoData(response.data))
                    is Content -> trackListState.postValue(TrackListState.SearchContent(response.data))
                }
            }
        }

        trackListState.value = TrackListState.Loading
        println("qqq ${Thread.currentThread().name}")

    }

    private fun defineState(text: String) {
        if (text.isNotEmpty()) {
            clearBtnState.value = ClearBtnState.TEXT
        }
    }

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
        private const val HISTORY_KEY = "history_tracks"
        fun factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(searchInteractor = Creator.provideTracksInteractor())
                }
            }
    }
}