package com.example.playlistmaker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.ui.model.MediaPlayerState
import com.example.playlistmaker.player.ui.model.PlayButtonState
import com.example.playlistmaker.player.ui.model.PlayerViewState
import com.example.playlistmaker.tools.DELAY300L
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent

class MediaService : Service(), MediaServiceController, KoinComponent {

    private val binder = LocalBinder()
    private val interactor: PlayerInteractor by inject()

    private val playerState = MutableStateFlow<PlayerViewState>(PlayerViewState.PlayBtn(PlayButtonState.PREPARED))
    override val playerStateFlow: StateFlow<PlayerViewState>
        get() = playerState.asStateFlow()

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var timerJob: Job? = null

    private val notificationId = 1
    private val notificationChannelId = "audio_channel"

    inner class LocalBinder : Binder() {
        fun getService(): MediaService = this@MediaService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun prepareMediaPlayer(uri: String) {
        playerState.value = PlayerViewState.PlayBtn(PlayButtonState.PREPARED)
        interactor.prepareMediaPlayer(uri)
        interactor.onTrackEnd {
            hideNotification()
            playerState.value = PlayerViewState.PlayBtn(PlayButtonState.PREPARED)
        }
    }

    override fun play() {
        interactor.startTrack()
        trackTimer()
        playerState.value = PlayerViewState.PlayBtn(PlayButtonState.PLAY)
    }

    override fun pause() {
        interactor.stopTrack()
        timerJob?.cancel()
        playerState.value = PlayerViewState.PlayBtn(PlayButtonState.PAUSE)
    }

    override fun stop() {
        interactor.stopMediaPlayer()
        timerJob?.cancel()
        playerState.value = PlayerViewState.PlayBtn(PlayButtonState.PREPARED)
        hideNotification()
    }

    private fun trackTimer() {
        println("qqq ${interactor.getTime()}")
        timerJob = coroutineScope.launch {
            while (interactor.getState() == MediaPlayerState.PLAYING) {
                println("qqq ${interactor.getTime()}")
                playerState.value = PlayerViewState.TrackTime(interactor.getTime())
                delay(DELAY300L)
            }
        }
    }

    private fun buildNotification(title: String, artist: String): Notification {
        val notificationBuilder = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle(title)
            .setContentText(artist)
            .setSmallIcon(R.drawable.ic_media_lib)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        return notificationBuilder.build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "Audio Playback",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun showNotification(title: String, artist: String) {
        val notification = buildNotification(title, artist)
        startForeground(notificationId, notification)
    }

    override fun hideNotification() {
        stopForeground(STOP_FOREGROUND_REMOVE)
    }
}