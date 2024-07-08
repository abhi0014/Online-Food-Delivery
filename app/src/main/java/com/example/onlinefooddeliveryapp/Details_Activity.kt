package com.example.onlinefooddeliveryapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.onlinefooddeliveryapp.Model.CartItem
import com.example.onlinefooddeliveryapp.databinding.ActivityDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Details_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private var foodName: String? = null
    private var foodPrice: String? = null
    private var foodDescription: String? = null
    private var foodIngredients: String? = null
    private var foodImage: String? = null

    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        foodName = intent.getStringExtra("MenuItemFoodName")
        foodPrice = intent.getStringExtra("MenuItemFoodPrice")
        foodDescription = intent.getStringExtra("MenuItemFoodDescription")
        foodIngredients = intent.getStringExtra("MenuItemFoodIngredients")
        foodImage = intent.getStringExtra("MenuItemFoodImage")

        Log.v("DetailsActivity", "Food Name: $foodName")
        Log.v("DetailsActivity", "Food Price: $foodPrice")
        Log.v("DetailsActivity", "Food Description: $foodDescription")
        Log.v("DetailsActivity", "Food Ingredients: $foodIngredients")
        Log.v("DetailsActivity", "Food Image URI: $foodImage")

        if (foodImage != null) {
            try {
                val uri = Uri.parse(foodImage)
                Log.v("DetailsActivity", "Parsed URI: $uri")
                Glide.with(this@Details_Activity).load(uri).into(binding.detailsFoodImage)
            } catch (e: Exception) {
                Log.e("DetailsActivity", "Error parsing URI: $foodImage", e)
            }
        } else {
            Log.e("DetailsActivity", "Food Image URI is null")
        }

        with(binding) {
            detailsFoodName.text = foodName
            detailsFoodDescription.text = foodDescription
            detailsFoodIngredients.text = foodIngredients
        }

        binding.imageButton.setOnClickListener {
            finish()
        }
        binding.detailsAddToCart.setOnClickListener {
            val database = FirebaseDatabase.getInstance().reference
            val userId  =auth.currentUser?.uid?:""
//            create a cartItem object
            val cartItem = CartItem(foodName.toString(),foodPrice.toString(),foodDescription.toString(),foodImage.toString(),1)

//            Save data Cart Item to firebase
            database.child("user").child(userId).child("CartItems").push().setValue(cartItem).addOnSuccessListener {
                Toast.makeText(this,"Item added Successfully In Cart",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this,"Item Not Added",Toast.LENGTH_SHORT).show()
            }

        }


    }
}
