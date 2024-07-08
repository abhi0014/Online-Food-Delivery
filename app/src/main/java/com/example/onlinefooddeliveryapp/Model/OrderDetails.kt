package com.example.onlinefooddeliveryapp.Model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.util.ArrayList

class OrderDetails():Serializable {

    var userID:String?=null
    var userName:String?=null
    var foodName:MutableList<String>?=null
    var foodPrice:MutableList<String>?=null
    var foodImage:MutableList<String>?=null
    var foodQuantities:MutableList<Int>?=null
    var address:String?=null
    var totalPrice:String?=null
    var phoneNumber:String?=null
    var orderAccepted:Boolean=false
    var paymentRecieved:Boolean=false
    var itemPushKey:String?=null
    var currentTime:Long=0

    constructor(parcel: Parcel) : this() {
        userID = parcel.readString()
        userName = parcel.readString()
        address = parcel.readString()
        totalPrice = parcel.readString()
        phoneNumber = parcel.readString()
        orderAccepted = parcel.readByte() != 0.toByte()
        paymentRecieved = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }

    constructor(
        userId: String,
        name: String,
        foodItemName: ArrayList<String>,
        foodItemPrice: ArrayList<String>,
        foodItemImage: ArrayList<String>,
        foodItemQuantities: ArrayList<Int>,
        address: String,
        totalAmt: String,
        phoneno: String,
        b: Boolean,
        b1: Boolean,
        itemPushKey: String?,
        time: Long
    ):this(){
        this.userID=userId
        this.userName=name
        this.foodName=foodItemName
        this.foodPrice=foodItemPrice
        this.foodImage=foodItemImage
        this.foodQuantities=foodItemQuantities
        this.address = address
        this.totalPrice = totalAmt
        this.phoneNumber=phoneno
        this.orderAccepted = orderAccepted
        this.paymentRecieved = paymentRecieved
        this.itemPushKey = itemPushKey
        this.currentTime = time


    }

    fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userID)
        parcel.writeString(userName)
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNumber)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentRecieved) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }

  fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }

}