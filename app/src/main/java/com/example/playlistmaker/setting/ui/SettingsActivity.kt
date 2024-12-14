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
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.setting.presentation.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this,
        SettingsViewModel.factory())[SettingsViewModel::class.java] }
    private val router = SettingsRouter(this)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        viewModel.themeStateLiveData().observe(this) {
            binding.themeSwitcher.isChecked = it
        }

       binding.settingsRootToolbar.setNavigationOnClickListener {
            router.goBack()
       }

        binding.btnShareApp.setOnClickListener {
            shareApp()
        }

        binding.btnContactSupport.setOnClickListener {
            contactSupport()
        }

        binding.btnThermsUse.setOnClickListener {
            thermsUse()
        }

       binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
           viewModel.changeTheme(isChecked)
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