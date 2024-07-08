package com.example.onlinefooddeliveryapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.onlinefooddeliveryapp.Model.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinefooddeliveryapp.adapter.MenuAdapter
import com.example.onlinefooddeliveryapp.databinding.FragmentMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MenuBottomSheetFragment : BottomSheetDialogFragment (){
    private  lateinit var binding:FragmentMenuBottomSheetBinding

    private lateinit var database : FirebaseDatabase
    private lateinit var menuItems : MutableList<MenuItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBottomSheetBinding.inflate(inflater,container,false)
        binding.backButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retriveMenuItems()
//        val adapter = MenuAdapter(ArrayList(menufoodname),ArrayList(menuitemprice),ArrayList(menuimage),requireContext())

    }

    private fun retriveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef = database.reference.child("menu")

        menuItems = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for(foodSnapshot in snapshot.children)
                {
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it)  }
                }

//                Once Data recieve set To adapter
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun setAdapter() {
        val adapter = MenuAdapter(menuItems,requireContext())
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter
    }

    companion object {

    }
}