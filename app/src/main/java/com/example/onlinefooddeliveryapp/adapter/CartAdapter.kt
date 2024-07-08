package com.example.onlinefooddeliveryapp.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.onlinefooddeliveryapp.databinding.CartitemBinding
import com.example.onlinefooddeliveryapp.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.MultiFactorAssertion
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartAdapter(
    private val context: Context,
    private val cartItems: MutableList<String>,
    private val cartItemPrices: MutableList<String>,
    private var cartDescriptions: MutableList<String>,
    private val cartImages: MutableList<String>,
    private var cartQuantity: MutableList<Int>,
    private var cartIngredients: MutableList<String>

) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val auth = FirebaseAuth.getInstance()

    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid ?: ""
        val cartItemNumber = cartItems.size

        itemQuantities = IntArray(cartItemNumber) { 1 }
        cartItemReference = database.reference.child("user").child(userId).child("CartItems")
    }

    companion object {
        private var itemQuantities: IntArray = intArrayOf()
        private lateinit var cartItemReference: DatabaseReference
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    inner class CartViewHolder(private val binding: CartitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                cartFoodName.text = cartItems[position]
                cartItemPrice.text = cartItemPrices[position]
//                Load Image using glide
                val uriString = cartImages[position]
                val uri = Uri.parse(uriString)
                cartItemQuantity.text = quantity.toString()
                Glide.with(context).load(uri).into(cartImage)

                minusButton.setOnClickListener {
                    decrease(position)
                }
                plusButton.setOnClickListener {
                    increase(position)
                }
                deleteButton.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteItem(position)
                    } else {
                        Log.e("CartAdapter", "Invalid adapter position: $itemPosition")
                    }

                }

            }
        }

        private fun deleteItem(position: Int) {
            Log.v("Position To delete : ", position.toString())
            val positionRetrive = position
            getUniqueKeyAtPosition(positionRetrive) { uniqueKey ->
                if (uniqueKey != null) {
                    removeItem(position, uniqueKey)
                } else {
                    val positionRetrieve = position
                    Log.e(
                        "CartAdapter",
                        "Unique key retrieval failed for position: $positionRetrieve"
                    )
                }
            }
//            cartItems.removeAt(position)
//            cartImages.removeAt(position)
//            cartItemPrices.removeAt(position)
//            notifyItemRemoved(position)
//            notifyItemChanged(position, cartItems.size)
        }

        private fun increase(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                cartQuantity[position] = itemQuantities[position]
                binding.cartItemQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun decrease(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                cartQuantity[position] = itemQuantities[position]
                binding.cartItemQuantity.text = itemQuantities[position].toString()
            }
        }
    }

    private fun removeItem(position: Int, uniqueKey: String) {
        if (uniqueKey != null) {
            // Ensure the position is within the bounds of the lists
            if (position >= 0 && position < cartItems.size) {
                cartItemReference.child(uniqueKey).removeValue().addOnSuccessListener {
                    cartItems.removeAt(position)
                    cartImages.removeAt(position)
                    cartItemPrices.removeAt(position)
                    cartDescriptions.removeAt(position)
                    cartQuantity.removeAt(position)
//                    cartIngredients.removeAt(position)
                    Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show()

                    // Update ItemQuantity
                    itemQuantities =
                        itemQuantities.filterIndexed { index, _ -> index != position }.toIntArray()
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, cartItems.size)
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e("CartAdapter", "Attempted to remove item at invalid index: $position")
            }
        } else {
            Log.e("CartAdapter", "Unique key is null for position: $position")
        }
    }


    private fun getUniqueKeyAtPosition(positionRetrive: Int, onComplete: (String?) -> Unit) {
        cartItemReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                var uniqueKey: String? = null
                snapshot.children.forEachIndexed { index, dataSnapshot ->
                    if (index == positionRetrive) {
                        uniqueKey = dataSnapshot.key
                        return@forEachIndexed
                    }
                }
                onComplete(uniqueKey)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CartAdapter", "Error retrieving data: ${error.message}")
                onComplete(null)
            }

        })
    }

    fun getUpdatedItemQuantities(): MutableList<Int> {

        val itemQuantity = mutableListOf<Int>()
        itemQuantity.addAll(cartQuantity)
        return itemQuantity


    }


}