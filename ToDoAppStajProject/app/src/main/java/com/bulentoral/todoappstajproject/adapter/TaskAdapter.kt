package com.bulentoral.todoappstajproject.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bulentoral.todoappstajproject.Model.TaskData
import com.bulentoral.todoappstajproject.databinding.ItemTodoBinding

class TaskAdapter(private var listOfTask: ArrayList<TaskData>, private val listener: OnItemClickListener) : RecyclerView.Adapter<TaskAdapter.TaskHolder>() {


    interface OnItemClickListener {
        fun onItemClick(id:String?,title: String?,task:String?,date:String?)
        fun onItemClickCheckBox(id:String?, isCompleted: Boolean?)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredList(filteredList: ArrayList<TaskData>){ // search yapmamızı sağlıyor
        listOfTask = filteredList
        notifyDataSetChanged()
    }


    inner class TaskHolder(private val itemBinding: ItemTodoBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(position: Int){
            val item = listOfTask[position]
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
                    listOfTask[adapterPosition].id,
                    listOfTask[adapterPosition].title,
                    listOfTask[adapterPosition].task,
                    listOfTask[adapterPosition].date
                )
            }
            itemBinding.checkbox.setOnClickListener {

                listener.onItemClickCheckBox(
                    listOfTask[adapterPosition].id,
                    isCompleted = itemBinding.checkbox.isChecked

                )

//                val id=listOfTask[adapterPosition].id
//                val isCompleted = itemBinding.checkbox.isChecked
//                val uid = FirebaseAuth.getInstance().currentUser?.uid
//                val database = FirebaseDatabase.getInstance().getReference("user")
//                if (uid != null) {
//                    if (id != null) {
//                        database.child(uid).child("Task").child(id).child("completed").setValue(isCompleted)
//                        //Toast.makeText(requireContext(),id + "\n "+isCompleted,Toast.LENGTH_SHORT).show()
//                    }
//                }
//
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(position)

    }
    override fun getItemCount(): Int {
        return listOfTask.size
    }

//    inline var TextView.strike: Boolean
//        set(visible) {
//            paintFlags = if (visible) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//            else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
//        }
//        get() = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG == Paint.STRIKE_THRU_TEXT_FLAG
//
//    fun  strike(completed: Boolean,textView: TextView){
//        if (completed){
//            textView.paintFlags =textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//        }
//        else{
//            textView.paintFlags =textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG.inc()
//        }
//    }

}


