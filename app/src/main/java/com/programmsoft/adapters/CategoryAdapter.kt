package com.programmsoft.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.programmsoft.aphorisms.App
import com.programmsoft.aphorisms.R
import com.programmsoft.aphorisms.databinding.ItemCategoryBinding
import com.programmsoft.room.entity.AphorismCategory

class CategoryAdapter : ListAdapter<AphorismCategory, CategoryAdapter.ViewHolder>(MyDiffUtil()) {
    lateinit var itemClick: OnItemClickListener

    fun interface OnItemClickListener {
        fun onClick(id: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClick = listener
    }

    inner class ViewHolder(private var binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("DiscouragedApi")
        fun onBind(aphorismCategory: AphorismCategory) {
            binding.tvName.text = aphorismCategory.nameUzb
            binding.root.animation = AnimationUtils.loadAnimation(App.instance, R.anim.item_anim)
            binding.cvCategory.setOnClickListener {
                itemClick.onClick(aphorismCategory.id!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class MyDiffUtil : DiffUtil.ItemCallback<AphorismCategory>() {
        override fun areItemsTheSame(oldItem: AphorismCategory, newItem: AphorismCategory): Boolean {
            return when {
                oldItem.id != newItem.id -> {
                    false
                }

                else -> true
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: AphorismCategory, newItem: AphorismCategory): Boolean {
            return when {
                oldItem.id != newItem.id -> {
                    false
                }

                else -> true
            }
        }

    }
}