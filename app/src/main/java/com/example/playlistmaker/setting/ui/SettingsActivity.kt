package com.example.playlistmaker.setting.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.setting.presentation.SettingsView
import com.example.playlistmaker.tools.SHARED_PREFERENCE_THEME
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity(), SettingsView {

    private lateinit var themeSwitcher: SwitchMaterial
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

        val toolbarSettings = findViewById<Toolbar>(R.id.settings_root_toolbar)
        val btnShareApp = findViewById<Button>(R.id.btn_share_app)
        val btnContactSupport = findViewById<Button>(R.id.btn_contact_support)
        val btnThermsUse = findViewById<Button>(R.id.btn_therms_use)
        themeSwitcher = findViewById(R.id.theme_switcher)

        val presenter = Creator.createSettingsPresenter(
            view = this,
            router = SettingsRouter(this),
            sharedPreferences = sharedPrefsTheme
        )
        presenter.setTheme()

        toolbarSettings.setNavigationOnClickListener {
            presenter.onClickedBack()
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
        themeSwitcher.setOnClickListener {
            presenter.setNewSwitcherTheme()
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

    override fun setThemeSwitcher(isChecked: Boolean) {
        themeSwitcher.isChecked = isChecked
    }

    override fun onThemeSwitchChanged(): Boolean {
        return themeSwitcher.isChecked
    }

}