package com.basem.todoff.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basem.todoff.R
import com.basem.todoff.adapter.TasksAdpater
import com.basem.todoff.data.DatabaseTasks
import com.basem.todoff.data.TaskItem
import com.basem.todoff.databinding.TasksFragmentBinding
import com.google.android.material.snackbar.Snackbar

class TasksFragment() : Fragment(R.layout.tasks_fragment), SearchView.OnQueryTextListener,
    TasksAdpater.Myclicklisener {
    var _binding: TasksFragmentBinding? = null
    val binding get() = _binding!!
    var position: Int? = null
    var tasksAdpater: TasksAdpater? = null
    private var recyclerView: RecyclerView? = null
    var tempList: MutableList<TaskItem>? = null
    var sortedList: MutableList<TaskItem> = mutableListOf()
    var mlist: MutableList<TaskItem> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)


    }


    /////////////////////////////////////AppBar Icons ////////////////////////////////////////////////////////////////////////////
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.app_icons, menu)
        val search = menu.findItem(R.id.search)
        val searchView = search.actionView as SearchView
        searchView.setOnQueryTextListener(this)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                deleteall()
                return true
            }
            R.id.nameUp -> {
                return true
            }
            else -> {
                println("hi")
                return true
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = "Todo"

        _binding = TasksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

/////////////floating button /////////////////////////////////////
        binding.floatingActionButton2.setOnClickListener {
            mlist.clear()

            val action = TasksFragmentDirections.actionTasksFragmentToAddFragment()
            findNavController().navigate(action)


        }
         getData()
        recycleSetup(mlist)


        //////////////////SWIPE SETUP////////////////////////////////////////////////////////////////////////////////////////////

        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false

                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    //  snackbar?.dismiss()

                    position = viewHolder.absoluteAdapterPosition
                    deleteOneItem(viewHolder.absoluteAdapterPosition)
                }
            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.taskRecycle)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    /////////////////Getting Data/////////////////////////////////////////////////////////////////////////////////
    fun getData() {
        var dataList: MutableList<TaskItem>
        dataList = arrayListOf()
        var db = DatabaseTasks.getInstance(context)
        DatabaseTasks.db_write.execute {
            dataList = db.daoitems().important()
            dataList.forEach {
                mlist.add(it)
                tasksAdpater?.notifyDataSetChanged()
            }

        }

    }


////////////////////Recycle setup/////////////////////////////////////////////////////////////////////////////////

    fun recycleSetup(list: MutableList<TaskItem>) {
        tasksAdpater = TasksAdpater(list, this)
        recyclerView = view?.findViewById(R.id.taskRecycle)
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tasksAdpater
            setHasFixedSize(true)
        }
    }


/////////////////delete all items/////////////////////////////////////////////////////////////////////////////////

    fun deleteall() {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Are you sure you want to delete all the items permanently")
            .setCancelable(true)
            .setPositiveButton("Yes") { dialog, id ->
                val db = DatabaseTasks.getInstance(context)
                DatabaseTasks.db_write.execute {
                    db.daoitems().deleteall()
                    mlist.clear()
                    activity?.runOnUiThread {

                        tasksAdpater?.notifyDataSetChanged()

                    }
                }
            }
            .setNegativeButton("Cancel") { dialog, id -> dialog.dismiss() }
        val alert = builder.create()
        alert.show()


    }
///////////////// Search /////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onQueryTextSubmit(query: String): Boolean {

        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {

        searchData(newText)
        return false

    }

    fun searchData(query: String) {
        /* tempList?.clear()
         val db = DatabaseTasks.getInstance(context)
         DatabaseTasks.db_write.execute {
             val searchList = db.daoitems().search(query)

             activity?.runOnUiThread {
                 tasksAdpater?.addlist(searchList)
             }


         } */
        val searchList: MutableList<TaskItem>
        searchList = arrayListOf()
        mlist.forEach {
            if (it.title!!.contains(query)) {
                searchList.add(it)
            }
        }
        tasksAdpater?.addlist(searchList)

    }

/////////////////Real delete one item/////////////////////////////////////////////////////////////////////////////////

    fun deleteOneItem(position: Int) {

        var db = DatabaseTasks.getInstance(context)
        DatabaseTasks.db_write.execute {
            val todo = mlist[position]
            if (todo!!.done == 1) {
                if (mlist.isNotEmpty()) {
                    db.daoitems().delete(todo!!)
                    mlist.remove(todo)
                    activity?.runOnUiThread {
                        tasksAdpater?.notifyItemRemoved(position)
                        Snackbar.make(requireView(), "todo deleted", Snackbar.LENGTH_LONG).apply {
                            setAction("Undo") {
                                saveItem(todo, position)
                            }
                        }.show()
                    }

                }
            } else {
                activity?.runOnUiThread {
                    tasksAdpater?.notifyDataSetChanged()
                    Snackbar.make(requireView(), "Make it done first", Snackbar.LENGTH_SHORT)
                        .show()
                }

            }


        }
    }

    private fun saveItem(item: TaskItem, position: Int) {
        val db = DatabaseTasks.getInstance(context)
        DatabaseTasks.db_write.execute {
            db.daoitems().insert_update(item)
            mlist.add(position, item)

            activity?.runOnUiThread {
                tasksAdpater?.notifyItemInserted(position)
            }
        }
    }
/////////////////Snack bar/////////////////////////////////////////////////////////////////////////////////

    override fun onClick(position: Int) {
        update(position)
    }

    fun update(p: Int) {
        val done = mlist[p]!!.done
        val key = mlist[p]!!.id
        val todo = mlist[p]
        val db = DatabaseTasks.getInstance(context)
        DatabaseTasks.db_write.execute {
            if (done == 0) {
                db.daoitems().done(key)
                todo.done = 1

            } else {
                db.daoitems().undone(key)
                todo.done = 0
            }
            activity?.runOnUiThread {
                tasksAdpater?.notifyDataSetChanged()


            }
        }
    }


}