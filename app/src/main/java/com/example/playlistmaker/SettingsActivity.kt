package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbarSettings = findViewById<Toolbar>(R.id.settings_root_toolbar)
        val btnShareApp = findViewById<Button>(R.id.btn_share_app)
        val btnContactSupport = findViewById<Button>(R.id.btn_contact_support)
        val btnThermsUse = findViewById<Button>(R.id.btn_therms_use)

        toolbarSettings.setNavigationOnClickListener {
            finish()
        }

        btnShareApp.setOnClickListener {
            shareApp()
        }

        btnContactSupport.setOnClickListener {
            contactSupport()
        }

        btnThermsUse.setOnClickListener {
            thermsUse()
        }
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Привет! Ознакомься с курсом по Андроид-разработке в Практикуме: https://practicum.yandex.ru/android-developer/")
        }
        startActivity(Intent.createChooser(shareIntent, "Поделиться через"))
    }

    private fun contactSupport() {
        val contactSupportIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("gnesnydmitry@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Сообщение разработчикам и разработчицам приложения Playlist Maker")
            putExtra(Intent.EXTRA_TEXT, "Спасибо разработчикам и разработчицам за крутое приложение!")
        }
        startActivity(contactSupportIntent)
    }

    private fun thermsUse() {
        val url = "https://yandex.ru/legal/practicum_offer/"
        val thermsUseIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(thermsUseIntent)
    }

}