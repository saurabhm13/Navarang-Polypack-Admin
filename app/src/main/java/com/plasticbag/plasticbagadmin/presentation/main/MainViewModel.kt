package com.plasticbag.plasticbagadmin.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.plasticbag.plasticbagadmin.model.LoginRequest
import com.plasticbag.plasticbagadmin.model.ProductDetails
import com.plasticbag.plasticbagadmin.util.Constants.Companion.ADMIN
import com.plasticbag.plasticbagadmin.util.Constants.Companion.LOGIN_REQUEST
import com.plasticbag.plasticbagadmin.util.Constants.Companion.PRODUCTS
import com.plasticbag.plasticbagadmin.util.Constants.Companion.USER
import com.plasticbag.plasticbagadmin.util.Constants.Companion.USER_DETAILS

class MainViewModel(): ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference

    private var _productLiveData = MutableLiveData<List<ProductDetails>>()
    val productLiveData: LiveData<List<ProductDetails>> get() = _productLiveData

    private var _loginRequestLiveData = MutableLiveData<List<LoginRequest>>()
    val loginRequestLiveData: LiveData<List<LoginRequest>> get() = _loginRequestLiveData

    fun getProducts() {
        database.child(PRODUCTS).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<ProductDetails>()

                for (dataSnapshot in snapshot.children) {

                    val product = dataSnapshot.getValue(ProductDetails::class.java)
                    product?.let {
                        productList.add(product)
                    }
                }

                _productLiveData.value = productList
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getLoginRequest() {
        database.child(ADMIN).child(LOGIN_REQUEST).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val loginList = mutableListOf<LoginRequest>()

                for (dataSnapshot in snapshot.children) {
                    val login = dataSnapshot.getValue(LoginRequest::class.java)

                    login?.let {
                        loginList.add(login)
                    }
                }

                _loginRequestLiveData.value = loginList
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun approveLoginRequest(loginRequest: LoginRequest) {
        database.child(USER).child(loginRequest.userId).child(USER_DETAILS).child("userVerified").setValue(true)
        database.child(ADMIN).child(LOGIN_REQUEST).child(loginRequest.userId).removeValue()
    }

    fun denyLoginRequest(loginRequest: LoginRequest) {
        database.child(ADMIN).child(LOGIN_REQUEST).child(loginRequest.userId).removeValue()
    }

}