package com.wattpad.android.ui

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wattpad.android.R
import com.wattpad.android.databinding.ItemStoryLoadStateBinding

/**
 * Purpose of creating this adapter class it to add loading state to stories adapter in MainActivity.
 * So whenever user scrolls the stories list a progressbar will shown on each time when user reaches
 * end of the list and will send new request to server in order to fetch new stories.
 */

class StoryLoadStateAdapter (private val retry: () -> Unit) :
    LoadStateAdapter<StoryLoadStateAdapter.PagingLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: PagingLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): PagingLoadStateViewHolder {
        return PagingLoadStateViewHolder.create(parent, retry)
    }

    class PagingLoadStateViewHolder (private val binding: ItemStoryLoadStateBinding,
                                     retry: () -> Unit): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState !is LoadState.Loading
            binding.errorMsg.isVisible = loadState !is LoadState.Loading
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): PagingLoadStateViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_story_load_state, parent, false)
                val binding = ItemStoryLoadStateBinding.bind(view)
                binding.progressBar.indeterminateDrawable.colorFilter = PorterDuffColorFilter(
                    Color.parseColor("#FF4500"), PorterDuff.Mode.SRC_IN
                )
                return PagingLoadStateViewHolder(binding, retry)
            }
        }
    }
}