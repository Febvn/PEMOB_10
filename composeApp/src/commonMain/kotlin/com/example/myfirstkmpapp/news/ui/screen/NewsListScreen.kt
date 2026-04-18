package com.example.myfirstkmpapp.news.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.example.myfirstkmpapp.news.ui.components.ShimmerItem
import com.example.myfirstkmpapp.news.ui.components.NewsArticleItem
import com.example.myfirstkmpapp.news.ui.viewmodel.NewsUiState
import com.example.myfirstkmpapp.news.ui.viewmodel.NewsViewModel
import com.example.myfirstkmpapp.news.util.getRelativeTime

class NewsListScreen(private val viewModel: NewsViewModel) : Screen {
    @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val uiState by viewModel.uiState.collectAsState()
        val searchQuery by viewModel.searchQuery.collectAsState()
        val selectedCategory by viewModel.selectedCategory.collectAsState()

        var isRefreshing by remember { mutableStateOf(false) }
        val listState = rememberLazyListState()
        
        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                viewModel.loadNews(isPullRefresh = true)
            }
        )

        // Infinite scroll: load more when reaching the bottom
        LaunchedEffect(listState) {
            snapshotFlow {
                val layoutInfo = listState.layoutInfo
                val totalItems = layoutInfo.totalItemsCount
                val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                lastVisible to totalItems
            }.collect { (lastVisible, totalItems) ->
                // Jika user sudah menscroll ke paling bawah (mentok)
                if (totalItems > 0 && lastVisible >= totalItems - 1) {
                    viewModel.loadNews(isLoadMore = true) 
                }
            }
        }
        
        // Reset isRefreshing when state changes to Success
        val currentState = uiState
        LaunchedEffect(currentState) {
            if (currentState is NewsUiState.Success || currentState is NewsUiState.Error) {
                isRefreshing = false
            }
        }

        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .statusBarsPadding()
                ) {
                    Text(
                        text = "Top Stories",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-1).sp
                        ),
                        modifier = Modifier.padding(16.dp)
                    )

                    // Search + Filter Row
                    var showCategories by remember { mutableStateOf(false) }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.onSearchQueryChanged(it) },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Search world news...") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            shape = CircleShape,
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = { showCategories = !showCategories },
                            modifier = Modifier
                                .background(
                                    if (showCategories) MaterialTheme.colorScheme.primary 
                                    else MaterialTheme.colorScheme.primaryContainer, 
                                    CircleShape
                                )
                                .size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filter",
                                tint = if (showCategories) MaterialTheme.colorScheme.onPrimary 
                                       else MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = showCategories,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(16.dp))

                            // Category Chips (Horizontal Scroll)
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(viewModel.categories) { category ->
                                    FilterChip(
                                        selected = selectedCategory == category,
                                        onClick = { viewModel.onCategorySelected(category) },
                                        label = { Text(category) },
                                        shape = CircleShape,
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Main Content
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .pullRefresh(pullRefreshState)
                ) {
                    when (val state = uiState) {
                        is NewsUiState.Loading -> {
                            LazyColumn {
                                items(5) { ShimmerItem() }
                            }
                        }
                        is NewsUiState.Success -> {
                            if (state.articles.isEmpty()) {
                                EmptyState()
                            } else {
                                LazyColumn(
                                    state = listState,
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(bottom = 24.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(state.articles) { article ->
                                        NewsArticleItem(article) { clicked ->
                                            navigator.push(NewsDetailScreen(clicked, viewModel))
                                        }
                                    }
                                }
                            }
                        }
                        is NewsUiState.Error -> {
                            ErrorState(state.message) { viewModel.loadNews() }
                        }
                    }

                    PullRefreshIndicator(
                        refreshing = isRefreshing,
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            }
        }
    }

    @Composable
    private fun EmptyState() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("No articles found", style = MaterialTheme.typography.titleLarge)
                Text("Try searching for something else.", style = MaterialTheme.typography.bodySmall)
            }
        }
    }

    @Composable
    private fun ErrorState(msg: String, onRetry: () -> Unit) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Something went wrong", style = MaterialTheme.typography.titleLarge)
            Text(msg, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onRetry) { Text("Try Again") }
        }
    }
}
