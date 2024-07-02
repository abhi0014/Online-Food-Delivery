package com.example.onlinefooddeliveryapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinefooddeliveryapp.R
import com.example.onlinefooddeliveryapp.adapter.MenuAdapter
import com.example.onlinefooddeliveryapp.databinding.FragmentSearchBinding
import com.google.android.material.search.SearchView


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var  adapter : MenuAdapter
    private val originalMenuFoodName = listOf("Breakfast","Green Salad","Rolls","Tea","Ice Cream","Cup Cakes","BreakFast","Green Salad","Rolls")
    private val originalMenuItemPrice = listOf("$5","$6","$7","$5","$6","$7","$5","$6","$7")
    private val originalMenuImage = listOf(R.drawable.breakfast,R.drawable.green_salad,R.drawable.rolls,R.drawable.tea,R.drawable.ice_cream,R.drawable.cupcakes,R.drawable.breakfast,R.drawable.green_salad,R.drawable.rolls)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val filterMenuFoodName = mutableListOf<String>()
    private val filterMenuItemPrice = mutableListOf<String>()
    private val filterMenuItemImage = mutableListOf<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        adapter = MenuAdapter(filterMenuFoodName,filterMenuItemPrice,filterMenuItemImage,requireContext())

        binding.menuRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter

//       Setup for search View
        setupSearchView()
//        show all menu item
        showAllMenuItem()
        return binding.root
    }

    private fun showAllMenuItem() {

        filterMenuFoodName.clear()
        filterMenuItemPrice.clear()
        filterMenuItemImage.clear()


        filterMenuFoodName.addAll(originalMenuFoodName)
        filterMenuItemPrice.addAll(originalMenuItemPrice)
        filterMenuItemImage.addAll(originalMenuImage)

        adapter.notifyDataSetChanged()
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
        filterMenuFoodName.clear()
        filterMenuItemPrice.clear()
        filterMenuItemImage.clear()

        originalMenuFoodName.forEachIndexed{index, foodName ->
            if(foodName.contains(p0.toString(), ignoreCase = true)){
                filterMenuFoodName.add(foodName)
                filterMenuItemPrice.add(originalMenuItemPrice[index])
                filterMenuItemImage.add(originalMenuImage[index])
            }
        }
        adapter.notifyDataSetChanged()
    }

    companion object {

    }
}