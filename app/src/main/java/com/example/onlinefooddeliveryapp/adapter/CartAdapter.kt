package com.example.onlinefooddeliveryapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.onlinefooddeliveryapp.databinding.CartitemBinding
import com.example.onlinefooddeliveryapp.databinding.FragmentProfileBinding

class CartAdapter(private val CartItems:MutableList<String>,private val CartItemPrice:MutableList<String>,private val CartImage:MutableList<Int>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val itemQuantities = IntArray(CartItems.size){1}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return CartItems.size
    }
   inner class CartViewHolder(private val binding: CartitemBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                cartFoodName.text = CartItems[position]
                cartItemPrice.text = CartItemPrice[position]
                cartImage.setImageResource(CartImage[position])
                cartItemQuantity.text = quantity.toString()

                minusButton.setOnClickListener{
                    decrease(position)
                }
                plusButton.setOnClickListener {
                    increase(position)
                }
                deleteButton.setOnClickListener {
                    val itemPosition = adapterPosition
                    if(itemPosition!=RecyclerView.NO_POSITION)
                    {
                        deleteItem(position)
                    }

                }

            }
        }

       fun deleteItem(position: Int)
       {
           CartItems.removeAt(position)
           CartImage.removeAt(position)
           CartItemPrice.removeAt(position)
           notifyItemRemoved(position)
           notifyItemChanged(position,CartItems.size)
       }
       fun increase(position: Int){
           if(itemQuantities[position]<10)
           {
               itemQuantities[position]++
               binding.cartItemQuantity.text = itemQuantities[position].toString()
           }
       }
       fun decrease(position: Int){
           if(itemQuantities[position]>1)
           {
               itemQuantities[position]--
               binding.cartItemQuantity.text = itemQuantities[position].toString()
           }
       }
    }


}