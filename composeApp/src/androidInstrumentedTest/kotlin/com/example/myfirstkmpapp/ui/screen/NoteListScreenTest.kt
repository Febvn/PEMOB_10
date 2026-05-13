package com.example.myfirstkmpapp.ui.screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import cafe.adriel.voyager.navigator.Navigator
import com.example.myfirstkmpapp.data.Note
import com.example.myfirstkmpapp.ui.theme.SkeuomorphicTheme
import com.example.myfirstkmpapp.ui.theme.SkeuPalette
import com.example.myfirstkmpapp.viewmodel.NoteUiState
import com.example.myfirstkmpapp.viewmodel.NoteViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class NoteListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = mockk<NoteViewModel>(relaxed = true)
    private val uiState = MutableStateFlow(NoteUiState())
    private val isOnline = MutableStateFlow(true)

    private fun setContent() {
        composeTestRule.setContent {
            SkeuomorphicTheme {
                Navigator(NoteListScreen(viewModel))
            }
        }
    }

    @Test
    fun emptyList_showsEmptyMessage() {
        uiState.value = NoteUiState(notes = emptyList(), isLoading = false)
        every { viewModel.uiState } returns uiState
        every { viewModel.isOnline } returns isOnline

        setContent()

        composeTestRule.onNodeWithText("Empty Note List").assertIsDisplayed()
    }

    @Test
    fun notesList_showsNotes() {
        val notes = listOf(
            Note(id = 1, title = "Test Note 1", content = "Content 1"),
            Note(id = 2, title = "Test Note 2", content = "Content 2")
        )
        uiState.value = NoteUiState(notes = notes, isLoading = false)
        every { viewModel.uiState } returns uiState
        every { viewModel.isOnline } returns isOnline

        setContent()

        composeTestRule.onNodeWithText("Test Note 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Note 2").assertIsDisplayed()
    }

    @Test
    fun searchNoMatch_showsNoMatchMessage() {
        uiState.value = NoteUiState(notes = emptyList(), searchQuery = "qwerty", isLoading = false)
        every { viewModel.uiState } returns uiState
        every { viewModel.isOnline } returns isOnline

        setContent()

        composeTestRule.onNodeWithText("No matches found").assertIsDisplayed()
    }
}
