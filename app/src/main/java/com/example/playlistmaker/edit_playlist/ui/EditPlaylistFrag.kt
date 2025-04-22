package com.example.playlistmaker.edit_playlist.ui

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creat_album.ui.CreatePlaylistFrag
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.edit_playlist.presentation.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFrag : CreatePlaylistFrag() {

    override val viewModel by viewModel<EditPlaylistViewModel>()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drawScreen(playlist!!)
    }
    
    private fun drawScreen(playlist: Playlist) {
        with(binding) {
            name.setText(playlist.name)
            description.setText(playlist.description)
            createPlaylist.text = "Сохранить"
            toolbar.title = "Редактировать"
        }

        Glide.with(this)
            .load(playlist.uri)
            .placeholder(R.drawable.placeholder_for_edit_frag)
            .transform(RoundedCorners(8))
            .into(binding.addImage)
    }
}