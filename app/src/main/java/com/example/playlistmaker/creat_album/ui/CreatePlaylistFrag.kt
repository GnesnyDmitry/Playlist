package com.example.playlistmaker.creat_album.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creat_album.presentation.CreatePlaylistViewModel
import com.example.playlistmaker.creat_album.ui.model.CreatePlaylistViewState
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.main.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFrag : Fragment(R.layout.fragment_create_playlist) {

    private lateinit var binding: FragmentCreatePlaylistBinding
    private val viewModel by viewModel<CreatePlaylistViewModel>()
    private var dialogGoBack: AlertDialog? = null

    val pickMediaForPlaylist =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                Glide.with(this)
                    .load(it)
                    .transform(RoundedCorners(8))
                    .into(binding.addImage)
                viewModel.doOnPhotoChange(uri.toString())
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareDialogGoBack()

        binding.toolbar.setNavigationOnClickListener {
            viewModel.onGoBack()
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.uiStateFlow.collect { state ->
                when (state) {
                    is CreatePlaylistViewState.SavePlaylist -> { savePlaylist(state.name) }
                    is CreatePlaylistViewState.Default -> {  }
                    is CreatePlaylistViewState.GoBack -> { goBack() }
                    is CreatePlaylistViewState.ShowDialog -> { dialogGoBack?.show() }
                }
            }
        }

        binding.createPlaylist.setOnClickListener {
            viewModel.createPlaylist()
        }

        binding.addImage.setOnClickListener {
            pickMediaForPlaylist.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        binding.name.doOnTextChanged { text, _, _, _ ->
            binding.createPlaylist.isEnabled = !text.isNullOrBlank()
            viewModel.doOnNameTextChange(text.toString())
        }

        binding.description.doOnTextChanged { text, _, _, _ ->
            viewModel.doOnDescriptionTextChange(text.toString())
        }
    }

    private fun savePlaylist(playlist: String) {
        Snackbar
            .make(requireContext(), binding.root, getString(R.string.playlist_created, playlist), Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.blue))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .show()
        goBack()
    }

    private fun goBack() {
        if (requireActivity() is MainActivity) {
            findNavController().navigateUp()
        } else {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun prepareDialogGoBack() {
        dialogGoBack = AlertDialog.Builder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потярены")
            .setNegativeButton("Отмена") { _, _, -> viewModel.hideDialogGoBack() }
            .setPositiveButton("Завершить") { _,_ -> findNavController().navigateUp() }
            .create()
        dialogGoBack?.setCanceledOnTouchOutside(false)
        dialogGoBack?.setCancelable(false)
    }

    companion object { fun newInstance() = CreatePlaylistFrag() }
}