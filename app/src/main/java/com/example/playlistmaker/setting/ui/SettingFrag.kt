package com.example.playlistmaker.setting.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingBinding
import com.example.playlistmaker.setting.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingFrag : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.themeStateLiveData().observe(viewLifecycleOwner) {
            binding.themeSwitcher.isChecked = it
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