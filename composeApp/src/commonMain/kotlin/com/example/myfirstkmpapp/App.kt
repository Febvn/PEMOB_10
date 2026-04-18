package com.example.myfirstkmpapp

import androidx.compose.runtime.*
import com.example.myfirstkmpapp.ui.screen.ProfileScreen
import com.example.myfirstkmpapp.ui.theme.SkeuomorphicTheme
import com.example.myfirstkmpapp.viewmodel.ProfileViewModel
import com.example.myfirstkmpapp.viewmodel.ProfileUiState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myfirstkmpapp.viewmodel.NoteViewModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.myfirstkmpapp.navigation.MainScreen
import com.example.myfirstkmpapp.news.ui.viewmodel.NewsViewModel
import com.example.myfirstkmpapp.news.repository.NewsRepository
import com.example.myfirstkmpapp.news.data.remote.NewsApiService
import com.example.myfirstkmpapp.viewmodel.SettingsViewModel

/**
 * App — Entry point utama aplikasi
 *
 * Menerapkan SkeuomorphicTheme dan mengelola navigasi utama menggunakan Voyager.
 */
@Composable
fun App(dependencyContainer: DependencyContainer) {
    val settingsViewModel: SettingsViewModel = viewModel { 
        SettingsViewModel(dependencyContainer.settingsRepository) 
    }
    val noteViewModel: NoteViewModel = viewModel { 
        NoteViewModel(
            dependencyContainer.noteRepository,
            settingsViewModel.sortOrder
        ) 
    }
    val newsViewModel: NewsViewModel = viewModel { 
        NewsViewModel(NewsRepository(NewsApiService())) 
    }
    val profileViewModel: ProfileViewModel = viewModel { ProfileViewModel() }
    
    val profileState by profileViewModel.uiState.collectAsState()
    val isDarkMode by settingsViewModel.isDarkMode.collectAsState()

    SkeuomorphicTheme(
        palette = profileState.currentPalette,
        isDark = isDarkMode
    ) {
        Navigator(MainScreen(noteViewModel, profileViewModel, newsViewModel, settingsViewModel)) { navigator ->
            SlideTransition(navigator)
        }
    }
}