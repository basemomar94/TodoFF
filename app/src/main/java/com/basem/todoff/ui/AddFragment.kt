package com.basem.todoff.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.basem.todoff.R
import com.basem.todoff.data.DatabaseTasks
import com.basem.todoff.data.TaskItem
import com.basem.todoff.databinding.AddFragmentBinding

class AddFragment() : Fragment(R.layout.add_fragment) {

    var _binding: AddFragmentBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.show()


        _binding = AddFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.itemET.requestFocus()
        binding.itemET.showSoftInputOnFocus
        binding.itemET.showKeyboard()





        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

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
                    binding.floatingActionButton.hideKeyboard()


                    val action = AddFragmentDirections.actionAddFragmentToTasksFragment()
                    findNavController().navigate(action)

                }


            }

        } else {

            Toast.makeText(context, "Enter an item", Toast.LENGTH_LONG).show()

        }
    }

    private fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun View.hideKeyboard (){
        val imm= context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0)
    }


}