package com.example.playlistmaker.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.media.MediaActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.search.ui.SearchActivity
import com.example.playlistmaker.setting.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnSearch = findViewById<Button>(R.id.activity_root_btn_search)
        val btnMedia = findViewById<Button>(R.id.activity_root_btn_media)
        val btnSettings = findViewById<Button>(R.id.activity_root_btn_settings)

        btnSearch.setOnClickListener {
            val goSearchActivity = Intent(this, SearchActivity::class.java)
            goSearchActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(goSearchActivity)
        }

        btnMedia.setOnClickListener {
            val goMediaActivity = Intent(this, MediaActivity::class.java)
            goMediaActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(goMediaActivity)
        }

        btnSettings.setOnClickListener {
            val goSettingsActivity = Intent(this, SettingsActivity::class.java)
            goSettingsActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(goSettingsActivity)
        }
    }
}