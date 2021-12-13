package com.example.todoff.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoff.R
import com.example.todoff.adapter.TasksAdpater
import com.example.todoff.data.DatabaseTasks
import com.example.todoff.data.TaskItem
import com.example.todoff.databinding.TasksFragmentBinding
import com.google.android.material.snackbar.Snackbar

class TasksFragment() : Fragment(R.layout.tasks_fragment), SearchView.OnQueryTextListener,
    TasksAdpater.Myclicklisener {
    lateinit var adpater: TasksAdpater
    var _binding: TasksFragmentBinding? = null
    val binding get() = _binding!!
    var taskList: ArrayList<TaskItem>? = null
    var query: String? = null
    var position: Int? = null
    var realDelete: Boolean = true
    var tasksAdpater: TasksAdpater? = null
    var tempList: ArrayList<TaskItem>? = null
    var snackbar: Snackbar? = null
    lateinit var mlist: ArrayList<TaskItem>
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
           /* R.id.order -> {
                showOrderMenue()

                when (item.itemId) {
                    R.id.nameUp -> ordernameD()

                    R.id.namdeD -> ordernameUp()

                }

                return true
            }*/
            R.id.nameUp -> {
                ordernameD()
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
        _binding = TasksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

/////////////floating button /////////////////////////////////////
        binding.floatingActionButton2.setOnClickListener {
            println("float")
            searchData("b")
            val action = TasksFragmentDirections.actionTasksFragmentToAddFragment()
            findNavController().navigate(action)
            update(1)

        }
        getData()


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

                    fakeDelete(viewHolder.absoluteAdapterPosition)


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
        var db = DatabaseTasks.getInstance(context)
        DatabaseTasks.db_write.execute {
            mlist = db.daoitems().important() as ArrayList<TaskItem>
            taskList = mlist
            activity?.runOnUiThread {
                recycleSetup(mlist)
            }
        }
    }


////////////////////Recycle setup/////////////////////////////////////////////////////////////////////////////////

    fun recycleSetup(list: ArrayList<TaskItem>) {
        binding.taskRecycle.layoutManager = LinearLayoutManager(context)
        tasksAdpater = TasksAdpater(list,this)
        binding.taskRecycle.adapter = tasksAdpater
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
                    activity?.runOnUiThread {
                        getData()

                    }
                }
            }
            .setNegativeButton("Cancel") { dialog, id -> dialog.dismiss() }
        val alert = builder.create()
        alert.show()


    }
///////////////// Search /////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onQueryTextSubmit(query: String): Boolean {


        TODO("Not yet implemented")
    }

    override fun onQueryTextChange(newText: String): Boolean {

        searchData(newText)
        return false

        TODO("Not yet implemented")
    }

    fun searchData(query: String) {
        tempList?.clear()
        val db = DatabaseTasks.getInstance(context)
        DatabaseTasks.db_write.execute {
            mlist = db.daoitems().search(query) as ArrayList<TaskItem>
            activity?.runOnUiThread {
                recycleSetup(mlist)
            }


        }

    }

  /*  fun showOrderMenue() {

        var view: View = activity!!.findViewById(R.id.order)
        val popupMenu: PopupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.order_menue, popupMenu.menu)
        popupMenu.show()
    }*/

    fun ordernameUp() {
        println("order up")
        val db = DatabaseTasks.getInstance(context)
        DatabaseTasks.db_write.execute {
            mlist = db.daoitems().sortbynamAsc() as ArrayList<TaskItem>
            activity?.runOnUiThread {
                recycleSetup(mlist)
            }


        }
    }

    fun ordernameD() {
        val db = DatabaseTasks.getInstance(context)
        DatabaseTasks.db_write.execute {
            mlist = db.daoitems().sortbynamDesc() as ArrayList<TaskItem>
            activity?.runOnUiThread {
                recycleSetup(mlist)
            }


        }
    }


/////////////////Fake delete one item/////////////////////////////////////////////////////////////////////////////////

    fun fakeDelete(position: Int) {
        val item = taskList?.get(position)
        taskList?.remove(item)
        recycleSetup(taskList!!)


        showSnack()


    }
/////////////////Real delete one item/////////////////////////////////////////////////////////////////////////////////

    fun deleteOneItem(position: Int) {
        var db = DatabaseTasks.getInstance(context)
        DatabaseTasks.db_write.execute {
            mlist = db.daoitems().important() as ArrayList<TaskItem>
            val item = mlist[position]
            db.daoitems().delete(item)
        }
    }
/////////////////Snack bar/////////////////////////////////////////////////////////////////////////////////

    fun showSnack() {
        snackbar?.dismiss()

        snackbar = Snackbar.make(
            binding.tasksLayout,
            "you have just deleted an item",
            Snackbar.LENGTH_LONG
        ).addCallback(object : Snackbar.Callback() {

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                //deleteOneItem(position!!)
                println("dismissed")
                println(realDelete)
                if (realDelete) {
                    deleteOneItem(position!!)
                } else {
                    realDelete = true
                }

            }

            override fun onShown(sb: Snackbar?) {
                super.onShown(sb)

            }
        })


        snackbar!!.setAction("Undo", View.OnClickListener {
            getData()
            realDelete = false

        })



        snackbar!!.view.setPadding(0,0,0,0)

        snackbar!!.show()

    }

    override fun onClick(position: Int) {
       update(position)

    }
    fun update(p :Int){
        val db = DatabaseTasks.getInstance(context)
        DatabaseTasks.db_write.execute {
         //   db.daoitems().update()
            activity?.runOnUiThread {
                getData()
            }
        }
    }


}