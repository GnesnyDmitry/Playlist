package com.example.playlistmaker.search.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.tools.TRACK_KEY

class SearchRouter(private val activity: AppCompatActivity) {

    fun openPlayerActivity(track: Track) {
        val intent = Intent(activity, PlayerActivity::class.java)
        intent.putExtra(TRACK_KEY, track)
        activity.startActivity(intent)
    }
}