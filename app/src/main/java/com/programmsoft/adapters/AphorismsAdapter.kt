package com.programmsoft.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.programmsoft.aphorisms.App
import com.programmsoft.aphorisms.R
import com.programmsoft.aphorisms.databinding.ItemAphorismBinding
import com.programmsoft.models.AphorismItemResponse
import com.programmsoft.utils.Functions
import javax.inject.Inject

class AphorismsAdapter @Inject constructor() :
    PagingDataAdapter<AphorismItemResponse, AphorismsAdapter.ViewHolder>(differCallback) {
    lateinit var itemClick: OnItemClickListener
    private lateinit var context: Context
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    fun interface OnItemClickListener {
        fun onClick(id: Long)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClick = listener
    }

    inner class ViewHolder(var binding: ItemAphorismBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(aphorism: AphorismItemResponse) {
            binding.tvAphorism.text = aphorism.text
            @Suppress("SENSELESS_COMPARISON")
            binding.logo.isVisible = Functions.db.aphorismDao().getAphorismNewById(aphorism.id) == 1

            binding.root.animation = AnimationUtils.loadAnimation(App.instance, R.anim.item_anim)
            binding.cvAphorism.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = bindingAdapterPosition
//                notifyItemChanged(previousPosition)
//                notifyItemChanged(selectedPosition)
                itemClick.onClick(aphorism.id)
            }
            if (bindingAdapterPosition == selectedPosition) {
                binding.logo.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        return ViewHolder(
            ItemAphorismBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position)!!)
    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<AphorismItemResponse>() {
            override fun areItemsTheSame(
                oldItem: AphorismItemResponse,
                newItem: AphorismItemResponse
            ): Boolean {
                return oldItem.id == oldItem.id
            }

            override fun areContentsTheSame(
                oldItem: AphorismItemResponse,
                newItem: AphorismItemResponse
            ): Boolean {
                return when {
                    oldItem.id != newItem.id -> {
                        false
                    }

                    else -> true
                }
            }
        }
    }
}