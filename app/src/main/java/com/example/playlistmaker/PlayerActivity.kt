package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.model.Track
import com.example.playlistmaker.tools.getTimeFormat
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity(), PlayerView {

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var btnPlay: ImageView
    private var playButtonManager: PlayButtonManager? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        btnPlay = binding.btnPlay

        val track =
            requireNotNull(intent.getParcelableExtra(SearchActivity.TRACK_KEY, Track::class.java))

        playButtonManager = PlayButtonManager(this)
        playButtonManager?.sendTrackForManager(track)

        track.let {
            binding.apply {
                Glide.with(root)
                    .load(it.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                    .placeholder(R.drawable.player_placeholder)
                    .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.size_8dp)))
                    .into(trackPicture)

                trackName.text = it.trackName
                artistName.text = it.artistName
                currentTime.text = (it.trackTimeMillis.getTimeFormat())
                txtDurationRight.text = (it.trackTimeMillis.getTimeFormat())
                txtYearRight.text = it.releaseDate.substring(0, 4)
                txtAlbumRight.text = it.collectionName.takeIf { it.isNotEmpty() } ?: ""
                txtStyleRight.text = it.primaryGenreName
                txtCountryRight.text = it.country
            }
        }

        btnPlay.setOnClickListener {
            playButtonManager?.onClickedBtn()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playButtonManager?.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        playButtonManager?.onPause()
    }

    override fun changeImageForPlayButton(image: Int) {
        btnPlay.setImageResource(image)
    }

    override fun updateTrackTimer(time: Int) {
        binding.currentTime.text = time.getTimeFormat()
    }
}