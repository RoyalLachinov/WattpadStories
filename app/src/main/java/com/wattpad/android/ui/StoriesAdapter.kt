package com.wattpad.android.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wattpad.android.data.model.Story
import com.wattpad.android.databinding.ItemStoryBinding
import com.wattpad.android.util.ViewHelper.loadImage

/**
 * This class is PagingAdapter is to load PagingData which contains Stories.
 */

class StoriesAdapter : PagingDataAdapter<Story,
        RecyclerView.ViewHolder>(pagingDataAdapterDiffCallback) {

    companion object {
        val pagingDataAdapterDiffCallback = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.modifyDate == newItem.modifyDate
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val story = getItem(position)!!
        val binding = (holder as ViewHolder).binding

        binding.imageView.loadImage(story.cover)
        story.title.let {
            binding.tvTitle.text = it
        }
         story.user.fullName.let {
             binding.tvFullName.text = it
         }

    }

    class ViewHolder(val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root)
}