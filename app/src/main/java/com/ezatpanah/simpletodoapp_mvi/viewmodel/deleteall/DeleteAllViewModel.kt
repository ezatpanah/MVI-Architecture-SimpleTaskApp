package com.ezatpanah.simpletodoapp_mvi.viewmodel.deleteall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezatpanah.simpletodoapp_mvi.repository.DbRepository
import com.ezatpanah.simpletodoapp_mvi.viewmodel.add.AddTaskState
import com.ezatpanah.simpletodoapp_mvi.viewmodel.main.MainIntent
import com.ezatpanah.simpletodoapp_mvi.viewmodel.main.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteAllViewModel @Inject constructor(private val repository: DbRepository) : ViewModel() {

    private val _state = MutableStateFlow<DeleteAllState>(DeleteAllState.Idle)
    val state: StateFlow<DeleteAllState> get() = _state

    fun handleIntent(intent: DeleteAllIntent) {
        when (intent) {
            is DeleteAllIntent.DeleteAllTask -> deleteAllTask()
        }
    }

    private fun deleteAllTask() = viewModelScope.launch {
        _state.emit(DeleteAllState.DeleteAllTask(repository.deleteAllTasks()))
    }
}