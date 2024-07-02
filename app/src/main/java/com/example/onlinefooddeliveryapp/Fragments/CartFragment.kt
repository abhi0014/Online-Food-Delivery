package com.example.onlinefooddeliveryapp.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinefooddeliveryapp.PayOut_Activity
import com.example.onlinefooddeliveryapp.R
import com.example.onlinefooddeliveryapp.adapter.CartAdapter
import com.example.onlinefooddeliveryapp.databinding.CartitemBinding
import com.example.onlinefooddeliveryapp.databinding.FragmentCartBinding


class CartFragment : Fragment() {
   private lateinit var binding: FragmentCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cartfoodname = listOf("BreakFast","Green Salad","Rolls")
        val cartitemprice = listOf("$5","$6","$7")
        val cartimage = listOf(R.drawable.breakfast,R.drawable.green_salad,R.drawable.rolls)
        val adapter = CartAdapter(ArrayList(cartfoodname),ArrayList(cartitemprice),ArrayList(cartimage))
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = adapter

        binding.proceedButton.setOnClickListener {
            val intent = Intent(requireContext(),PayOut_Activity::class.java)
            startActivity(intent)
        }
    }
    companion object {

    }
}