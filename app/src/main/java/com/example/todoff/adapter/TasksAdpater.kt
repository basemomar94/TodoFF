package com.example.todoff.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.todoff.R
import com.example.todoff.data.TaskItem

class TasksAdpater(private val mList: ArrayList<TaskItem>) :
    RecyclerView.Adapter<TasksAdpater.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {




        val title: TextView = itemView.findViewById(R.id.taskTV)
        val check: CheckBox = itemView.findViewById(R.id.checkItem)
        val important: ImageView = itemView.findViewById(R.id.importantImg)

        init {
            itemView.setOnClickListener {
             itemView.setOnClickListener(this)
            }
        }

        override fun onClick(p0: View?) {
            println("work")

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.itme_task, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsList = mList[position]
        holder.title.text = itemsList.title
        holder.check.isChecked = itemsList.done == 1
        holder.important.isVisible = itemsList.important == 1
        holder.important.isInvisible = itemsList.important == 0
        holder.title.paint.isStrikeThruText = itemsList.done == 1


    }

    override fun getItemCount(): Int {

        return mList.size
    }


}