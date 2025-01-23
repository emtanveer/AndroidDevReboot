package com.codingtroops.repositories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.codingtroops.repositories.utils.CustomSwipeRefresh
import com.codingtroops.repositories.utils.ManualCircularProgressIndicator
import kotlinx.coroutines.delay


@Composable
//fun RepositoriesScreen(repos: List<Repository>) {
fun RepositoriesScreen(repos: LazyPagingItems<Repository>) {

    var isRefreshing by remember { mutableStateOf(false) }
    var isInitialLoading by remember { mutableStateOf(true) } // Track if the app is loading for the first time

    // Reset the refresh state after loading is complete
    LaunchedEffect(repos.loadState.refresh) {
        if (repos.loadState.refresh is LoadState.NotLoading) {
            isRefreshing = false
            isInitialLoading = false // After first load, we no longer consider it initial loading
        }
    }

    CustomSwipeRefresh(
        isRefreshing = isRefreshing, // Only show the pull-to-refresh when user manually triggers it
        onRefresh = {
            isRefreshing = true // Trigger the refresh action
            repos.refresh()
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                vertical = 8.dp,
                horizontal = 8.dp
            )
        ) {
            items(count = repos.itemCount) { repo ->
                // Ensure repo is not null before passing to RepositoryItem
                repo?.let { itemsIndex ->
                    RepositoryItem(index = itemsIndex, item = repos[itemsIndex]!!)
                }
            }

            val refreshLoadState = repos.loadState.refresh
            val appendLoadState = repos.loadState.append

            when {
                // Show the loading spinner on initial load or if data is refreshing
                refreshLoadState is LoadState.Loading && isInitialLoading -> {
                    item {
                        LoadingItem(Modifier.fillParentMaxSize()) // Circular progress indicator for initial load
                    }
                }

                // Don't show the pull-to-refresh indicator during the first load
                refreshLoadState is LoadState.Loading && !isInitialLoading -> {
                    item {
                        LoadingItem(Modifier.fillMaxSize()) // Circular progress indicator for refresh
                    }
                }

                refreshLoadState is LoadState.Error -> {
                    val error = refreshLoadState.error
                    item {
                        ErrorItem(
                            message = error.localizedMessage ?: "",
                            modifier = Modifier.fillParentMaxSize(),
                            onClick = { repos.retry() }
                        )
                    }
                }

                appendLoadState is LoadState.Loading -> {
                    item {
                        LoadingItem(Modifier.fillMaxWidth()) // For appending more data
                    }
                }

                appendLoadState is LoadState.Error -> {
                    val error = appendLoadState.error
                    item {
                        ErrorItem(
                            message = error.localizedMessage ?: "",
                            onClick = { repos.retry() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RepositoryItem(index: Int, item: Repository) {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .padding(8.dp)
            .height(120.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = index.toString(),
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .weight(0.2f)
                    .padding(8.dp)
            )
            Column(modifier = Modifier.weight(0.8f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )
            }
        }
    }
}

@Composable
fun ErrorItem(
    message: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement =
        Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            maxLines = 2,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.h6,
            color = Color.Red
        )
        Button(
            onClick = onClick,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Try Again")
        }
    }
}

@Composable
fun LoadingItem(
    modifier: Modifier = Modifier
) {
    //For the Progressbar animation (progress by 25% of circle to completing to full)
    var progress by remember { mutableStateOf(0.25f) }

    LaunchedEffect(Unit) {
        // Simulate progress change (e.g., for loading)
        while (progress < 1f) {
            delay(100)
            progress += 0.05f
        }
    }

    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment =
        Alignment.CenterHorizontally
    ) {
        //CircularProgressIndicator()
        ManualCircularProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxSize()
        )
    }
}