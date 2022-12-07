package com.ezatpanah.simplenoteapp_mvi.db

import androidx.room.*
import com.ezatpanah.simplenoteapp_mvi.utils.Constants.NOTE_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNote(entity: NoteEntity)

    @Update
    suspend fun updateNote(entity: NoteEntity)

    @Delete
    suspend fun deleteNote(entity: NoteEntity)

    @Query("SELECT * FROM $NOTE_TABLE")
    fun getAllNotes(): Flow<MutableList<NoteEntity>>

    @Query("SELECT * FROM $NOTE_TABLE WHERE id ==:id")
    fun getNote(id: Int): Flow<NoteEntity>

    @Query("SELECT * FROM $NOTE_TABLE WHERE pr == :priority")
    fun filterNote(priority: String): Flow<MutableList<NoteEntity>>

    @Query("SELECT * FROM $NOTE_TABLE WHERE title LIKE '%' || :title || '%' ")
    fun searchNote(title: String): Flow<MutableList<NoteEntity>>

}