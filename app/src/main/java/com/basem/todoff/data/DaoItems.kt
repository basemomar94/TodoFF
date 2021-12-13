package com.basem.todoff.data

import androidx.room.*


@Dao
interface DaoItems {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert_update(taskItem: TaskItem)

    @Delete
    fun delete(taskItem: TaskItem)

    @Query("Select * from task_database")
    fun getdata(): List<TaskItem>

    @Query("Delete from task_database")
    fun deleteall()


    @Query("Select * From task_database Where title Like '%' || :search || '%'")
    fun search(search: String): List<TaskItem>

    @Query("Select * From task_database Order by title Asc")
    fun sortbynamAsc () : List<TaskItem>

    @Query("Select * From task_database Order by title DESC")
    fun sortbynamDesc () : List<TaskItem>

    @Query("Select * From task_database Order by done Asc,important Desc")
    fun important () : List<TaskItem>

    @Query("Update task_database Set done = 1 Where id=:keyid")
   fun update(keyid: Int)
}