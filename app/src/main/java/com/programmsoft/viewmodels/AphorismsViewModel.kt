package com.programmsoft.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.programmsoft.paging.AphorismPagingSource
import com.programmsoft.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AphorismsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val aphorismList = Pager(PagingConfig(1))
    {
        AphorismPagingSource(repository)
    }.flow.cachedIn(viewModelScope)

    val factsListRetry = Pager(PagingConfig(1))
    {
        AphorismPagingSource(repository)
    }.flow.cachedIn(viewModelScope)
}