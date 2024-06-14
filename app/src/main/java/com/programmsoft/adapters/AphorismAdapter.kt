package com.programmsoft.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.programmsoft.aphorisms.App
import com.programmsoft.aphorisms.R
import com.programmsoft.aphorisms.databinding.ItemAphorismBinding
import com.programmsoft.room.entity.Aphorism


class AphorismAdapter : ListAdapter<Aphorism, AphorismAdapter.ViewHolder>(MyDiffUtil()) {
    lateinit var itemClick: OnItemClickListener
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    fun interface OnItemClickListener {
        fun onClick(id: Long)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClick = listener
    }

    inner class ViewHolder(var binding: ItemAphorismBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(aphorism: Aphorism) {
            binding.tvAphorism.text = aphorism.text
            binding.logo.isVisible = aphorism.news == 1
            binding.root.animation = AnimationUtils.loadAnimation(App.instance, R.anim.item_anim)
            binding.cvAphorism.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = bindingAdapterPosition
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                itemClick.onClick(aphorism.aphorismId)
            }
            if (bindingAdapterPosition == selectedPosition) {
                binding.logo.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAphorismBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Aphorism>() {
        override fun areItemsTheSame(oldItem: Aphorism, newItem: Aphorism): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Aphorism, newItem: Aphorism): Boolean {
            return when {
                oldItem.bookmark != newItem.bookmark -> {
                    false
                }

                oldItem.news != newItem.news -> {
                    false
                }

                else -> true
            }
        }

    }
}