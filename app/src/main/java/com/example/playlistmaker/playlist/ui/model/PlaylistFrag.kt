package com.example.playlistmaker.playlist.ui.model

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.adapters.PlaylistBottomSheetAdapter
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.playlist.presentation.PlaylistViewModel
import com.example.playlistmaker.tools.TRACK_KEY
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.Locale

class PlaylistFrag: Fragment(R.layout.fragment_playlist) {

    private val viewModel by viewModel<PlaylistViewModel> { parametersOf(arguments?.getLong(PLAYLIST_KEY))}
    private lateinit var binding: FragmentPlaylistBinding
    private val trackAdapter by lazy { PlaylistBottomSheetAdapter() }
    private var trackBottomSheet: BottomSheetBehavior<ConstraintLayout>? = null
    private var dotsBottomSheet: BottomSheetBehavior<ConstraintLayout>? = null
    private var trackDialog: AlertDialog? = null
    private var playlistTotalTime: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        trackAdapter.action = null
        trackAdapter.longClick = null
        trackBottomSheet = null
        dotsBottomSheet = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylist()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.uiStateflow.collect { state ->
                when (state) {
                    is PlaylistState.Default -> {  }
                    is PlaylistState.EmptyShare -> { showSnack(false) }
                    is PlaylistState.Share -> { showShareApps(state.playlist, state.tracks) }
                    is PlaylistState.Dots -> { showDotsBottomSheet(state.playlist) }
                    is PlaylistState.EmptyBottomSheet -> {
                        showSnack(true)
                        drawScreen(state.playlist)
                    }
                    is PlaylistState.SuccessBottomSheet -> {
                        playlistTotalTime = state.totalTime
                        drawScreenWithBottomSheet(state.playlist, state.tracks)
                    }
                }
            }
        }

        trackBottomSheet = BottomSheetBehavior.from(binding.tracksBottomSheet.root)
        dotsBottomSheet = BottomSheetBehavior.from(binding.dotsBottomsSheet.root)

        trackAdapter.action = { track ->
            findNavController().navigate(
                resId = R.id.action_playlistFrag_to_playerActivity,
                args = bundleOf(TRACK_KEY to track)
            )
        }

        trackAdapter.longClick = { prepareTrackDialog(it) }

        binding.back.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.share.setOnClickListener { viewModel.onClickShare() }
        binding.dots.setOnClickListener { viewModel.onClickDots() }
    }

    private fun showShareApps(playlist: Playlist, tracks: List<Track>) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, playlistToString(playlist, tracks))
            type = "text/plain"
        }
        val chooserIntent = Intent.createChooser(shareIntent, "Share APK")
        activity?.startActivity(chooserIntent)
    }

    private fun playlistToString(playlist: Playlist, tracks: List<Track>): String {
        val stringB = StringBuilder()
        stringB.append("${playlist.name}\n")
        stringB.append("${playlist.description}\n")
        stringB.append(resources.getQuantityString(R.plurals.track_count, playlist.trackCount, playlist.trackCount) + "\n\n")
        tracks.forEachIndexed { index, track ->
            stringB.append("${index + 1}. ${track.artistName} - ${track.trackName} (${toMinute(track.trackTimeMillis)})\n")
        }

        return stringB.toString()
    }

    private fun showDotsBottomSheet(playlist: Playlist) {

        with(binding.dotsBottomsSheet) {
            item.artistName.text = playlist.name
            item.timeTrack.text = toMinute(playlistTotalTime)
            share.setOnClickListener { viewModel.onClickShare() }
            remove.setOnClickListener { preparePlaylistDialog() }
            change.setOnClickListener {
                findNavController().navigate(
                    resId = R.id.action_playlistFrag_to_editPlaylistFrag,
                    args = bundleOf(TRACK_KEY to playlist)
                )
            }
            Glide.with(requireActivity())
                .load(playlist.uri)
                .placeholder(R.drawable.placeholder)
                .into(item.pictureTrack)
        }
        dotsBottomSheet?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun preparePlaylistDialog() {
        trackDialog= AlertDialog.Builder(requireContext())
            .setTitle("Хотите удалить плейлист?")
            .setNegativeButton("Нет") { _, _ -> trackDialog?.dismiss() }
            .setPositiveButton("да") { _, _ ->  removePlaylist() }
            .create()
        trackDialog?.setCancelable(false)
        trackDialog?.setCanceledOnTouchOutside(false)
        trackDialog?.show()
    }

    private fun removePlaylist() {
        viewModel.deletePlaylist()
        findNavController().navigateUp()
    }

    private fun prepareTrackDialog(trackId: Int) {
        trackDialog = AlertDialog.Builder(requireContext())
            .setTitle("Хотите удалить трек?")
            .setNegativeButton("Нет") { _, _ -> trackDialog?.dismiss() }
            .setPositiveButton("Да") { _, _ -> viewModel.deleteTrack(trackId) }
            .create()
        trackDialog?.setCanceledOnTouchOutside(false)
        trackDialog?.setCancelable(false)
        trackDialog?.show()
    }
    private fun showSnack(bottomSheetIsEmpty: Boolean) {
        val message =
            if (bottomSheetIsEmpty) "В плейлисте нет треков"
            else "В этом плейлисте нет списка треков, которым можно поделиться"
        Snackbar
            .make(requireContext(), binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.blue))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .show()
    }

    private fun drawScreenWithBottomSheet(playlist: Playlist, tracks: List<Track>) {
        drawScreen(playlist)
        trackAdapter.items.clear()
        trackAdapter.items = tracks.toMutableList().asReversed()
        binding.tracksBottomSheet.recycler.adapter = trackAdapter
        trackBottomSheet?.isHideable = false
        trackBottomSheet?.state = BottomSheetBehavior.STATE_EXPANDED
        dotsBottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun drawScreen(playlist: Playlist) {
        Glide.with(requireActivity())
            .load(playlist.uri)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .into(binding.image)
        with(binding) {
            name.text = playlist.name
            year.text = playlist.description
            time.text = toMinute(playlistTotalTime)
            trackCount.text = getTrackCount(playlist.trackCount)
        }
        if (playlist.trackList.isEmpty()) {
            trackBottomSheet?.isHideable = true
            trackBottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
        }
        dotsBottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun getTrackCount(count: Int): String {
        return resources.getQuantityString(R.plurals.track_count, count, count)
    }

    private fun toMinute(totalTime: Int?): String {
        val timeMinute = totalTime?.div(1000)?.div(60) ?: 0
        val config = Configuration(requireContext().resources.configuration)
        config.setLocale((Locale("ru")))
        val localizedContext = requireContext().createConfigurationContext(config)
        return localizedContext.resources.getQuantityString(R.plurals.minutes_count, timeMinute, timeMinute)
    }

    companion object { const val PLAYLIST_KEY = "playlist_key" }
}