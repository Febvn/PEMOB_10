package com.example.myfirstkmpapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.example.myfirstkmpapp.ui.components.BottomNavBar
import com.example.myfirstkmpapp.viewmodel.NoteViewModel
import com.example.myfirstkmpapp.viewmodel.ProfileViewModel
import com.example.myfirstkmpapp.viewmodel.SettingsViewModel

class MainScreen(
    private val noteViewModel: NoteViewModel,
    private val profileViewModel: ProfileViewModel,
    private val settingsViewModel: SettingsViewModel
) : Screen {

    @Composable
    override fun Content() {
        val tabs = listOf(
            NotesTab(noteViewModel),
            FavoritesTab(noteViewModel),
            ProfileTab(profileViewModel),
            SettingsTab(settingsViewModel)
        )

        TabNavigator(tabs.first()) { tabNavigator ->
            Scaffold(
                bottomBar = {
                    BottomNavBar(
                        tabNavigator = tabNavigator,
                        tabs = tabs
                    )
                }
            ) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    CurrentTab()
                }
            }
        }
    }
}
