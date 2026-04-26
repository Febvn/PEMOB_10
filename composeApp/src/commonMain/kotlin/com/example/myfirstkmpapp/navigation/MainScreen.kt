package com.example.myfirstkmpapp.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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

    @OptIn(ExperimentalMaterial3Api::class)
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
                topBar = {
                    TopAppBar(
                        title = { Text("Notes App", style = MaterialTheme.typography.titleMedium) },
                        actions = {
                            val isOnline by noteViewModel.isOnline.collectAsState()
                            val latency by noteViewModel.latency.collectAsState()
                            
                            val color = if (isOnline) Color(0xFF4CAF50) else Color(0xFFF44336)
                            val statusText = if (isOnline) {
                                if (latency > 0) "$latency ms" else "Online"
                            } else "Offline"
                            val icon = if (isOnline) Icons.Default.Wifi else Icons.Default.WifiOff
                            
                            Surface(
                                color = color.copy(alpha = 0.1f),
                                shape = MaterialTheme.shapes.extraSmall,
                                modifier = Modifier.padding(end = 16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = color,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = statusText,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = color
                                    )
                                }
                            }
                        }
                    )
                },
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
