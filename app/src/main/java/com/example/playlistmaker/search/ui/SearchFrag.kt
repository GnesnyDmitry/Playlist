package com.example.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.TrackAdapter
import com.example.playlistmaker.search.presentation.SearchViewModel
import com.example.playlistmaker.search.ui.model.ClearBtnState
import com.example.playlistmaker.search.ui.model.SearchViewState
import com.example.playlistmaker.tools.Debouncer
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFrag : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModel<SearchViewModel>()
    private val  trackAdapter by lazy { TrackAdapter() }
    private val router by lazy { SearchRouter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = trackAdapter
        }


        trackAdapter.action = { track ->
            router.openPlayerActivity(track)
            viewModel.onClickedTrack(track)
        }

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

        viewModel.tracksSearch.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) showNewTracks(list)
        }

        viewModel.searchViewStateLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchViewState.Default -> {
                    showEmptyList()
                    println("qqq $state default")
                }

                is SearchViewState.Loading -> {
                    showLoadingState()
                    println("qqq $state default")
                }

                is SearchViewState.HistoryContent -> {
                    if (state.list.isEmpty()) showEmptyList()
                    else showHistoryTracks(list = state.list)
                    println("qqq $state history")
                }

                is SearchViewState.SearchContent -> {
                    viewModel.updateTrackList(state.list)
                    println("qqq $state search content")
                    showNewTracks(list = state.list)
                }

                is SearchViewState.NoData -> showNoDataState()
                is SearchViewState.Error -> showErrorState()
                is SearchViewState.ClearBtn -> updateClearBtState(state.state)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        trackAdapter.action = null
    }


    private fun updateClearBtState(state: Enum<ClearBtnState>) {
        if (state == ClearBtnState.TEXT) {
            clearBtnIsVisible(true)
        } else {
            showKeyboard(false)
            clearBtnIsVisible(false)
        }
    }

    private fun showNewTracks(list: List<Track>) {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = true
        binding.placeholderNothingFound.isVisible = false
        binding.placeholderNoConnection.isVisible = false
        setItems(list)
    }

    private fun setItems(items: List<Track>) {
        trackAdapter.items.clear()
        trackAdapter.items.addAll(items)
        trackAdapter.notifyDataSetChanged()
    }

    private fun showNoDataState() {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = true
        trackAdapter.items.clear()
        binding.placeholderNothingFound.isVisible = true
        binding.btnUpdate.isVisible = true
    }

    private fun showErrorState() {
        binding.progressBar.isVisible = false
        binding.recyclerView.isVisible = true
        trackAdapter.items.clear()
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
        trackAdapter.items.clear()
    }

    private fun showLoadingState() {
        showEmptyList()
        binding.progressBar.isVisible = true
    }

    private fun showHistoryTracks(list: List<Track>) {
        trackAdapter.items.clear()
        binding.progressBar.isVisible = false
        trackAdapter.items.addAll(list)
        trackAdapter.notifyDataSetChanged()
        binding.recyclerView.isVisible = true
        binding.headerSearchRoot.isVisible = true
        binding.btnClearHistory.isVisible = true

    }

    private fun showKeyboard(show: Boolean) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (show) {
            imm.showSoftInput(binding.edittextSearchRoot, InputMethodManager.SHOW_IMPLICIT)
        } else {
            imm.hideSoftInputFromWindow(binding.edittextSearchRoot.windowToken, 0)
        }
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