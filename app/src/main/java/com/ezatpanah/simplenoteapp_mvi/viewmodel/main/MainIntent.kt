package com.ezatpanah.simplenoteapp_mvi.viewmodel.main

import com.ezatpanah.simplenoteapp_mvi.db.NoteEntity

sealed class MainIntent {
    object LoadAllNotes : MainIntent()
    data class SearchNote(val search: String) : MainIntent()
    data class FilterNote(val filter: String) : MainIntent()
    data class DeleteNote(val entity: NoteEntity) : MainIntent()
    data class ClickToDetail(val id: Int) : MainIntent()
}