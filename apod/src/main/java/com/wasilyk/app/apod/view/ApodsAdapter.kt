package com.wasilyk.app.apod.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.wasilyk.app.apod.databinding.ApodsItemViewBinding
import com.wasilyk.app.apod.model.entitities.Apod
import javax.inject.Inject

class ApodsAdapter @Inject constructor(
    diffCallback: DiffUtil.ItemCallback<Apod>
) : PagingDataAdapter<Apod, ApodsAdapter.ViewHolder>(diffCallback) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ApodsItemViewBinding.bind(itemView)

        fun bind(apod: Apod?) {
            apod?.let {
                binding.apply {
                    imageView.load(apod.url) {
                        crossfade(1000)
                        transformations(RoundedCornersTransformation(10F))
                    }
                    titleView.text = apod.title
                    dateView.text = apod.date
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ApodsItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
}