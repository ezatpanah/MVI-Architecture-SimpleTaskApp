package com.ezatpanah.simplenoteapp_mvi.viewmodel.main

import com.ezatpanah.simplenoteapp_mvi.db.NoteEntity

sealed class MainState {
    object Empty : MainState()
    data class LoadNotes(val list: MutableList<NoteEntity>) : MainState()
    data class DeleteNote(val unit: Unit) : MainState()
    data class GoToDetail(val id: Int) : MainState()
}