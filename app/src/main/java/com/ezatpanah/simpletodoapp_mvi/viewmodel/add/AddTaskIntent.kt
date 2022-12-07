package com.ezatpanah.simpletodoapp_mvi.viewmodel.add

import com.ezatpanah.simpletodoapp_mvi.db.TaskEntity

sealed class AddTaskIntent {

    object SpinnerList : AddTaskIntent()
    data class SaveTask(val entity: TaskEntity) : AddTaskIntent()
    data class UpdateTask(val entity: TaskEntity) : AddTaskIntent()
    data class TaskDetail(val id: Int) : AddTaskIntent()

}

