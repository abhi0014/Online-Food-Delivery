package com.example.onlinefooddeliveryapp.Fragments

import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.onlinefooddeliveryapp.MenuBottomSheetFragment
import com.example.onlinefooddeliveryapp.Model.MenuItem
import com.example.onlinefooddeliveryapp.R
import com.example.onlinefooddeliveryapp.adapter.MenuAdapter
import com.example.onlinefooddeliveryapp.adapter.PopularAdapter
import com.example.onlinefooddeliveryapp.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewAllMenu.setOnClickListener {
            val bottomSheetDialog = MenuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager, "Test")
        }


//        Retrive And Display Popular Menu Item

        retriveAndDisplayPopularItem()
        return binding.root


    }

    private fun retriveAndDisplayPopularItem() {

        database = FirebaseDatabase.getInstance()
        val foodRef = database.reference.child("menu")
        menuItems = mutableListOf()

        //retrive menu item from database

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
//                Display randomly popular Item
                randomPopularItem()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun randomPopularItem() {
        // create a shuffled list of menu Item

        val index = menuItems.indices.toList().shuffled()
        val numItemToShow = 6

        val subsetMenuItem = index.take(numItemToShow).map { menuItems[it] }


        setPopularAdapter(subsetMenuItem)

    }

    private fun setPopularAdapter(subsetMenuItem: List<MenuItem>) {
        val adapter = MenuAdapter(subsetMenuItem, requireContext())
        binding.homeFragmentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.homeFragmentRecyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
            }

            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMessage = "Selected Image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })


    }


}