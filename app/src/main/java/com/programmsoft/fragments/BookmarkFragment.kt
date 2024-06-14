package com.programmsoft.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.programmsoft.adapters.AphorismAdapter
import com.programmsoft.aphorisms.R
import com.programmsoft.aphorisms.databinding.FragmentBookmarkBinding
import com.programmsoft.room.entity.Aphorism
import com.programmsoft.utils.Functions
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.function.Consumer

class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {
    private val binding: FragmentBookmarkBinding by viewBinding()
    val aphorismAdapter = AphorismAdapter()

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Functions.db.aphorismDao().getAllByBookmark(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(@SuppressLint("NewApi")
            object : Consumer<List<Aphorism>>,
                io.reactivex.rxjava3.functions.Consumer<List<Aphorism>> {
                override fun accept(t: List<Aphorism>) {
                    aphorismAdapter.submitList(t)
                    val isListEmpty = t.isEmpty()
                    binding.imgEmpty.visibility = if (isListEmpty) View.VISIBLE else View.GONE
                    binding.rvBookmark.visibility = if (isListEmpty) View.GONE else View.VISIBLE
                }
            },
                @SuppressLint("NewApi")
                object : Consumer<Throwable>,
                    io.reactivex.rxjava3.functions.Consumer<Throwable> {
                    override fun accept(t: Throwable) {

                    }

                })
        binding.rvBookmark.adapter = aphorismAdapter
        adapterItemClick()
    }

    private fun adapterItemClick() {
        aphorismAdapter.setOnItemClickListener { factId ->
            Functions.showDialogWithArgument(requireActivity().supportFragmentManager, factId)
        }
    }

    override fun onResume() {
        super.onResume()
        val bookmarkCount = Functions.db.aphorismDao().getAllBookmark()
        binding.imgEmpty.visibility = if (bookmarkCount == 0) View.VISIBLE else View.GONE

    }


}