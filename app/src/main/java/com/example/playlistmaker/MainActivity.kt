package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
            startActivity(goSearchActivity)
        }

        btnMedia.setOnClickListener {
            val goMediaActivity = Intent(this, MediaActivity::class.java)
            startActivity(goMediaActivity)
        }

        btnSettings.setOnClickListener {
            val goSettingsActivity = Intent(this, SettingsActivity::class.java)
            startActivity(goSettingsActivity)
        }
    }
}