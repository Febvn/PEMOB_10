package com.example.myfirstkmpapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.myfirstkmpapp.news.ui.screen.NewsListScreen

object NewsTab : Tab {
    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 3u,
            title = "News",
            icon = rememberVectorPainter(Icons.Default.Newspaper)
        )

    @Composable
    override fun Content() {
        Navigator(NewsListScreen())
    }
}
