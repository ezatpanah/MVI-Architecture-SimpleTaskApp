package com.ezatpanah.simpletodoapp_mvi.viewmodel.deleteall



sealed class DeleteAllState {
    object Idle : DeleteAllState()
    data class DeleteAllTask(val unit: Unit) : DeleteAllState()
}