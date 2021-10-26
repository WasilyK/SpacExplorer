package com.wasilyk.app.apod.di

import androidx.recyclerview.widget.DiffUtil
import com.wasilyk.app.apod.model.entitities.Apod
import dagger.Module
import dagger.Provides

@Module
class DiffCallbackModule {

    @Provides
    fun provideDiffCallback(): DiffUtil.ItemCallback<Apod> =
        object : DiffUtil.ItemCallback<Apod>() {

            override fun areItemsTheSame(oldItem: Apod, newItem: Apod) =
                true

            override fun areContentsTheSame(oldItem: Apod, newItem: Apod) =
                true
        }
}