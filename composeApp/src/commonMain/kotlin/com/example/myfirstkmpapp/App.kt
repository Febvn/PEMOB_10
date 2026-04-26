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
import com.example.myfirstkmpapp.viewmodel.SettingsViewModel

/**
 * App — Entry point utama aplikasi
 *
 * Menerapkan SkeuomorphicTheme dan mengelola navigasi utama menggunakan Voyager.
 */
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val settingsViewModel: SettingsViewModel = koinViewModel()
    val noteViewModel: NoteViewModel = koinViewModel()
    val profileViewModel: ProfileViewModel = koinViewModel()
    
    val profileState by profileViewModel.uiState.collectAsState()
    val isDarkMode by settingsViewModel.isDarkMode.collectAsState()

    SkeuomorphicTheme(
        palette = profileState.currentPalette,
        isDark = isDarkMode
    ) {
        Navigator(MainScreen(noteViewModel, profileViewModel, settingsViewModel)) { navigator ->
            SlideTransition(navigator)
        }
    }
}