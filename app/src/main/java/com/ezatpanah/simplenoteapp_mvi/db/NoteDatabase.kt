package com.ezatpanah.simplenoteapp_mvi.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ezatpanah.simplenoteapp_mvi.db.NoteDao
import com.ezatpanah.simplenoteapp_mvi.db.NoteEntity

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}