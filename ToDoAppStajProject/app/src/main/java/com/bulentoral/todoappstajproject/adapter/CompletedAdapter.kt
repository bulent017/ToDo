package com.bulentoral.todoappstajproject.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bulentoral.todoappstajproject.Model.TaskData
import com.bulentoral.todoappstajproject.databinding.ItemTodoCompletedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CompletedAdapter(private var listOfCompletedTask :ArrayList<TaskData>,private val listener: CompletedAdapter.OnItemClickListener): RecyclerView.Adapter<CompletedAdapter.CompletedHolder>() {
    interface OnItemClickListener {
        fun onItemClick(id:String?)
        fun onItemLongClick(id: String?)
    }


    inner class CompletedHolder(private val itemBinding: ItemTodoCompletedBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(position: Int){
            val item = listOfCompletedTask[position]
            item.apply {
                itemBinding.apply {
                    txtShowTitle.text = title
                    txtShowTask.text = task
                    txtShowDate.text = date
                    checkbox.isChecked = completed
                }
            }
        }
        init {
            itemView.setOnClickListener {//tüm cardview tıklanabilir oluyor
                listener.onItemClick(
                    listOfCompletedTask[adapterPosition].id,

                )
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedHolder {
        val binding = ItemTodoCompletedBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CompletedHolder(binding)
    }

    override fun onBindViewHolder(holder: CompletedHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return listOfCompletedTask.size
    }
}