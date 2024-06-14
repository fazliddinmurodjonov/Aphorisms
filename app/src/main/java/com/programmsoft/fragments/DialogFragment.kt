package com.programmsoft.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.like.LikeButton
import com.like.OnLikeListener
import com.programmsoft.aphorisms.BuildConfig
import com.programmsoft.aphorisms.databinding.DialogAboutAppBinding
import com.programmsoft.aphorisms.databinding.DialogAphorismBinding
import com.programmsoft.utils.Functions

class DialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())
        when (tag) {
            "about_app" -> {
                setupAboutApp(dialog)
            }

            "aphorism" -> {
                createAphorism(dialog)
            }
        }
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.show()
        return dialog
    }

    private fun createAphorism(dialog: Dialog) {
        val aphorismId = requireArguments().getLong("aphorism_id")
        val aphorism = Functions.db.aphorismDao().getByAphorismId(aphorismId)
        val view = DialogAphorismBinding.inflate(layoutInflater, null, false)
        dialog.setContentView(view.root)
        view.btnBookmark.isLiked = aphorism.bookmark == 1
        view.text.text = aphorism.text
        if (aphorism.news == 1) {
            Functions.db.aphorismDao().updateNew(aphorismId)
        }
        view.send.setOnClickListener {
            Functions.sendData(requireActivity(), aphorism.text)
        }
        view.btnBookmark.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton?) {
                view.btnBookmark.isLiked = true
                Functions.db.aphorismDao().updateBookmark(aphorismId, 1)
            }

            override fun unLiked(likeButton: LikeButton?) {
                view.btnBookmark.isLiked = false
                Functions.db.aphorismDao().updateBookmark(aphorismId, 0)
            }
        })
        if (aphorism.text.length > 400) {
            view.scrollView.layoutParams =
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1000
                )

        }
        view.cvShare.setOnClickListener {
            Functions.shareData(requireContext(), aphorism.text)
        }
        view.cvCopy.setOnClickListener {
            Functions.copyData(requireContext(), aphorism.text)
        }
        view.root.setOnClickListener {
            dialog.dismiss()
        }
        view.root.setOnClickListener {
            dialog.dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupAboutApp(dialog: Dialog) {
        val dialogView = DialogAboutAppBinding.inflate(layoutInflater, null, false)
        dialog.setCancelable(true)
        dialogView.appVersion.text = "v${BuildConfig.VERSION_NAME}"
        dialog.setContentView(dialogView.root)
    }
}