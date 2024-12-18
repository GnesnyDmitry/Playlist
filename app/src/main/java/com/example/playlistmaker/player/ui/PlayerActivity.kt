package com.example.playlistmaker.player.ui

import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.search.ui.SearchActivity
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.player.presentation.PlayerViewModel
import com.example.playlistmaker.player.ui.model.PlayButtonState
import com.example.playlistmaker.player.ui.model.PlayerViewState
import com.example.playlistmaker.tools.getTimeFormat
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAudioPlayerBinding.inflate(layoutInflater) }
    private lateinit var btnPlay: ImageView
    private val viewModel by viewModel<PlayerViewModel>()
    private val router = PlayerRouter(this)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { router.goBack() }

        btnPlay = binding.btnPlay

        val track = getTrack()

        track.let {
            track.previewUrl?.let { url -> viewModel.preparePlayer(url) }
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
            viewModel.onClickedBtnPlay()
        }

        viewModel.playBtnStateLiveData().observe(this) { state ->
            when (state) {
                is PlayerViewState.PlayBtn -> {
                    when (state.state) {
                        PlayButtonState.PREPARED -> {
                            updateTrackTimer(0)
                            changeImageForPlayButton(R.drawable.player_play)
                        }
                        PlayButtonState.PLAY -> {
                            changeImageForPlayButton(R.drawable.player_pause)
                        }
                        PlayButtonState.PAUSE -> {
                            changeImageForPlayButton(R.drawable.player_play)
                        }
                    }
                }
                is PlayerViewState.TrackTime -> {
                    updateTrackTimer(state.state)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopTrack()
    }


    private fun changeImageForPlayButton(image: Int?) {
        image?.let {
            btnPlay.setImageResource(image)
        }
    }

    private fun updateTrackTimer(time: Int) {
        binding.currentTime.text = time.getTimeFormat()
    }

    private fun getTrack(): Track {
        val track: Track? = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(SearchActivity.TRACK_KEY, Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(SearchActivity.TRACK_KEY)
        }
        return requireNotNull(track)
    }
}