package com.wasilyk.app.apod.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wasilyk.app.apod.model.repository.Repository
import javax.inject.Inject

class ApodViewModelFactory @Inject constructor(
    private val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when(modelClass) {
            ApodViewModel::class.java -> ApodViewModel(repository) as T
            else -> throw Throwable("Unknowm ViewModel class")
        }
}