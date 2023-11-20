package com.plasticbag.plasticbagadmin.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.plasticbag.plasticbagadmin.model.ProductDetails
import com.plasticbag.plasticbagadmin.util.Constants.Companion.PRODUCTS

class MainViewModel(): ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference

    private var _productLiveData = MutableLiveData<List<ProductDetails>>()
    val productLiveData: LiveData<List<ProductDetails>> get() = _productLiveData



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


}