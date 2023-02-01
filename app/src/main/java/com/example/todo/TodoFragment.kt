package com.example.todo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.databinding.FragmentTodoBinding
import com.google.firebase.database.*
import java.util.*

class TodoFragment : Fragment() {

    private lateinit var binding: FragmentTodoBinding
    private lateinit var db : DatabaseReference
    private lateinit var todoList : MutableList<DataToDo >
    private lateinit var adapter : ToDoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTodoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //So finally this is where we create the ToDo list
        todoList = mutableListOf()
        db = FirebaseDatabase.getInstance().getReference("TodoList")

        //And here we create a todo adapter
        adapter = ToDoAdapter(todoList)
        binding.todoRecycler.setHasFixedSize(true)
        binding.todoRecycler.adapter = adapter
        binding.todoRecycler.layoutManager = LinearLayoutManager(requireContext()) //for display

        updateCheckBox()
        getTodoData()


        binding.todoButton.setOnClickListener {
            addToDOData()
        }
    }

    private fun getTodoData() {
        // Read from the database
        db.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.exists()) {
                    dataSnapshot.children.forEach{
                        val value = it.getValue(DataToDo::class.java)
                        if(value !in todoList) {
                            todoList.add(value!!)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }


            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.d("MainActivity", "Error: $error")
            }
        })
    } 

    private fun updateCheckBox() {
        adapter.itemOnClick = {
            Log.d("TodoFragment", it.checkBox.toString())
            val itemMap = mapOf(
                "item" to it.item,
                "checkBox" to it.checkBox
            )
            db.child(it.item.lowercase()).updateChildren(itemMap)
        }
    }

    private fun addToDOData() {
        val text = binding.todoText.editText?.text.toString()
        val todo = DataToDo(text.replaceFirstChar
        { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }, false)

        if (todo !in todoList && text != "") {
            //Adding to database
            db.child(text.lowercase()).setValue(todo)
                .addOnCompleteListener{
                    Toast.makeText(requireContext(), "Successful", Toast.LENGTH_SHORT).show()
                }
            binding.todoText.editText?.text?.clear()
        }
        else {
            Toast.makeText(requireContext(), "Fill in required Fields", Toast.LENGTH_SHORT).show()
        }
    }
}