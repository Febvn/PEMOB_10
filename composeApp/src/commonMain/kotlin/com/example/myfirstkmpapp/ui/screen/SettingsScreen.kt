package com.example.myfirstkmpapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myfirstkmpapp.ui.theme.LocalSkeuPalette
import com.example.myfirstkmpapp.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val palette = LocalSkeuPalette.current
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(palette.background, palette.surfaceVariant.copy(alpha = 0.2f))
                )
            )
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = palette.onSurface
                ),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Theme Setting
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DarkMode, contentDescription = null, color = palette.primary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Dark Mode", style = MaterialTheme.typography.bodyLarge, color = palette.onSurface)
                }
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { viewModel.setDarkMode(it) },
                    colors = SwitchDefaults.colors(checkedThumbColor = palette.primary)
                )
            }

            Divider(color = palette.onSurfaceLight.copy(alpha = 0.1f))

            // Sort Order Setting
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Sort, contentDescription = null, color = palette.primary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Sort Order", style = MaterialTheme.typography.bodyLarge, color = palette.onSurface)
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                val options = listOf("Newest", "Oldest", "A-Z")
                options.forEach { option ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = sortOrder == option,
                            onClick = { viewModel.setSortOrder(option) },
                            colors = RadioButtonDefaults.colors(selectedColor = palette.primary)
                        )
                        Text(
                            text = option,
                            modifier = Modifier.padding(start = 8.dp),
                            color = palette.onSurface
                        )
                    }
                }
            }
        }
    }
}
