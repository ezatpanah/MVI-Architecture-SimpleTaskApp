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
            is MainIntent.LoadAllTasks -> fetchAllTasksList()
            is MainIntent.SearchTask -> searchTask(intent.search)
            is MainIntent.FilterTask -> filterTask(intent.filter)
            is MainIntent.DeleteTask -> deletingTask(intent.entity)
            is MainIntent.ClickToDetail -> goToDetailPage(intent.id)
        }
    }

    private fun goToDetailPage(id: Int) = viewModelScope.launch {
        _state.emit(MainState.GoToDetail(id))
    }

    private fun deletingTask(entity: TaskEntity) = viewModelScope.launch {
        _state.emit(MainState.DeleteTask(repository.deleteTask(entity)))
    }

    private fun filterTask(filter: String) = viewModelScope.launch {
        val data = repository.filterTask(filter)
        data.collect {
            _state.value = if (it.isNotEmpty()) {
                MainState.LoadTasks(it)
            } else {
                MainState.Empty
            }
        }
    }

    private fun searchTask(search: String) = viewModelScope.launch {
        val data = repository.searchTask(search)
        data.collect {
            _state.emit(if (it.isNotEmpty()) {
                MainState.LoadTasks(it)
            } else {
                MainState.Empty
            })
        }
    }

    private fun fetchAllTasksList() = viewModelScope.launch {
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