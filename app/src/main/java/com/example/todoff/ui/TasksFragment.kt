package com.example.todoff.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Adapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoff.R
import androidx.appcompat.widget.SearchView;

import com.example.todoff.adapter.TasksAdpater
import com.example.todoff.data.DatabaseTasks
import com.example.todoff.data.TaskItem
import com.example.todoff.databinding.TasksFragmentBinding

class TasksFragment() : Fragment(R.layout.tasks_fragment), SearchView.OnQueryTextListener {
    lateinit var adpater: TasksAdpater
    var _binding: TasksFragmentBinding? = null
    val binding get() = _binding!!
    var taskList: List<TaskItem>? = null
    var query: String? = null
    var tempList: ArrayList<TaskItem>? = null
    lateinit var mlist: ArrayList<TaskItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.app_icons, menu)
        val search = menu.findItem(R.id.search)
        val searchView = search.actionView as SearchView
        //searchView.isSubmitButtonEnabled=true
        searchView.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                deleteall()

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
        binding.floatingActionButton2.setOnClickListener {
            println("float")
            searchData("b")
            val action = TasksFragmentDirections.actionTasksFragmentToAddFragment()
            findNavController().navigate(action)

            binding.test.setOnClickListener {
                println("button")

                searchData("ggg")


            }


        }

        getData()


        //  binding.taskRecycle.layoutManager=LinearLayoutManager(context)
        // binding.taskRecycle.adapter=TasksAdpater(taskList!!)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun getData() {
        var db = DatabaseTasks.getInstance(context)
        DatabaseTasks.db_write.execute {
            mlist = db.daoitems().getdata() as ArrayList<TaskItem>
            taskList = mlist

            activity?.runOnUiThread {
                recycleSetup(mlist)

            }

        }

    }

    fun recycleSetup(list: ArrayList<TaskItem>) {
        binding.taskRecycle.layoutManager = LinearLayoutManager(context)
        binding.taskRecycle.adapter = TasksAdpater(list)

    }

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
            println(mlist!!.size)

            activity?.runOnUiThread {

                recycleSetup(mlist)
                println(mlist)

            }

          /*  activity?.runOnUiThread {

                mlist!!.forEach {

                    if (it.title!!.contains(query)) {

                        tempList?.add(it)
                        recycleSetup(tempList!!)


                    }

                }

                // recycleSetup(mlist!!)

            }*/
        }

    }

    fun searchTest(query: String) {

    }


}