package com.example.onlinefooddeliveryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.onlinefooddeliveryapp.databinding.ActivityPayOutBinding

class PayOut_Activity : AppCompatActivity() {
    private lateinit var  binding:ActivityPayOutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.PlaceorderButton.setOnClickListener {
            val intent = Intent(this,CongratsActivity::class.java)
            startActivity(intent)
        }
        binding.imageButton.setOnClickListener{
            finish()
        }

    }
}

class ActivityPayOutBinding {

}
