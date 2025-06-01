package com.example.playlistmaker.search.presentation

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.ResponseState
import com.example.playlistmaker.search.domain.ResponseState.Content
import com.example.playlistmaker.search.domain.ResponseState.Error
import com.example.playlistmaker.search.domain.ResponseState.NoConnect
import com.example.playlistmaker.search.domain.ResponseState.NoData
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.ui.SearchScreenUiStateMapper
import com.example.playlistmaker.search.ui.model.SearchViewState
import com.example.playlistmaker.tools.DELAY1000L
import com.example.playlistmaker.tools.TRACK_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: TrackInteractor,
    val searchScreenUiStateMapper: SearchScreenUiStateMapper
) : ViewModel() {

    private val trackListHistory = mutableListOf<Track>()

    private val queryFlow = MutableSharedFlow<String>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val searchViewState = MutableStateFlow<SearchViewState>(SearchViewState.Default)

    @OptIn(FlowPreview::class)
    val searchViewStateFlow: Flow<SearchViewState> = queryFlow
        .debounce(DELAY1000L)
        .distinctUntilChanged()
        .map(::mapToSearchViewState)

        .flowOn(Dispatchers.IO)

    val resultFlow: Flow<SearchViewState> = merge(
        searchViewState,
        searchViewStateFlow
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SearchViewState.Default
        )

    private suspend fun mapToSearchViewState(query: String): SearchViewState {
        return if (query.isEmpty()) {
            SearchViewState.HistoryContent(
                searchInteractor.getTracksFromLocalStorage(HISTORY_KEY)
            )
        } else {
            getViewState(searchInteractor.searchTracks(query))
        }
    }

    private fun getViewState(state: ResponseState): SearchViewState {
        return when (state) {
            is NoConnect -> SearchViewState.Error
            is Error -> SearchViewState.Error
            is NoData -> SearchViewState.NoData(state.data)
            is Content -> SearchViewState.SearchContent(state.data)
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
        searchViewState.tryEmit(SearchViewState.Default)
        searchInteractor.clearTrackLocalStorage(HISTORY_KEY)
    }

    fun onTextChange(text: String) {
            queryFlow.tryEmit(text)
    }

    fun showHistoryContent() {
        println("qqq showHistory")
        searchViewState.tryEmit(SearchViewState.Loading)
        searchViewState.tryEmit(
            SearchViewState.HistoryContent(
                searchInteractor.getTracksFromLocalStorage(HISTORY_KEY)
            )
        )
    }

    fun hideHistory() {
        println("qqq hideHistory")
        searchViewState.tryEmit(SearchViewState.Default)
    }

    companion object {
        private const val HISTORY_KEY = "history_tracks"
    }
}