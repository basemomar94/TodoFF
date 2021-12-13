package com.basem.todoff.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.Executors

const val DATABASE_NAME = "Tasks_Database"

@Database(entities = [TaskItem::class], version = 1, exportSchema = false)
abstract class DatabaseTasks : RoomDatabase() {

    abstract fun daoitems(): DaoItems

    companion object {

        private var INSTANCE: DatabaseTasks? = null
        val db_write = Executors.newFixedThreadPool(4)

        fun getInstance(context: Context?): DatabaseTasks {

            val tempINSTANCE = INSTANCE
            if (tempINSTANCE != null) {
                return tempINSTANCE
            }

            kotlin.synchronized(this) {

                val instance = Room.databaseBuilder(
                    context!!?.applicationContext,
                    DatabaseTasks::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance

            }


        }


    }


}