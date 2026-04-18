package com.example.myfirstkmpapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.myfirstkmpapp.ui.components.NoteItem
import com.example.myfirstkmpapp.ui.theme.LocalSkeuPalette
import com.example.myfirstkmpapp.viewmodel.NoteViewModel

class NoteListScreen(val viewModel: NoteViewModel) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val uiState by viewModel.uiState.collectAsState()
        val palette = LocalSkeuPalette.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            palette.background,
                            palette.surfaceVariant.copy(alpha = 0.3f),
                            palette.background
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "My Notes",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = palette.onSurface,
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = "${uiState.notes.size} notes stored locally",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = palette.onSurfaceLight
                    ),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    placeholder = { Text("Search notes...", color = palette.onSurfaceLight.copy(alpha = 0.5f)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .shadow(4.dp, MaterialTheme.shapes.medium, ambientColor = palette.shadowDark),
                    shape = MaterialTheme.shapes.medium,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = palette.surface,
                        unfocusedContainerColor = palette.surface,
                        focusedIndicatorColor = palette.primary,
                        unfocusedIndicatorColor = palette.onSurfaceLight.copy(alpha = 0.2f),
                        cursorColor = palette.primary
                    ),
                    singleLine = true
                )

                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = palette.primary)
                    }
                } else if (uiState.notes.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(bottom = 80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (uiState.searchQuery.isEmpty()) "Empty Note List" else "No matches found",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = palette.onSurfaceLight.copy(alpha = 0.5f)
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (uiState.searchQuery.isEmpty()) "Start by creating your first note" else "Try adjusting your search query",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = palette.onSurfaceLight.copy(alpha = 0.4f)
                                )
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 120.dp)
                    ) {
                        items(uiState.notes, key = { it.id }) { note ->
                            NoteItem(
                                note = note,
                                onClick = { navigator.push(NoteDetailScreen(note.id, viewModel)) },
                                onFavoriteClick = { viewModel.toggleFavorite(note.id, note.isFavorite) }
                            )
                        }
                    }
                }
            }

            // Floating Action Button
            FloatingActionButton(
                onClick = { navigator.push(AddNoteScreen(viewModel)) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
                    .shadow(12.dp, CircleShape, ambientColor = palette.shadowDark),
                containerColor = palette.primary,
                contentColor = palette.onPrimary,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    }
}
