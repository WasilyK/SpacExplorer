package com.wasilyk.app.apod.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.wasilyk.app.apod.model.repository.Repository

class ApodViewModel(
    private val repository: Repository
) : ViewModel() {

    val apodsFlow = Pager(PagingConfig(
        pageSize = Repository.NUMBER_APODS_IN_REQUEST,
        maxSize = 50
    )) {
        repository
    }.flow
        .cachedIn(viewModelScope)
}