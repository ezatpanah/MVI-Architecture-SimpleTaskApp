package com.ezatpanah.simplenoteapp_mvi.viewmodel.add

import com.ezatpanah.simplenoteapp_mvi.db.NoteEntity

sealed class AddNoteIntent {

    object SpinnerList : AddNoteIntent()
    data class SaveNote(val entity: NoteEntity) : AddNoteIntent()
    data class UpdateNote(val entity: NoteEntity) : AddNoteIntent()
    data class NoteDetail(val id: Int) : AddNoteIntent()

}

