package com.example.playlistmaker.edit_playlist.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.creat_album.ui.CreatePlaylistFrag
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.edit_playlist.presentation.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
            .transform(RoundedCorners(8))
            .into(binding.addImage)
    }
}