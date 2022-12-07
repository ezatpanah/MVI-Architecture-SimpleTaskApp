package com.ezatpanah.simplenoteapp_mvi.viewmodel.add

import com.ezatpanah.simplenoteapp_mvi.db.NoteEntity

sealed class AddNoteState {

    object Idle : AddNoteState()
    data class SpinnerData(val catList : MutableList<String> , val prList : MutableList<String>) : AddNoteState()
    data class ErrorMsg(val msg : String) : AddNoteState()
    data class SaveNote(val unit : Unit) : AddNoteState()
    data class UpdateNote(val unit: Unit) : AddNoteState()
    data class NoteDetail(val entity: NoteEntity) : AddNoteState()

}
