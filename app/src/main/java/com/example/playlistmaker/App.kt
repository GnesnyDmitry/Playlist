package com.example.playlistmaker

import android.app.Application
import android.content.Context
import com.example.playlistmaker.di.createPlaylistModule
import com.example.playlistmaker.di.dataBaseModule
import com.example.playlistmaker.di.mediaModule
import com.example.playlistmaker.di.playerModule
import com.example.playlistmaker.di.searchModule
import com.example.playlistmaker.di.settingModule
import com.example.playlistmaker.setting.data.ThemeSwitcherImpl
import com.example.playlistmaker.setting.domain.api.SettingsInteractor
import com.google.gson.Gson
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {

    internal val gson = Gson()
    internal lateinit var themeSwitcher: ThemeSwitcherImpl


    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(searchModule, playerModule, settingModule, mediaModule, dataBaseModule, createPlaylistModule)
        }

        instance = this

        themeSwitcher = ThemeSwitcherImpl(getSharedPreferences("app_preference", MODE_PRIVATE))
    }

    companion object {
        lateinit var instance: App
    }
}