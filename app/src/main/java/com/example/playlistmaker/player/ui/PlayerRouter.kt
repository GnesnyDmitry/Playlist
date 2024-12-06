package com.example.playlistmaker.player.ui

import androidx.appcompat.app.AppCompatActivity

class PlayerRouter(private val view: AppCompatActivity) {
    fun goBack() { view.finish() }
}