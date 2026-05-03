package com.example.myfirstkmpapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.myfirstkmpapp.ui.theme.LocalSkeuPalette
import com.example.myfirstkmpapp.viewmodel.NoteViewModel

class NoteDetailScreen(private val noteId: Long, private val viewModel: NoteViewModel) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val palette = LocalSkeuPalette.current
        val note = viewModel.getNoteById(noteId) ?: return

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Note Details") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        val uiState by viewModel.uiState.collectAsState()
                        
                        if (uiState.isAiLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp).padding(end = 16.dp),
                                color = palette.primary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            IconButton(onClick = { 
                                // Deteksi bahasa sederhana: jika mengandung banyak kata umum Indonesia, terjemahkan ke Inggris, dan sebaliknya
                                val targetLang = if (note.content.contains("yang") || note.content.contains("dan")) "English" else "Indonesian"
                                viewModel.translateNote(note, targetLang) 
                            }) {
                                Icon(Icons.Outlined.Translate, contentDescription = "Translate", tint = palette.primary)
                            }
                        }

                        IconButton(onClick = { viewModel.shareNote(note) }) {
                            Icon(Icons.Default.Share, contentDescription = "Share", tint = palette.primary)
                        }
                        IconButton(onClick = { navigator.push(EditNoteScreen(noteId, viewModel)) }) {
                            Icon(Icons.Outlined.Edit, contentDescription = "Edit")
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
                
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = palette.onSurface,
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier.fillMaxWidth()
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
                        .padding(24.dp)
                ) {
                    Text(
                        text = note.content,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = palette.onSurface,
                            lineHeight = 28.sp
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
