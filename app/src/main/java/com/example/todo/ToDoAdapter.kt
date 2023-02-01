package com.example.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//Created to make views in our app and set the content of our items in the right order
class ToDoAdapter (
    var todo : List<DataToDo>
    //this will now take this data to set everything
        ) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>(){

    //Creating an on click listener for the checkBox that the DataTodo can be accessed
    var itemOnClick : ((DataToDo) -> Unit)? = null


    //Every Adapter of the recyclerview must hold an inner class which will be the view holder class
    inner class ToDoViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val itemText: TextView = itemView.findViewById(R.id.itemText)
        var itemCheckBox: CheckBox = itemView.findViewById(R.id.itemCheckBox)
    }
    //Now this class need a data class to get the data that our view will look like

    //We can use CTRL i to get the necessary methods that we will override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        //this is called when we scroll to get a new item in the recycler view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return ToDoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        //this basically binds our data to the corresponding view
        holder.apply {
            itemText.text = todo[position].item
            itemCheckBox.isChecked = todo[position].checkBox

            itemCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (itemCheckBox.isChecked == isChecked) {
                    todo[position].checkBox = isChecked

                }else {
                    todo[position].checkBox = isChecked

                }
            }

            //Making an invocation for the checkBox on click listener.
            holder.itemCheckBox.setOnClickListener {
                itemOnClick?.invoke(DataToDo(itemText.text.toString() , itemCheckBox.isChecked))
            }

        }
    }

    override fun getItemCount(): Int {
        //returns how many items we have in the recycler view
        return todo.size
    }
}