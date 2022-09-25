package com.bulentoral.todoappstajproject.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*

import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bulentoral.todoappstajproject.Model.TaskData
import com.bulentoral.todoappstajproject.R
import com.bulentoral.todoappstajproject.adapter.CompletedAdapter
import com.bulentoral.todoappstajproject.adapter.TaskAdapter
import com.bulentoral.todoappstajproject.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), TaskAdapter.OnItemClickListener {

    private lateinit var database: DatabaseReference
    private lateinit var searchView : SearchView
    private lateinit var dataAdapter: TaskAdapter

    private lateinit var listOfTask : ArrayList<TaskData>
    private lateinit var listOfCompletedTask :ArrayList<TaskData>
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listOfCompletedTask = ArrayList()
        listOfTask = ArrayList()
        readDataFromFirebase()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.apply {
            toolbar.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.action_done -> {findNavController().navigate(R.id.action_homeFragment_to_fragmentCreateTask)
                    true
                    }
                    R.id.checklist -> {findNavController().navigate(R.id.action_homeFragment_to_fragmentCompletedTask)
                        true
                    }
                    else -> {false}
                }
            }
            dataAdapter = TaskAdapter(listOfTask,this@HomeFragment)

            recyclerview.adapter = dataAdapter
            recyclerview.setHasFixedSize(true)
            recyclerview.layoutManager = LinearLayoutManager(requireContext())


        }
        searchView = binding.searchTask
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filteredList(newText)
                }
                return  true
            }

        })


        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun readDataFromFirebase(){
        //var auth = FirebaseAuth.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        database = FirebaseDatabase.getInstance().getReference("user").child(uid!!).child("Task")

        database.addValueEventListener(object :ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    listOfTask.clear()// clear yapmayınca 2 ye katlıyor
                    for (taskSnapshot in snapshot.children){
                        val task = taskSnapshot.getValue<TaskData>()
                        if (task != null) {

                            task.id = taskSnapshot.key
                            if (!task.completed){
                                listOfTask.add(task)
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


    fun filteredList( text: String){
        var filteredList:ArrayList<TaskData>  = ArrayList()
        for (item:TaskData in listOfTask){
            if (item.title!!.lowercase(Locale.getDefault()).contains(text.lowercase()) || item.task!!.lowercase(
                    Locale.getDefault()).contains(text.lowercase()) ){
                filteredList.add(item)
            }
            if (filteredList.isEmpty()){
                //Toast.makeText(requireContext(),"No data Found",Toast.LENGTH_SHORT).show()
            }
            dataAdapter.setFilteredList(filteredList)
        }

    }



    override fun onItemClick(id: String?,title: String?,task:String?,date:String?) {
        //Toast.makeText(requireContext(),id + "\n"+title +"\n"+ task,Toast.LENGTH_SHORT).show()
        if(id!= null && title!= null && task!= null && date != null){
            val data : Array<String> = arrayOf(id, title, task,date)
            val action =    HomeFragmentDirections.actionHomeFragmentToFragmentUpdateTask2(data)
            findNavController().navigate(action)
        }
        else{
            print("dostum burası boş geldi")
        }
        //findNavController().navigate(R.id.action_homeFragment_to_fragmentUpdateTask2)
    }

    override fun onItemClickCheckBox(id: String?, isCompleted: Boolean?) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        database = FirebaseDatabase.getInstance().getReference("user")
        if (uid != null) {
            if (id != null) {
                database.child(uid).child("Task").child(id).child("completed").setValue(isCompleted)
                //Toast.makeText(requireContext(),id + "\n "+isCompleted,Toast.LENGTH_SHORT).show()
            }
        }


    }


}