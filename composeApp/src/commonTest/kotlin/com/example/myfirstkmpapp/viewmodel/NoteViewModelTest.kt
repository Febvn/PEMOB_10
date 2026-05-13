package com.example.myfirstkmpapp.viewmodel

import app.cash.turbine.test
import com.example.myfirstkmpapp.data.Note
import com.example.myfirstkmpapp.data.remote.GeminiService
import com.example.myfirstkmpapp.repository.NoteRepository
import com.example.myfirstkmpapp.repository.SettingsRepository
import com.example.myfirstkmpapp.util.NetworkMonitor
import com.example.myfirstkmpapp.util.ShareManager
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModelTest {
    private lateinit var repository: NoteRepository
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var networkMonitor: NetworkMonitor
    private lateinit var shareManager: ShareManager
    private lateinit var geminiService: GeminiService
    private lateinit var viewModel: NoteViewModel

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        settingsRepository = mockk(relaxed = true)
        networkMonitor = mockk(relaxed = true)
        shareManager = mockk(relaxed = true)
        geminiService = mockk(relaxed = true)

        every { settingsRepository.sortOrder } returns flowOf("Newest")
        every { repository.getAllNotes() } returns flowOf(emptyList())
        every { networkMonitor.isOnline } returns flowOf(true)
        every { networkMonitor.latency } returns flowOf(0L)

        viewModel = NoteViewModel(
            repository,
            settingsRepository,
            networkMonitor,
            shareManager,
            geminiService
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        viewModel.uiState.test {
            // First item is initial state from MutableStateFlow (isLoading = true)
            val initialState = awaitItem()
            assertTrue(initialState.isLoading)
            
            // Second item is from observeNotes() after repository emits (isLoading = false)
            val loadedState = awaitItem()
            assertFalse(loadedState.isLoading)
            assertTrue(loadedState.notes.isEmpty())
        }
    }

    @Test
    fun `search query updates state`() = runTest {
        val query = "test query"
        viewModel.onSearchQueryChanged(query)
        
        assertEquals(query, viewModel.searchQuery.value)
        assertEquals(query, viewModel.uiState.value.searchQuery)
    }

    @Test
    fun `addNote calls repository insert`() = runTest {
        val title = "Title"
        val content = "Content"
        
        viewModel.addNote(title, content)
        advanceUntilIdle()
        
        coVerify { repository.insertNote(match { it.title == title && it.content == content }) }
    }

    @Test
    fun `deleteNote calls repository delete`() = runTest {
        val noteId = 1L
        viewModel.deleteNote(noteId)
        advanceUntilIdle()
        
        coVerify { repository.deleteNote(noteId) }
    }

    @Test
    fun `uiState updates when repository emits new notes`() = runTest {
        val notes = listOf(Note(id = 1, title = "Note 1", content = "Content 1", timestamp = 123L))
        val notesFlow = MutableStateFlow<List<Note>>(emptyList())
        every { repository.getAllNotes() } returns notesFlow
        
        // Re-init viewModel to pick up the new mock flow
        viewModel = NoteViewModel(repository, settingsRepository, networkMonitor, shareManager, geminiService)

        viewModel.uiState.test {
            // First item from initial empty list
            assertEquals(emptyList(), awaitItem().notes)
            
            // Update flow
            notesFlow.value = notes
            
            // Second item from updated list
            assertEquals(notes, awaitItem().notes)
        }
    }
}
