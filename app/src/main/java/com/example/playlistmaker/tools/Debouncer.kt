package com.example.playlistmaker.tools

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Debouncer(
    private val coroutineScope: CoroutineScope,
    private val delay: Long = DELAY800L,
) {
    private var job: Job? = null
    private var available = true

    fun onClick(action: () -> Unit) {
        if (available) {
            available = false
            job?.cancel()
            job = coroutineScope.launch {
                action()
                delay(delay)
                available = true
            }
        }
    }
}

fun <T> debounce(
    delayMillis: Long = DELAY800L,
    coroutineScope: CoroutineScope,
    deferredUsing: Boolean = false,
    action: (T) -> Unit,
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (deferredUsing) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted != false || deferredUsing) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                println("qqq debouncer${System.currentTimeMillis()}")
                action(param)
            }
        }
    }
}