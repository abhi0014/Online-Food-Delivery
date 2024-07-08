package com.example.onlinefooddeliveryapp.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.onlinefooddeliveryapp.Model.OrderDetails
import com.example.onlinefooddeliveryapp.R
import com.example.onlinefooddeliveryapp.RecentOrderItem
import com.example.onlinefooddeliveryapp.adapter.BuyAgainAdapter
import com.example.onlinefooddeliveryapp.databinding.FragmentHistoryBinding
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var datatbase :FirebaseDatabase
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var userId :String
    private var listOfOrderItem :MutableList<OrderDetails> = mutableListOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater,container,false)

        auth = FirebaseAuth.getInstance()
        datatbase = FirebaseDatabase.getInstance()
        //retrive and display the user order history
        retriveBuyHistory()


//        binding.recentBuyItem.setOnClickListener {
//            seeItemRecentBuy()
//        }

//        setupRecyclerView()
        return binding.root
    }

    private fun seeItemRecentBuy() {
        if(listOfOrderItem.isNotEmpty()){

        listOfOrderItem.firstOrNull()?.let { recentBuy ->
            val intent = Intent(requireContext(), RecentOrderItem::class.java)
            intent.putExtra("RecentBuyOrderItem", recentBuy)
            startActivity(intent)
        }
        }else{
            Toast.makeText(context,"List empty",Toast.LENGTH_SHORT).show()
        }
    }

    private fun retriveBuyHistory() {
        binding.recentBuyItem.visibility = View.INVISIBLE

        userId = auth.currentUser?.uid ?:""
        val buyItemRef = datatbase.reference.child("user").child(userId).child("BuyHistory")
        val sortingQuery = buyItemRef.orderByChild("currentTime")

        sortingQuery.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(buySnapshot in snapshot.children)
                {
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItem.add(it)
                    }
                }
                listOfOrderItem.reverse()
                if(listOfOrderItem.isNotEmpty())
                {
                    setDataInRecentBuyItem()
                    setPreviousBuyItemRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HistoryFragment", "Error retrieving buy history", error.toException())            }

        })
    }

    private fun setDataInRecentBuyItem() {
        binding.recentBuyItem.visibility = View.VISIBLE
        val recentOrderItem = listOfOrderItem.firstOrNull()

        recentOrderItem?.let {
            with(binding){

                buyagainFoodName.text = it.foodName?.firstOrNull()?:""
                buyagainFoodPrice.text= it.foodPrice?.firstOrNull()?:""
                val image = it.foodImage?.firstOrNull()?:""
                val uri = Uri.parse(image)

                Glide.with(requireContext()).load(uri).into(buyagainImageView)

                listOfOrderItem.reverse()

            }
        }
    }

    private fun setPreviousBuyItemRecyclerView() {
        val buyAgainFoodName = mutableListOf<String>()
        val buyAgainFoodPrice=mutableListOf<String>()
        val buyAgainImage =mutableListOf<String>()

        for(i in 0 until listOfOrderItem.size ){
            listOfOrderItem[i].foodName?.firstOrNull()?.let {
                buyAgainFoodName.add(it)
                listOfOrderItem[i].foodPrice?.firstOrNull()?.let {
                    buyAgainFoodPrice.add(it)
                    listOfOrderItem[i].foodImage?.firstOrNull()?.let {
                        buyAgainImage.add(it)
                    }
                }
            }
        }
        for(i in 1 until listOfOrderItem.size)
        {
            Log.v("recent history of food name :" , listOfOrderItem[i].foodName.toString())
        }
        val rv = binding.buyAgainRecyclerView
        rv.layoutManager=LinearLayoutManager(requireContext())
        buyAgainAdapter = BuyAgainAdapter(buyAgainFoodName,buyAgainFoodPrice,buyAgainImage,requireContext())
        rv.adapter = buyAgainAdapter

    }




}