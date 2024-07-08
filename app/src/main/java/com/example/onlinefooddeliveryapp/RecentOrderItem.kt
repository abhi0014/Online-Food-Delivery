package com.example.onlinefooddeliveryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinefooddeliveryapp.Model.OrderDetails
import com.example.onlinefooddeliveryapp.adapter.RecentBuyAdapter
import com.example.onlinefooddeliveryapp.databinding.ActivityRecentOrderItemBinding

class RecentOrderItem : AppCompatActivity() {

    private lateinit var binding : ActivityRecentOrderItemBinding

    private lateinit var allFoodNames:ArrayList<String>
    private lateinit var allFoodPrices:ArrayList<String>
    private lateinit var allFoodImages:ArrayList<String>
    private lateinit var allFoodQuantities:ArrayList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecentOrderItemBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val recentOrderItems = intent.getSerializableExtra("RecentBuyOrderItem") as ArrayList<OrderDetails>
        recentOrderItems?.let { orderDetails ->
            if (orderDetails.isNotEmpty())
            {
                val recentOrderItem = orderDetails[0]

                allFoodNames = recentOrderItem.foodName as ArrayList<String>
                allFoodPrices = recentOrderItem.foodPrice as ArrayList<String>
                allFoodImages = recentOrderItem.foodImage as ArrayList<String>
                allFoodQuantities = recentOrderItem.foodQuantities as ArrayList<Int>

            }

        }
        setAdapter()
    }

    private fun setAdapter() {
        val rv = binding.recentOrderItemRecyclerView
        rv.layoutManager= LinearLayoutManager(this)
        val adapter = RecentBuyAdapter(this,allFoodNames,allFoodPrices,allFoodImages,allFoodQuantities)
        rv.adapter = adapter
    }
}