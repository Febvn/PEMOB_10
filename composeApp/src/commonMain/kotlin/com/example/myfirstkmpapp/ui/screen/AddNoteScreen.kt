package com.example.myfirstkmpapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.myfirstkmpapp.ui.theme.LocalSkeuPalette
import com.example.myfirstkmpapp.viewmodel.NoteViewModel

class AddNoteScreen(private val viewModel: NoteViewModel) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val palette = LocalSkeuPalette.current
        
        var title by remember { mutableStateOf("") }
        var content by remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Add Note") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        val uiState by viewModel.uiState.collectAsState()
                        val snackbarHostState = remember { SnackbarHostState() }

                        LaunchedEffect(uiState.aiError) {
                            uiState.aiError?.let {
                                snackbarHostState.showSnackbar(it)
                            }
                        }

                        if (uiState.isAiLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp).padding(end = 16.dp),
                                color = palette.primary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            IconButton(
                                onClick = {
                                    if (content.isNotBlank()) {
                                        viewModel.generateAiContent(
                                            "Please improve and expand this note, keep it professional: $content"
                                        ) { improvedContent ->
                                            content = improvedContent
                                        }
                                    } else {
                                        viewModel.generateAiContent(
                                            "Give me a creative idea for a new note."
                                        ) { aiSuggestion ->
                                            content = aiSuggestion
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Outlined.AutoAwesome, contentDescription = "AI Assistant", tint = palette.primary)
                            }
                        }

                        IconButton(
                            onClick = {
                                if (title.isNotBlank() || content.isNotBlank()) {
                                    viewModel.addNote(title, content)
                                    navigator.pop()
                                }
                            },
                            enabled = (title.isNotBlank() || content.isNotBlank()) && !uiState.isAiLoading
                        ) {
                            Icon(Icons.Outlined.Save, contentDescription = "Save")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = palette.background,
                        titleContentColor = palette.onSurface,
                        navigationIconContentColor = palette.onSurfaceLight,
                        actionIconContentColor = palette.primary
                    )
                )
            },
            snackbarHost = { 
                val uiState by viewModel.uiState.collectAsState()
                if (uiState.aiError != null) {
                    SnackbarHost(remember { SnackbarHostState() }.apply {
                        LaunchedEffect(uiState.aiError) {
                            showSnackbar(uiState.aiError!!)
                        }
                    })
                }
            },
            containerColor = palette.background
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                palette.background,
                                palette.surfaceVariant.copy(alpha = 0.2f)
                            )
                        )
                    )
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Note Title") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = palette.primary,
                        unfocusedBorderColor = palette.onSurfaceLight.copy(alpha = 0.3f),
                        unfocusedPlaceholderColor = palette.onSurfaceLight.copy(alpha = 0.5f),
                        focusedPlaceholderColor = palette.onSurfaceLight.copy(alpha = 0.3f)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = palette.shadowDark
                        )
                        .clip(RoundedCornerShape(20.dp))
                        .background(palette.surface)
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        placeholder = { Text("Start typing your note...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 300.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            unfocusedPlaceholderColor = palette.onSurfaceLight.copy(alpha = 0.5f),
                            focusedPlaceholderColor = palette.onSurfaceLight.copy(alpha = 0.3f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
