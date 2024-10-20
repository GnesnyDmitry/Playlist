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
import com.google.android.material.switchmaterial.SwitchMaterial

const val SHARED_PREFERENCE_THEME = "shared_preference_theme"

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

        val sharedPrefsTheme = getSharedPreferences(SHARED_PREFERENCE_THEME, MODE_PRIVATE)
        val editor = sharedPrefsTheme.edit()

        val toolbarSettings = findViewById<Toolbar>(R.id.settings_root_toolbar)
        val btnShareApp = findViewById<Button>(R.id.btn_share_app)
        val btnContactSupport = findViewById<Button>(R.id.btn_contact_support)
        val btnThermsUse = findViewById<Button>(R.id.btn_therms_use)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.theme_switcher)

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

        val switchState = sharedPrefsTheme.getBoolean("THEME_STATE", false)
        themeSwitcher.isChecked = switchState

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            editor.putBoolean("THEME_STATE", checked)
            editor.apply()
        }
    }



    private fun shareApp() {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_text))
            startActivity(Intent.createChooser(this, getString(R.string.share_app_text)))
        }
    }

    private fun contactSupport() {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.support_body))
            startActivity(this)
        }
    }

    private fun thermsUse() {
        val url = getString(R.string.url)
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            startActivity(this)
        }
    }
}