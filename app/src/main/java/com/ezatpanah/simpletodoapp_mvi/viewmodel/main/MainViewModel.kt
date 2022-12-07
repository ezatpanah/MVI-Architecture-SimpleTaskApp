package com.ezatpanah.simpletodoapp_mvi.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezatpanah.simpletodoapp_mvi.db.TaskEntity
import com.ezatpanah.simpletodoapp_mvi.repository.DbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: DbRepository) : ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.Empty)
    val state: StateFlow<MainState> get() = _state

    fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.LoadAllTasks -> fetchAllNotesList()
            is MainIntent.SearchTask -> searchNote(intent.search)
            is MainIntent.FilterTask -> filterNote(intent.filter)
            is MainIntent.DeleteTask -> deletingNote(intent.entity)
            is MainIntent.ClickToDetail -> goToDetailPage(intent.id)
        }
    }

    private fun goToDetailPage(id: Int) {
        _state.value = MainState.GoToDetail(id)
    }

    private fun deletingNote(entity: TaskEntity) = viewModelScope.launch {
        _state.value = MainState.DeleteTask(repository.deleteTask(entity))
    }

    private fun filterNote(filter: String) = viewModelScope.launch {
        val data = repository.filterTask(filter)
        data.collect {
            _state.value = if (it.isNotEmpty()) {
                MainState.LoadTasks(it)
            } else {
                MainState.Empty
            }
        }
    }

    private fun searchNote(search: String) = viewModelScope.launch {
        val data = repository.searchTask(search)
        data.collect {
            _state.value = if (it.isNotEmpty()) {
                MainState.LoadTasks(it)
            } else {
                MainState.Empty
            }
        }
    }

    private fun fetchAllNotesList() = viewModelScope.launch {
        val data = repository.getAllTasks()
        data.collect {
            _state.emit(
                if (it.isNotEmpty()) {
                    MainState.LoadTasks(it)
                } else {
                    MainState.Empty
                }
            )
        }

    }

}