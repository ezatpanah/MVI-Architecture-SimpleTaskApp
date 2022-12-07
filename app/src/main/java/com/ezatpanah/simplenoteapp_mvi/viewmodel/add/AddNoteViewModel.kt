package com.ezatpanah.simplenoteapp_mvi.viewmodel.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezatpanah.simplenoteapp_mvi.db.NoteEntity
import com.ezatpanah.simplenoteapp_mvi.repository.DbRepository
import com.ezatpanah.simplenoteapp_mvi.utils.Constants.EDUCATION
import com.ezatpanah.simplenoteapp_mvi.utils.Constants.HEALTH
import com.ezatpanah.simplenoteapp_mvi.utils.Constants.HIGH
import com.ezatpanah.simplenoteapp_mvi.utils.Constants.HOME
import com.ezatpanah.simplenoteapp_mvi.utils.Constants.LOW
import com.ezatpanah.simplenoteapp_mvi.utils.Constants.NORMAL
import com.ezatpanah.simplenoteapp_mvi.utils.Constants.WORK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(private val repository: DbRepository) : ViewModel() {

    val addNoteIntent = Channel<AddNoteIntent>()
    private val _state = MutableStateFlow<AddNoteState>(AddNoteState.Idle)
    val state: StateFlow<AddNoteState> get() = _state

    init {
        handleIntents()
    }

    private fun handleIntents() = viewModelScope.launch {
        addNoteIntent.consumeAsFlow().collect { intent ->
            when (intent) {
                is AddNoteIntent.SpinnerList -> {
                    fetchSpinnerList()
                }
                is AddNoteIntent.SaveNote -> {
                    saveNoteData(intent.entity)
                }
                is AddNoteIntent.NoteDetail -> fetchNoteDetail(intent.id)
                is AddNoteIntent.UpdateNote -> updateData(intent.entity)
            }

        }
    }

    private fun fetchSpinnerList() = viewModelScope.launch {
        val catList = mutableListOf(WORK, EDUCATION, HOME, HEALTH)
        val prList = mutableListOf(HIGH, NORMAL, LOW)

        // First way with out
        // _state.value=AddNoteState.SpinnerData(catList,prList)

        //Second way with emit ( should add viewModelScope )
        _state.emit(AddNoteState.SpinnerData(catList, prList))
    }


    private fun saveNoteData(noteEntity: NoteEntity) = viewModelScope.launch {
        _state.emit(
            try {
                AddNoteState.SaveNote(repository.saveNote(noteEntity))

            } catch (e: Exception) {
                AddNoteState.ErrorMsg(e.message.toString())
            }
        )
    }

    private fun updateData(entity: NoteEntity) = viewModelScope.launch {
        _state.value = try {
            AddNoteState.UpdateNote(repository.updateNote(entity))
        } catch (e: Exception) {
            AddNoteState.ErrorMsg(e.message.toString())
        }
    }

    private fun fetchNoteDetail(id: Int) = viewModelScope.launch {
        repository.getDetailsNote(id).collect {
            _state.value = AddNoteState.NoteDetail(it)
        }
    }

}
