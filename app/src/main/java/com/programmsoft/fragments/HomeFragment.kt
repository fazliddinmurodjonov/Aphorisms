package com.programmsoft.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.programmsoft.adapters.AphorismsAdapter
import com.programmsoft.adapters.LoadMoreAdapter
import com.programmsoft.aphorisms.R
import com.programmsoft.aphorisms.databinding.FragmentHomeBinding
import com.programmsoft.utils.ConnectivityManagers
import com.programmsoft.utils.Functions
import com.programmsoft.utils.Functions.getDate
import com.programmsoft.utils.Functions.getDateAddedDay
import com.programmsoft.utils.Functions.getDateInMilliseconds
import com.programmsoft.utils.SharedPreference
import com.programmsoft.viewmodels.AphorismsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var aphorismsAdapter: AphorismsAdapter

    private val viewModel: AphorismsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        SharedPreference.init(requireActivity())
        permissionOfNotification(requireContext())
        Functions.setTimeOfAlarmManager(requireContext())
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.aphorismList.collect { facts ->
                    aphorismsAdapter.submitData(facts)
                }
            }
        }
        aphorismsAdapter.setOnItemClickListener {
            Functions.db.aphorismDao().updateNew(it)
            Functions.showDialogWithArgument(requireActivity().supportFragmentManager, it)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                aphorismsAdapter.loadStateFlow.collect {
                    val state = it.refresh
                    binding.progressBar.isVisible = state is LoadState.Loading
                }
            }
        }
        if (!ConnectivityManagers.isNetworkAvailable) {
            binding.retryLayout.root.visibility = View.VISIBLE
        }

        binding.retryLayout.retryBtn.setOnClickListener {
            if (ConnectivityManagers.isNetworkAvailable) {
                binding.retryLayout.root.visibility = View.INVISIBLE
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.factsListRetry.collect { facts ->
                        aphorismsAdapter.submitData(facts)
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.actionBar.cvSearch.setOnClickListener {
            findNavController().navigate(R.id.fragment_search)
        }
        binding.rvAphorism.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = aphorismsAdapter
        }
        binding.rvAphorism.adapter = aphorismsAdapter.withLoadStateFooter(
            LoadMoreAdapter {
                aphorismsAdapter.retry()
            }
        )
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
                //   Functions.appNotifications(requireContext())
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