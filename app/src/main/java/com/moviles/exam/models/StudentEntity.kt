package com.moviles.exam.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val courseId: Int,
    val isSynced: Boolean = true,
    val serverId: Int? = null,
    val isPendingDelete: Boolean = false,
    val isPendingUpdate: Boolean = false
)

fun StudentEntity.toModel(): Student {
    return Student(
        id = serverId ?: id,
        name = name,
        email = email,
        phone = phone,
        courseId = courseId
    )
}

fun Student.toEntity(): StudentEntity {
    return StudentEntity(
        id = id ?: 0,
        name = name,
        email = email,
        phone = phone,
        courseId = courseId,
        isSynced = id != null,
        serverId = id
    )
}

fun Student.toUnsyncedAddEntity(): StudentEntity {
    return StudentEntity(
        id = 0,
        name = name,
        email = email,
        phone = phone,
        courseId = courseId,
        isSynced = false,
        serverId = null
    )
}

fun Student.toUnsyncedDeleteEntity(localId: Int): StudentEntity {
    return StudentEntity(
        id = localId,
        name = name,
        email = email,
        phone = phone,
        courseId = courseId,
        isSynced = false,
        serverId = id,
        isPendingDelete = true
    )
}

fun Student.toUnsyncedUpdateEntity(localId: Int): StudentEntity {
    return StudentEntity(
        id = localId,
        name = name,
        email = email,
        phone = phone,
        courseId = courseId,
        isSynced = false,
        serverId = id,
        isPendingUpdate = true
    )
}