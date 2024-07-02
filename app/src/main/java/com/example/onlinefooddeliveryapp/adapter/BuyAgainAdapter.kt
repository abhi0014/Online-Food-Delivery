package com.example.onlinefooddeliveryapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.onlinefooddeliveryapp.databinding.BuyAgainItemBinding

class BuyAgainAdapter(private val buyAgainFoodName: List<String>, private val buyAgainFoodPrice: List<String>,
                      private val buyAgainImage: List<Int>): RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
       val binding=BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BuyAgainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return buyAgainFoodName.size
    }
    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
        holder.bind(buyAgainFoodName[position],buyAgainFoodPrice[position],buyAgainImage[position])
    }

    inner class BuyAgainViewHolder(private val binding: BuyAgainItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(foodname: String, foodprice: String, foodimage: Int) {
            binding.buyagainFoodName.text = foodname
            binding.buyagainFoodPrice.text = foodprice
            binding.buyagainImageView.setImageResource(foodimage)
        }

    }

    }


