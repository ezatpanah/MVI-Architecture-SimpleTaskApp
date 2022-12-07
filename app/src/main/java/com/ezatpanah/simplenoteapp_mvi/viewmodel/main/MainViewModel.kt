package com.ezatpanah.simplenoteapp_mvi.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezatpanah.simplenoteapp_mvi.db.NoteEntity
import com.ezatpanah.simplenoteapp_mvi.repository.DbRepository
import com.ezatpanah.simplenoteapp_mvi.repository.DbRepository_Factory
import com.ezatpanah.simplenoteapp_mvi.viewmodel.add.AddNoteIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: DbRepository) : ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.Empty)
    val state: StateFlow<MainState> get() = _state

    fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.LoadAllNotes -> fetchAllNotesList()
            is MainIntent.SearchNote -> searchNote(intent.search)
            is MainIntent.FilterNote -> filterNote(intent.filter)
            is MainIntent.DeleteNote -> deletingNote(intent.entity)
            is MainIntent.ClickToDetail -> goToDetailPage(intent.id)
        }
    }

    private fun goToDetailPage(id: Int) {
        _state.value = MainState.GoToDetail(id)
    }

    private fun deletingNote(entity: NoteEntity) = viewModelScope.launch {
        _state.value = MainState.DeleteNote(repository.deleteNote(entity))
    }

    private fun filterNote(filter: String) = viewModelScope.launch {
        val data = repository.filterNote(filter)
        data.collect {
            _state.value = if (it.isNotEmpty()) {
                MainState.LoadNotes(it)
            } else {
                MainState.Empty
            }
        }
    }

    private fun searchNote(search: String) = viewModelScope.launch {
        val data = repository.searchNote(search)
        data.collect {
            _state.value = if (it.isNotEmpty()) {
                MainState.LoadNotes(it)
            } else {
                MainState.Empty
            }
        }
    }

    private fun fetchAllNotesList() = viewModelScope.launch {
        val data = repository.getAllNotes()
        data.collect {
            _state.emit(
                if (it.isNotEmpty()) {
                    MainState.LoadNotes(it)
                } else {
                    MainState.Empty
                }
            )
        }

    }

}