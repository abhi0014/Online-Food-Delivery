package com.example.onlinefooddeliveryapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinefooddeliveryapp.adapter.NotificationAdapter
import com.example.onlinefooddeliveryapp.databinding.FragmentNotificationBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Notification_Bottom_Fragment : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentNotificationBottomBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBottomBinding.inflate(layoutInflater,container,false)
        val notification = listOf("Your order has been cancelled successfully","Order has been taken by driver","Congrats Your has been placed")
        val notificationImages = listOf(R.drawable.sademoji,R.drawable.truck,R.drawable.successful)
        val adapter = NotificationAdapter(ArrayList(notification), ArrayList(notificationImages))
        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationRecyclerView.adapter=adapter
        return binding.root
    }

    companion object {

    }
}