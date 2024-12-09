package com.example.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.search.presentation.SearchViewModel
import com.example.playlistmaker.search.ui.model.ClearBtnState
import com.example.playlistmaker.search.ui.model.TrackListState

class SearchActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            SearchViewModel.factory()
        )[SearchViewModel::class.java]
    }
    private val trackAdapter = TrackAdapter()
    private val router = SearchRouter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = trackAdapter
        }


        trackAdapter.action = { track ->
            router.openPlayerActivity(track)
            viewModel.onClickedTrack(track)
        }

        binding.searchRootToolbar.setNavigationOnClickListener { finish() }


        binding.edittextSearchRoot.doOnTextChanged { text, _, _, _ ->
            viewModel.onTextChange("$text")
        }

        binding.edittextSearchRoot.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) viewModel.onClickEditText("${binding.edittextSearchRoot.text}")
        }

        binding.btnClearHistory.setOnClickListener {
            viewModel.onClickedBtnClearHistory()
        }

        binding.btnUpdate.setOnClickListener {
            viewModel.onClickedRefresh("${binding.edittextSearchRoot.text}")
        }

        binding.iconClearText.setOnClickListener {
            viewModel.onClickClearEditText()
        }

        viewModel.clearButtonStateLiveData().observe(this) { state ->
            when (state!!) {
                ClearBtnState.FOCUS -> showKeyboard(true)
                ClearBtnState.TEXT -> clearBtnIsVisible(true)
                ClearBtnState.DEFAULT -> {
                    showKeyboard(false)
                    clearBtnIsVisible(false)
                }
            }
        }

        viewModel.trackListStateLiveData().observe(this) { state ->
            when (state) {
                is TrackListState.Default -> showEmptyList()
                is TrackListState.Loading -> showLoadingState()
                is TrackListState.HistoryContent -> {
                    if (state.list.isEmpty()) showEmptyList()
                    else showHistoryTracks(list = state.list)
                }
                is TrackListState.SearchContent -> {
                    showNewTracks(list = state.list)
                }
                is TrackListState.NoData -> showNoDataState()
                is TrackListState.Error -> showErrorState()
            }
        }

    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop("${binding.edittextSearchRoot.text}")
    }

    override fun onDestroy() {
        super.onDestroy()
        trackAdapter.action = null
    }

    private fun showNewTracks(list: List<Track>) {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = true
        binding.placeholderNothingFound.isVisible = false
        binding.placeholderNoConnection.isVisible = false
        setItems(list)
    }

    private fun setItems(items: List<Track>) {
        trackAdapter.trackList.clear()
        trackAdapter.trackList.addAll(items)
        trackAdapter.notifyDataSetChanged()
    }

    private fun showNoDataState() {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = true
        trackAdapter.trackList.clear()
        binding.placeholderNothingFound.isVisible = true
        binding.btnUpdate.isVisible = true
    }

    private fun showErrorState() {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = true
        trackAdapter.trackList.clear()
        binding.placeholderNoConnection.isVisible = true
        binding.btnUpdate.isVisible = false
    }


    private fun showEmptyList() {
        binding.progressBar.isVisible = false
        binding.placeholderNoConnection.isVisible = false
        binding.placeholderNothingFound.isVisible = false
        binding.recyclerView.isVisible = false
        binding.headerSearchRoot.isVisible = false
        binding.btnClearHistory.isVisible = false
        trackAdapter.trackList.clear()
    }

    private fun showLoadingState() {
        showEmptyList()
        binding.progressBar.isVisible = true
    }

    private fun showHistoryTracks(list: List<Track>) {
        trackAdapter.trackList.clear()
        binding.progressBar.isVisible = false
        trackAdapter.trackList.addAll(list)
        trackAdapter.notifyDataSetChanged()
        binding.recyclerView.isVisible = true
        binding.headerSearchRoot.isVisible = true
        binding.btnClearHistory.isVisible = true

    }

    private fun showKeyboard(show: Boolean) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (show)
            imm.showSoftInput(binding.edittextSearchRoot, 0)
        else
            imm.hideSoftInputFromWindow(binding.edittextSearchRoot.windowToken, 0)
    }

    private fun clearBtnIsVisible(show: Boolean) {
        binding.iconClearText.isVisible =
            if (show) true
            else {
                binding.edittextSearchRoot.setText("")
                false
            }
    }

    companion object {
        const val TRACK_KEY = "track"
    }
}