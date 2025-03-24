package com.example.playlistmaker.media.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.TrackAdapter
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.media.presentation.FavoriteTracksViewModel
import com.example.playlistmaker.search.ui.SearchRouter
import com.example.playlistmaker.tools.Debouncer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

    private val viewModel by viewModel<FavoriteTracksViewModel>()
    private lateinit var binding: FragmentFavoriteTracksBinding
    private val trackAdapter by lazy { TrackAdapter() }
    private val router by lazy { SearchRouter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        trackAdapter.action = null
        println("qqq destroy FavoriteFrag")

    }

    override fun onPause() {
        super.onPause()
        println("qqq pause FavoriteFrag")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("qqq create FavoriteFrag")
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackAdapter.action = ::openPlayerActivity

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = trackAdapter
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.trackFlow.collectLatest { list ->
                    if (list.isEmpty()) {
                        showEmptyList()
                    } else {
                        showFavoriteTracks(list)
                    }
                }
            }
        }

    }

    private fun openPlayerActivity(track: Track) {
        router.openPlayerActivity(track)
        println("qqq openplayer")
    }
    private fun showEmptyList() {
        binding.recyclerView.isVisible = false
        binding.placeholderNothingFound.isVisible = true
    }

    private fun showFavoriteTracks(list: List<Track>) {
        binding.placeholderNothingFound.isVisible = false
        binding.recyclerView.isVisible = true
        trackAdapter.items.clear()
        trackAdapter.items.addAll(list)
        trackAdapter.notifyDataSetChanged()
    }
    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }
}