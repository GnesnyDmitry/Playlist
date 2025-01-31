package com.example.playlistmaker.media.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaBinding
import com.example.playlistmaker.media.presentation.MediaViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFrag : Fragment() {

    private lateinit var binding: FragmentMediaBinding
    private val viewModel by viewModel<MediaViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MediaPagerAdapter(this)
        binding.viewPager2.adapter = adapter

        TabLayoutMediator(binding.tablayout, binding.viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.favorites_tracks)
                1 -> getString(R.string.playlists)
                else -> null
            }
        }.attach()

        viewModel.currentTab.observe(viewLifecycleOwner) { tab ->
            binding.viewPager2.currentItem = tab
        }

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setCurrentTab(position)
            }
        })
    }
}