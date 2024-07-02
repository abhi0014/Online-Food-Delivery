package com.example.onlinefooddeliveryapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinefooddeliveryapp.adapter.CartAdapter
import com.example.onlinefooddeliveryapp.adapter.MenuAdapter
import com.example.onlinefooddeliveryapp.databinding.FragmentMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MenuBottomSheetFragment : BottomSheetDialogFragment (){
    private  lateinit var binding:FragmentMenuBottomSheetBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBottomSheetBinding.inflate(inflater,container,false)
        binding.backButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menufoodname = listOf("Burger","Sandwich","Pizza","Burger","Sandwich","Pizza","Burger","Sandwich","Pizza")
        val menuitemprice = listOf("$5","$6","$7","$5","$6","$7","$5","$6","$7")
        val menuimage = listOf(R.drawable.breakfast,R.drawable.green_salad,R.drawable.ice_cream,R.drawable.cupcakes,R.drawable.tea,R.drawable.rolls,R.drawable.breakfast,R.drawable.green_salad,R.drawable.ice_cream)
        val adapter = MenuAdapter(ArrayList(menufoodname),ArrayList(menuitemprice),ArrayList(menuimage),requireContext())
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter
    }

    companion object {

    }
}