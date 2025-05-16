package com.moviles.exam.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moviles.exam.models.StudentEntity

@Dao
interface StudentDao {

    @Query("SELECT * FROM students WHERE courseId = :courseId AND isPendingDelete = 0")
    suspend fun getStudentsByCourse(courseId: Int): List<StudentEntity>

    @Query("SELECT * FROM students WHERE id = :studentId")
    suspend fun getStudentById(studentId: Int): StudentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: StudentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(students: List<StudentEntity>)

    @Query("DELETE FROM students WHERE id = :studentId")
    suspend fun deleteStudentById(studentId: Int)

    @Query("SELECT * FROM students WHERE isSynced = 0")
    suspend fun getAllUnsyncedStudents(): List<StudentEntity>

    @Query("UPDATE students SET isSynced = 1, isPendingDelete = 0, isPendingUpdate = 0 WHERE id = :studentId")
    suspend fun markAsSynced(studentId: Int)

    @Query("UPDATE students SET serverId = :serverId WHERE id = :localId")
    suspend fun updateServerId(localId: Int, serverId: Int)

    @Query("SELECT * FROM students WHERE serverId = :serverId")
    suspend fun getStudentByServerId(serverId: Int): StudentEntity?

    @Query("SELECT * FROM students WHERE isPendingDelete = 1")
    suspend fun getPendingDeleteStudents(): List<StudentEntity>

    @Query("UPDATE students SET isPendingDelete = 0 WHERE id = :studentId")
    suspend fun markAsNotPendingDelete(studentId: Int)

    @Query("SELECT * FROM students WHERE isPendingUpdate = 1")
    suspend fun getPendingUpdateStudents(): List<StudentEntity>
}