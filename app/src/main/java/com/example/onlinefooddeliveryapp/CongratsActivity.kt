package com.example.onlinefooddeliveryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony.Mms.Intents
import com.example.onlinefooddeliveryapp.databinding.ActivityCongratsBinding

class CongratsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCongratsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCongratsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.GoHomeButton.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}