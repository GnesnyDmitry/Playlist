package com.example.playlistmaker.media

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMediaBinding.inflate(layoutInflater) }
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

        binding.toolbar.setNavigationOnClickListener { finish() }
    }
}