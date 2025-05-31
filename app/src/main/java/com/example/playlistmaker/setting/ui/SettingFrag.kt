package com.example.playlistmaker.setting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.playlistmaker.theme.PlaylistMakerTheme
import com.example.playlistmaker.setting.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingFrag : Fragment() {

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                PlaylistMakerTheme { SettingScreen(viewModel)  }
            }
        }
    }
}