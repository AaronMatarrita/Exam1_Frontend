package com.moviles.exam.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moviles.exam.dao.StudentDao
import com.moviles.exam.models.StudentEntity

@Database(entities = [StudentEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
}