package com.ezatpanah.simpletodoapp_mvi.viewmodel.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezatpanah.simpletodoapp_mvi.db.TaskEntity
import com.ezatpanah.simpletodoapp_mvi.repository.DbRepository
import com.ezatpanah.simpletodoapp_mvi.utils.Constants.EDUCATION
import com.ezatpanah.simpletodoapp_mvi.utils.Constants.HEALTH
import com.ezatpanah.simpletodoapp_mvi.utils.Constants.HIGH
import com.ezatpanah.simpletodoapp_mvi.utils.Constants.HOME
import com.ezatpanah.simpletodoapp_mvi.utils.Constants.LOW
import com.ezatpanah.simpletodoapp_mvi.utils.Constants.NORMAL
import com.ezatpanah.simpletodoapp_mvi.utils.Constants.WORK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(private val repository: DbRepository) : ViewModel() {

    val addTaskIntent = Channel<AddTaskIntent>()
    private val _state = MutableStateFlow<AddTaskState>(AddTaskState.Idle)
    val state: StateFlow<AddTaskState> get() = _state

    init {
        handleIntents()
    }

    private fun handleIntents() = viewModelScope.launch {
        addTaskIntent.consumeAsFlow().collect { intent ->
            when (intent) {
                is AddTaskIntent.SpinnerList -> {
                    fetchSpinnerList()
                }
                is AddTaskIntent.SaveTask -> {
                    saveTaskData(intent.entity)
                }
                is AddTaskIntent.TaskDetail -> fetchTaskDetail(intent.id)
                is AddTaskIntent.UpdateTask -> updateData(intent.entity)
            }

        }
    }

    private fun fetchSpinnerList() = viewModelScope.launch {
        val catList = mutableListOf(WORK, EDUCATION, HOME, HEALTH)
        val prList = mutableListOf(HIGH, NORMAL, LOW)

        // First way with out
        // _state.value=AddTaskState.SpinnerData(catList,prList)

        //Second way with emit ( should add viewModelScope )
        _state.emit(AddTaskState.SpinnerData(catList, prList))
    }


    private fun saveTaskData(taskEntity: TaskEntity) = viewModelScope.launch {
        _state.emit(
            try {
                AddTaskState.SaveTask(repository.saveTask(taskEntity))

            } catch (e: Exception) {
                AddTaskState.ErrorMsg(e.message.toString())
            }
        )
    }

    private fun updateData(entity: TaskEntity) = viewModelScope.launch {
        _state.value = try {
            AddTaskState.UpdateTask(repository.updateTask(entity))
        } catch (e: Exception) {
            AddTaskState.ErrorMsg(e.message.toString())
        }
    }

    private fun fetchTaskDetail(id: Int) = viewModelScope.launch {
        repository.getDetailsTask(id).collect {
            _state.value = AddTaskState.TaskDetail(it)
        }
    }

}
