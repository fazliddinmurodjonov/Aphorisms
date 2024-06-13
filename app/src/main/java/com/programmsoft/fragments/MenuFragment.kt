package com.programmsoft.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.programmsoft.aphorisms.R
import com.programmsoft.aphorisms.databinding.FragmentMenuBinding


class MenuFragment : Fragment(R.layout.fragment_menu) {
    private val binding: FragmentMenuBinding by viewBinding()
}