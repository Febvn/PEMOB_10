package com.example.myfirstkmpapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.myfirstkmpapp.ui.screen.FavoritesScreen
import com.example.myfirstkmpapp.ui.screen.NoteListScreen
import com.example.myfirstkmpapp.ui.screen.ProfileScreen
import com.example.myfirstkmpapp.viewmodel.NoteViewModel
import com.example.myfirstkmpapp.viewmodel.ProfileViewModel

import com.example.myfirstkmpapp.viewmodel.SettingsViewModel
import com.example.myfirstkmpapp.ui.screen.SettingsScreen
import androidx.compose.material.icons.filled.Settings

class NotesTab(private val noteViewModel: NoteViewModel) : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.List)
            return remember {
                TabOptions(
                    index = 1u,
                    title = "Notes",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(NoteListScreen(noteViewModel)) { navigator ->
            SlideTransition(navigator)
        }
    }
}

class FavoritesTab(private val noteViewModel: NoteViewModel) : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Star)
            return remember {
                TabOptions(
                    index = 2u,
                    title = "Favorite",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(FavoritesScreen(noteViewModel)) { navigator ->
            SlideTransition(navigator)
        }
    }
}

class ProfileTab(private val profileViewModel: ProfileViewModel) : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Person)
            return remember {
                TabOptions(
                    index = 3u,
                    title = "Profile",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        ProfileScreen(viewModel = profileViewModel)
    }
}

class SettingsTab(private val settingsViewModel: SettingsViewModel) : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Settings)
            return remember {
                TabOptions(
                    index = 4u,
                    title = "Settings",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        SettingsScreen(viewModel = settingsViewModel)
    }
}
