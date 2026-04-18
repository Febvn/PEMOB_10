package com.example.myfirstkmpapp.news.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.myfirstkmpapp.news.ui.components.NewsArticleItem
import com.example.myfirstkmpapp.news.ui.viewmodel.NewsViewModel

class NewsFavoritesScreen(private val viewModel: NewsViewModel) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val favoriteArticles by viewModel.favoriteArticles.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            "Saved Content", 
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black)
                        ) 
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                if (favoriteArticles.isEmpty()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center).padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Safe Haven is Empty", 
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Articles you save will appear here for quick access.", 
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
 else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(favoriteArticles) { article ->
                            NewsArticleItem(article) {
                                navigator.push(NewsDetailScreen(article, viewModel))
                            }
                        }
                    }
                }
            }
        }
    }
}
