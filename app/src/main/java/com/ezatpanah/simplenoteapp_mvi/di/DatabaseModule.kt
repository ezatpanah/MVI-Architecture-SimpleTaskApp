package com.ezatpanah.simplenoteapp_mvi.di

import android.content.Context
import androidx.room.Room
import com.ezatpanah.simplenoteapp_mvi.utils.Constants.NOTE_DATABASE
import com.ezatpanah.simplenoteapp_mvi.db.NoteDatabase
import com.ezatpanah.simplenoteapp_mvi.db.NoteEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, NoteDatabase::class.java, NOTE_DATABASE )
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDao(db : NoteDatabase) = db.noteDao()

    @Provides
    @Singleton
    fun provideEntity() = NoteEntity()

}