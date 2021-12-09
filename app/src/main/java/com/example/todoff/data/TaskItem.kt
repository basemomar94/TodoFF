package com.example.todoff.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Task_Database")
class TaskItem(


) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var title: String?=null
    var important: Int = 0
    var done: Int = 0
}
