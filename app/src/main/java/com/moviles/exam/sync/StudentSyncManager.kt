package com.moviles.exam.sync


import android.util.Log
import com.moviles.exam.models.toModel
import com.moviles.exam.network.StudentApi
import com.moviles.exam.dao.StudentDao
import com.moviles.exam.models.toEntity

class StudentSyncManager(
    private val studentApi: StudentApi,
    private val studentDao: StudentDao
) {

    suspend fun syncLocalChanges() {
        Log.d("StudentSyncManager", "Iniciando sincronización de cambios locales...")
        try {
            syncAddedStudents()
            syncUpdatedStudents()
            syncDeletedStudents()
            Log.d("StudentSyncManager", "Sincronización de cambios locales completada.")
        } catch (e: Exception) {
            Log.e("StudentSyncManager", "Error durante la sincronización: ${e.localizedMessage}")
            throw e
        }
    }

    private suspend fun syncAddedStudents() {
        val localStudentsToAdd = studentDao.getAllUnsyncedStudents().filter { it.serverId == null && !it.isPendingDelete && !it.isPendingUpdate }
        localStudentsToAdd.forEach { localStudent ->
            try {
                val newStudentFromServer = studentApi.registerStudent(localStudent.toModel())
                studentDao.markAsSynced(localStudent.id)
                newStudentFromServer.id?.let { serverId ->
                    studentDao.updateServerId(localStudent.id, serverId)
                    Log.d("StudentSyncManager", "Nuevo estudiante sincronizado: ${newStudentFromServer.name} (Server ID: $serverId)")
                }
            } catch (e: Exception) {
                Log.e("StudentSyncManager", "Error al sincronizar nuevo estudiante ${localStudent.name}: ${e.localizedMessage}")
            }
        }
    }

    private suspend fun syncUpdatedStudents() {
        val pendingUpdateStudents = studentDao.getPendingUpdateStudents()
        pendingUpdateStudents.forEach { studentToUpdate ->
            studentToUpdate.serverId?.let { serverId ->
                try {
                    val updatedStudentFromServer = studentApi.updateStudent(serverId, studentToUpdate.toModel())
                    studentDao.insert(updatedStudentFromServer.toEntity())
                    studentDao.markAsSynced(studentToUpdate.id)
                    Log.d("StudentSyncManager", "Estudiante actualizado en servidor (Server ID: $serverId): ${updatedStudentFromServer.name}")
                } catch (e: Exception) {
                    Log.e("StudentSyncManager", "Error al actualizar estudiante en servidor (Server ID: $serverId): ${e.localizedMessage}")
                }
            }
        }
    }

    private suspend fun syncDeletedStudents() {
        val pendingDeleteStudents = studentDao.getPendingDeleteStudents()
        pendingDeleteStudents.forEach { studentToDelete ->
            studentToDelete.serverId?.let { serverId ->
                try {
                    studentApi.deleteStudent(serverId)
                    studentDao.deleteStudentById(studentToDelete.id)
                    Log.d("StudentSyncManager", "Estudiante eliminado del servidor (Server ID: $serverId)")
                } catch (e: Exception) {
                    Log.e("StudentSyncManager", "Error al eliminar estudiante del servidor (Server ID: $serverId): ${e.localizedMessage}")
                }
            } ?: run {
                studentDao.deleteStudentById(studentToDelete.id)
            }
        }
    }
}