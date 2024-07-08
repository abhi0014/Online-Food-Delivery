package com.example.onlinefooddeliveryapp.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinefooddeliveryapp.Model.CartItem
import com.example.onlinefooddeliveryapp.PayOut_Activity
import com.example.onlinefooddeliveryapp.R
import com.example.onlinefooddeliveryapp.StartActivity
import com.example.onlinefooddeliveryapp.adapter.CartAdapter
import com.example.onlinefooddeliveryapp.databinding.CartitemBinding
import com.example.onlinefooddeliveryapp.databinding.FragmentCartBinding
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var foodNames: MutableList<String>
    private lateinit var foodPrices: MutableList<String>
    private lateinit var foodDescriptions: MutableList<String>
    private lateinit var foodImagesUri: MutableList<String>
    private lateinit var foodIngredients: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        retriveCartItems()


        binding.proceedButton.setOnClickListener {
//            Get ordered Item details Before proceeding to check out

            getOrderItemDetails()

        }
    }

    private fun getOrderItemDetails() {
        val orderIdReference = database.reference.child("user").child(userId).child("CartItems")
        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodDescription = mutableListOf<String>()
        val foodImage = mutableListOf<String>()
        val foodIngredient = mutableListOf<String>()
//        Get item quantity
        val foodQuantities = cartAdapter.getUpdatedItemQuantities()

        orderIdReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    //get cartItem in separate List
                    val orderItems = foodSnapshot.getValue(CartItem::class.java)
                    //add item details to into list
                    orderItems?.foodName?.let { foodName.add(it) }
                    orderItems?.foodPrice?.let { foodPrice.add(it) }
                    orderItems?.foodDescription?.let { foodDescription.add(it) }
                    orderItems?.foodImage?.let { foodImage.add(it) }
                    orderItems?.foodIngredient?.let { foodIngredient.add(it) }
                }

                orderNow(
                    foodName,
                    foodPrice,
                    foodDescription,
                    foodImage,
                    foodIngredient,
                    foodQuantities
                )
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Order Making Failed. Please Try Later",
                    Toast.LENGTH_SHORT
                ).show()

            }

        })
    }

    private fun orderNow(
        foodName: MutableList<String>,
        foodPrice: MutableList<String>,
        foodDescription: MutableList<String>,
        foodImage: MutableList<String>,
        foodIngredient: MutableList<String>,
        foodQuantities: MutableList<Int>
    ) {

        if (isAdded && context != null) {
            val intent = Intent(requireContext(), PayOut_Activity::class.java)
            intent.putExtra("FoodItemName", foodName as ArrayList<String>)
            intent.putExtra("FoodItemPrice", foodPrice as ArrayList<String>)
            intent.putExtra("FoodItemDescription", foodDescription as ArrayList<String>)
            intent.putExtra("FoodItemImage", foodImage as ArrayList<String>)
            intent.putExtra("FoodItemIngredient", foodIngredient as ArrayList<String>)
            intent.putExtra("FoodItemQuantities", foodQuantities as ArrayList<Int>)

            startActivity(intent)
        }
    }

    private fun retriveCartItems() {
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""
        val foodRef = database.reference.child("user").child(userId).child("CartItems")

//      List to store cart Item
        foodNames = mutableListOf()
        foodPrices = mutableListOf()
        foodDescriptions = mutableListOf()
        foodIngredients = mutableListOf()
        foodImagesUri = mutableListOf()
        quantity = mutableListOf()

        //fetch data from database

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val carItems = foodSnapshot.getValue(CartItem::class.java)
                    carItems?.foodName?.let { foodNames.add(it) }
                    carItems?.foodPrice?.let { foodPrices.add(it) }
                    carItems?.foodDescription?.let { foodDescriptions.add(it) }
                    carItems?.foodImage?.let { foodImagesUri.add(it) }
                    carItems?.foodQuantity?.let { quantity.add(it) }
                    carItems?.foodIngredient?.let { foodIngredients.add(it) }

                }
                setAdapter()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Data Not Fetch", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun setAdapter() {
        cartAdapter = CartAdapter(
            requireContext(),
            foodNames,
            foodPrices,
            foodDescriptions,
            foodImagesUri,
            quantity,
            foodIngredients
        )
        binding.cartRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.cartRecyclerView.adapter = cartAdapter
    }

    companion object {

    }
}