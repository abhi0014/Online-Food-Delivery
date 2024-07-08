package com.example.onlinefooddeliveryapp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlinefooddeliveryapp.Details_Activity
import com.example.onlinefooddeliveryapp.Model.MenuItem
import com.example.onlinefooddeliveryapp.databinding.MenuItemBinding

class MenuAdapter(
    private val menuItems: List<MenuItem>,
    private val requireContext: Context
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuAdapter.MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }

    inner class MenuViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {

                    openDetailsActivity(position)
                }
            }
        }

//        set Data into recycler View item name ,price ,image
        fun bind(position: Int) {

            val item = menuItems[position]
            binding.apply {

                menuFoodName.text = item.foodName
                menuPrice.text = item.foodPrice
                val uri = Uri.parse(item.foodImage)

                Glide.with(requireContext).load(uri).into(menuImage)

            }
        }

    }

    private fun openDetailsActivity(position: Int) {
        val item = menuItems[position]
//        A intent to open details Activity and passing data
        val intent = Intent(requireContext, Details_Activity::class.java).apply {


            putExtra("MenuItemFoodName", item.foodName)
            putExtra("MenuItemFoodPrice", item.foodPrice)
            putExtra("MenuItemFoodDescription", item.foodDescription)
            putExtra("MenuItemFoodIngredients", item.foodIngredients)
            putExtra("MenuItemFoodImage", item.foodImage)
        }
        requireContext.startActivity(intent)
    }
}

