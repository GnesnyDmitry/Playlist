package com.example.playlistmaker.player.ui

import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.ui.SearchActivity
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.player.presentation.PlayerPresenter
import com.example.playlistmaker.player.presentation.PlayerView
import com.example.playlistmaker.tools.getTimeFormat

class PlayerActivity : AppCompatActivity(), PlayerView {

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var btnPlay: ImageView
    private var presenter: PlayerPresenter? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { presenter?.onClickBtnBack() }

        presenter = Creator.createPlayerPresenter(
            view = this,
            router = PlayerRouter(this)
        )

        btnPlay = binding.btnPlay

        val track = getTrack()

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
            presenter?.onClickedBtnPlay()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.stopMediaPlayer()
    }

    override fun onPause() {
        super.onPause()
        presenter?.stopTrack()
    }

    override fun changeImageForPlayButton(image: Int) {
        btnPlay.setImageResource(image)
    }

    override fun updateTrackTimer(time: Int) {
        binding.currentTime.text = time.getTimeFormat()
    }

    override fun getTrack(): Track {
        val track: Track? = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(SearchActivity.TRACK_KEY, Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(SearchActivity.TRACK_KEY)
        }
        return requireNotNull(track)
    }
}