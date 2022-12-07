package com.ezatpanah.simpletodoapp_mvi.viewmodel.deleteall

import androidx.lifecycle.ViewModel
import com.ezatpanah.simpletodoapp_mvi.repository.DbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DeleteAllViewModel @Inject constructor(private val repository: DbRepository) : ViewModel() {

    private val _state = MutableStateFlow<DeleteAllState>(DeleteAllState.Empty)
    val state: StateFlow<DeleteAllState> get() = _state


}