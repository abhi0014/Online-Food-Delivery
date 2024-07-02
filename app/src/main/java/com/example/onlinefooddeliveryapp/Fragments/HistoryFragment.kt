package com.example.onlinefooddeliveryapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinefooddeliveryapp.R
import com.example.onlinefooddeliveryapp.adapter.BuyAgainAdapter
import com.example.onlinefooddeliveryapp.databinding.FragmentHistoryBinding


class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater,container,false)
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView(){
        val buyagainfoodname = listOf("BreakFast","Green Salad","Ice Cream","Rolls","Cup Cakes","Tea and Coffee","Ice Cream","Logo")
        val buyagainfoodprice= listOf("$5","$5","$5","$5","$5","$5","$5","$5")
        val buyagainimage = listOf(R.drawable.breakfast,R.drawable.green_salad,R.drawable.ice_cream,R.drawable.rolls,R.drawable.cupcakes,R.drawable.tea,R.drawable.ice_cream,R.drawable.logo)
        val adapter = BuyAgainAdapter(buyagainfoodname,buyagainfoodprice,buyagainimage)
        binding.buyAgainRecyclerView.adapter=adapter
        binding.buyAgainRecyclerView.layoutManager=LinearLayoutManager(requireContext())
    }

    companion object {

    }
}