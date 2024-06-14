package com.programmsoft.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.programmsoft.aphorisms.R
import com.programmsoft.aphorisms.databinding.FragmentSettingsBinding
import com.programmsoft.utils.Functions

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val binding: FragmentSettingsBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createUI()
        clicks()
    }

    private fun createUI() {
        binding.cvInfo.tvName.text = resources.getText(R.string.about_app)
        binding.cvRate.tvName.text = resources.getText(R.string.rate)
        binding.cvOtherApps.tvName.text = resources.getText(R.string.other_apps)
        binding.cvShare.tvName.text = resources.getText(R.string.share)
        binding.cvInfo.icon.setImageResource(R.drawable.info)
        binding.cvRate.icon.setImageResource(R.drawable.rate)
        binding.cvOtherApps.icon.setImageResource(R.drawable.other_apps)
        binding.cvShare.icon.setImageResource(R.drawable.share)
    }

    private fun clicks() {
        binding.cvInfo.cv.setOnClickListener {
            Functions.showDialog(childFragmentManager, "about_app")
        }
        binding.cvRate.cv.setOnClickListener {
            Functions.rateApp(requireContext())
        }
        binding.cvOtherApps.cv.setOnClickListener {
            Functions.otherApps(requireContext())
        }
        binding.cvShare.cv.setOnClickListener {
            Functions.shareApp(requireContext())
        }
    }

}