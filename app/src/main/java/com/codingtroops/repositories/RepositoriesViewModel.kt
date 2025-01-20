package com.codingtroops.repositories

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class RepositoriesViewModel(
    // private val restInterface: RepositoriesApiService = DependencyContainer.repositoriesRetrofitClient
    private val reposPagingSource: RepositoriesPagingSource = RepositoriesPagingSource()
) : ViewModel() {
//    val repositories = mutableStateOf(emptyList<Repository>())
//    init {
//        viewModelScope.launch {
//            repositories.value = restInterface.getRepositories().repos
//        }
//    }

    val repositories: Flow<PagingData<Repository>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { //By doing so, the Paging library will know which PagingSource object to query for new pages
                reposPagingSource //instance of type RepositoriesPagingSource
            }).flow.cachedIn(viewModelScope)
}