package com.example.onlinefooddeliveryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val createAcc = findViewById<Button>(R.id.createAccount)

        createAcc.setOnClickListener {
            Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}