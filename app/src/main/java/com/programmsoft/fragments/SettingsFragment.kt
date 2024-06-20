package com.programmsoft.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.programmsoft.aphorisms.R
import com.programmsoft.aphorisms.databinding.FragmentSettingsBinding
import com.programmsoft.utils.Functions
import com.programmsoft.utils.Functions.isAllowNotifications
import com.programmsoft.utils.SharedPreference


class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val binding: FragmentSettingsBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createUI()
        clicks()
    }

    private fun createUI() {
        SharedPreference.init(requireContext())
        binding.cvInfo.tvName.text = resources.getText(R.string.about_app)
        binding.cvRate.tvName.text = resources.getText(R.string.rate)
        binding.cvOtherApps.tvName.text = resources.getText(R.string.other_apps)
        binding.cvShare.tvName.text = resources.getText(R.string.share)
        binding.cvInfo.icon.setImageResource(R.drawable.info)
        binding.cvRate.icon.setImageResource(R.drawable.rate)
        binding.cvOtherApps.icon.setImageResource(R.drawable.other_apps)
        binding.cvShare.icon.setImageResource(R.drawable.share)
        binding.cvNotification.switchNotification.setTrackResource(R.drawable.track)
        binding.cvNotification.switchNotification.setThumbResource(R.drawable.thumb)
        binding.cvNotification.switchNotification.outlineAmbientShadowColor =
            ContextCompat.getColor(requireContext(), R.color.red_custom)
        binding.cvNotification.switchNotification.isChecked = SharedPreference.isAllowNotification
        binding.cvNotification.switchNotification.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                if (isAllowNotifications(requireContext())) {
                    SharedPreference.isAllowNotification = true
                    Functions.setTimeOfAlarmManager(requireContext())
                } else {
                    binding.cvNotification.switchNotification.isChecked = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionOfNotification(requireContext())
                    }
                }
            } else {
                SharedPreference.isAllowNotification = false
            }
        }
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun permissionOfNotification(context: Context) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
            }

            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                  Functions.appNotifications(requireContext())
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGrandted ->
            Boolean
        }
}