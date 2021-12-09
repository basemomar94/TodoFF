package com.example.todoff.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoff.R
import com.example.todoff.data.DatabaseTasks
import com.example.todoff.data.TaskItem
import com.example.todoff.databinding.AddFragmentBinding

class AddFragment() : Fragment(R.layout.add_fragment) {

    var _binding: AddFragmentBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = AddFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            addtoDatabase()
        }








    }

    private fun addtoDatabase() {
        val model = TaskItem()
        var title: String = binding.itemET.text.toString()
        var important: Int = 0
        if (binding.AddCheckbox.isChecked) {
            important = 1
        }
        if (binding.itemET.text.isNotEmpty()) {
            model.title = title
            model.important = important
            val db = DatabaseTasks.getInstance(context)
            DatabaseTasks.db_write.execute {
                db.daoitems().insert_update(model)
                binding.itemET.text.clear()
                activity?.runOnUiThread {
                    Toast.makeText(context,"item has been added",Toast.LENGTH_LONG).show()
                    val action= AddFragmentDirections.actionAddFragmentToTasksFragment()
                    findNavController().navigate(action)
                }






            }

        } else {

            Toast.makeText(context,"Enter an item",Toast.LENGTH_LONG).show()

        }
    }


}