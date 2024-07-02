package com.example.onlinefooddeliveryapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.onlinefooddeliveryapp.Details_Activity
import com.example.onlinefooddeliveryapp.databinding.MenuItemBinding

class MenuAdapter(private val menuItems:MutableList<String>,private val menuItemPrice:MutableList<String>,private val menuItemImage:MutableList<Int>,private val requireContext : Context): RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private val itemClickListener: OnClickListener ?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuAdapter.MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }
    inner class MenuViewHolder(private val binding:MenuItemBinding) :RecyclerView.ViewHolder(binding.root) {
        init{
            binding.root.setOnClickListener{
                val position = adapterPosition
                if(position!= RecyclerView.NO_POSITION) {

                    itemClickListener?.onItemClick(position)
                }
                    //                set on clickListener to open details

                    val intent = Intent(requireContext,Details_Activity::class.java)
                    intent.putExtra("MenuItemName", menuItems[position])
                    intent.putExtra("menuItemImage", menuItemImage[position])

                    requireContext.startActivity(intent)

            }
        }
        fun bind(position: Int) {
            binding.apply {

                menuFoodName.text = menuItems[position]
                menuPrice.text = menuItemPrice[position]
                menuImage.setImageResource( menuItemImage[position] )



            }
        }

    }
}

private fun OnClickListener.onItemClick(position: Int) {
    TODO("Not yet implemented")
}
