package com.example.playlistmaker.media.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.media.presentation.MediaViewModel
import com.example.playlistmaker.player.presentation.PlayerViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMediaBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<MediaViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter = MediaPagerAdapter(this)
        binding.viewPager2.adapter = adapter

        TabLayoutMediator(binding.tablayout, binding.viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.favorites_tracks)
                1 -> getString(R.string.playlists)
                else -> null
            }
        }.attach()

        viewModel.currentTab.observe(this) { tab ->
            binding.viewPager2.currentItem = tab
        }

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setCurrentTab(position)
            }
        })

        binding.toolbar.setNavigationOnClickListener { finish() }
    }
}