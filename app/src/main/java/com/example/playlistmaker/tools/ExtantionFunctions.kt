package com.example.playlistmaker.tools

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

fun View.debounceClickListener(debouncer: Debouncer, listener: () -> Unit) {
    setOnClickListener { debouncer.onClick(listener) }
}
fun Int.getTimeFormat(): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)
}

fun <T : Parcelable?> Bundle.getParcelableFromBundle(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        getParcelable(key, clazz)
    else {
        @Suppress("DEPRECATION")
        getParcelable<T>(key)
    }
}

