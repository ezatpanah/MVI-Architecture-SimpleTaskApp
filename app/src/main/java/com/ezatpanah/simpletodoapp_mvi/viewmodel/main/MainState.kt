package com.ezatpanah.simpletodoapp_mvi.viewmodel.main

import com.ezatpanah.simpletodoapp_mvi.db.TaskEntity

sealed class MainState {
    object Empty : MainState()
    data class LoadTasks(val list: MutableList<TaskEntity>) : MainState()
    data class DeleteTask(val unit: Unit) : MainState()
    data class GoToDetail(val id: Int) : MainState()
}