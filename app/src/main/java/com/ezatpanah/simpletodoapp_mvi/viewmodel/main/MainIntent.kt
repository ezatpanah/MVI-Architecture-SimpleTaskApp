package com.ezatpanah.simpletodoapp_mvi.viewmodel.main

import com.ezatpanah.simpletodoapp_mvi.db.TaskEntity

sealed class MainIntent {
    object LoadAllTasks : MainIntent()
    data class SearchTask(val search: String) : MainIntent()
    data class FilterTask(val filter: String) : MainIntent()
    data class DeleteTask(val entity: TaskEntity) : MainIntent()
    data class ClickToDetail(val id: Int) : MainIntent()
}