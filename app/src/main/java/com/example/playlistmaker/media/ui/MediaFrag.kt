package com.example.playlistmaker.media.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.example.playlistmaker.theme.PlaylistMakerTheme
import com.example.playlistmaker.media.presentation.MediaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFrag : Fragment() {

    private val viewModel by viewModel<MediaViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                PlaylistMakerTheme { MediaScreen()  }
            }
        }
    }
}