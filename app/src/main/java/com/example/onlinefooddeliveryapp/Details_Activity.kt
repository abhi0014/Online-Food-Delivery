package com.example.onlinefooddeliveryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.onlinefooddeliveryapp.databinding.ActivityDetailsBinding

class Details_Activity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val foodName = intent.getStringExtra("MenuItemName")
        val foodImage = intent.getIntExtra("menuItemImage",0)

        binding.detailsFoodName.text = foodName
        binding.DetailsFoodImage.setImageResource(foodImage)
        binding.imageButton.setOnClickListener{
            finish()
        }

    }
}