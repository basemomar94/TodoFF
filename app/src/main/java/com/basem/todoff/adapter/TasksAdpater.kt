package com.basem.todoff.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.basem.todoff.R
import com.basem.todoff.data.TaskItem

class TasksAdpater(
    private var mList: MutableList<TaskItem>,
    val listner: Myclicklisener,


    ) :
    RecyclerView.Adapter<TasksAdpater.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val title: TextView = itemView.findViewById(R.id.taskTV)
        val check: CheckBox = itemView.findViewById(R.id.checkItem)
        val important: ImageView = itemView.findViewById(R.id.importantImg)

        init {

            itemView.setOnClickListener {
                val position: Int = absoluteAdapterPosition
                listner.onClick(position)

            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.itme_task, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemview = mList[position]
        holder.title.text = itemview!!.title
        holder.check.isChecked = itemview.done == 1
        holder.important.isVisible = itemview.important == 1
        holder.important.isInvisible = itemview.important == 0
        holder.title.paint.isStrikeThruText = itemview.done == 1
        if (itemview.done == 1) {
            holder.title.setTextColor(Color.GRAY)
            holder.important.alpha = 0.4F
        } else {
            holder.title.setTextColor(Color.BLACK)
            holder.important.alpha = 1F
        }
    }

    override fun getItemCount(): Int {

        return mList.size
    }

    interface Myclicklisener {
        fun onClick(position: Int)
    }

    fun addlist(list: MutableList<TaskItem>) {
        mList = list
        notifyDataSetChanged()
    }


}