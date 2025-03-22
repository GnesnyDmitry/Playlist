package com.example.playlistmaker.player.ui

import android.os.Build
import android.os.Bundle
import android.os.Message
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.BottomSheetPlaylistAdapter
import com.example.playlistmaker.PlaylistAdapter
import com.example.playlistmaker.R
import com.example.playlistmaker.creat_album.ui.CreatePlaylistFrag
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.player.presentation.PlayerViewModel
import com.example.playlistmaker.player.ui.model.BottomSheetState
import com.example.playlistmaker.player.ui.model.PlayButtonState
import com.example.playlistmaker.player.ui.model.PlayerViewState
import com.example.playlistmaker.search.ui.SearchFrag
import com.example.playlistmaker.tools.getTimeFormat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAudioPlayerBinding.inflate(layoutInflater) }
    private lateinit var btnPlay: ImageView
    private val viewModel by viewModel<PlayerViewModel>()
    private val router = PlayerRouter(this)
    private val playlistAdapter by lazy { BottomSheetPlaylistAdapter() }
    private val bottomSheetContainer by lazy { findViewById<ConstraintLayout>(R.id.bottom_sheet) }
    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(bottomSheetContainer) }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initBottomSheet()

        binding.toolbar.setNavigationOnClickListener { router.goBack() }

        btnPlay = binding.btnPlay

        val track = getTrack()

        playlistAdapter.action = { playlist -> viewModel.onClickedPlaylist(track, playlist) }

        track.let {
            viewModel.getFavoriteState(track.trackId)
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

        viewModel.btnLikeLiveData().observe(this) { state ->
            if (state) {
                binding.btnLike.setImageResource(R.drawable.player_like)
            } else {
                binding.btnLike.setImageResource(R.drawable.player_dislike)
            }
        }

        binding.btnLike.setOnClickListener { viewModel.onClickBtnLike(track) }

        binding.btnAdd.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            viewModel.onClickAddTrackInPlaylist()
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

        lifecycleScope.launch {
            viewModel.bottomSheetStateFlow.collect { state ->
                when (state) {
                    is BottomSheetState.Empty -> showBottomSheet(emptyList())
                    is BottomSheetState.Playlists -> showBottomSheet(state.playlists)
                    is BottomSheetState.TrackExistInPlaylist -> hideBottomSheet("Трек уже добавлен в плейлист ${state.playlistName}")
                    is BottomSheetState.AddNewTrackInPlaylist -> hideBottomSheet("Добавлено в плейлист ${state.playlistName}")
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopTrack()
    }

    private fun hideBottomSheet(message: String) {
        Snackbar
            .make(this, binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.blue))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun showBottomSheet(playlists: List<Playlist>) {
        if (playlists.isNotEmpty()) {
            binding.bottomSheet.newAlbum.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                supportFragmentManager.beginTransaction()
                    .replace(R.id.player_containerView, CreatePlaylistFrag.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
            playlistAdapter.items = playlists
            binding.bottomSheet.recycler.adapter = playlistAdapter
            playlistAdapter.notifyDataSetChanged()
        }
    }

    private fun initBottomSheet() {
        bottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.coordinator.setBackgroundColor(
                                ContextCompat.getColor(this@PlayerActivity, R.color.transparent)
                            )
                        }

                        else -> {
                            binding.coordinator.setBackgroundColor(
                                ContextCompat.getColor(this@PlayerActivity, R.color.background)
                            )
                        }
                    }
                }
            })
        }
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
        val track: Track? =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(SearchFrag.TRACK_KEY, Track::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(SearchFrag.TRACK_KEY)
            }
        return requireNotNull(track)
    }
}