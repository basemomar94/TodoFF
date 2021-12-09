package com.example.todoff.data

import androidx.room.*
import com.example.todoff.data.TaskItem


@Dao
interface DaoItems {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
 fun insert_update (taskItem: TaskItem)

 @Delete
fun delete (taskItem: TaskItem)

@Query("Select * from task_database")
fun getdata () : List<TaskItem>

@Query("Delete from task_database")
fun deleteall()
}