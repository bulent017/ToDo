package com.bulentoral.todoappstajproject.View

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bulentoral.todoappstajproject.Model.TaskData
import com.bulentoral.todoappstajproject.R
import com.bulentoral.todoappstajproject.adapter.CompletedAdapter
import com.bulentoral.todoappstajproject.adapter.TaskAdapter
import com.bulentoral.todoappstajproject.databinding.FragmentCompletedTaskBinding
import com.bulentoral.todoappstajproject.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FragmentCompletedTask : Fragment(), CompletedAdapter.OnItemClickListener {
    private lateinit var database: DatabaseReference
    private lateinit var listOfCompletedTask :ArrayList<TaskData>
    private var _binding: FragmentCompletedTaskBinding? = null
    private lateinit var dataAdapter: CompletedAdapter
    private val binding get() = _binding!!
    private lateinit var builder: AlertDialog.Builder
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCompletedTaskBinding.inflate(inflater, container, false)
        listOfCompletedTask = ArrayList()

        readDataFromFirebase()
        binding.toolbar.setOnClickListener {
            requireActivity().onBackPressed()
        }
//        binding.toolbar.setOnMenuItemClickListener{
//            when(it.itemId){
//                R.id.deleteAll -> {
//                    val uid = FirebaseAuth.getInstance().currentUser?.uid
//                    database = Firebase.database.reference
//                    FirebaseDatabase.getInstance().getReference("user").child(uid!!).child("Task")
//
//                    true
//                }
//                else->{ false }
//
//            }
//
//
//        }

        binding.apply {
            dataAdapter = CompletedAdapter(listOfCompletedTask,this@FragmentCompletedTask)
            recyclerviewCompletedTask.adapter = dataAdapter
            recyclerviewCompletedTask.setHasFixedSize(true)
            recyclerviewCompletedTask.layoutManager = LinearLayoutManager(requireContext())

        }

        return binding.root
    }


    private fun readDataFromFirebase(){
        //var auth = FirebaseAuth.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        database = FirebaseDatabase.getInstance().getReference("user").child(uid!!).child("Task")

        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    listOfCompletedTask.clear()// clear yapmayınca 2 ye katlıyor
                    for (taskSnapshot in snapshot.children){
                        val task = taskSnapshot.getValue<TaskData>()
                        if (task != null) {

                            task.id = taskSnapshot.key
                            if (task.completed){
                                listOfCompletedTask.add(task)
                            }


                            //Toast.makeText(requireContext(),task.completed.toString(),Toast.LENGTH_LONG).show()

                        }
                    }
                    dataAdapter.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onItemClick(id: String?) {
        database = Firebase.database.reference
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        println("uid: "+uid)
        builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete").setMessage("Do you want to delete task ?").
        setCancelable(true).
        setPositiveButton("Yes")
        {dialoginterface,it ->
            if (uid != null) {
                //database.child("user").child(uid).child("Task").child(id!!).removeValue()
               if (id!= null){
                   database.child("user").child(uid).child("Task").child(id).removeValue()

               }

            }
        }.setNegativeButton("No"){dialoginterface,it -> dialoginterface.cancel()}.show()


    }

    override fun onItemLongClick(id: String?) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            database.child("user").child(uid).child("Task").child(id!!).removeValue()
        }
        println("girdi")

    }

    // swipe kullanarak silme işlemi yaptık fakat etkisiz.
   /* override fun onItemClick(id: String?) {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                val position = viewHolder.adapterPosition
                database = Firebase.database.reference
                if (direction == ItemTouchHelper.LEFT) {
                    if (uid != null) {
                        database.child("user").child(uid).child("Task").child(id!!).removeValue()
                    }
                }
//                else if (direction == ItemTouchHelper.RIGHT) {
//                    GlobalScope.launch(Dispatchers.IO) {
//                        db.todoDao().finishTask(adapter.getItemId(position))
//                    }
//                }
            }
            override fun onChildDraw(
                canvas: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView

                    val paint = Paint()
                    val icon: Bitmap

                    if (dX < 0) {

                        icon = BitmapFactory.decodeResource(resources, R.mipmap.ic_delete_white_png)

                        paint.color = Color.parseColor("#D32F2F")

                        canvas.drawRect(
                            itemView.right.toFloat() + dX, itemView.top.toFloat(),
                            itemView.right.toFloat(), itemView.bottom.toFloat(), paint
                        )

                        canvas.drawBitmap(
                            icon,
                            itemView.right.toFloat() - icon.width,
                            itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height.toFloat()) / 2,
                            paint
                        )


                    }
                    viewHolder.itemView.translationX = dX


                } else {
                    super.onChildDraw(
                        canvas,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }

        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerviewCompletedTask)
    }*/

}