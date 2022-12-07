package com.ezatpanah.simpletodoapp_mvi.viewmodel.deleteall

import com.ezatpanah.simpletodoapp_mvi.db.TaskEntity

sealed class DeleteAllIntent {
    object DeleteAllTask : DeleteAllIntent()
}