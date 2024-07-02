package com.example.onlinefooddeliveryapp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.onlinefooddeliveryapp.Details_Activity
import com.example.onlinefooddeliveryapp.databinding.PopularitemBinding

class PopularAdapter (private val items:List<String>,private val price:List<String>,private val image:List<Int>,private val requireContext: Context): RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(PopularitemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }



    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val item = items[position]
        val images = image[position]
        Log.v("HELLOOOOOOO",position.toString())
        val prices = price[position]
        holder.bind(item,images,prices)

        holder.itemView.setOnClickListener{
            //                set on clickListener to open details

            val intent = Intent(requireContext, Details_Activity::class.java)
            intent.putExtra("MenuItemName",item)
            intent.putExtra("menuItemImage", images)

            requireContext.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
    class PopularViewHolder (private val binding: PopularitemBinding):RecyclerView.ViewHolder(binding.root) {
        private val imageView =binding.imageView5
        fun bind(item: String, images: Int, prices: String) {
            binding.foodNamePopular.text = item
            binding.pricePopular.text = prices
            imageView.setImageResource(images)
        }

    }
}