package com.ezatpanah.simpletodoapp_mvi.viewmodel.add

import com.ezatpanah.simpletodoapp_mvi.db.TaskEntity

sealed class AddTaskState {

    object Idle : AddTaskState()
    data class SpinnerData(val catList : MutableList<String> , val prList : MutableList<String>) : AddTaskState()
    data class ErrorMsg(val msg : String) : AddTaskState()
    data class SaveTask(val unit : Unit) : AddTaskState()
    data class UpdateTask(val unit: Unit) : AddTaskState()
    data class TaskDetail(val entity: TaskEntity) : AddTaskState()

}
