package com.programmsoft.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.programmsoft.adapters.AphorismAdapter
import com.programmsoft.aphorisms.R
import com.programmsoft.aphorisms.databinding.FragmentSearchingBinding
import com.programmsoft.room.entity.Aphorism
import com.programmsoft.utils.Functions

class SearchingFragment : Fragment(R.layout.fragment_searching) {
    private val binding: FragmentSearchingBinding by viewBinding()
    private val aphorismAdapter = AphorismAdapter()
    val list = Functions.db.aphorismDao().getAllAphorisms()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.close.setOnClickListener {
            if (binding.editText.text.toString().isNotEmpty()) {
                binding.editText.setText("")
            } else {
                findNavController().popBackStack()
            }
        }
        aphorismAdapter.submitList(list)
        binding.rvSearch.adapter = aphorismAdapter
        textChanged()
        adapterItemClick()
    }

    private fun adapterItemClick() {
        aphorismAdapter.setOnItemClickListener { factId ->
            Functions.showDialogWithArgument(requireActivity().supportFragmentManager, factId)

        }
    }

    private fun textChanged() {
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })
    }

    fun filter(text: String) {
        val filteredList = ArrayList<Aphorism>()
        val factList = Functions.db.aphorismDao().getAllAphorisms()
        for (fact in factList) {
            if (fact.text.lowercase().contains(text.lowercase())) {
                filteredList.add(fact)
            }
        }
        aphorismAdapter.submitList(filteredList)
    }
}