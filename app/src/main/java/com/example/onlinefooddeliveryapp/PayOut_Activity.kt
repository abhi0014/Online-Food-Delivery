package com.example.onlinefooddeliveryapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.onlinefooddeliveryapp.Model.OrderDetails
import com.example.onlinefooddeliveryapp.databinding.ActivityPayOutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject

class PayOut_Activity : AppCompatActivity() , PaymentResultWithDataListener {
    private lateinit var  binding:ActivityPayOutBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var name : String
    private lateinit var address:String
    private lateinit var phoneno:String
    private lateinit var totalAmt : String
    private lateinit var foodItemName : ArrayList<String>
    private lateinit var foodItemPrice : ArrayList<String>
    private lateinit var foodItemDescription : ArrayList<String>
    private lateinit var foodItemImage : ArrayList<String>
    private lateinit var foodItemIngredients : ArrayList<String>
    private lateinit var foodItemQuantities : ArrayList<Int>
    private lateinit var userId: String

    private lateinit var  databaseReference: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)



        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()
        //set user data

        setUserData()


        val intent = intent
        foodItemName = intent.getStringArrayListExtra("FoodItemName") as ArrayList<String>
        foodItemPrice = intent.getStringArrayListExtra("FoodItemPrice") as ArrayList<String>
        foodItemDescription = intent.getStringArrayListExtra("FoodItemDescription") as ArrayList<String>
        foodItemImage = intent.getStringArrayListExtra("FoodItemImage") as ArrayList<String>
        foodItemIngredients = intent.getStringArrayListExtra("FoodItemIngredient") as ArrayList<String>
        foodItemQuantities = intent.getIntegerArrayListExtra("FoodItemQuantities") as ArrayList<Int>

        totalAmt =   "$"+calculateTotalAmt().toString()
        binding.payoutPrice.setText(totalAmt)

//       Integrating Razorpay Payment Gateway
        Checkout.preload(applicationContext)
        val co = Checkout()
        co.setKeyID("rzp_test_BWd4mRz0NEZMrk")
        binding.payoutImage.setOnClickListener {

            val user = auth.currentUser
            if(user!=null)
            {
                userId = user.uid
                Log.v("ndjnfknfkndf",userId)
                val userRef = databaseReference.child("user").child(userId)

                userRef.addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val uname = snapshot.child("username").getValue(String::class.java) ?: ""
                            val phone = snapshot.child("phone").getValue(String::class.java) ?: ""
                            val email = snapshot.child("email").getValue(String::class.java)?:""

                            Log.v("PayOutActivity", "Snapshot: ${snapshot.value}")


                            if(email.isNotBlank() && phone.isNotBlank() &&  uname.isNotBlank())
                            {
                                Log.v("username : " , uname)
                                initPayment(email,phone,uname)
                            }else{
                                Toast.makeText(this@PayOut_Activity,"Field are not completed for payment",Toast.LENGTH_SHORT).show()
                            }

                        }else{
                            Toast.makeText(this@PayOut_Activity,"Data Not Available",Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("PayOutActivity", "Database error: ${error.message}")
                    }
                })
            }


        }

        binding.PlaceorderButton.setOnClickListener {

            //get data from textView
            name = binding.payoutName.text.toString()
            address = binding.payoutAddress.text.toString()
            phoneno = binding.payoutPhoneNumber.text.toString()
            if(name.isNotBlank() && address.isNotBlank() && phoneno.isNotBlank())
            {
                placeOrder()
            }else{
                Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show()
            }
        }
        binding.imageButton.setOnClickListener{
            finish()
        }

    }


    private fun initPayment(email: String, phone: String, uname: String) {


        val activity: Activity = this
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name",uname)
            options.put("description","Trying Payment Gateway")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","http://example.com/image/rzp.jpg")
            options.put("theme.color", "#3399cc")
            options.put("currency","INR")
//            options.put("order_id", "order_DBJOWzybf0sJbb")
            options.put("amount",5000)//pass amount in currency subunits

            val retryObj =  JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("email",email)
            prefill.put("contact",phone)

            options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun placeOrder() {
        val userId = auth.currentUser?.uid?:""
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("OrderDetails").push().key

        val orderDetails = OrderDetails(userId,name,foodItemName,foodItemPrice,foodItemImage,foodItemQuantities,address,totalAmt,phoneno,false,false,itemPushKey,time)
        val orderRef = databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderRef.setValue(orderDetails).addOnSuccessListener {
            val intent = Intent(this,CongratsActivity::class.java)
            startActivity(intent)
            removeItemFromCart()
            AddOrderToHistory(orderDetails)
        }.addOnFailureListener {
            Toast.makeText(this,"Failed to order",Toast.LENGTH_SHORT).show()
        }
    }

    private fun AddOrderToHistory(orderDetails: OrderDetails) {

        databaseReference.child("user").child(userId).child("BuyHistory")
            .child(orderDetails.itemPushKey!!).setValue(orderDetails).addOnSuccessListener {

            }

    }


    private fun removeItemFromCart() {
        val cartItemRef = databaseReference.child("user").child(userId).child("CartItems")
        cartItemRef.removeValue()
    }

    private fun calculateTotalAmt(): Int {
        var totalAmount = 0
        for(i in 0 until foodItemPrice.size)
        {
            var price = foodItemPrice[i]
            val lastChar = price.last()
            val priceIntValue =  if(lastChar == '$'){
                price.dropLast(1).toInt()
            }else{
                price.toInt()
            }

            var quantity = foodItemQuantities[i]


            totalAmount += priceIntValue*quantity


        }
            return  totalAmount
    }

    private fun setUserData() {
        val user = auth.currentUser
        if(user!=null)
        {
            userId = user.uid
            val userRef = databaseReference.child("user").child(userId)

            userRef.addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists())
                    {
                        val name = snapshot.child("username").getValue(String::class.java)?:""
                        val address = snapshot.child("address").getValue(String::class.java)?:""
                        val phone = snapshot.child("phone").getValue(String::class.java)?:""

                        binding.apply {
                            payoutName.setText(name)
                            payoutAddress.setText(address)
                            payoutPhoneNumber.setText(phone)
                        }
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }

    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        Toast.makeText(this,"Payment Success",Toast.LENGTH_SHORT).show()
        Log.v("Payment Data ", p1.toString())
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this,"Error : ${p1}",Toast.LENGTH_SHORT).show()


    }
}

class ActivityPayOutBinding {

}
