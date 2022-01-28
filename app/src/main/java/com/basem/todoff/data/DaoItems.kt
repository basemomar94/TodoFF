package com.basem.todoff.data

import androidx.room.*


@Dao
interface DaoItems {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert_update(taskItem: TaskItem)

    @Delete
    fun delete(taskItem: TaskItem)

    @Query("Select * from task_database")
    fun getdata(): MutableList<TaskItem>

    @Query("Delete from task_database")
    fun deleteall()


    @Query("Select * From task_database Where title Like '%' || :search || '%'")
    fun search(search: String): MutableList<TaskItem>

    @Query("Select * From task_database Order by title Asc")
    fun sortbynamAsc () : MutableList<TaskItem>

    @Query("Select * From task_database Order by title DESC")
    fun sortbynamDesc () : MutableList<TaskItem>

    @Query("Select * From task_database Order by done Asc,important Desc")
    fun important () : MutableList<TaskItem>

    @Query("Update task_database Set done = 1 Where id=:keyid")
   fun done(keyid: Int)

    @Query("Update task_database Set done = 3 Where id=:keyid")
    fun makeImportant(keyid: Int)


    @Query("Update task_database Set done = 0 Where id=:keyid")
    fun undone(keyid: Int)
}