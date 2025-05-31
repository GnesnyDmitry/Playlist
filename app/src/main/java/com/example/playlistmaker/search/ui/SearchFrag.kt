package com.example.playlistmaker.search.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.example.playlistmaker.theme.PlaylistMakerTheme
import com.example.playlistmaker.search.presentation.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFrag : Fragment() {

    private val viewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                PlaylistMakerTheme { SearchScreen(viewModel)  }
            }
        }
    }

    companion object {
        const val TRACK_KEY = "track"
    }

}