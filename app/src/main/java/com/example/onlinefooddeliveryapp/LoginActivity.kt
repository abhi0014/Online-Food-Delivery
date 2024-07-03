package com.example.onlinefooddeliveryapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.onlinefooddeliveryapp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    private lateinit var binding: ActivityLoginBinding
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)




        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()


        val currentUser = auth.currentUser
        val currentEmail = auth.currentUser?.email.toString()
        if (currentUser != null) {

            Log.v("Current User is :: ", currentEmail)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Exit onCreate to avoid re-initializing the login UI
        }
        binding.dontHaveAcc.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {

            email = binding.loginEmail.text.toString().trim()
            password = binding.loginPassword.text.toString().trim()

            if (email.isNotBlank() && password.isNotBlank()) {
                createUser()
            }
            else {
                Toast.makeText(this, "Fill all details", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun createUser() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Login Successfull", Toast.LENGTH_SHORT)
                    .show()
                finish()
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }


}


