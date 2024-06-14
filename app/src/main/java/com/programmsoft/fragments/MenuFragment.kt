package com.programmsoft.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.programmsoft.adapters.CategoryAdapter
import com.programmsoft.aphorisms.R
import com.programmsoft.aphorisms.databinding.FragmentMenuBinding
import com.programmsoft.utils.Functions


class MenuFragment : Fragment(R.layout.fragment_menu) {
    private val binding: FragmentMenuBinding by viewBinding()
    private lateinit var categoryAdapter: CategoryAdapter

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        categoryAdapter = CategoryAdapter()
        val factCategoryList = Functions.db.aphorismCategoryDao().getAllWithoutFlowable()
        categoryAdapter.submitList(factCategoryList)
        binding.rvCategory.adapter = categoryAdapter
        adapterItemClick()
        val a=Functions.db.aphorismDao().getAllAphorisms()
        for (aphorism in a) {
            Log.d("DDD", "categoryId: ${aphorism.categoryId}")
        }
    }

    private fun adapterItemClick() {
        categoryAdapter.setOnItemClickListener { categoryId ->
            val bundleOf = bundleOf("category_id" to categoryId)
            findNavController().navigate(R.id.fragment_category, bundleOf)
        }
    }
}