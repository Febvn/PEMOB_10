package com.example.myfirstkmpapp.news.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.example.myfirstkmpapp.news.data.model.NewsArticle
import com.example.myfirstkmpapp.news.data.remote.NewsApiService
import com.example.myfirstkmpapp.news.repository.NewsRepository
import com.example.myfirstkmpapp.news.ui.viewmodel.NewsUiState
import com.example.myfirstkmpapp.news.ui.viewmodel.NewsViewModel

class NewsListScreen : Screen {
    @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        
        // Manual DI for simplicity in this task
        val viewModel = remember { 
            NewsViewModel(NewsRepository(NewsApiService())) 
        }
        val uiState by viewModel.uiState.collectAsState()
        
        var isRefreshing by remember { mutableStateOf(false) }
        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshing,
            onRefresh = {
                viewModel.loadNews()
            }
        )

        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Space News Reader") })
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .pullRefresh(pullRefreshState)
            ) {
                when (val state = uiState) {
                    is NewsUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is NewsUiState.Success -> {
                        isRefreshing = false
                        NewsList(state.articles) { article ->
                            navigator.push(NewsDetailScreen(article))
                        }
                    }
                    is NewsUiState.Error -> {
                        isRefreshing = false
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                            Button(onClick = { viewModel.loadNews() }) {
                                Text("Retry")
                            }
                        }
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

    @Composable
    fun NewsList(articles: List<NewsArticle>, onArticleClick: (NewsArticle) -> Unit) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(articles) { article ->
                NewsArticleItem(article, onArticleClick)
            }
        }
    }

    @Composable
    fun NewsArticleItem(article: NewsArticle, onArticleClick: (NewsArticle) -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onArticleClick(article) },
            shape = MaterialTheme.shapes.large,
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column {
                AsyncImage(
                    model = article.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(20.dp)) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Text(
                            text = article.newsSite,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = article.summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = article.publishedAt.take(10), // Simple date format
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}
