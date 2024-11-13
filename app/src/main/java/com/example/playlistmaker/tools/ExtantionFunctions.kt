package com.example.playlistmaker.tools

import android.view.View
import java.text.SimpleDateFormat
import java.util.Locale

fun View.debounceClickListener(debouncer: Debouncer, listener: () -> Unit) {
    setOnClickListener { debouncer.onClick(listener) }
}
fun Int.getTimeFormat(): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)
}