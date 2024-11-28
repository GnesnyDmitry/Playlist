package com.example.playlistmaker.tools

import android.os.Handler
import android.os.Looper

class Debouncer(
    private val handler: Handler = Handler(Looper.getMainLooper())
) {
    private val DELAY_1000 = 1000L
    private var isClickAllowed = true

    fun onClick(listener: () -> Unit) {
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, DELAY_1000)
            listener.invoke()
        } else return
    }
}