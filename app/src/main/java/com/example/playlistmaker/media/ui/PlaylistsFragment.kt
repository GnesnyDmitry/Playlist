package com.example.playlistmaker.media.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.PlaylistAdapter
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.media.presentation.PlaylistViewModel
import com.example.playlistmaker.media.ui.model.PlaylistViewState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment(R.layout.fragment_playlists) {

    private val viewModel by viewModel<PlaylistViewModel>()
    private lateinit var binding: FragmentPlaylistsBinding
    private val router by lazy {  }
    private val playlistAdapter by lazy { PlaylistAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = playlistAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiStateFlow.collect { state ->
                when (state) {
                    is PlaylistViewState.Empty -> { showEmptyList() }
                    is PlaylistViewState.PlaylistSuccess -> { showPlaylists(state.state) }
                }
            }
        }

        binding.btnNewPlaylist.setOnClickListener {
            requireActivity().findNavController(R.id.container_view)
            .navigate(R.id.action_mediaFrag_to_albumFrag) }
    }

    private fun showPlaylists(list: List<Playlist>) {
        binding.recyclerView.isVisible = true
        binding.placeholderNothingFound.isVisible = false
        playlistAdapter.items = list as MutableList<Playlist>
        playlistAdapter.notifyDataSetChanged()

    }

    private fun showEmptyList() {
        binding.recyclerView.isVisible = false
        binding.placeholderNothingFound.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        playlistAdapter.action = null
    }

    companion object {

        @JvmStatic
        fun newInstance() = PlaylistsFragment()
    }
}