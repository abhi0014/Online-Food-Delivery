package com.example.onlinefooddeliveryapp.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.disklrucache.DiskLruCache.Value
import com.example.onlinefooddeliveryapp.LoginActivity
import com.example.onlinefooddeliveryapp.Model.UserModel
import com.example.onlinefooddeliveryapp.R
import com.example.onlinefooddeliveryapp.databinding.ActivityLoginBinding
import com.example.onlinefooddeliveryapp.databinding.FragmentProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {


    private lateinit var auth:FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: FragmentProfileBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        binding.saveButton.setOnClickListener {
            val name = binding.profileName.text.toString()
            val email= binding.profileEmail.text.toString()
            val address = binding.profileAddress.text.toString()
            val phone = binding.profilePhone.text.toString()

            updateUserdata(name,email,address,phone)
        }

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)

        binding.logoutButton.setOnClickListener {
            Firebase.auth.signOut()

            // Sign out from Google
            googleSignInClient.signOut().addOnCompleteListener {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }

        saveUserData()



        return binding.root
    }

    private fun updateUserdata(name: String, email: String, address: String, phone: String) {

            val userId = auth.currentUser?.uid
        if(userId!=null)
        {
            val userRef = databaseReference.child("user").child(userId)
            val userData = hashMapOf(
                "username" to name,
                "email" to email,
                "address" to address,
                "phone" to phone
                )

            userRef.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(),"Profile Updated Successfully",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(),"Profile Updation Failed",Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun saveUserData() {
        val userId = auth.currentUser?.uid
        if(userId!=null)
        {
            val userRef = databaseReference.child("user").child(userId)

            userRef.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists())
                    {
                        val userProfile = snapshot.getValue(UserModel::class.java)
                        if(userProfile!=null)
                        {
                            binding.apply {
                                profileName.setText(userProfile.username)
                                profileAddress.setText(userProfile.address)
                                profileEmail.setText(userProfile.email)
                                profilePhone.setText(userProfile.phone)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }


}