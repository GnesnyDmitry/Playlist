package com.example.playlistmaker.media.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MediaViewModel: ViewModel() {
    private val _currentTab = MutableLiveData(0)
    val currentTab: LiveData<Int> = _currentTab

    fun setCurrentTab(tab: Int) {
        _currentTab.value = tab
    }
}