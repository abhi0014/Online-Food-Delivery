package com.example.onlinefooddeliveryapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinefooddeliveryapp.Model.MenuItem
import com.example.onlinefooddeliveryapp.adapter.MenuAdapter
import com.example.onlinefooddeliveryapp.databinding.FragmentSearchBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var  adapter : MenuAdapter
    private lateinit var database: FirebaseDatabase
    private val originalMenuItems = mutableListOf<MenuItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater,container,false)
//        adapter = MenuAdapter(filterMenuFoodName,filterMenuItemPrice,filterMenuItemImage,requireContext())

        // retrive all item from database
        retirveMenuItem()

//       Setup for search View
        setupSearchView()

        return binding.root
    }

    private fun retirveMenuItem() {
        database = FirebaseDatabase.getInstance()
        val foodRef = database.reference.child("menu")

        foodRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children)
                {
                    val menuItem  = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let {
                        originalMenuItems.add(it)
                    }
                }
                showAllMenu()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun showAllMenu() {
        val filterMenuItem = ArrayList(originalMenuItems)
        setAdapter(filterMenuItem)
    }

    private fun setAdapter(filterMenuItem: List<MenuItem>) {
        adapter = MenuAdapter(filterMenuItem , requireContext())
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter
    }


    private fun setupSearchView(){
        binding.searchView2.setOnQueryTextListener(object: android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String): Boolean {
                filterMenuItems(p0)
                return true
            }

            override fun onQueryTextChange(p0: String): Boolean {
                filterMenuItems(p0)
                return true
            }
        })
    }

    private fun filterMenuItems(p0: String) {
        val filterMenuItems = originalMenuItems.filter {
            it.foodName?.contains(p0,ignoreCase = true) == true
        }
        setAdapter(filterMenuItems)
    }


}