package br.edu.scl.ifsp.sharedlist.model

import androidx.room.*

@Dao
interface TaskDao {
    @Insert
    fun createTask(task: Task)
    @Query("SELECT * FROM Task WHERE id = :id")
    fun retrieveTask(id: Int): Task?
    @Query("SELECT * FROM Task")
    fun retrieveTasks(): MutableList<Task>
    @Update
    fun updateTask(task: Task): Int
    @Delete
    fun deleteTask(task: Task): Int
}