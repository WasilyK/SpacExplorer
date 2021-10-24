package com.wasilyk.app.apod.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wasilyk.app.apod.databinding.ApodsLoadItemViewBinding

class ApodsLoadStateAdapter(private val retry: () -> Unit)
    : LoadStateAdapter<ApodsLoadStateAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ApodsLoadItemViewBinding.bind(itemView)
        val progressView = binding.progressView
        val errorView = binding.errorView
        val errorTextView = binding.errorTextView
        val retryButton = binding.retryButton
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        when(loadState) {
            is LoadState.Loading -> {
                holder.errorView.isVisible = false
                holder.progressView.isVisible = true
            }
            is LoadState.Error -> {
                holder.progressView.isVisible = false
                holder.errorView.isVisible = true
                holder.errorTextView.text = loadState.error.localizedMessage ?: "Unknown error"
                holder.retryButton.setOnClickListener { retry.invoke() }
            }
            is LoadState.NotLoading -> { }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        ViewHolder(
            ApodsLoadItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root)
}
